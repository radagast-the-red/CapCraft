package com.duke.capitalismCraft.listener;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.duke.capitalismCraft.DatabaseHandler;

public class ChunkListener implements Listener {
	
	DatabaseHandler dbHandler;
	
	public ChunkListener(DatabaseHandler _dbHandler) {
		
		dbHandler = _dbHandler;
		
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		
		Chunk chunk = event.getChunk();
		
		if(dbHandler.newChunk(chunk)) {
			
			dbHandler.createPropertyData(chunk);
			
		}
		
	}

}
