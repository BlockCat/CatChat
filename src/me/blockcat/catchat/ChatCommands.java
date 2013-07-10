package me.blockcat.catchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommands implements CommandExecutor {

	public CatChat plugin;

	public ChatCommands(CatChat plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length == 0) {
			if (!(sender instanceof Player) || sender.hasPermission("catchat.admin") || sender.isOp()) {
				this.showHelp(sender);
			} else {
				sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " No permissions!");
			}
			return true;
		}
		Player player = (Player) sender;
		
		if (!CatChat.hasPerms(player, "catchat.admin")) {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " No permissions!");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
				plugin.reloadConfig();
				plugin.loadConfig();
				System.out.println("Chat Reloaded");
				player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " has reloaded!");
				return true;
		} else if (args[0].equalsIgnoreCase("prefix")) {
				switch(args.length) {
				//has no add or remove
				case 1:
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong input.");
					sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "prefix " + ChatColor.GOLD + "<group> [prefix]");
					return true;
				case 2:
					CatChat.config.set("Prefix." + args[1], "");
					plugin.saveConfig();
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Removed prefix of: " + ChatColor.GOLD + args[1]);
					return true;
				case 3:
					String s = "";
					for (int i = 2; i < args.length; i++) {
						s = (!s.equalsIgnoreCase("")) ? s + " " + args[i] : args[i];
					}
					CatChat.config.set("Prefix." + args[1], s);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Changed prefix of: " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " to " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', s));
					return true;
				}
		} else if (args[0].equalsIgnoreCase("suffix")) {
				switch(args.length) {
				//has no add or remove
				case 1:
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong input.");
					sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "suffix " + ChatColor.GOLD + "<group> [suffix]");
					return true;
				case 2:
					CatChat.config.set("Suffix." + args[1], "");
					plugin.saveConfig();
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Removed suffix of: " + ChatColor.GOLD + args[1]);
					return true;
				case 3:
					String s = "";
					for (int i = 2; i < args.length; i++) {
						s = (!s.equalsIgnoreCase("")) ? s + " " + args[i] : args[i];
					}
					CatChat.config.set("Suffix." + args[1], s);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Changed  suffix of: " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " to " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', s));
					return true;
				}
		} else if (args[0].equalsIgnoreCase("priority")) {
				switch(args.length) {
				//has no add or remove
				case 1:
				case 2:
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong input.");
					sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "priority " + ChatColor.GOLD + "<group> <#>");
					return true;
				case 3:
					try {
					int s = Integer.parseInt(args[2]);
					CatChat.config.set("Priority." + args[1], s);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Changed priority of: " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " to " + ChatColor.WHITE + s);
					return true;
					} catch (Exception e) {
						sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong input.");
						sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "priority " + ChatColor.GOLD + "<group> <#>");
					}
				}
		} else {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong usage!");
			showHelp(sender);
		}
		return true;
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "~~" + ChatColor.GOLD + "CatChat" + ChatColor.GREEN + "~~");
		sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "reload " + ChatColor.AQUA + "~Reload configuration.");
		sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "prefix " + ChatColor.AQUA + "~Change prefix.");
		sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "suffix " + ChatColor.AQUA + "~Change suffix.");
		sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "priority " + ChatColor.AQUA + "~Change priority.");
	}

}
