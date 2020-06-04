package com.duke.capitalismCraft;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import com.mongodb.*;

public class DatabaseHandler {

	// collection variables
	private DBCollection players;
	private DBCollection properties;
	private DBCollection govPriceData;

	// other stuff
	private DB mcserverdb;
	private MongoClient client;

	public boolean connect(String ip, int port) {

		try {
			client = new MongoClient(ip, port);
		} catch (UnknownHostException e) {
			System.out.println("Could not connect to database");
			e.printStackTrace();
			return false;
		}

		mcserverdb = client.getDB("mcserver");
		players = mcserverdb.getCollection("players");
		properties = mcserverdb.getCollection("properties");
		govPriceData = mcserverdb.getCollection("govPriceData");

		return true;

	}

	@SuppressWarnings("deprecation")
	public double getCash(String name) {
		UUID uuid = Bukkit.getPlayer(name).getUniqueId();

		DBObject r = new BasicDBObject("uuid", uuid);
		DBObject found = players.findOne(r);

		return (double) found.get("cash");

	}

	public double getCash(UUID uuid) {

		DBObject r = new BasicDBObject("uuid", uuid);
		DBObject found = players.findOne(r);

		return (double) found.get("cash");

	}

	public boolean newPlayer(UUID uuid) {

		DBObject r = new BasicDBObject("uuid", uuid);
		DBObject found = players.findOne(r);

		if (found == null) {
			return true;
		}

		return false;

	}

	public void createPlayerData(UUID uuid, double cash) {
		DBObject obj = new BasicDBObject("uuid", uuid);
		obj.put("cash", cash);
		players.insert(obj);
	}

	public boolean newChunk(Chunk chunk) {

		int[] coords = { chunk.getX(), chunk.getZ() };

		DBObject r = new BasicDBObject("coords", coords);
		DBObject found = properties.findOne(r);

		if (found == null) {
			return true;
		}

		return false;

	}

	public void createPropertyData(Chunk property) {
		int[] coords = { property.getX(), property.getZ() };
		DBObject obj = new BasicDBObject("coords", coords);
		obj.put("price", 30000.00);
		obj.put("owner", "gov");
		properties.insert(obj);
	}

	public DBObject getPropertyData(Chunk property) {

		int[] coords = { property.getX(), property.getZ() };

		DBObject r = new BasicDBObject("coords", coords);
		DBObject found = properties.findOne(r);

		if (found == null) {
			createPropertyData(property);
		}

		return found;

	}
	
	public List<DBObject> getAllProperties() {
		
		List<DBObject> allProperties;
		
		allProperties = properties.find().toArray();
		
		return allProperties;
		
	}

	public void updatePlayerData(UUID uuid, double cash) {

		DBObject r = new BasicDBObject("uuid", uuid);
		DBObject found = players.findOne(r);
		DBObject obj = new BasicDBObject("uuid", uuid);

		obj.put("cash", cash);

		players.update(found, obj);

	}

	public void updatePropertyData(Chunk property, double price, String owner) {
		BasicDBList coords = new BasicDBList();
		coords.add(property.getX());
		coords.add(property.getZ());
		DBObject r = new BasicDBObject("coords", coords);
		DBObject found = properties.findOne(r);
		DBObject obj = new BasicDBObject("coords", coords);

		obj.put("price", price);
		obj.put("owner", owner);

		properties.update(found, obj);

	}

	public double getItemPrice(String itemName) {

		double price;

		DBObject r = new BasicDBObject("name", itemName);
		DBObject found = govPriceData.findOne(r);

		price = (double) found.get("price");

		return price;

	}

}
