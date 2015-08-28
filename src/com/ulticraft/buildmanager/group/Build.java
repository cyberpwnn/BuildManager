package com.ulticraft.buildmanager.group;

import java.io.Serializable;
import java.util.ArrayList;

public class Build implements Serializable
{
	private static final long serialVersionUID = -1648031766464622940L;
	
	private int id;
	private String title;
	private String description;
	private ArrayList<Status> updates;
	private SerializableLocation location;
	private boolean finished;
	private Deadline deadline;
	private int completed;
	
	public Build(int id, String title, String description, ArrayList<Status> updates, SerializableLocation location)
	{
		this.id = id;
		this.title = title;
		this.description = description;
		this.updates = updates;
		this.location = location;
		this.finished = false;
		this.completed = 0;
	}

	public int getCompleted()
	{
		return completed;
	}

	public void setCompleted(int completed)
	{
		this.completed = completed;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
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

	public ArrayList<Status> getUpdates()
	{
		return updates;
	}

	public void setUpdates(ArrayList<Status> updates)
	{
		this.updates = updates;
	}
	
	public void addUpdate(Status status)
	{
		updates.add(status);
	}

	public SerializableLocation getLocation()
	{
		return location;
	}

	public void setLocation(SerializableLocation location)
	{
		this.location = location;
	}

	public boolean isFinished()
	{
		return finished;
	}

	public void setFinished(boolean finished)
	{
		this.finished = finished;
	}
	
	public void setDeadline(Deadline deadline)
	{
		this.deadline = deadline;
	}
	
	public Deadline getDeadline()
	{
		return deadline;
	}
}
