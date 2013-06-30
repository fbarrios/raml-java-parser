package org.raml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.raml.model.parameter.UriParameter;
import org.raml.parser.annotation.Key;
import org.raml.parser.annotation.Mapping;
import org.raml.parser.annotation.Parent;
import org.raml.parser.annotation.Scalar;
import org.raml.parser.resolver.ResourceHandler;

public class Resource
{

    @Scalar
    private String name;

    @Parent(property = "uri")
    private String parentUri;

    @Key
    private String relativeUri;

    @Mapping
    private Map<String, UriParameter> uriParameters = new HashMap<String, UriParameter>();

    @Mapping(handler = ResourceHandler.class, implicit = true)
    private Map<String, Resource> resources = new HashMap<String, Resource>();

    @Mapping(implicit = true)
    private Map<ActionType, Action> actions = new HashMap<ActionType, Action>();

    private List<?> uses = new ArrayList();


    public Resource()
    {
    }

    public void setRelativeUri(String relativeUri)
    {
        this.relativeUri = relativeUri;
    }

    public void setParentUri(String parentUri)
    {
        this.parentUri = parentUri;
    }

    public Map<ActionType, Action> getActions()
    {
        return actions;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getRelativeUri()
    {
        return relativeUri;
    }

    public String getUri()
    {
        if (parentUri.endsWith("/"))
        {
            return parentUri + relativeUri.substring(1);
        }
        return parentUri + relativeUri;
    }

    public List<?> getUses()
    {
        return uses;
    }

    public Action getAction(ActionType name)
    {
        return actions.get(name);
    }

    public Map<String, Resource> getResources()
    {
        return resources;
    }

    public Map<String, UriParameter> getUriParameters()
    {
        return uriParameters;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Resource))
        {
            return false;
        }

        Resource resource = (Resource) o;

        return parentUri.equals(resource.parentUri) && relativeUri.equals(resource.relativeUri);

    }

    @Override
    public int hashCode()
    {
        int result = parentUri.hashCode();
        result = 31 * result + relativeUri.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Resource{" +
               "name='" + name + '\'' +
               ", uri='" + getUri() + '\'' +
               '}';
    }

    public Resource getResource(String path)
    {
        for (Resource resource : resources.values())
        {
            if (path.startsWith(resource.getRelativeUri()))
            {
                if (path.length() == resource.getRelativeUri().length())
                {
                    return resource;
                }
                if (path.charAt(resource.getRelativeUri().length()) == '/')
                {
                    return resource.getResource(path.substring(resource.getRelativeUri().length()));
                }
            }
        }
        return null;
    }
}