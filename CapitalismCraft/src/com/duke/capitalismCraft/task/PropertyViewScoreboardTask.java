package com.duke.capitalismCraft.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.duke.capitalismCraft.DatabaseHandler;
import com.mongodb.DBObject;

public class PropertyViewScoreboardTask extends BukkitRunnable {

	DatabaseHandler dbHandler;
	Player player;

	public PropertyViewScoreboardTask(DatabaseHandler _dbHandler, Player _player) {
		dbHandler = _dbHandler;
	    player = _player;
	}

	@Override
	public void run() {

		// check if we need to cancel this
		if(!(player.getWorld().equals(Bukkit.getWorld("PropertyWorld")))) {
			
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			this.cancel();
			return;
		
		}
		
		// get the property associated with the block the player is looking at
		Block target = player.getTargetBlockExact(20);
		
		if(target == null) {
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			return;
		}
		
		int x = target.getX();
		int z = target.getZ();
		Chunk targetChunk = Bukkit.getWorld("world").getChunkAt(x, z);
		DBObject property = dbHandler.getPropertyData(targetChunk);
		
		// get key info about the property
		String owner = (String) property.get("owner");
		int chunkX = targetChunk.getX();
		int chunkZ = targetChunk.getZ();
		String biome = targetChunk.getBlock(8, 0, 8).getBiome().name().toLowerCase().replace('_', ' ');
		
		// create/update a scoreboard for the player
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("a", "b", "c");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Property at " + chunkX +", " + chunkZ + "");
		Score score0 = objective.getScore("Owner: " + owner);
		score0.setScore(0);
		Score score1 = objective.getScore("Biome: " + biome);
		score1.setScore(0);
		
		// make the player see it, hopefully
		player.setScoreboard(scoreboard);
		
	}

}
