package com.ulticraft.buildmanager.group;

import java.io.Serializable;
import java.util.ArrayList;
import org.bukkit.entity.Player;

public class Manager implements Serializable
{
	private static final long serialVersionUID = -8577757089361373697L;
	
	private String name;
	private String uuid;
	private ArrayList<Integer> managing;
	private int selected;
	
	public Manager(Player player)
	{
		this.name = player.getName();
		this.uuid = player.getUniqueId().toString();
		this.managing = new ArrayList<Integer>();
		this.selected = 0;
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
	
	public void addManagedBuild(Build build)
	{
		managing.add(build.getId());
	}
	
	public void delManagedBuild(Build build)
	{
		managing.remove(new Integer(build.getId()));
	}

	public ArrayList<Integer> getManaging()
	{
		return managing;
	}

	public void setManaging(ArrayList<Integer> managing)
	{
		this.managing = managing;
	}

	public int getSelected()
	{
		return selected;
	}

	public void setSelected(int selected)
	{
		this.selected = selected;
	}
}
