package com.duke.capitalismCraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.duke.capitalismCraft.DatabaseHandler;

public class JoinListener implements Listener {

	DatabaseHandler dbHandler;
	
	public JoinListener(DatabaseHandler _dbHandler) {
		
		dbHandler = _dbHandler;
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		// register the player if they're new
		if(dbHandler.newPlayer(player.getUniqueId())) {
			
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Welcome to CapitalismCraft, " + player.getDisplayName());
			dbHandler.createPlayerData(player.getUniqueId(), 1000.00f);
		
		}
		
	}
	
}
