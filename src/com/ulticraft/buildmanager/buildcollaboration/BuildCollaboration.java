package com.ulticraft.buildmanager.buildcollaboration;

import java.io.Serializable;
import java.util.ArrayList;

public class BuildCollaboration implements Serializable
{
	private static final long serialVersionUID = -7622201433277500112L;
	
	private ArrayList<Manager> managers;
	private ArrayList<Builder> builders;
	
	public BuildCollaboration()
	{
		managers = new ArrayList<Manager>();
		builders = new ArrayList<Builder>();
	}
	
	public Builder getBuilder(String name)
	{
		for(Builder i : builders)
		{
			if(i.getName().equals(name))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Manager getManager(String name)
	{
		for(Manager i : managers)
		{
			if(i.getName().equals(name))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public boolean isBuilder(String name)
	{
		return getBuilder(name) != null;
	}
	
	public boolean isManager(String name)
	{
		return getManager(name) != null;
	}
	
	public void addManager(Manager manager)
	{
		managers.add(manager);
	}
	
	public void addBuilder(Builder builder)
	{
		builders.add(builder);
	}
	
	public void removeManager(Manager manager)
	{
		managers.remove(manager);
	}
	
	public void removeBuilder(Builder builder)
	{
		builders.remove(builder);
	}

	public ArrayList<Manager> getManagers()
	{
		return managers;
	}

	public void setManagers(ArrayList<Manager> managers)
	{
		this.managers = managers;
	}

	public ArrayList<Builder> getBuilders()
	{
		return builders;
	}

	public void setBuilders(ArrayList<Builder> builders)
	{
		this.builders = builders;
	}
}
