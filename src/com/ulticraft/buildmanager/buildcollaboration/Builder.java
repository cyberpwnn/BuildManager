package com.ulticraft.buildmanager.buildcollaboration;

import java.io.Serializable;
import java.util.ArrayList;

public class Builder implements Serializable
{
	private static final long serialVersionUID = -7602284846008192392L;
	
	private String name;
	private String uuid;
	private ArrayList<Integer> assignments;
	
	public Builder(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getUuid()
	{
		return uuid;
	}

	public ArrayList<Integer> getAssignments()
	{
		return assignments;
	}
}
