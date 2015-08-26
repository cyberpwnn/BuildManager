package com.ulticraft.buildmanager;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.ulticraft.buildmanager.buildcollaboration.Assignment;
import com.ulticraft.buildmanager.buildcollaboration.BuildCollaboration;
import com.ulticraft.core.RawText;

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
	
	public void message(CommandSender sender, String msg)
	{
		sender.sendMessage(Final.TAG + msg);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("buildmanager"))
		{
			if(args.length == 0)
			{
				if(getBuildCollaboration().isManager(sender.getName()))
				{
					message(sender, Final.HR);
					new RawText().addTextWithHoverCommand(getBuildCollaboration().getManager(sender.getName()).getAssignments().size() + " Assignments", RawText.COLOR_YELLOW, "/buildmanager assignments", getBuildCollaboration().getManager(sender.getName()).getShortDescription(), RawText.COLOR_GOLD).tellRawTo(this, (Player) sender);
					
					message(sender, Final.HR);
				}
				
				else if(getBuildCollaboration().isBuilder(sender.getName()))
				{
					
				}
				
				else if(sender.hasPermission(Final.PERM_MASTER))
				{
					
				}
				
				else
				{
					 message(sender, ChatColor.RED + "You must be a builder, manager, or admin to use this!");
				}
			}
			
			else if(args[0].equals("assignments"))
			{
				if(!getBuildCollaboration().isManager(sender.getName()))
				{
					return false;
				}
				
				message(sender, Final.HR);
				
				for(Assignment i : getBuildCollaboration().getManager(sender.getName()).getAssignments())
				{
					new RawText().addTextWithHoverCommand(i.getTitle(), RawText.COLOR_GREEN, "/buildmanager assignment " + i.getId(), i.getDescription(), RawText.COLOR_AQUA).tellRawTo(this, (Player) sender);
				}
				
				message(sender, Final.HR);
			}
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
