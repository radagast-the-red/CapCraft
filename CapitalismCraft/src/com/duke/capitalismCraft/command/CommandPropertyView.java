package com.duke.capitalismCraft.command;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.duke.capitalismCraft.CapitalismCraft;
import com.duke.capitalismCraft.DatabaseHandler;
import com.duke.capitalismCraft.task.PropertyViewScoreboardTask;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class CommandPropertyView implements CommandExecutor {

	DatabaseHandler dbHandler;
	World propertyWorld;
	HashMap<UUID, Location> previousPlayerLocations;
	Plugin plugin;

	public CommandPropertyView(DatabaseHandler _dbHandler, World _propertyWorld,
			HashMap<UUID, Location> _previousPlayerLocations, Plugin _plugin) {

		dbHandler = _dbHandler;
		propertyWorld = _propertyWorld;
		previousPlayerLocations = _previousPlayerLocations;
		plugin = _plugin;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// make sure args are right
		if (args.length > 1)
			return false;

		if (args.length == 1) {
			if (args[0].equals("update")) {

				// check that perms are ok
				if (!((Player) sender).hasPermission("capitalismcraft.propertyview.update")) {

					((Player) sender).sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return true;

				}

				// get all properties
				List<DBObject> properties = dbHandler.getAllProperties();

				// iterate through properties and draw blocks
				for (DBObject property : properties) {

					// get the property's data
					BasicDBList coords = (BasicDBList) property.get("coords");
					String owner = (String) property.get("owner");

					// get the block which is to be manipulated
					Block block = propertyWorld.getBlockAt(((int) coords.get(0)), 3, ((int) coords.get(1)));

					// actually change the block
					if (owner.equals("gov")) {
						block.setType(Material.GREEN_WOOL);
						continue;
					} else {
						block.setType(Material.RED_WOOL);
						continue;
					}

				}

				return true;

			} else
				return false;
		}

		try {
			if (args[0].equals("update")) {
				return true;
			}
		} catch (Exception e) {

		}

		// get the player
		Player player = (Player) sender;

		// check if they're going out
		if (player.getWorld().equals(propertyWorld)) {

			// create a location
			Location back;

			back = previousPlayerLocations.get(player.getUniqueId());

			if (back == null)
				back = new Location(Bukkit.getWorld("world"), 0,
						Bukkit.getWorld("world").getHighestBlockAt(0, 0).getY(), 0);

			// teleport the player
			player.teleport(back);
			return true;
		}

		// write their previous location to the HashMap
		previousPlayerLocations.put(player.getUniqueId(), player.getLocation());

		// create location to teleport to
		Location location = new Location(propertyWorld, 0, propertyWorld.getHighestBlockAt(0, 0).getY(), 0);

		// teleport the player
		player.teleport(location);

		// set up scoreboard repeating task
		PropertyViewScoreboardTask pvst = new PropertyViewScoreboardTask(dbHandler, player);
		pvst.runTaskTimer(plugin, 10, 5);

		return true;

	}

}
