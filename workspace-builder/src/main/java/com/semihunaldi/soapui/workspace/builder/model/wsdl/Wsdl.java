package com.semihunaldi.soapui.workspace.builder.model.wsdl;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.semihunaldi.soapui.workspace.builder.model.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.util.List;

/**
 * Created by semih on 4.04.2017.
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class Wsdl extends AbstractModel
{
    private String serviceName;

    private String name;

    private File wsdlFile;

    WsdlInterface wsdlInterface;

    private List<String> operationNames = Lists.newArrayList();

    private List<WsdlOperationHandler> wsdlOperationHandlers = Lists.newArrayList();

    public Optional<WsdlOperationHandler> getOperationHandlerByName(String operationName)
    {
        for(WsdlOperationHandler wsdlOperationHandler : this.getWsdlOperationHandlers())
        {
            if(wsdlOperationHandler.getOperationName().equals(operationName))
            {
                return Optional.of(wsdlOperationHandler);
            }
        }
        return Optional.absent();
    }
}
