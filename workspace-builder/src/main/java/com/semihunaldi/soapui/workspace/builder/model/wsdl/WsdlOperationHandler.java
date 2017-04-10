package com.semihunaldi.soapui.workspace.builder.model.wsdl;

import com.eviware.soapui.impl.wsdl.WsdlOperation;
import lombok.Data;

/**
 * Created by semih on 4.04.2017.
 */

@Data
public class WsdlOperationHandler
{
    private String operationName;

    private WsdlOperation wsdlOperation;

    private String input;

    private String output;
}
