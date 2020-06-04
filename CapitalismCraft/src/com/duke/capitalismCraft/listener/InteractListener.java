package com.duke.capitalismCraft.listener;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.duke.capitalismCraft.DatabaseHandler;
import com.mongodb.DBObject;

public class InteractListener implements Listener {

	DatabaseHandler dbHandler;

	public InteractListener(DatabaseHandler _dbHandler) {

		dbHandler = _dbHandler;

	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {

		if (canInteract(event.getPlayer(), event.getClickedBlock()))
			return;
		else
			event.setCancelled(true);

	}

	// checks if a player can interact with a block
	public boolean canInteract(Player player, Block target) {

		// if the player is an op then just let it happen
		if (player.hasPermission("capitalismcraft.interactall"))
			return true;

		// get chunk and property information
		if(target == null) return true;
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

}
