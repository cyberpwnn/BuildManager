package com.ulticraft.buildmanager.buildcollaboration;

import java.io.Serializable;
import java.util.ArrayList;

public class Manager implements Serializable
{
	private static final long serialVersionUID = -7447508704024298785L;
	
	private String name;
	private String uuid;
	private ArrayList<Assignment> assignments;
	
	public Manager(String name)
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
	
	public ArrayList<Assignment> getAssignments()
	{
		return assignments;
	}
	
	public void assign(Assignment assignment)
	{
		assignments.add(assignment);
	}
	
	public void unassign(Assignment assignment)
	{
		assignments.remove(assignment);
	}
	
	public String getShortDescription()
	{
		int comp = 0;
		int unf = 0;
		int late = 0;
		String f = "";
		
		for(Assignment i : assignments)
		{
			if(i.isCompleted())
			{
				comp++;
			}
			
			else
			{
				unf++;
			}
			
			if(i.getDeadline().isLate())
			{
				late++;
			}
		}
		
		if(comp != 0)
		{
			f = f + comp + " completed  ";
		}
		
		if(unf != 0)
		{
			f = f + unf + " unfinished  ";
		}
		
		if(late != 0)
		{
			f = f + late + " late  ";
		}
		
		if(f.equals(""))
		{
			f = "No Assignments";
		}
		
		return f;
	}
}
