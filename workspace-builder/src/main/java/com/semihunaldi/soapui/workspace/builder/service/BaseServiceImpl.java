package com.semihunaldi.soapui.workspace.builder.service;

/**
 * Created by semih on 4.04.2017.
 */
public class BaseServiceImpl
{
    protected String getFileNameWithoutExtension(String fileName)
    {
        return fileName.substring(0,fileName.lastIndexOf("."));
    }
}
