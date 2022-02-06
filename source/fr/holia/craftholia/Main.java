package fr.holia.craftholia;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.holia.craftholia.Main;
import fr.holia.craftholia.commands.Commands;

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

public class Main extends JavaPlugin implements Listener {
	
	private static Main instance;

	@Override
	public void onEnable() {
		
		print("CraftHolia version : " + Variables.version);
		print("Type of version : " + Variables.typeVersion);
		
		this.getServer().getPluginManager().registerEvents(this, this);
		
		getCommand("newevent").setExecutor(new Commands());
		getCommand("event").setExecutor(new Commands());
		getCommand("craftholia").setExecutor(new Commands());
		getCommand("spawn").setExecutor(new Commands());
		getCommand("setspawn").setExecutor(new Commands());
		
		Bukkit.getPluginManager().registerEvents(new Commands(), this);
		
		instance = this;
		
		checkConfigYML();
		
	}
	
	@Override
	public void onDisable() {
		print("[CraftHolia] Plugin disable, CraftHolia is created by Holia#3033 !");
	}
	
	public static Main getInstance() { return instance; }
		
	private void createFile(String fileName) {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		File file = new File(getDataFolder(), fileName + ".yml");
	
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
		
	public File getFile(String fileName) {
		return new File(getDataFolder(), fileName + ".yml");
	}
	
	public void print(String message) {
		System.out.print(message);
	}
	
	public void checkConfigYML() {
		if (!getFile("config").exists()) {
			
			print("[CraftHolia] Config file not found. CraftHolia create config.yml file for you ;)");
			
			createFile("config");
			
			FileConfiguration config = YamlConfiguration.loadConfiguration(getFile("config"));
			
			config.set("config.join_message", "&2%player% as join the server!");
			config.set("config.quit_message", "&2%player% as left the server!");
			
			config.options().header("Config file for CraftHolia\n\nIf you need help, go to https://github.com/holia-dev/CraftHolia");
			
			config.set("event.enable", false);
			config.set("event.prefix", "&f[&bEvent&f]");
			config.set("event.message", "A New event as launch!");
			config.set("event.prefix_cmd", "Type");
			config.set("event.suffix_cmd", "To join this event!");
			
			config.set("spawn.enable", true);
			
			/**/
			
			try {
				config.save(getFile("config"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			System.out.print("[CraftHolia] config.yml file as loaded to the server.");
		}
		
		if (!getFile("eventlist").exists()) {
			
			print("[CraftHoliaEvent] Eventlist file not found. CraftHolia create eventlist.yml for you ;)");
			
			createFile("eventlist");
			
			FileConfiguration eventlist = YamlConfiguration.loadConfiguration(getFile("eventlist"));
			
			eventlist.options().header("Event list for CraftHoliaEvent\n\nIf you need help, go to https://github.com/holia-dev/CraftHolia");
			eventlist.set("pvp.location.X", "12");
			eventlist.set("pvp.location.Y", "34");
			eventlist.set("pvp.location.Z", "56");
			eventlist.set("pvp.location.changeWorld.enable", false);
			eventlist.set("pvp.location.changeWorld.worldName", "world");
			
			try {
				eventlist.save(getFile("eventlist"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			print("[CraftHolia] eventlist.yml as loaded to the server.");
		}
		
		if (!getFile("spawn").exists()) {
			
			print("[CraftHoliaSpawn] Spawn file not found, CraftHolia create spawn.yml for you ;)");
			
			createFile("spawn");
			
			FileConfiguration spawn = YamlConfiguration.loadConfiguration(getFile("spawn"));
			
			spawn.options().header("Spawn config file for CraftHoliaSpawn\n\nIf you need help, go to https://github.com/holia-dev/CraftHolia");
			spawn.set("spawn.location.X", "100");
			spawn.set("spawn.location.Y", "100");
			spawn.set("spawn.location.Z", "100");
			spawn.set("spawn.location.changeWorld.enable", false);
			spawn.set("spawn.location.changeWorld.worldName", "world");
			spawn.set("spawn.spawn_on_join", true);
			
			try {
				spawn.save(getFile("spawn"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			print("[CraftHolia] spawn.yml as loaded to the server.");
		}
	}
	
	
	
	/*
	 * 
	 * Join and Left.  
	 * 
	 */
	
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		
		e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("config.join_message").replace("%player%", player.getName())));
		
		FileConfiguration spawn = YamlConfiguration.loadConfiguration(Main.getInstance().getFile("spawn"));
		
		String PositionX = spawn.getString("spawn.location.X");
		String PositionY = spawn.getString("spawn.location.Y");
		String PositionZ = spawn.getString("spawn.location.Z");
		
		World SpawnWorldName = Main.getInstance().getServer().getWorld(spawn.getString("spawn.location.changeWorld.worldName"));
		
		double XPosition = Double.parseDouble(PositionX);
		double YPosition = Double.parseDouble(PositionY);
		double ZPosition = Double.parseDouble(PositionZ);
		
		if (spawn.getString("spawn.spawn_on_join") == "true") {
			if (spawn.getString("spawn.location.changeWorld.enable") == "true") {
				if (Main.getInstance().getServer().getPluginManager().getPlugin("Multiverse-Core") != null) {
					Location spawnLocation = new Location(SpawnWorldName, XPosition, YPosition, ZPosition);
					player.teleport(spawnLocation);
				} else {
					player.sendMessage(ChatColor.RED + "Error occured with Multiverse-Core and CraftHolia");
					System.out.print("Error, Multiverse-Core is not installed on this server.");
				}
			} else if (spawn.getString("spawn.location.changeWorld.enable") == "false") {
				Location spawnLocation = new Location(Bukkit.getWorld("world"), XPosition, YPosition, ZPosition);
				player.teleport(spawnLocation);
			}
		} else {
			// SpawnOnJoin is disable.
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("config.quit_message").replace("%player%", player.getName())));
	}
}
