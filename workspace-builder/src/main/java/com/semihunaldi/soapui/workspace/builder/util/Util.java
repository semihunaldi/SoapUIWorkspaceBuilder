package com.semihunaldi.soapui.workspace.builder.util;

import com.google.common.collect.Lists;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * Created by semih on 08.09.2016.
 */
public class Util
{
    public static List<Node> getNodeListItems(NodeList nodeList)
    {
        List<Node> nodes = Lists.newArrayList();
        for(int i = 0 ; i < nodeList.getLength() ; i++)
        {
            Node node = nodeList.item(i);
            if(node.getLocalName() != null)
            {
                nodes.add(node);
            }
        }
        return nodes;
    }
}
