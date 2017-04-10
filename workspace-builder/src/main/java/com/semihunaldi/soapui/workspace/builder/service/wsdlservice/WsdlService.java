package com.semihunaldi.soapui.workspace.builder.service.wsdlservice;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.iface.Operation;
import com.semihunaldi.soapui.workspace.builder.model.wsdl.Wsdl;
import com.semihunaldi.soapui.workspace.builder.model.wsdl.WsdlOperationHandler;
import com.semihunaldi.soapui.workspace.builder.service.BaseServiceImpl;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * Created by semih on 4.04.2017.
 */
public class WsdlService extends BaseServiceImpl
{
    private static final int MEGABYTE = (1024*1024);

    public Wsdl prepareWsdl(WsdlProject project, File wsdlFile) throws Exception
    {
        WsdlInterface[] ifaces = new WsdlInterface[0];
        try
        {
            ifaces = WsdlInterfaceFactory.importWsdl(project,wsdlFile.getPath(), true);
        }
        catch (OutOfMemoryError e)
        {
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            memoryBean.gc();
            return null;
        }
        if (ifaces.length > 0)
        {
            WsdlInterface wsdlInterface = ifaces[0];
            Wsdl wsdl = new Wsdl();
            wsdl.setWsdlFile(wsdlFile);
            wsdl.setWsdlInterface(wsdlInterface);
            wsdl.setServiceName(wsdlInterface.getName());
            wsdl.setName(wsdlInterface.getName());
            for (Operation operation : wsdlInterface.getOperationList())
            {
                WsdlOperationHandler wsdlOperationHandler = new WsdlOperationHandler();
                WsdlOperation op = (WsdlOperation) operation;
                String input = op.createRequest(true);
                String output = op.createResponse(true);
                wsdlOperationHandler.setInput(input);
                wsdlOperationHandler.setOutput(output);
                wsdlOperationHandler.setWsdlOperation(op);
                wsdlOperationHandler.setOperationName(op.getName());
                wsdl.getWsdlOperationHandlers().add(wsdlOperationHandler);
                wsdl.getOperationNames().add(op.getName());
            }
            return wsdl;
        }
        return null;
    }
}
