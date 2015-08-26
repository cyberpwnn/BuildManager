package com.ulticraft.buildmanager.buildcollaboration;

import java.io.Serializable;

public class Status implements Serializable
{
	private static final long serialVersionUID = -2756260432519797182L;
	
	private String title;
	private String description;
	
	public Status(String title, String description)
	{
		this.title = title;
		this.description = description;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
}
