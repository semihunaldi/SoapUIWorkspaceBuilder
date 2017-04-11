package com.semihunaldi.soapui.workspace.builder.sample;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.workspace.Workspace;
import com.google.common.collect.Lists;
import com.semihunaldi.soapui.workspace.builder.model.wsdl.Wsdl;
import com.semihunaldi.soapui.workspace.builder.service.listener.SoapUIProjectListener;
import com.semihunaldi.soapui.workspace.builder.service.workspace.WorkspaceBuilderFactory;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by semih on 10.04.2017.
 */
public class Main
{
    public static void main(String[] args)
    {
        try
        {
            String desktopPath = System.getProperty("user.home") + "/Desktop";
            File workspace = new File(desktopPath);
            URL url = Main.class.getClassLoader().getResource("wsdls/GlobalWeather.wsdl");
            List<File> wsdlFiles = Lists.newArrayList(new File(url.toURI()));
            WorkspaceBuilderFactory workspaceBuilderFactory = new WorkspaceBuilderFactory(wsdlFiles,workspace,"My_Workspace",null);
            workspaceBuilderFactory.setSoapUIProjectListener(new SoapUIProjectListener()
            {
                @Override
                public void doWhatYouWant(Wsdl wsdl, WsdlProject project, Workspace workspace)
                {
                    System.out.println("Hey I'm listening wsdl projects");
                    System.out.println("Wsdl Name : " + wsdl.getName());
                    System.out.println("Project Name : " +project.getName());
                    System.out.println("Workspace Name : " + workspace.getName());
                    //You can add requests, mock services, urls to project in this listener
                }
            });
            Workspace generatedWorkspace = workspaceBuilderFactory.createWorkSpaceWithProjects();
            System.out.println(workspaceBuilderFactory.getErrorOccuredWslds());
            //Check your desktop and import workspace to SoapUI
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
