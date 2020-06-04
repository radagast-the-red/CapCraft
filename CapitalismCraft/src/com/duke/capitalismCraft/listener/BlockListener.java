package com.duke.capitalismCraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import com.duke.capitalismCraft.DatabaseHandler;
import com.mongodb.DBObject;

public class BlockListener implements Listener {

	DatabaseHandler dbHandler;

	public BlockListener(DatabaseHandler _dbHandler) {

		dbHandler = _dbHandler;

	}

	// hard cancels / banned happenings
	@EventHandler
	public void onBlockExplodeEvent(BlockExplodeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockIgniteEvent(BlockIgniteEvent event) {
		event.setCancelled(true);
	}

	// permissions required
	@EventHandler
	public void onBlockCanBuildEvent(BlockCanBuildEvent event) {
		
		if(event.getBlock().getWorld().equals(Bukkit.getWorld("PropertyWorld"))) event.setBuildable(false);
		
		Player player = event.getPlayer();

		if (canInteract(player, event.getBlock()))
			return;

		event.setBuildable(false);
	}

	@EventHandler
	public void onBlockDamageEvent(BlockDamageEvent event) {
		
		if(event.getBlock().getWorld().equals(Bukkit.getWorld("PropertyWorld"))) event.setCancelled(true);
		
		Player player = event.getPlayer();

		if (canInteract(player, event.getBlock()))
			return;

		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		
		if(event.getBlock().getWorld().equals(Bukkit.getWorld("PropertyWorld"))) event.setCancelled(true);
		
	}

	@EventHandler
	public void onBlockFertilizeEvent(BlockFertilizeEvent event) {
		Player player = event.getPlayer();

		if (canInteract(player, event.getBlock()))
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onSignChangeEvent(SignChangeEvent event) {
		Player player = event.getPlayer();

		if (canInteract(player, event.getBlock()))
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockFromToEvent(BlockFromToEvent event) {
		try {
		Block from = event.getBlock();
		Block to = event.getToBlock();

		Chunk fromChunk = from.getChunk();
		DBObject fromProperty = dbHandler.getPropertyData(fromChunk);

		String fromOwner = (String) fromProperty.get("owner");

		if (canInteract(fromOwner, to))
			return;
		event.setCancelled(true);
		return;
		} catch(NullPointerException e) {
			return;
		}
	}

	// checks if a player can interact with a block
	public boolean canInteract(Player player, Block target) {

		// if the player is an op then just let it happen
		if (player.hasPermission("capitalismcraft.interactall"))
			return true;

		// get chunk and property information
		Chunk targetChunk = target.getChunk();
		DBObject property = dbHandler.getPropertyData(targetChunk);

		// who owns the property?
		String owner = (String) property.get("owner");

		// check if the player can do stuff here
		if (player.getName().equals(owner))
			return true;
		else
			return false;
	}

	public boolean canInteract(String player, Block target) {

		// get chunk and property information
		Chunk targetChunk = target.getChunk();
		DBObject property = dbHandler.getPropertyData(targetChunk);

		// who owns the property?
		String owner = (String) property.get("owner");

		// check if the player can do stuff here
		if (player.equals(owner))
			return true;
		else
			return false;
	}

}
