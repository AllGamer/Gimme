package com.dustinessington.gimme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;


public class ItemDb
{
	private final static Logger logger = Logger.getLogger("Minecraft");
	private static Map<String, ItemStack> map = new HashMap<String, ItemStack>();

	@SuppressWarnings("LoggerStringConcat")
	public static void load(File folder, String fname) throws IOException
	{
		folder.mkdirs();
		File file = new File(folder, fname);

		if (!file.exists())
		{
			file.createNewFile();
			InputStream res = ItemDb.class.getResourceAsStream("/items.csv");
			FileWriter tx = new FileWriter(file);
			try
			{
				for (int i = 0; (i = res.read()) > 0;) tx.write(i);
			}
			finally
			{
				try
				{
					tx.flush();
					tx.close();
					res.close();
				}
				catch (Exception ex)
				{
				}
			}
		}

		BufferedReader rx = new BufferedReader(new FileReader(file));
		try
		{
			map.clear();

			for (int i = 0; rx.ready(); i++)
			{
				try
				{
					String line = rx.readLine().trim().toLowerCase();
					if (line.startsWith("#")) continue;
					String[] parts = line.split("[^a-z0-9]");
					if (parts.length < 2) continue;
					int numeric = Integer.parseInt(parts[1]);
					ItemStack stack = new ItemStack(numeric, 64);
					if (parts.length > 2 && !parts[2].equals("0")) stack.setData(new MaterialData(numeric, Byte.parseByte(parts[2])));
					if (parts.length > 3) stack.setAmount(Integer.parseInt(parts[3]));
					if (stack.getAmount() < 1 || stack.getAmount() > 64) stack.setAmount(64);
					map.put(parts[0], stack);
				}
				catch (Exception ex)
				{
					logger.warning("Error parsing " + fname + " on line " + i);
				}
			}
		}
		finally
		{
			rx.close();
		}
	}

	public static ItemStack get(String id) throws Exception
	{
		ItemStack retval = getUnsafe(id);
		retval.setAmount(64);
		if (map.containsValue(retval) || true) return retval;
		throw new Exception("Unknown item numeric: " + retval);
	}

	private static ItemStack getUnsafe(String id) throws Exception
	{
		try
		{
			return new ItemStack(Integer.parseInt(id));
		}
		catch (NumberFormatException ex)
		{
			if (map.containsKey(id)) return map.get(id);
			throw new Exception("Unknown item name: " + id);
		}
	}
}
