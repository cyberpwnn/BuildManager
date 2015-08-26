package com.ulticraft.buildmanager.group;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class Deadline implements Serializable
{
	private static final long serialVersionUID = 4298697384141194814L;
	
	private Date date;
	private DateFormat dateFormat;
	
	public Deadline(Date date)
	{
		this.date = date;
		this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
	}
	
	public String format()
	{
		return dateFormat.format(date);
	}
	
	public String getDueDate()
	{
		Date today = new Date();
		long diff = today.getTime() - date.getTime();
		
		if(diff <= 0)
		{
			return "LATE";
		}
		
		else
		{
			if(diff < 60000)
			{
				return "Due NOW";
			}
			
			else if(diff < 60000 * 60)
			{
				return "Due in " + diff / 1000 / 60 + "m";
			}
			
			else if(diff < 60000 * 60 * 24)
			{
				return "Due in " + diff / 1000 / 60 / 60 + "h";
			}
			
			else
			{
				return "Due in " + diff / 1000 / 60 / 60 / 24 + "d";
			}
		}
	}
}
