package com.ulticraft.buildmanager.group;

import java.io.Serializable;
import java.util.ArrayList;
import org.bukkit.entity.Player;

public class Builder implements Serializable
{
	private static final long serialVersionUID = -5237442705060537567L;
	
	private String name;
	private String uuid;
	private ArrayList<Integer> assigned;
	private ArrayList<Integer> taken;
	private ArrayList<Integer> finished;
	
	public Builder(Player player)
	{
		this.name = player.getName();
		this.uuid = player.getUniqueId().toString();
		this.assigned = new ArrayList<Integer>();
		this.taken = new ArrayList<Integer>();
		this.finished = new ArrayList<Integer>();
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public ArrayList<Integer> getAssigned()
	{
		return assigned;
	}

	public void setAssigned(ArrayList<Integer> assigned)
	{
		this.assigned = assigned;
	}

	public ArrayList<Integer> getTaken()
	{
		return taken;
	}

	public void setTaken(ArrayList<Integer> taken)
	{
		this.taken = taken;
	}

	public ArrayList<Integer> getFinished()
	{
		return finished;
	}

	public void setFinished(ArrayList<Integer> finished)
	{
		this.finished = finished;
	}
	
	public void addAssigned(Build build)
	{
		assigned.add(build.getId());
	}
	
	public void delAssigned(Build build)
	{
		assigned.remove(new Integer(build.getId()));
	}
	
	public void addTaken(Build build)
	{
		taken.add(build.getId());
	}
	
	public void delTaken(Build build)
	{
		taken.remove(new Integer(build.getId()));
	}
	
	public void addFinished(Build build)
	{
		finished.add(build.getId());
	}
	
	public void delFinished(Build build)
	{
		finished.remove(new Integer(build.getId()));
	}
	
	public boolean isAssigned(Build build)
	{
		return assigned.contains(build.getId());
	}
}
