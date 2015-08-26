package com.ulticraft.buildmanager.group;

import java.io.Serializable;
import org.bukkit.Location;

public class Status implements Serializable
{
	private static final long serialVersionUID = 1601662302070453896L;
	
	private String description;
	private String name;
	private Location location;
	
	public Status(String description, String name)
	{
		this.description = description;
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Location getLocation()
	{
		return location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}
}
