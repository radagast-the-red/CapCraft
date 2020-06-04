package com.duke.capitalismCraft.listener;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.duke.capitalismCraft.DatabaseHandler;
import com.duke.capitalismCraft.format.DoubleDisplay;

public class StoreListener implements Listener {

	DatabaseHandler dbHandler;
	ArrayList<Inventory> storeInventoryList;

	public StoreListener(DatabaseHandler _dbHandler, ArrayList<Inventory> _storeInventoryList) {

		dbHandler = _dbHandler;
		storeInventoryList = _storeInventoryList;

	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {

		// if the destination inventory isn't the store inventory, disregard
		if (!storeInventoryList.contains(event.getInventory()))
			return;

		// get the player
		Player player = (Player) event.getInventory().getHolder();
		
		// loop through the inventory contents and sort items based on sellability. also count up the total payout
		double payout = 0;
		
		ItemStack[] itemStacks = event.getInventory().getContents();
		
		ArrayList<ItemStack> sellable = new ArrayList<ItemStack>();
		ArrayList<ItemStack> notSellable = new ArrayList<ItemStack>();
		
		for(int i = 0; i < itemStacks.length; i++) {
			try {
				double thisStackPayout = dbHandler.getItemPrice(itemStacks[i].getType().name());
				thisStackPayout *= itemStacks[i].getAmount();
				payout += thisStackPayout;
				sellable.add(itemStacks[i]);
			} catch (Exception e) {
				notSellable.add(itemStacks[i]);
			}
		}
		
		// print all unsold items and give them back to the player
		
		for(int i = 0; i < notSellable.size(); i++) {
			ItemStack e = notSellable.get(i);
			if(e == null) continue;
			player.getInventory().addItem(e);
			player.sendMessage(ChatColor.RED + "Could not sell " + e.getAmount() + " " + e.getType().toString().toLowerCase().replace('_', ' '));
		}
		
		// print all sold items
		for(int i = 0; i < sellable.size(); i++) {
			ItemStack e = sellable.get(i);
			player.sendMessage(ChatColor.GREEN + "Sold " + e.getAmount() + " " + e.getType().toString().toLowerCase().replace('_', ' '));
		}
		
		// print total, pay the player
		UUID uuid = player.getUniqueId();
		dbHandler.updatePlayerData(uuid, dbHandler.getCash(uuid) + payout);
		if(payout != 0)
			player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "A total of " + ChatColor.GREEN + "" + ChatColor.BOLD + "$" + DoubleDisplay.display(payout) + ChatColor.BLUE + "" + ChatColor.BOLD + " has been transferred to you by the government!");
		
		// clear and delete the inventory
		storeInventoryList.remove(event.getInventory());
		event.getInventory().clear();

	}

}
