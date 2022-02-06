package fr.holia.craftholia.commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.holia.craftholia.Main;
import fr.holia.craftholia.Variables;

/*
 * 
 * 
 * CraftHolia Project.
 * Created by Holia#3033
 * 
 * Website of the source :
 * https://github.com/holia-dev/CraftHolia
 * 
 * 
 */

public class Commands implements Listener, CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	
		if (label.equalsIgnoreCase("newevent")) {
    		if (sender.isOp()) {
    			if (args.length == 0) {
    				sender.sendMessage(ChatColor.RED + "Usage: /newevent <Type>");
    			} else if (args.length == 1) {
    				
    				if (!Main.getInstance().getFile("eventlist").exists()) {
    					sender.sendMessage(ChatColor.RED + "eventlist.yml as not found on your server! Please restart !");
    				} else {
    					
    					FileConfiguration eventlist = YamlConfiguration.loadConfiguration(Main.getInstance().getFile("eventlist"));
    					
    					if (eventlist.getString(args[0]) == null) {
    						sender.sendMessage(ChatColor.RED + "craftholiaevent: The Event " + args[0] + " not exist on eventlist.yml");
    					} else {
    						if (Main.getInstance().getConfig().getString("event.enable") == "true") {
    	    					String prefix = Main.getInstance().getConfig().getString("event.prefix");
    	            			String message = Main.getInstance().getConfig().getString("event.message");
    	            			
    	            			String prefixEventCommand = Main.getInstance().getConfig().getString("event.prefix_cmd");
    	            			String suffixEventCommand = Main.getInstance().getConfig().getString("event.suffix_cmd");
    	            			
    	            			String completeMessage = ChatColor.translateAlternateColorCodes('&', prefixEventCommand) + ChatColor.YELLOW + " /event " + args[0] + " " +  ChatColor.translateAlternateColorCodes('&', suffixEventCommand);
    	            			
    	            			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix));
    	            			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    	            			
    	            			Bukkit.broadcastMessage(completeMessage);
    	    				} else {
    	    					sender.sendMessage(ChatColor.RED + "CraftHoliaEvent is not enabled on this server, change this to config.yml");
    	    				}
    					}
    					
    				}
    			} else {
    				sender.sendMessage(ChatColor.RED + "Usage: /newevent <Type>");
    			}
    		} else {
    			sender.sendMessage(ChatColor.RED + "You must have the permission MINECRAFT.OP");
    		}
    	} else if (label.equalsIgnoreCase("event")) {
    		if (args.length == 0) {
    			sender.sendMessage(ChatColor.RED + "Usage : /event <Type>");
    		} else if (args.length == 1) {
    			FileConfiguration eventlist = YamlConfiguration.loadConfiguration(Main.getInstance().getFile("eventlist"));
    			
    			if (Main.getInstance().getConfig().getString("event.enable") == "true") {
	    			if (!Main.getInstance().getFile("eventlist").exists()) {
	    				sender.sendMessage(ChatColor.RED + "Error ! eventlist.yml not found ! Please restart !");
	    			} else {
	    				if (eventlist.getString(args[0]) == null) {
	    					sender.sendMessage(ChatColor.RED + "craftholiaevent: The Event " + args[0] + " not found on eventlist.yml");
	    				} else {
	    					String PositionX = eventlist.getString(args[0] + ".location.X");
	    					String PositionY = eventlist.getString(args[0] + ".location.Y");
	    					String PositionZ = eventlist.getString(args[0] + ".location.Z");
	    					
	    					World WorldName = Main.getInstance().getServer().getWorld(eventlist.getString(args[0] + ".location.changeWorld.worldName"));
	    					
	    					double XPosition = Double.parseDouble(PositionX);
	    					double YPosition = Double.parseDouble(PositionY);
	    					double ZPosition = Double.parseDouble(PositionZ);
	    					
	    					Player player = (Player) sender;
	    					
	    					// sender.sendMessage("X:" + PositionX + " Y:" + PositionY + " Z:" + PositionZ); // Check XYZ Position.
	    					
	    					if (eventlist.getString(args[0] + ".location.changeWorld.enable") == "true") {
	    						if (Main.getInstance().getServer().getPluginManager().getPlugin("Multiverse-Core") != null) {
	    							Location location = new Location(WorldName, XPosition, YPosition, ZPosition);
		    						player.teleport(location);
	    						} else {
	    							System.out.print("[CraftHolia] To change the world, you must have Multiverse Core plugin installed.");
	    							if (sender.isOp()) {
	        							sender.sendMessage(ChatColor.RED + "craftholia: Please view the server console.");	
	    							} else {
	    								sender.sendMessage(ChatColor.RED + "Contact a Administrator with this error : CRAFTHOLIA.ERROR.CHANGEWORLD.MULTIVERSE");
	    							}
	    						}
	    					} else if (eventlist.getString(args[0] + ".location.changeWorld.enable") == "false") {
	    						Location location = new Location(Bukkit.getWorld("world"), XPosition, YPosition, ZPosition);
	    						player.teleport(location);
	    					} else {
	    						sender.sendMessage(ChatColor.RED + "Error in eventlist.yml, please change the value of " + args[0] + ".location.changeWorld.enable !");
	    					}
	    				}
	    			}
    			} else {
    				sender.sendMessage(ChatColor.RED + "CraftHoliaEvent is not enabled on this server, change this to config.yml");
    			}
    			
    		} else {
    			sender.sendMessage(ChatColor.RED + "Usage: /event <Type>");
    		}
    		
    	}
    	else if (label.equalsIgnoreCase("craftholia")) {
    		if (args.length == 0) {
    			sender.sendMessage("CraftHolia version " + Variables.version);
        		sender.sendMessage("CraftHolia is created by Holia#3033");
        		sender.sendMessage("More info to : github.com/holia-dev/CraftHolia");
    		} else if (args.length == 1) {
	    		if (args[0].equalsIgnoreCase("reload")) {
	    			if (sender.isOp()) {
	    				FileConfiguration eventlist = YamlConfiguration.loadConfiguration(Main.getInstance().getFile("eventlist"));
		    			FileConfiguration config = YamlConfiguration.loadConfiguration(Main.getInstance().getFile("config"));
		    			FileConfiguration spawn = YamlConfiguration.loadConfiguration(Main.getInstance().getFile("spawn"));
		    					    			
		    			sender.sendMessage("CraftHolia as reload!");
	    			} else {
	    				sender.sendMessage(ChatColor.RED + "You must have MINECRAFT.OP permision to use this command!");
	    			}
	    		} else if (args[0].equalsIgnoreCase("help")) {
    				sender.sendMessage("To get help, go to https://github.com/holia-dev/CraftHolia");
    			}
    		} else {
    			sender.sendMessage("Undefined command, type /craftholia help.");
    		}
    	}
		
    	else if (label.equalsIgnoreCase("spawn")) {
    		if (Main.getInstance().getFile("spawn").exists()) {
    			
    			if (Main.getInstance().getConfig().getString("spawn.enable") == "true") {
    				
    				FileConfiguration spawn = YamlConfiguration.loadConfiguration(Main.getInstance().getFile("spawn"));
    				
    				String PositionX = spawn.getString("spawn.location.X");
    				String PositionY = spawn.getString("spawn.location.Y");
    				String PositionZ = spawn.getString("spawn.location.Z");
    				
    				World SpawnWorldName = Main.getInstance().getServer().getWorld(spawn.getString("spawn.location.changeWorld.worldName"));
    				
    				double XPosition = Double.parseDouble(PositionX);
    				double YPosition = Double.parseDouble(PositionY);
    				double ZPosition = Double.parseDouble(PositionZ);
    				
    				Player player = (Player) sender;
	    			
	    			if (spawn.getString("spawn.location.changeWorld.enable") == "true") {
		    			if (Main.getInstance().getServer().getPluginManager().getPlugin("Multiverse-Core") != null) {
		    				Location spawnLocation = new Location(SpawnWorldName, XPosition, YPosition, ZPosition);
		    				player.teleport(spawnLocation);
		    			} else {
		    				System.out.print("[CraftHoliaSpawn] Multiverse-Core plugin is not instaled on this server, please install this to use changeWorld function.");
		    			}
	    			} else if (spawn.getString("spawn.location.changeWorld.enable") == "false") {  				
	    				Location spawnLocation = new Location(Bukkit.getWorld("world"), XPosition, YPosition, ZPosition);
	    				player.teleport(spawnLocation);
	    			} else {
	    				sender.sendMessage("Please enter true or false in spawn.location.changeWorld.enable!");
	    			}
    			} else {
    				sender.sendMessage("CraftHoliaSpawn is not enabled on this server!");
    			}
    			
    		} else {
    			sender.sendMessage(ChatColor.RED + "Error ! spawn.yml file not found! Restart your server !");
    		}
    	}
		
    	else if (label.equalsIgnoreCase("setspawn")) {    		
    		if (sender.isOp()) {
    			FileConfiguration spawn = YamlConfiguration.loadConfiguration(Main.getInstance().getFile("spawn"));
    			Player player = (Player) sender;
    			
    			spawn.set("spawn.location.X", player.getLocation().getX());
    			spawn.set("spawn.location.Y", player.getLocation().getY());
    			spawn.set("spawn.location.Z", player.getLocation().getZ());
    			spawn.set("spawn.location.changeWorld.worldName", player.getWorld().getName());
    			
    			try {
					spawn.save(Main.getInstance().getFile("spawn"));
					sender.sendMessage("Server Spawn location as changed!");
					sender.sendMessage("Type /setworldspawn to change the world spawn.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    		} else {
    			sender.sendMessage("You must have MINECRAFT.OP permission to use this command!");
    		}
    	}
    	
    	return false;
	}

}
