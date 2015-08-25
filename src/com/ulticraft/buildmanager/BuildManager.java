package com.ulticraft.buildmanager;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import com.ulticraft.buildmanager.buildcollaboration.BuildCollaboration;

public class BuildManager extends JavaPlugin
{
	private Logger logger;
	private BuildConfig bc;
	private BuildCollaboration bm;
	
	@Override
	public void onEnable()
	{
		logger = getLogger();
		
		bm = new BuildCollaboration();
		bc = new BuildConfig(this);
	}
	
	@Override
	public void onDisable()
	{
		bc.save();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("buildmanager"))
		{
			return false;
		}
		
		return false;
	}
	
	public void info(String msg)
	{
		logger.info(msg);
	}
	
	public void warn(String msg)
	{
		getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "WARN: " + msg);
	}
	
	public void fatal(String msg)
	{
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "FATAL: " + msg);
	}
	
	public void verbose(String msg)
	{
		getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "VERBOSE: " + msg);
	}
	
	public BuildConfig getBuildConfig()
	{
		return bc;
	}
	
	public BuildCollaboration getBuildCollaboration()
	{
		return bm;
	}

	public void setBuildCollaboration(BuildCollaboration bm)
	{
		this.bm = bm;
	}
}
