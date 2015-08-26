package com.ulticraft.buildmanager;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.ulticraft.buildmanager.group.Build;
import com.ulticraft.buildmanager.group.BuildGroup;
import com.ulticraft.buildmanager.group.Builder;
import com.ulticraft.buildmanager.group.Manager;
import com.ulticraft.buildmanager.group.Status;
import com.ulticraft.core.PlayerUtils;

public class BuildManager extends JavaPlugin
{
	private Logger logger;
	private BuildConfig bc;
	private BuildGroup bg;
	private PlayerUtils pu;
	
	@Override
	public void onEnable()
	{
		logger = getLogger();
		
		pu = new PlayerUtils(this);
		bc = new BuildConfig(this);
		bg = bc.load();
	}
	
	@Override
	public void onDisable()
	{
		bc.save(bg);
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
				if(bg.isManager(sender.getName()))
				{
					message(sender, "Your a manager");
				}
				
				else if(bg.isBuilder(sender.getName()))
				{
					message(sender, "Your a builder");
				}
				
				else if(sender.hasPermission(Final.PERM_MASTER))
				{
					message(sender, "Your an admin");
				}
				
				else
				{
					message(sender, ChatColor.RED + "You are not a builder, manager or admin!");
				}
			}
			
			else if(args[0].equals("addmanager") || args[0].equals("+manager"))
			{
				if(sender.hasPermission(Final.PERM_MASTER))
				{
					if(args.length != 2)
					{
						message(sender, ChatColor.RED + "/bm +manager <player>");
						return true;
					}
					
					if(!pu.canFindPlayer(args[1]))
					{
						message(sender, ChatColor.RED + "Cannot find '" + args[1] + "'");
						return false;
					}
					
					Player fp = pu.findPlayer(args[1]);
					
					if(!bg.isManager(fp.getName()))
					{
						bg.addManager(new Manager(fp));
						message(sender, ChatColor.GREEN + fp.getName() + " is now a manager!");
						
						if(!bg.isBuilder(fp.getName()))
						{
							bg.addBuilder(new Builder(fp));
							message(sender, ChatColor.YELLOW + fp.getName() + " is now a builder also!");
						}
					}
					
					else
					{
						message(sender, ChatColor.RED + fp.getName() + " is already a manager!");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You do not have permission for that!");
				}
			}
			
			else if(args[0].equals("delmanager") || args[0].equals("-manager"))
			{
				if(sender.hasPermission(Final.PERM_MASTER))
				{
					if(args.length != 2)
					{
						message(sender, ChatColor.RED + "/bm -manager <bally>");
						return true;
					}
					
					if(!pu.canFindPlayer(args[1]))
					{
						message(sender, ChatColor.RED + "Cannot find '" + args[1] + "'");
						return false;
					}
					
					Player fp = pu.findPlayer(args[1]);
					
					if(bg.isManager(fp.getName()))
					{
						bg.delManager(fp.getName());
						message(sender, ChatColor.GREEN + fp.getName() + " is no longer a manager!");
						
						if(bg.isBuilder(fp.getName()))
						{
							bg.delBuilder(fp.getName());
							message(sender, ChatColor.YELLOW + fp.getName() + " is no longer a builder also!");
						}
					}
					
					else
					{
						message(sender, ChatColor.RED + fp.getName() + " is not a manager!");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You do not have permission for that!");
				}
			}
			
			else if(args[0].equals("addbuilder") || args[0].equals("+builder"))
			{
				if(sender.hasPermission(Final.PERM_MASTER))
				{
					if(args.length != 2)
					{
						message(sender, ChatColor.RED + "/bm +builder <player>");
						return true;
					}
					
					if(!pu.canFindPlayer(args[1]))
					{
						message(sender, ChatColor.RED + "Cannot find '" + args[1] + "'");
						return false;
					}
					
					Player fp = pu.findPlayer(args[1]);
					
					if(!bg.isBuilder(fp.getName()))
					{
						bg.addBuilder(new Builder(fp));
						message(sender, ChatColor.GREEN + fp.getName() + " is now a builder!");
					}
					
					else
					{
						message(sender, ChatColor.RED + fp.getName() + " is already a builder!");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You do not have permission for that!");
				}
			}
			
			else if(args[0].equals("delbuilder") || args[0].equals("-builder"))
			{
				if(sender.hasPermission(Final.PERM_MASTER))
				{
					if(args.length != 2)
					{
						message(sender, ChatColor.RED + "/bm -builder <bally>");
						return true;
					}
					
					if(!pu.canFindPlayer(args[1]))
					{
						message(sender, ChatColor.RED + "Cannot find '" + args[1] + "'");
						return false;
					}
					
					Player fp = pu.findPlayer(args[1]);
					
					if(bg.isBuilder(fp.getName()))
					{
						bg.delBuilder(fp.getName());
						message(sender, ChatColor.GREEN + fp.getName() + " is no longer a builder!");
						
						if(bg.isManager(fp.getName()))
						{
							bg.delManager(fp.getName());
							message(sender, ChatColor.YELLOW + fp.getName() + " is no longer a manager also!");
						}
					}
					
					else
					{
						message(sender, ChatColor.RED + fp.getName() + " is already a builder!");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You do not have permission for that!");
				}
			}
			
			else if(args[0].equals("addbuild") || args[0].equals("+build"))
			{
				if(bg.isManager(sender.getName()))
				{
					if(args.length >= 2)
					{
						String name = "";
						
						for(int i = 1; i < args.length; i++)
						{
							name = name + " " + args[i];
						}
						
						name = name.substring(1);
						
						if(bg.getBuild(name) != null)
						{
							message(sender, ChatColor.RED + "Build already exists " + ChatColor.DARK_RED + name);
							return true;
						}
						
						bg.addBuild(new Build(bg.newBuildId(), name, "", new ArrayList<Status>(), ((Player) sender).getLocation()));
						message(sender, ChatColor.AQUA + "Created new Build " + ChatColor.GREEN + name);
					}
					
					else
					{
						message(sender, ChatColor.RED + "/bm +build <multi space title>");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}
			
			else if(args[0].equals("delbuild") || args[0].equals("-build"))
			{
				if(bg.isManager(sender.getName()))
				{
					if(args.length >= 2)
					{
						String name = "";
						
						for(int i = 1; i < args.length; i++)
						{
							name = name + " " + args[i];
						}
						
						name = name.substring(1);
						
						if(bg.getBuild(name) != null)
						{
							bg.delBuild(name);
							message(sender, ChatColor.AQUA + "Deleted Build " + ChatColor.GREEN + name);
						}
						
						else
						{
							message(sender, ChatColor.RED + "Cannot find Build " + ChatColor.DARK_RED + name);
						}
					}
					 
					else
					{
						message(sender, ChatColor.RED + "/bm +build <multi space title>");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}
			
			else if(args[0].equals("select"))
			{
				if(bg.isManager(sender.getName()))
				{
					if(args.length >= 2)
					{
						String name = "";
						
						for(int i = 1; i < args.length; i++)
						{
							name = name + " " + args[i];
						}
						
						name = name.substring(1);
						
						if(bg.getBuild(name) != null)
						{
							bg.getManager(sender.getName()).setSelected(bg.getBuild(name).getId());
							message(sender, ChatColor.AQUA + "Selected Build " + ChatColor.GREEN + name);
						}
						
						else
						{
							message(sender, ChatColor.RED + "Cannot find Build " + ChatColor.DARK_RED + name);
						}
					}
					
					else
					{
						message(sender, ChatColor.RED + "/bm select <build name>");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}
			
			else if(args[0].equals("assign"))
			{
				if(bg.isManager(sender.getName()))
				{
					if(args.length != 2)
					{
						message(sender, ChatColor.RED + "/bm assign <player>");
						return true;
					}
					
					if(!pu.canFindPlayer(args[1]))
					{
						message(sender, ChatColor.RED + "Cannot find '" + args[1] + "'");
						return false;
					}
					
					Player fp = pu.findPlayer(args[1]);
					
					if(bg.isBuilder(fp.getName()))
					{
						Build selected = bg.getBuild(bg.getManager(sender.getName()).getSelected());
						
						if(selected == null)
						{
							message(sender, ChatColor.RED + "No build selected");
							return true;
						}
						
						if(bg.getBuilder(fp.getName()).isAssigned(selected))
						{
							message(sender, ChatColor.RED + fp.getName() + " is already assigned to " + selected.getTitle());
							return true;
						}
						
						bg.getBuilder(fp.getName()).addAssigned(selected);
						message(sender, ChatColor.AQUA + fp.getName() + " has been assigned to " + ChatColor.GREEN + selected.getTitle());
					}
					
					else
					{
						message(sender, ChatColor.RED + fp.getName() + " is not a builder!");
					}
				}
			}
			
			else if(args[0].equals("unassign"))
			{
				if(bg.isManager(sender.getName()))
				{
					if(args.length != 2)
					{
						message(sender, ChatColor.RED + "/bm unassign <bally>");
						return true;
					}
					
					if(!pu.canFindPlayer(args[1]))
					{
						message(sender, ChatColor.RED + "Cannot find '" + args[1] + "'");
						return false;
					}
					
					Player fp = pu.findPlayer(args[1]);
					
					if(bg.isBuilder(fp.getName()))
					{
						Build selected = bg.getBuild(bg.getManager(sender.getName()).getSelected());
						
						if(selected == null)
						{
							message(sender, ChatColor.RED + "No build selected");
							return true;
						}
						
						if(!bg.getBuilder(fp.getName()).isAssigned(selected))
						{
							message(sender, ChatColor.RED + fp.getName() + " is not assigned to " + selected.getTitle());
							return true;
						}
						
						bg.getBuilder(fp.getName()).delAssigned(selected);
						message(sender, ChatColor.AQUA + fp.getName() + " has been removed from " + ChatColor.GREEN + selected.getTitle());
					}
					
					else
					{
						message(sender, ChatColor.RED + fp.getName() + " is not a builder!");
					}
				}
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
	
	public void setBuildGroup(BuildGroup bg)
	{
		this.bg = bg;
	}
	
	public BuildGroup getBuildGroup()
	{
		return bg;
	}
}
