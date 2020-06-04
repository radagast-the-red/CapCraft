package com.duke.capitalismCraft.command;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.duke.capitalismCraft.DatabaseHandler;
import com.duke.capitalismCraft.format.DoubleDisplay;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class CommandProperty implements CommandExecutor {

	DatabaseHandler dbHandler;

	public CommandProperty(DatabaseHandler _dbHandler) {

		dbHandler = _dbHandler;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length < 1) {

			sender.sendMessage(ChatColor.RED + "This command requires parameters: " + "\n    /property info [x] [z]"
					+ "\n    /property buy [x] [z]");
			return true;

		}

		if (args.length >= 4) {

			sender.sendMessage(ChatColor.RED + "This command uses a maximum of 3 parameters: "
					+ "\n    /property info [x] [z]" + "\n    /property buy [x] [z]");
			return true;

		}

		if (args[0].equals("info")) {

			if(args.length == 1) {
				
				Player player = (Player) sender;
				Location playerLocation = player.getLocation();
				
				String[] bargs = {
						"",
						Double.toString(playerLocation.getBlockX()),
						Double.toString(playerLocation.getBlockZ())
				};
				
				if (info(sender, bargs))
					return true;
				else
					return false;
				
			}
			
			if (info(sender, args))
				return true;
			else
				return false;

		}

		if (args[0].equals("buy")) {
			
			if(args.length == 1) {
				
				Player player = (Player) sender;
				Location playerLocation = player.getLocation();
				
				String[] bargs = {
						"",
						Double.toString(playerLocation.getBlockX()),
						Double.toString(playerLocation.getBlockZ())
				};
				
				if (buy(sender, bargs))
					return true;
				else
					return false;

			}
			
			if (buy(sender, args))
				return true;
			else
				return false;

		}
		
		sender.sendMessage(ChatColor.RED + "This command requires parameters: " + "\n    /property info [x] [z]"
				+ "\n    /property buy [x] [z]");
		return true;

	}

	public boolean info(CommandSender sender, String[] args) {

		Chunk target = findChunk(sender, args);
		if (target == null)
			return false;

		DBObject property = dbHandler.getPropertyData(target);
		BasicDBList coords = (BasicDBList) property.get("coords");
		double price = (double) property.get("price");
		String owner = (String) property.get("owner");

		sender.sendMessage(ChatColor.BLUE + "Property at " + ChatColor.WHITE + coords.get(0) + ", " + coords.get(1)
				+ ChatColor.BLUE + "\n    Owner: " + owner + "\n    Price: " + ChatColor.GREEN + "$"
				+ DoubleDisplay.display(price));

		return true;

	}

	public boolean buy(CommandSender sender, String[] args) {
		
		
		sender.sendMessage(ChatColor.RED + "This command has been permanently disabled...");
		return false;
		

//		Player player = (Player) sender;
//
//		Chunk target = findChunk(sender, args);
//		if (target == null)
//			return false;
//
//		DBObject property = dbHandler.getPropertyData(target);
//		BasicDBList coords = (BasicDBList) property.get("coords");
//		double price = (double) property.get("price");
//		String owner = (String) property.get("owner");
//
//		double playerFunds = dbHandler.getCash(player.getUniqueId());
//
//		if (price > playerFunds) {
//			sender.sendMessage(
//					ChatColor.RED + "You are too broke to afford the cost of $" + DoubleDisplay.display(price));
//			return false;
//		}
//
//		dbHandler.updatePlayerData(player.getUniqueId(), playerFunds - price);
//		dbHandler.updatePropertyData(target, (double) property.get("price"), player.getName());
//
//		sender.sendMessage(ChatColor.BLUE + "You have purchased the property for " + ChatColor.RED + "$"
//				+ DoubleDisplay.display(price));
//		return true;

	}

	public Chunk findChunk(CommandSender sender, String[] args) {

		Player player = (Player) sender;
		World world = player.getWorld();

		double x;
		double z;

		try {
			x = Double.parseDouble(args[1]);
			z = Double.parseDouble(args[2]);
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "Malformed coordinates!");
			return null;
		}

		Location blockLocation = new Location(world, x, 10.0, z);
		Block block = world.getBlockAt(blockLocation);

		return block.getChunk();

	}

}
