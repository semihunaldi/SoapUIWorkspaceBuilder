package com.semihunaldi.soapui.workspace.builder.service.listener;


import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.workspace.Workspace;
import com.semihunaldi.soapui.workspace.builder.model.wsdl.Wsdl;

/**
 * Created by semih on 10.04.2017.
 */
public interface SoapUIProjectListener
{
    //throwed exceptions will be swollen
    public void doWhatYouWant(Wsdl wsdl, WsdlProject project, Workspace workspace);
}
