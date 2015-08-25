package com.ulticraft.buildmanager.buildcollaboration;

import java.io.Serializable;
import java.util.ArrayList;

public class BuildCollaboration implements Serializable
{
	private static final long serialVersionUID = -7622201433277500112L;
	
	private ArrayList<Manager> managers;
	private ArrayList<Builder> builders;
	private ArrayList<Assignment> assignments;
	
	public BuildCollaboration()
	{
		
	}
	
	public void addManager(Manager manager)
	{
		managers.add(manager);
	}
	
	public void addBuilder(Builder builder)
	{
		builders.add(builder);
	}
	
	public void removeManager(Manager manager)
	{
		managers.remove(manager);
	}
	
	public void removeBuilder(Builder builder)
	{
		builders.remove(builder);
	}

	public ArrayList<Manager> getManagers()
	{
		return managers;
	}

	public void setManagers(ArrayList<Manager> managers)
	{
		this.managers = managers;
	}

	public ArrayList<Builder> getBuilders()
	{
		return builders;
	}

	public void setBuilders(ArrayList<Builder> builders)
	{
		this.builders = builders;
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
}
