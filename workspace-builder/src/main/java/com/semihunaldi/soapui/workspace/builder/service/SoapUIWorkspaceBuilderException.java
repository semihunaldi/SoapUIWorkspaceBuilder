package com.semihunaldi.soapui.workspace.builder.service;

/**
 * Created by semih on 04.04.2017.
 */
public class SoapUIWorkspaceBuilderException extends RuntimeException
{
    public SoapUIWorkspaceBuilderException(String message, Throwable cause)
    {
        super(message + " " + cause.getMessage());
    }

    public SoapUIWorkspaceBuilderException(String message)
    {
        super(message);
    }
}
