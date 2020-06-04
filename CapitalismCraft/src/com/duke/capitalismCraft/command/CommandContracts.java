package com.duke.capitalismCraft.command;

import org.bukkit.command.CommandExecutor;

import com.duke.capitalismCraft.DatabaseHandler;

public class CommandContracts implements CommandExecutor {

	DatabaseHandler dbHandler;
	
	public CommandContracts(DatabaseHandler _dbHandler) {
		
		dbHandler = _dbHandler;
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		
		
	}
	
}
