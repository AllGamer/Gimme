package net.craftrepo.Gimme;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

/**
 * CraftRepo Bans for Bukkit
 * @author AllGamer
 * 
 * Copyright 2011 AllGamer, LLC.
 * See LICENSE for licensing information.
 */

public class gimmeConfiguration 
{

	private gimme plugin;
	private File folder;
	private final Logger log = Logger.getLogger("Minecraft");
	private String logPrefix;

	public gimmeConfiguration(File folder, gimme instance) 
	{
		this.folder = folder;
		this.plugin = instance;
		this.logPrefix = gimme.logPrefix;
	}

	public void setupConfigs() 
	{
		File config = new File(this.folder, "config.yml");
		if (!config.exists()) 
		{
			try 
				{
					log.info(logPrefix + " - Creating config directory... ");
					log.info(logPrefix + " - Creating config files... ");
					config.createNewFile();
					FileWriter fstream = new FileWriter(config);
					BufferedWriter out = new BufferedWriter(fstream);

					out.write("#Gimme Configuration");
					out.write("#Please only put block IDs here.\n");
					out.write("#Don't complain if you put grass instead of 2 and it breaks...\n");
					out.write("\n");
					out.write("\n");
					out.write("#Put block IDs for blacklist mode here\n");
					out.write("denied:\n");
					out.write("    - 7\n");
					out.write("    - 46\n");
					out.write("\n");
					out.write("#Put block IDs for whitelist mode here\n");
					out.write("allowed:\n");
					out.write("    - 4\n");
					out.write("    - 2\n");
				
				
					out.close();
					fstream.close();
					log.info(logPrefix + " Make sure to edit your config file!");
				
				}
			catch (IOException ex) 
				{
					log.severe(logPrefix + " Error creating default Configuration File");
					log.severe(logPrefix + " " + ex);
					this.plugin.getServer().getPluginManager().disablePlugin((Plugin) this);
				}
			}
		}
	}
