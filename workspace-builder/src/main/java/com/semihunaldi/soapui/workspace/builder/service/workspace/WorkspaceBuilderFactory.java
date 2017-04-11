package com.semihunaldi.soapui.workspace.builder.service.workspace;

import com.eviware.soapui.impl.WorkspaceFactoryImpl;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.workspace.Workspace;
import com.eviware.soapui.model.workspace.WorkspaceFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.semihunaldi.soapui.workspace.builder.model.wsdl.Wsdl;
import com.semihunaldi.soapui.workspace.builder.service.BaseServiceImpl;
import com.semihunaldi.soapui.workspace.builder.service.SoapUIWorkspaceBuilderException;
import com.semihunaldi.soapui.workspace.builder.service.listener.SoapUIProjectListener;
import com.semihunaldi.soapui.workspace.builder.service.wsdlservice.WsdlService;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by semih on 7.04.2017.
 */

public class WorkspaceBuilderFactory extends BaseServiceImpl
{
    private WsdlService wsdlService = new WsdlService();

    private File workspaceLocation;

    private String workspaceName;

    private String ignoreWsdlStartingWith;

    private List<File> wsdlFiles = Lists.newArrayList();

    private static final String autoGeneratedWorkspaceFileNamePostFix = "auto-generated-workspace.xml";

    private SoapUIProjectListener soapUIProjectListener;

    @Getter
    private List<String> errorOccuredWslds = Lists.newArrayList();

    public WorkspaceBuilderFactory(List<File> wsdlFiles, File workspaceLocation, String workspaceName, String ignoreWsdlStartingWith)
    {
        checkSoapUiRunning();
        this.workspaceLocation = workspaceLocation;
        this.workspaceName = workspaceName;
        this.ignoreWsdlStartingWith = ignoreWsdlStartingWith;
        this.wsdlFiles = wsdlFiles;
    }

    public Workspace createWorkSpaceWithProjects() throws Exception
    {
        validate();
        checkAnyWsdlFilesExist();
        checkWsdlsUnderDirectory();
        return createWorkspaceAndProjects();
    }


    public void setSoapUIProjectListener(SoapUIProjectListener soapUIProjectListener)
    {
        this.soapUIProjectListener = soapUIProjectListener;
    }

    private void checkAnyWsdlFilesExist()
    {
        if(wsdlFiles.size()<1)
        {
            throw new RuntimeException("There aren't any .wsdl files under this directory.");
        }
    }

    private Workspace createWorkspaceAndProjects() throws Exception
    {
        String workspaceFileUrl = workspaceLocation.getPath().concat("/").concat(workspaceName).concat("_").concat(autoGeneratedWorkspaceFileNamePostFix);
        checkFileExistsAndDelete(workspaceFileUrl);
        WorkspaceFactory workspaceFactory = new WorkspaceFactoryImpl();
        Workspace workspace = workspaceFactory.openWorkspace(workspaceFileUrl, null);
        for (File wsdlFile : wsdlFiles)
        {
            if(StringUtils.isNotBlank(ignoreWsdlStartingWith) && wsdlFile.getName().startsWith(ignoreWsdlStartingWith))
            {
                continue;
            }
            createWorkspaceProjectsFromWsdl(workspace, wsdlFile);
        }
        workspace.save(true);
        return workspace;
    }

    private void checkWsdlsUnderDirectory()
    {
        for(File file : wsdlFiles)
        {
            checkFileExists(file);
            String extension = FilenameUtils.getExtension(file.getName());
            if(!extension.equals("wsdl"))
            {
                throw new SoapUIWorkspaceBuilderException(file.getPath().concat(" file is not a wsdl file."));
            }
        }
    }

    private void checkFileExists(File file)
    {
        if (!file.exists())
        {
            throw new SoapUIWorkspaceBuilderException(file.getPath().concat(" file does not exists."));
        }
    }

    private void checkFileExistsAndDelete(String filePath)
    {
        File file = new File(filePath);
        if (file.exists())
        {
            if (!file.delete())
            {
                throw new RuntimeException(filePath.concat(" file can not be deleted"));
            }
        }
    }

    private void createWorkspaceProjectsFromWsdl(Workspace workspace, File wsdlFile) throws Exception
    {
        String wsdlName = getFileNameWithoutExtension(wsdlFile.getName());
        String projectFilePath = workspaceLocation.getPath().concat("/").concat(workspaceName).concat("-auto-generated-project-").concat(wsdlName).concat(".xml");
        checkFileExistsAndDelete(projectFilePath);
        File projectFile = new File(projectFilePath);
        WsdlProject project = (WsdlProject) workspace.createProject(wsdlName, projectFile);
        prepareWsdlAndGenerateSoapUIRequests(project,wsdlFile,workspace);
    }

    private void prepareWsdlAndGenerateSoapUIRequests(WsdlProject project, File wsdlFile, Workspace workspace) throws Exception
    {
        Wsdl wsdl = wsdlService.prepareWsdl(project, wsdlFile);
        if(wsdl != null)
        {
            try
            {
                if(soapUIProjectListener != null)
                {
                    soapUIProjectListener.doWhatYouWant(wsdl,project,workspace);
                }
            }
            catch (Exception e)
            {
                //swallow
            }
            project.getInterfaceList().add(wsdl.getWsdlInterface());
            project.save();
        }
        else
        {
            project.release();
            workspace.getProjectList().remove(project);
            this.errorOccuredWslds.add(wsdlFile.toURI().toString());
        }
    }


    private void validate()
    {
        Preconditions.checkNotNull(workspaceLocation,"Workspace Location can not be null");
    }

    private void checkSoapUiRunning()
    {
        try
        {
            String line;
            String pidInfo ="";
            Process p =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

            BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = input.readLine()) != null) {
                pidInfo+=line;
            }

            input.close();

            if(pidInfo.toLowerCase().contains("soapui"))
            {
                throw new SoapUIWorkspaceBuilderException("Please close Soap UI application before running this automation tool");
            }
        }
        catch (SoapUIWorkspaceBuilderException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            //swallow
        }
    }
}
