package com.ulticraft.buildmanager.group;

import java.io.Serializable;

public class SerializableLocation implements Serializable
{
	private static final long serialVersionUID = -4863224785597953400L;
	
	private int x;
	private int y;
	private int z;
	private double p;
	private double w;
	private String world;
	
	public SerializableLocation(int x, int y, int z, double p, double w, String world)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.p = p;
		this.w = w;
		this.world = world;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getZ()
	{
		return z;
	}

	public void setZ(int z)
	{
		this.z = z;
	}

	public double getP()
	{
		return p;
	}

	public void setP(double p)
	{
		this.p = p;
	}

	public double getW()
	{
		return w;
	}

	public void setW(double w)
	{
		this.w = w;
	}

	public String getWorld()
	{
		return world;
	}

	public void setWorld(String world)
	{
		this.world = world;
	}
	
	public String toString()
	{
		return this.x + ", " + this.y + ", " + this.z;
	}
}
