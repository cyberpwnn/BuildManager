package com.ulticraft.buildmanager.buildcollaboration;

import java.io.Serializable;

public class Manager implements Serializable
{
	private static final long serialVersionUID = -7447508704024298785L;
	
	private String name;
	
	public Manager(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
}
