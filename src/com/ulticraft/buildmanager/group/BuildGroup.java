package com.ulticraft.buildmanager.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class BuildGroup implements Serializable
{
	private static final long serialVersionUID = -2870642921971094400L;

	private ArrayList<Builder> builders;
	private ArrayList<Manager> managers;
	private ArrayList<Build> builds;

	public BuildGroup()
	{
		this.builders = new ArrayList<Builder>();
		this.managers = new ArrayList<Manager>();
		this.builds = new ArrayList<Build>();
	}

	public ArrayList<Builder> getBuilders()
	{
		return builders;
	}

	public void setBuilders(ArrayList<Builder> builders)
	{
		this.builders = builders;
	}

	public ArrayList<Manager> getManagers()
	{
		return managers;
	}

	public void setManagers(ArrayList<Manager> managers)
	{
		this.managers = managers;
	}

	public ArrayList<Build> getBuilds()
	{
		return builds;
	}

	public void setBuilds(ArrayList<Build> builds)
	{
		this.builds = builds;
	}

	public void addBuilder(Builder builder)
	{
		builders.add(builder);
	}

	public void addManager(Manager manager)
	{
		managers.add(manager);
	}

	public void addBuild(Build build)
	{
		builds.add(build);
	}

	public void delBuilder(String name)
	{
		Iterator<Builder> iterator = builders.iterator();
		while(iterator.hasNext())
		{
			Builder i = (Builder) iterator.next();
			
			if(i.getName().equals(name))
			{
				builders.remove(i);
				break;
			}
		}
	}

	public void delManager(String name)
	{
		Iterator<Manager> iterator = managers.iterator();
		while(iterator.hasNext())
		{
			Manager i = (Manager) iterator.next();
			
			if(i.getName().equals(name))
			{
				managers.remove(i);
				break;
			}
		}
	}

	public void delBuild(int id)
	{
		Iterator<Build> iterator = builds.iterator();
		while(iterator.hasNext())
		{
			Build i = (Build) iterator.next();
			
			if(i.getId() == id)
			{
				builds.remove(i);
				break;
			}
		}
		
		for(Manager i : managers)
		{
			if(i.getManaging().contains(new Integer(id)))
			{
				i.getManaging().remove(new Integer(id));
			}
		}
		
		for(Builder i : builders)
		{
			if(i.getAssigned().contains(new Integer(id)))
			{
				i.getAssigned().remove(new Integer(id));
			}
		}
	}
	
	public void delBuild(String title)
	{
		Iterator<Build> iterator = builds.iterator();
		while(iterator.hasNext())
		{
			Build i = (Build) iterator.next();
			
			if(i.getTitle().equals(title))
			{
				builds.remove(i);
				break;
			}
		}
	}
	
	public Build getBuild(String title)
	{
		for(Build i : builds)
		{
			System.out.println(i.getTitle());
			
			if(i.getTitle().equals(title))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Build getBuild(int id)
	{
		Iterator<Build> iterator = builds.iterator();
		while(iterator.hasNext())
		{
			Build i = (Build) iterator.next();
			
			if(i.getId() == id)
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Builder getBuilder(String name)
	{
		Iterator<Builder> iterator = builders.iterator();
		while(iterator.hasNext())
		{
			Builder i = (Builder) iterator.next();
			
			if(i.getName().equals(name))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Manager getManager(String name)
	{
		Iterator<Manager> iterator = managers.iterator();
		while(iterator.hasNext())
		{
			Manager i = (Manager) iterator.next();
			
			if(i.getName().equals(name))
			{
				return i;
			}
		}
		
		return null;
	}

	public boolean isBuilder(String name)
	{
		for(Builder i : builders)
		{
			if(i.getName().equals(name))
			{
				return true;
			}
		}

		return false;
	}

	public boolean isManager(String name)
	{
		for(Manager i : managers)
		{
			if(i.getName().equals(name))
			{
				return true;
			}
		}

		return false;
	}
	
	public int newBuildId()
	{
		return builds.size() + 1;
	}
	
	public ArrayList<Manager> getManagers(int id)
	{
		ArrayList<Manager> mg = new ArrayList<Manager>();
		
		for(Manager i : managers)
		{
			if(i.getManaging().contains(new Integer(id)))
			{
				mg.add(i);
			}
		}
		
		return mg;
	}
	
	public ArrayList<Builder> getBuilders(int id)
	{
		ArrayList<Builder> mg = new ArrayList<Builder>();
		
		for(Builder i : builders)
		{
			if(i.getAssigned().contains(new Integer(id)))
			{
				mg.add(i);
			}
		}
		
		return mg;
	}
}
