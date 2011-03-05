package com.dustinessington.gimme;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;


import com.dustinessington.gimme.gimmeConfiguration;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import com.dustinessington.gimme.ItemDb;


// permissions 2.4 imports
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * gimme for Bukkit
 *
 * @author aetaric
 */

public class gimme extends JavaPlugin
{
    private final gimmePlayerListener playerListener = new gimmePlayerListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    private final Logger log = Logger.getLogger("Minecraft");
    public static PermissionHandler Permissions = null;
    public static String logPrefix = "gimme";
    private Player base;
    private gimmeConfiguration confSetup;
    public static Configuration config;
    public static String id = null;

    public gimme(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) 
    {
    	
    }

    private void user(Player base)
	{
		this.base = base;
	}

    public void configInit()
	{
		getDataFolder().mkdirs();
		config = new Configuration(new File(this.getDataFolder(), "config.yml"));
		confSetup = new gimmeConfiguration(this.getDataFolder(), this);
	}
    
    public ItemStack getItemInHand()
	{
		return base.getItemInHand();
	}
 
    public void setupPermissions() 
	{
		Plugin agbs = this.getServer().getPluginManager().getPlugin("Permissions");
		PluginDescriptionFile pdfFile = this.getDescription();

		if (gimme.Permissions == null) 
		{
			if (agbs != null) 
			{
				this.getServer().getPluginManager().enablePlugin(agbs);
				gimme.Permissions = ((Permissions) agbs).getHandler();
				log.info(logPrefix + " version " + pdfFile.getVersion() + " Permissions detected...");
			}
			else 
			{
				log.severe(logPrefix + " version " + pdfFile.getVersion() + " not enabled. Permissions not detected");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
	}

    public boolean arraySearch(Player[] list, Player target) 
	{
		for (Player p : list)
		{
			if (p.equals(target)) 
			{
				return true;
			}
		}
		return false;
	}
    
	public static String make(String[] split, int startingIndex) 
	{
		for (; startingIndex < split.length; startingIndex++) 
		{
			if (startingIndex == 1)
				id += "" + split[startingIndex];
			else
				id += " " + split[startingIndex];
		}
		return id;
	}
    
    public boolean onCommand(CommandSender sender, Command commandArg, String commandLabel, String[] args) 
	{
		Player player = (Player) sender;
		Server server = getServer();
		String command = commandArg.getName().toLowerCase();

		if (command.equalsIgnoreCase("gimme")) 
		{
			if (gimme.Permissions.has(player, "gimme.gimme")) 
			{
				
				if (args.length > 2) 
				{
					gimme.config.load();
					String[] x = gimme.config.getString("denied").split(",");
					String[] itemArgs = args[0].split("[^a-zA-Z0-9]");
					ItemStack stack = null;
					try 
					{
						stack = ItemDb.get(itemArgs[0]);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
					if ((stack).contains(gimme.config.getString("denied").split(",")))
					{
						sender.sendMessage("Giving " + args[1] + " of " + args[0] + " to " + ((Player) sender).getDisplayName() + ".");
						((Player) sender).getInventory().addItem(stack);
					}
					
				} 
				else 
				{
					player.sendMessage("Correct usage is /gimme [id] [amount]");
				}
				
			} 
			else 
			{
				player.sendMessage("You don't have access to this command.");
				log.info(logPrefix + " " + player + " tried to use command " + command + "! denied access." );
			}
			return true;
		}
		return true;
	}
    
    public void onEnable() 
    {
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        setupPermissions();
        configInit();

        log.info(logPrefix + " version " + this.getDescription().getVersion() + " enabled!");
    }
    
    public void onDisable() 
    {
 
    	log.info(logPrefix + " version " + this.getDescription().getVersion() + " disabled!");
    }
    
    public boolean isDebugging(final Player player) 
    {
        if (debugees.containsKey(player)) 
        {
            return debugees.get(player);
        } 
        else 
        {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) 
    {
        debugees.put(player, value);
    }

}

