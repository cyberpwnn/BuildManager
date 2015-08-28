package com.ulticraft.buildmanager;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.ulticraft.buildmanager.group.Build;
import com.ulticraft.buildmanager.group.BuildGroup;
import com.ulticraft.buildmanager.group.Builder;
import com.ulticraft.buildmanager.group.Deadline;
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
		bc.outputSpreadsheet(bg);
	}

	@Override
	public void onDisable()
	{
		bc.save(bg);
	}

	public void message(CommandSender sender, String msg)
	{
		if(bg.isManager(sender.getName()))
		{
			if(bg.getBuild(bg.getManager(sender.getName()).getSelected()) != null)
			{
				sender.sendMessage(String.format(Final.TAG_F, bg.getBuild(bg.getManager(sender.getName()).getSelected()).getTitle()) + msg);
				return;
			}
		}

		sender.sendMessage(Final.TAG + msg);
	}

	public void notifyBuilders(String msg)
	{
		for(Builder i : bg.getBuilders())
		{
			for(Player j : getServer().getOnlinePlayers())
			{
				if(j.getUniqueId().toString().equals(i.getUuid()))
				{
					j.sendMessage(Final.TAG + msg);
				}
			}
		}
	}

	public void notifyManagers(String msg)
	{
		for(Manager i : bg.getManagers())
		{
			for(Player j : getServer().getOnlinePlayers())
			{
				if(j.getUniqueId().toString().equals(i.getUuid()))
				{
					j.sendMessage(Final.TAG + msg);
				}
			}
		}
	}

	public void notifyBuilders(Build build, String msg)
	{
		for(Builder i : bg.getBuilders())
		{
			for(Player j : getServer().getOnlinePlayers())
			{
				if(j.getUniqueId().toString().equals(i.getUuid()) && i.getAssigned().contains(new Integer(build.getId())))
				{
					j.sendMessage(Final.TAG + msg);
				}
			}
		}
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
					sendBuilds((Player) sender);
				}

				else if(sender.hasPermission(Final.PERM_MASTER))
				{
					sender.sendMessage(Final.HR);
					message(sender, "Your an admin, You can only do 2 things!");
					message(sender, "/bm +manager <player>");
					message(sender, "/bm -manager <bally>");
					sender.sendMessage(Final.HR);
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
						notifyManagers(ChatColor.YELLOW + sender.getName() + " added manager " + fp.getName());
						bg.addManager(new Manager(fp));
						message(sender, ChatColor.GREEN + fp.getName() + " is now a manager!");

						if(!bg.isBuilder(fp.getName()))
						{
							bg.addBuilder(new Builder(fp));
							message(sender, ChatColor.YELLOW + fp.getName() + " is now a builder also!");
						}

						message(fp, ChatColor.GREEN + "You are now a Build Manager!");
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
							message(sender, ChatColor.YELLOW + fp.getName() + " is still a builder!");
						}

						message(fp, ChatColor.GREEN + "You are no longer a Build Manager!");
						notifyManagers(ChatColor.YELLOW + sender.getName() + " removed manager " + fp.getName());
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
						notifyManagers(ChatColor.YELLOW + sender.getName() + " added builder " + fp.getName());
						bg.addBuilder(new Builder(fp));
						message(sender, ChatColor.GREEN + fp.getName() + " is now a builder!");
						message(fp, ChatColor.GREEN + "You are now a Builder!");
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

						else
						{
							message(fp, ChatColor.GREEN + "You are no longer a Builder!");
						}

						message(fp, ChatColor.GREEN + "You are no longer a Builder!");
						notifyManagers(ChatColor.YELLOW + sender.getName() + " removed builder " + fp.getName());
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
						notifyManagers(ChatColor.YELLOW + sender.getName() + " added build " + ChatColor.GREEN + name);
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
							notifyManagers(ChatColor.YELLOW + sender.getName() + " removed build " + ChatColor.RED + name);
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

			else if(args[0].equals("select") || args[0].equals("sel") || args[0].equals("s"))
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

			else if(args[0].equals("unselect") || args[0].equals("uns"))
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
						message(fp, ChatColor.GREEN + "You were assigned to build " + ChatColor.AQUA + selected.getTitle());
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
						message(fp, ChatColor.RED + "You were removed from build " + ChatColor.AQUA + selected.getTitle());
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

			else if(args[0].equals("finished") || args[0].equals("finish") || args[0].equals("done"))
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
						notifyBuilders(selected, ChatColor.GREEN + sender.getName() + " marked " + ChatColor.AQUA + selected.getTitle() + ChatColor.GREEN + " as Finished!");
					}
				}

				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}

			else if(args[0].equals("reopen") || args[0].equals("open") || args[0].equals("edit"))
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
						notifyBuilders(selected, ChatColor.GREEN + sender.getName() + " re-opened " + ChatColor.AQUA + selected.getTitle());
					}
				}

				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}

			else if(args[0].equals("setdeadline") || args[0].equals("due"))
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
						selected.setDeadline(new Deadline(args[1], sender));

						message(sender, ChatColor.GREEN + "Due Date set to " + selected.getDeadline().format());
						notifyBuilders(selected, ChatColor.GREEN + sender.getName() + " set " + ChatColor.AQUA + selected.getTitle() + ChatColor.GREEN + " due on " + selected.getDeadline().format());
					}

					else
					{
						message(sender, ChatColor.GREEN + "/bm setdeadline <m/d/y>");
					}
				}

				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}

			else if(args[0].equals("setdesc") || args[0].equals("desc"))
			{
				if(bg.isManager(sender.getName()))
				{
					if(args.length < 2)
					{
						message(sender, ChatColor.RED + "/bm setdesc <build description>");
						return true;
					}

					Build selected = bg.getBuild(bg.getManager(sender.getName()).getSelected());

					if(selected == null)
					{
						message(sender, ChatColor.RED + "No build selected");
						return true;
					}

					String name = "";

					for(int i = 1; i < args.length; i++)
					{
						name = name + " " + args[i];
					}

					name = name.substring(1);

					selected.setDescription(name);
					message(sender, ChatColor.GREEN + "Set description to " + ChatColor.AQUA + name);
					notifyBuilders(selected, ChatColor.GREEN + sender.getName() + " changed " + ChatColor.AQUA + selected.getTitle() + ChatColor.GREEN + " description.");
				}

				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}

			else if(args[0].equals("loc") || args[0].equals("setwarp"))
			{
				if(bg.isManager(sender.getName()))
				{
					Build selected = bg.getBuild(bg.getManager(sender.getName()).getSelected());

					if(selected == null)
					{
						message(sender, ChatColor.RED + "No build selected");
						return true;
					}

					Player p = (Player) sender;
					Location l = p.getLocation();
					SerializableLocation s = new SerializableLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getPitch(), l.getYaw(), l.getWorld().getName());
					selected.setLocation(s);

					message(sender, ChatColor.GREEN + "Set warp to " + ChatColor.AQUA + s.toString());
					notifyBuilders(selected, ChatColor.GREEN + sender.getName() + " changed " + ChatColor.AQUA + selected.getTitle() + ChatColor.GREEN + " warp.");
				}

				else
				{
					message(sender, ChatColor.RED + "You aren't a manager!");
				}
			}

			else if(args[0].equals("w"))
			{
				if(args.length != 2)
				{
					message(sender, ChatColor.RED + "Use /bm and CLICK the colored elements! /bm w <element>");
					return true;
				}

				if(bg.isManager(sender.getName()))
				{
					if(bg.getBuild(Integer.valueOf(args[1])) != null)
					{
						SerializableLocation s = bg.getBuild(Integer.valueOf(args[1])).getLocation();
						World w = getServer().getWorld(s.getWorld());
						Location l = new Location(w, s.getX(), s.getY(), s.getZ(), (float) s.getW(), (float) s.getP());
						((Player) sender).teleport(l);
						return true;
					}
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
						spacePad(sender);
						sender.sendMessage(Final.HR);
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

							if(cm.substring(cm.length() - 2, cm.length()).equals(", "))
							{
								cm = cm.substring(0, cm.length() - 2);
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

						sendBuilds((Player) sender);

						return true;
					}
				}

				if(bg.isBuilder(sender.getName()))
				{
					if(args[1].equals("builds"))
					{
						spacePad(sender);
						sender.sendMessage(Final.HR);
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

							if(cm.length() > 2 && cm.substring(cm.length() - 2, cm.length()).equals(", "))
							{
								cm = cm.substring(0, cm.length() - 2);
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

						sendBuilds((Player) sender);
					}

					else if(args[1].equals("mybuilds"))
					{
						spacePad(sender);
						sender.sendMessage(Final.HR);
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

							if(cm.substring(cm.length() - 2, cm.length()).equals(", "))
							{
								cm = cm.substring(0, cm.length() - 2);
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

						sendBuilds((Player) sender);
					}

					else if(args[1].equals("build"))
					{
						if(args.length == 3)
						{
							spacePad(sender);
							sender.sendMessage(Final.HR);
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

								new RawText().addTextWithHoverCommand(" [BACK]  ", RawText.COLOR_LIGHT_PURPLE, "/bm q mybuilds", "Go back to YOUR Builds", RawText.COLOR_GOLD).addTextWithHover(b.getTitle(), RawText.COLOR_AQUA, b.getDescription(), RawText.COLOR_GREEN).addText(" " + dd, rt).tellRawTo(this, (Player) sender);
								new RawText().addTextWithHoverCommand(" [WARP]  ", RawText.COLOR_RED, "/bm w " + id, "Warp to " + b.getTitle(), RawText.COLOR_GOLD).tellRawTo(this, (Player) sender);
								sendBuilds((Player) sender);
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

		else if(bg.isBuilder(p.getName()))
		{
			p.sendMessage(Final.HR);
			new RawText().addTextWithHoverCommand("         My Builds", RawText.COLOR_AQUA, "/bm q mybuilds", bg.getBuilder(p.getName()).getAssigned().size() + " Builds", RawText.COLOR_AQUA).tellRawTo(this, p);
			p.sendMessage(Final.HR);
		}
	}

	public void spacePad(CommandSender sender)
	{
		for(int i = 0; i < 18; i++)
		{
			sender.sendMessage(" ");
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
