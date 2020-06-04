package com.duke.capitalismCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.duke.capitalismCraft.command.*;
import com.duke.capitalismCraft.listener.*;

public class CapitalismCraft extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		// set up the database
		DatabaseHandler dbHandler = new DatabaseHandler();
		dbHandler.connect("localhost", 27017);
		
		// store stuff
		ArrayList<Inventory> storeInventoryList = new ArrayList<Inventory>();
		Block storeMarker = Bukkit.getServer().getWorld("world").getBlockAt(-45, 71, 26);
		Chunk storeChunk = storeMarker.getChunk();
		
		// property world stuff
		HashMap<UUID, Location> previousPlayerLocations = new HashMap<UUID, Location>();
		World propertyWorld = Bukkit.getServer().createWorld(new WorldCreator("PropertyWorld"));
		
		// set up listeners
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new JoinListener(dbHandler), this);
		pm.registerEvents(new ChunkListener(dbHandler), this);
		pm.registerEvents(new BlockListener(dbHandler), this);
		pm.registerEvents(new InteractListener(dbHandler), this);
		pm.registerEvents(new StoreListener(dbHandler, storeInventoryList), this);
		
		// set up commands
		this.getCommand("store").setExecutor(new CommandStore(dbHandler, storeInventoryList, storeChunk));
		this.getCommand("bal").setExecutor(new CommandBal(dbHandler));
		this.getCommand("taxi").setExecutor(new CommandTaxi(dbHandler));
		this.getCommand("taxicheckfare").setExecutor(new CommandTaxiCheckFare());
		this.getCommand("property").setExecutor(new CommandProperty(dbHandler));
		this.getCommand("propertyview").setExecutor(new CommandPropertyView(dbHandler, propertyWorld, previousPlayerLocations, this));
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
}
