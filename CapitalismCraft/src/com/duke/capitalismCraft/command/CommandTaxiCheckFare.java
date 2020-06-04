package com.duke.capitalismCraft.command;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.duke.capitalismCraft.format.DoubleDisplay;

public class CommandTaxiCheckFare implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();

		// check that input isn't malformed
		if (args.length != 2) {
			sender.sendMessage(ChatColor.RED + "Please enter the X and Z values of your destination...");
			return false;
		}

		int x;
		int z;

		try {

			x = Integer.parseInt(args[0]);
			z = Integer.parseInt(args[1]);

		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "Please enter the X and Z values of your destination...");
			return false;
		}

		// check that we're in a suitable world
		World world = player.getWorld();
		Environment environment = world.getEnvironment();
		if (environment != Environment.NORMAL) {
			sender.sendMessage(ChatColor.RED + "The taxi service is not available here...");
			return false;
		}

		// calculate price
		Location current = player.getLocation();
		Location destination = world.getHighestBlockAt(x, z).getLocation();

		double distance = current.distance(destination);
		double price = 15 + distance * 0.5;

		sender.sendMessage(ChatColor.BLUE + "The trip would cost " + ChatColor.BOLD + "" + ChatColor.RED + "$" + DoubleDisplay.display(price));

		return true;
		
	}
	
}
