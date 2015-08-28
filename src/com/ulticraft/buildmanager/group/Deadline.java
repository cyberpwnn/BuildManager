package com.ulticraft.buildmanager.group;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.ulticraft.buildmanager.Final;

public class Deadline implements Serializable
{
	private static final long serialVersionUID = 4298697384141194814L;

	private Date date;
	private DateFormat dateFormat;
	private String inst;

	@SuppressWarnings("deprecation")
	public Deadline(String inst)
	{
		this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

		try
		{
			this.date = new Date(inst);
			this.inst = inst;
		}

		catch(Exception e)
		{
			if(inst != null)
			{
				this.date = new Date(this.inst);
			}
			
			else
			{
				this.date = new Date();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public Deadline(String inst, CommandSender sender)
	{
		this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

		try
		{
			this.date = new Date(inst);
			this.inst = inst;
		}

		catch(Exception e)
		{
			sender.sendMessage(Final.TAG + ChatColor.RED + "/bm due " + ChatColor.UNDERLINE + "<MM/DD/YY>");
		}
	}

	@SuppressWarnings("deprecation")
	public String format()
	{
		try
		{
			return dateFormat.format(date);
		}
		
		catch(Exception e)
		{
			if(inst != null)
			{
				date = new Date(inst);
				return format();
			}
			
			else
			{
				return "No Deadline";
			}
		}
	}
}
