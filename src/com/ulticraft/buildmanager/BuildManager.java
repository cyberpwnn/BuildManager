package com.ulticraft.buildmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.ulticraft.buildmanager.group.Build;
import com.ulticraft.buildmanager.group.BuildGroup;
import com.ulticraft.buildmanager.group.Builder;
import com.ulticraft.buildmanager.group.Manager;
import com.ulticraft.buildmanager.group.SerializableLocation;
import com.ulticraft.buildmanager.group.Status;
import com.ulticraft.core.PlayerUtils;
import com.ulticraft.core.RawText;

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
					sendBuilds((Player) sender);
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
				if(bg.isManager(sender.getName()))
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
					message(sender, ChatColor.RED + "You aren't a build manager!");
				}
			}
			
			else if(args[0].equals("delbuilder") || args[0].equals("-builder"))
			{
				if(bg.isManager(sender.getName()))
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
					message(sender, ChatColor.RED + "You aren't a build manager!");
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
						
						Location l = ((Player) sender).getLocation();
						SerializableLocation s = new SerializableLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getPitch(), l.getYaw(), l.getWorld().getName());
						
						bg.addBuild(new Build(bg.newBuildId(), name, "", new ArrayList<Status>(), s));
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
			
			else if(args[0].equals("unselect"))
			{
				if(bg.isManager(sender.getName()))
				{
					bg.getManager(sender.getName()).setSelected(0);
					message(sender, ChatColor.AQUA + "Unselected Build");
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
						bg.getManager(sender.getName()).addManagedBuild(selected);
					}
					
					else
					{
						message(sender, ChatColor.RED + fp.getName() + " is not a builder!");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
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
						message(sender, ChatColor.AQUA + fp.getName() + " removed from " + ChatColor.GREEN + selected.getTitle());
						bg.getManager(sender.getName()).delManagedBuild(selected);
					}
					
					else
					{
						message(sender, ChatColor.RED + fp.getName() + " is not a builder!");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}
			
			else if(args[0].equals("finished"))
			{
				if(bg.isManager(sender.getName()))
				{
					Build selected = bg.getBuild(bg.getManager(sender.getName()).getSelected());
					if(selected == null)
					{
						message(sender, ChatColor.RED + "No build selected");
						return true;
					}
					
					if(selected.isFinished())
					{
						message(sender, ChatColor.RED + "Build was already finished! Use /bm reopen");
					}
					
					else
					{
						selected.setFinished(true);
						message(sender, ChatColor.GREEN + selected.getTitle() + ChatColor.AQUA + " build finished!");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}
			
			else if(args[0].equals("reopen"))
			{
				if(bg.isManager(sender.getName()))
				{
					Build selected = bg.getBuild(bg.getManager(sender.getName()).getSelected());
					if(selected == null)
					{
						message(sender, ChatColor.RED + "No build selected");
						return true;
					}
					
					if(!selected.isFinished())
					{
						message(sender, ChatColor.RED + "Build is still open!");
					}
					
					else
					{
						selected.setFinished(false);
						message(sender, ChatColor.GREEN + selected.getTitle() + ChatColor.AQUA + " has been reopened!");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}
			
			else if(args[0].equals("setdeadline"))
			{
				if(bg.isManager(sender.getName()))
				{
					Build selected = bg.getBuild(bg.getManager(sender.getName()).getSelected());
					if(selected == null)
					{
						message(sender, ChatColor.RED + "No build selected");
						return true;
					}
					
					if(args.length == 2)
					{
						int d = Integer.valueOf(args[1]);
						
						selected.setDeadline(new Date(d * 1000 * 60 * 60 * 24 + new Date().getTime()));
						
						message(sender, ChatColor.RED + "Due Date set " + d + " days from now.");
					}
					
					else
					{
						message(sender, ChatColor.GREEN + "/bm setdeadline <days from now>");
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}
			
			else if(args[0].equals("q"))
			{
				if(!(args.length >= 2))
				{
					message(sender, ChatColor.RED + "Use /bm and CLICK the colored elements! /bm q <element>");
					return true;
				}
				
				if(bg.isManager(sender.getName()))
				{
					if(args[1].equals("assigned"))
					{
						for(int i : bg.getManager(sender.getName()).getManaging())
						{
							Build b = bg.getBuild(i);
							String desc = b.getDescription();
							int cont = 0;
							String cm = "";
							int mi = 0;
							
							for(Builder j : bg.getBuilders())
							{
								if(j.getAssigned().contains(i))
								{
									cont++;
									mi++;
									
									if(mi < 5)
									{
										if(mi == 4)
										{
											cm = cm + j.getName();
										}
										
										else
										{
											cm = cm + j.getName() + ", ";
										}
									}
								}
							}
							
							if(cm.substring(cm.length()-2, cm.length()).equals(", "))
							{
								cm = cm.substring(0, cm.length()-2);
							}
							
							if(cont > 4)
							{
								cm = cm + " + " + cont + " more";
							}
							
							if(desc == null || desc.equals(""))
							{
								desc = "No Description :[";
							}
							
							String dd = "No Deadline";
							String rt;
							
							if(b.getDeadline() != null)
							{
								dd = b.getDeadline().format();
							}
							
							if(dd.equals("LATE"))
							{
								rt = RawText.COLOR_RED;
							}
							
							else if(dd.equalsIgnoreCase("No Deadline"))
							{
								rt = RawText.COLOR_GREEN;
							}
							
							else
							{
								rt = RawText.COLOR_YELLOW;
							}
							
							if(b.isFinished())
							{
								rt = RawText.COLOR_GREEN;
								dd = "Completed";
							}
							
							new RawText().addTextWithHoverCommand(" " + b.getTitle() + " ", RawText.COLOR_GREEN, "/bm q build " + i, b.getDescription(), RawText.COLOR_GREEN).addTextWithHover(cont + " Builders ", RawText.COLOR_AQUA, cm, RawText.COLOR_AQUA).addText(dd, rt).tellRawTo(this, (Player) sender);
						}
						
						return true;
					}
				}
				
				if(bg.isBuilder(sender.getName()))
				{
					if(args[1].equals("builds"))
					{
						for(Build b : bg.getBuilds())
						{
							String desc = b.getDescription();
							int cont = 0;
							String cm = "";
							int mi = 0;
							
							for(Builder j : bg.getBuilders())
							{
								if(j.getAssigned().contains(b.getId()))
								{
									cont++;
									mi++;
									
									if(mi < 5)
									{
										if(mi == 4)
										{
											cm = cm + j.getName();
										}
										
										else
										{
											cm = cm + j.getName() + ", ";
										}
									}
								}
							}
							
							if(cm.length() > 2 && cm.substring(cm.length()-2, cm.length()).equals(", "))
							{
								cm = cm.substring(0, cm.length()-2);
							}
							
							if(cont > 4)
							{
								cm = cm + " + " + cont + " more";
							}
							
							if(desc == null || desc.equals(""))
							{
								desc = "No Description :[";
							}
							
							if(cm == "")
							{
								cm = "No Builders :[";
							}
							
							String dd = "No Deadline";
							String rt;
							
							if(b.getDeadline() != null)
							{
								dd = b.getDeadline().format();
							}
							
							if(dd.equals("LATE"))
							{
								rt = RawText.COLOR_RED;
							}
							
							else if(dd.equalsIgnoreCase("No Deadline"))
							{
								rt = RawText.COLOR_GREEN;
							}
							
							else
							{
								rt = RawText.COLOR_YELLOW;
							}
							
							if(b.isFinished())
							{
								rt = RawText.COLOR_GREEN;
								dd = "Completed";
							}
							
							new RawText().addTextWithHoverCommand(" " + b.getTitle() + " ", RawText.COLOR_GREEN, "/bm q build " + b.getId(), b.getDescription(), RawText.COLOR_GREEN).addTextWithHover(cont + " Builders ", RawText.COLOR_AQUA, cm, RawText.COLOR_AQUA).addText(dd, rt).tellRawTo(this, (Player) sender);
						}
					}
					
					else if(args[1].equals("mybuilds"))
					{
						for(int i : bg.getBuilder(sender.getName()).getAssigned())
						{
							Build b = bg.getBuild(i);
							String desc = b.getDescription();
							int cont = 0;
							String cm = "";
							int mi = 0;
							
							for(Builder j : bg.getBuilders())
							{
								if(j.getAssigned().contains(b.getId()))
								{
									cont++;
									mi++;
									
									if(mi < 5)
									{
										if(mi == 4)
										{
											cm = cm + j.getName();
										}
										
										else
										{
											cm = cm + j.getName() + ", ";
										}
									}
								}
							}
							
							if(cm.substring(cm.length()-2, cm.length()).equals(", "))
							{
								cm = cm.substring(0, cm.length()-2);
							}
							
							if(cont > 4)
							{
								cm = cm + " + " + cont + " more";
							}
							
							if(desc == null || desc.equals(""))
							{
								desc = "No Description :[";
							}
							
							String dd = "No Deadline";
							String rt;
							
							if(b.getDeadline() != null)
							{
								dd = b.getDeadline().format();
							}
							
							if(dd.equals("LATE"))
							{
								rt = RawText.COLOR_RED;
							}
							
							else if(dd.equalsIgnoreCase("No Deadline"))
							{
								rt = RawText.COLOR_GREEN;
							}
							
							else
							{
								rt = RawText.COLOR_YELLOW;
							}
							
							if(b.isFinished())
							{
								rt = RawText.COLOR_GREEN;
								dd = "Completed";
							}
							
							new RawText().addTextWithHoverCommand(" " + b.getTitle() + " ", RawText.COLOR_GREEN, "/bm q build " + b.getId(), b.getDescription(), RawText.COLOR_GREEN).addTextWithHover(cont + " Builders ", RawText.COLOR_AQUA, cm, RawText.COLOR_AQUA).addText(dd, rt).tellRawTo(this, (Player) sender);
						}
					}
					
					else if(args[1].equals("build"))
					{
						if(args.length == 3)
						{
							int id = Integer.valueOf(args[2]);
							
							if(bg.getBuild(id) != null)
							{
								Build b = bg.getBuild(id);
								
								String dd = "No Deadline";
								String rt;
								
								if(b.getDeadline() != null)
								{
									dd = b.getDeadline().format();
								}
								
								if(dd.equals("LATE"))
								{
									rt = RawText.COLOR_RED;
								}
								
								else if(dd.equalsIgnoreCase("No Deadline"))
								{
									rt = RawText.COLOR_GREEN;
								}
								
								else
								{
									rt = RawText.COLOR_YELLOW;
								}
								
								if(b.isFinished())
								{
									rt = RawText.COLOR_GREEN;
									dd = "Completed";
								}
								
								message(sender, Final.HR);
								new RawText().addTextWithHover("                    " + b.getTitle(), RawText.COLOR_AQUA, b.getDescription(), RawText.COLOR_GREEN).addText(" " + dd, rt).tellRawTo(this, (Player) sender);
								message(sender, Final.HR);
							}
						}
					}
				}
				
				else
				{
					message(sender, ChatColor.RED + "You aren't a builder or manager!");
				}
			}
		}
		
		return false;
	}
	
	public void sendBuilds(Player p)
	{
		if(bg.isManager(p.getName()))
		{
			p.sendMessage(Final.HR);
			new RawText().addTextWithHoverCommand("         Builds ", RawText.COLOR_GREEN, "/bm q builds", bg.getBuilds().size() + " Total Builds", RawText.COLOR_DARK_GREEN).addTextWithHoverCommand("Assigned ", RawText.COLOR_YELLOW, "/bm q assigned", bg.getManager(p.getName()).getManaging().size() + " Assigned", RawText.COLOR_GOLD).addTextWithHoverCommand("My Builds", RawText.COLOR_AQUA, "/bm q mybuilds", bg.getBuilder(p.getName()).getAssigned().size() + " Builds", RawText.COLOR_AQUA).tellRawTo(this, p);
			p.sendMessage(Final.HR);
		}
		
		else if(bg.isManager(p.getName()))
		{
			p.sendMessage(Final.HR);
			new RawText().addTextWithHoverCommand("              Builds ", RawText.COLOR_GREEN, "/bm q builds", bg.getBuilds().size() + " Total Builds", RawText.COLOR_DARK_GREEN).addTextWithHoverCommand("My Builds", RawText.COLOR_AQUA, "/bm q mybuilds", bg.getBuilder(p.getName()).getAssigned().size() + " Builds", RawText.COLOR_AQUA).tellRawTo(this, p);
			p.sendMessage(Final.HR);
		}
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
