package com.ulticraft.buildmanager.buildcollaboration;

import java.io.Serializable;
import com.ulticraft.core.Duration;

public class Deadline implements Serializable
{
	private static final long serialVersionUID = -7575541197329777870L;
	
	private Duration duration;
	
	public Deadline(int days, int hours, int minutes)
	{
		duration = new Duration(days, hours, minutes, 0, 0);
	}
	
	public Duration getDuration()
	{
		return duration;
	}
	
	public boolean isLate()
	{
		if(duration.getTotalDuration() <= 0)
		{
			return true;
		}
		
		return false;
	}
}
