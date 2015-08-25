package com.ulticraft.buildmanager.buildcollaboration;

import org.bukkit.Location;

public class Assignment
{
	private Deadline deadline;
	private Location location;
	private String assigner;
	private int id;
	
	public Assignment(String assigner, Deadline deadline, Location location, int id)
	{
		this.assigner = assigner;
		this.deadline = deadline;
		this.location = location;
		this.id = id;
	}

	public Deadline getDeadline()
	{
		return deadline;
	}

	public void setDeadline(Deadline deadline)
	{
		this.deadline = deadline;
	}

	public Location getLocation()
	{
		return location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getAssigner()
	{
		return assigner;
	}

	public void setAssigner(String assigner)
	{
		this.assigner = assigner;
	}
}
