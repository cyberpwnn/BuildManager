package com.ulticraft.buildmanager.buildcollaboration;

import java.util.ArrayList;
import org.bukkit.Location;

public class Assignment
{
	private Deadline deadline;
	private Location location;
	private String assigner;
	private boolean completed;
	private int percent;
	private int id;
	private String title;
	private String description;
	private ArrayList<Status> statuses;
	
	public Assignment(String assigner, String title, String description, Deadline deadline, Location location, int id)
	{
		this.assigner = assigner;
		this.deadline = deadline;
		this.location = location;
		this.title = title;
		this.description = description;
		this.id = id;
		this.statuses = new ArrayList<Status>();
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
	
	public boolean isCompleted()
	{
		return completed;
	}
	
	public int getPercent()
	{
		return percent;
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

	public ArrayList<Status> getStatuses()
	{
		return statuses;
	}

	public void setStatuses(ArrayList<Status> statuses)
	{
		this.statuses = statuses;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public void setPercent(int percent)
	{
		this.percent = percent;
	}
	
	public void addStatus(Status status)
	{
		statuses.add(status);
	}
}
