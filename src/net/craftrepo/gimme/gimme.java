package net.craftrepo.gimme;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

import net.craftrepo.gimme.gimmeConfiguration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

// permissions 2.4 imports
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * CraftRepo gimme for Bukkit
 * @author AllGamer
 * 
 * Copyright 2011 AllGamer, LLC.
 * See LICENSE for licensing information.
 */

public class gimme extends JavaPlugin
{
	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	private final Logger log = Logger.getLogger("Minecraft");
	public static PermissionHandler Permissions = null;
	public static String logPrefix = "[Gimme]";
	private gimmeConfiguration confSetup;
	public gimme plugin;
	public static Configuration config;
	public static String id = null;
	public int amount = 64;

	public void configInit()
	{
		getDataFolder().mkdirs();
		config = new Configuration(new File(this.getDataFolder(), "config.yml"));
		confSetup = new gimmeConfiguration(this.getDataFolder(), this);
	}

	public void setupPermissions() 
	{
		Plugin perms = this.getServer().getPluginManager().getPlugin("Permissions");
		PluginDescriptionFile pdfFile = this.getDescription();

		if (gimme.Permissions == null) 
		{
			if (perms != null) 
			{
				this.getServer().getPluginManager().enablePlugin(perms);
				gimme.Permissions = ((Permissions) perms).getHandler();
				log.info(logPrefix + " version " + pdfFile.getVersion() + " Permissions detected...");
			}
			else 
			{
				log.severe(logPrefix + " version " + pdfFile.getVersion() + " not enabled. Permissions not detected.");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
	}

	public boolean itemdeny(int args)
	{
		gimme.config.load();
		String x = gimme.config.getNodeList("denied", null).toString();
		log.info(logPrefix + " " + Integer.toString(args));
		if (x.contains(Integer.toString(args)))
		{
			return true;
		}
		return false;
	}

	public boolean onCommand(CommandSender sender, Command commandArg, String commandLabel, String[] arg) 
	{
		Player player = (Player) sender;
		PlayerInventory inventory = player.getInventory();
		String command = commandArg.getName().toLowerCase();
		if (command.equalsIgnoreCase("gimme")) 
		{
			if (player.isOp() || gimme.Permissions.has(player, "gimme.gimme") || (gimme.Permissions.has(player, "gimme.*") || gimme.Permissions.has(player, "*"))) 
			{
					ItemStack itemstack = new ItemStack(Integer.valueOf(arg[0]));
					@SuppressWarnings("unused")
					boolean check = itemdeny(Integer.valueOf(arg[0]));
					if (!(itemdeny(Integer.valueOf(arg[0]))))
					{
						if (arg.length == 1) 
						{
							itemstack.setAmount(amount);
						}
						if (arg.length == 2)
						{
							itemstack.setAmount(Integer.parseInt(arg[1]));
						}
							player.sendMessage("Here you go!");
							inventory.addItem(itemstack);
					}
				}
				else
				{
					player.sendMessage("Correct usage is /gimme [item] {amount}");
				}
			} 
			else 
			{
				player.sendMessage("You don't have access to this command.");
				log.info(logPrefix + " - " + player.getDisplayName() + " tried to use command " + command + "! Denied access." );
			}
			return true;
		}

	public void onEnable() 
	{
		setupPermissions();
		configInit();
		confSetup.setupConfigs();
		log.info(logPrefix + " version " + this.getDescription().getVersion() + " enabled!");
	}

	public void onDisable() 
	{
		log.info(logPrefix + " version " + this.getDescription().getVersion() + " disabled!");
	}

	public boolean isDebugging(final Player player) 
	{
		if (debugees.containsKey(player)) 
			return debugees.get(player);

		return false;
	}

	public void setDebugging(final Player player, final boolean value) 
	{
		debugees.put(player, value);
	}

}

