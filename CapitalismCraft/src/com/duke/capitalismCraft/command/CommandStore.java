package com.duke.capitalismCraft.command;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.duke.capitalismCraft.DatabaseHandler;

public class CommandStore implements CommandExecutor {

	DatabaseHandler dbHandler;
	ArrayList<Inventory> storeInventoryList;
	Chunk storeChunk;
	
	public CommandStore(DatabaseHandler _dbHandler, ArrayList<Inventory> _storeInventoryList, Chunk _storeChunk) {
		
		dbHandler = _dbHandler;
		storeInventoryList = _storeInventoryList;
		storeChunk = _storeChunk;
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// don't take any args
		if(args.length != 0) return false;
	
		// get the store marker block and thus the store chunk
		Player player = (Player) sender;
		
		// check that the player is inside the store chunk
		if(!player.getLocation().getChunk().equals(storeChunk)) {
			
			player.sendMessage(ChatColor.RED + "You must be inside the store in order to sell your items...");
			return true;
			
		}
		
		// create, add to arraylist, and open the inventory
		Inventory storeInventory = Bukkit.createInventory(player, 9);
		storeInventoryList.add(storeInventory);
		player.openInventory(storeInventory);
		return true;
		
		
	}
	
}
