package com.duke.capitalismCraft.command;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import com.duke.capitalismCraft.DatabaseHandler;
import com.duke.capitalismCraft.format.DoubleDisplay;

public class CommandBal implements CommandExecutor {
	
	DatabaseHandler dbHandler;
	
	public CommandBal(DatabaseHandler _dbHandler) {
		
		dbHandler = _dbHandler;
	
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		double returnValue;
		String playerName;
		
		if(args.length > 0) {
			Player player = (Player) sender;
			
			if(!player.hasPermission("capitalismcraft.bal.others") && !player.getName().equals(args[0])) {
				sender.sendMessage(ChatColor.RED + "None of your business.");
				return false;
			}
			
			playerName = args[0];
			returnValue = dbHandler.getCash(playerName);
		} else {
			Player player = (Player) sender;
			playerName = player.getName();
			returnValue = dbHandler.getCash(playerName);
		}
		
		sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + playerName + " has $" + DoubleDisplay.display(returnValue));
		
		return true;
		
	}
	
}
