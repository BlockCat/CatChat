package me.blockcat.catchat;

import java.util.ArrayList;
import java.util.List;

import me.blockcat.catchat.ChannelHandler.Channel;

import org.bukkit.Bukkit;
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
				this.showAdminHelp(sender);
			} else {
				this.showUserHelp(sender);
			}
			return true;
		}
		Player player = (Player) sender;

		if (args[0].equalsIgnoreCase("group") || args[0].equalsIgnoreCase("g") || args[0].equalsIgnoreCase("channel")|| args[0].equalsIgnoreCase("ch")) {
			if (args.length - 1 == 0) {
				this.showGroupHelp(player);
				return true;
			}
			if (args[1].equalsIgnoreCase("create")) {
				if (CatChat.hasPerms(player, "catchat.group.create")) {
					if (args.length < 3) {
						sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong input.");
						sender.sendMessage(ChatColor.RED + "/catchat channel" + ChatColor.GREEN + " create " + ChatColor.GOLD + "<name> [password]");
						return true;
					} 
					//text is handled within method
					plugin.getChannelHandler().createChannel(player, args[2], (args.length >= 4) ? args[3] : "");
					return true;
				} else {
					player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " No permissions!");
					return true;
				}
			} else if (args[1].equalsIgnoreCase("join")) {
				if (CatChat.hasPerms(player, "catchat.group.join")) {
					if (args.length < 3) {
						sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong input.");
						sender.sendMessage(ChatColor.RED + "/catchat channel" + ChatColor.GREEN + " join " + ChatColor.GOLD + "<name> [password]");
						return true;
					}
					//textmessages is handled within method
					plugin.getChannelHandler().joinChannel(player, args[2], (args.length >= 4) ? args[3] : "");
					return true;
				} else {
					player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " No permissions!");
				}
			} else if (args[1].equalsIgnoreCase("leave")) {
				if (CatChat.hasPerms(player, "catchat.group.join")) {
					plugin.getChannelHandler().leaveChannel(player);
					return true;
				} else {
					player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " No permissions!");
					return true;
				}
			} else if (args[1].equalsIgnoreCase("ban")) {
				if (args.length < 3) 
				{
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong input.");
					sender.sendMessage(ChatColor.RED + "/catchat channel" + ChatColor.GREEN + " ban " + ChatColor.GOLD + "<name>");
					return true;
				}

				Channel ch = plugin.getChannelHandler().getChannel(player);
				if (ch.owner.equalsIgnoreCase(player.getName())) 
				{
					ch.banList.add(args[2]);
					Player banned = Bukkit.getPlayerExact(args[2]);
					if (banned != null) {
						banned.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " You got banned from the channel!");
						plugin.getChannelHandler().leaveChannel(banned);
					} else {
						plugin.getChannelHandler().leaveChannel(args[2]);
					}
				} else {
					player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " You are not the owner of this channel!");
					return true;
				}				
			} else if (args[1].equalsIgnoreCase("unban")) {
				if (args.length < 3) 
				{
					sender.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong input.");
					sender.sendMessage(ChatColor.RED + "/catchat channel" + ChatColor.GREEN + "unban " + ChatColor.GOLD + "<name>");
					return true;
				}
				
				Channel ch = plugin.getChannelHandler().getChannel(player);
				if (ch.owner.equalsIgnoreCase(player.getName())) 
				{
					List<String> l = new ArrayList<String>();
					l.add(args[2]);
					ch.banList.removeAll(l);
					
					Player unBanned = Bukkit.getPlayerExact(args[2]);
					if (unBanned != null) {
						unBanned.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " You got unbanned from channel: " + ChatColor.AQUA + ch.getName());
						return true;
					}
				} else {
					player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " You are not the owner of this channel!");
					return true;
				}				
			}
		}

		//start admin commands.
		if (!CatChat.hasPerms(player, "catchat.admin")) {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " No permissions!");
			return true;
		}

		if (args[0].equalsIgnoreCase("reload")) {
			plugin.reloadConfig();
			plugin.loadConfig();
			System.out.println("[CatChat] Chat Reloaded");
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
			showAdminHelp(sender);
		}
		return true;
	}

	private void showGroupHelp(Player player) {
		player.sendMessage(ChatColor.GREEN + "~~" + ChatColor.GOLD + "CatChat:" + ChatColor.AQUA + " channels" + ChatColor.GREEN + "~~");
		player.sendMessage(ChatColor.RED + "/catchat channel" + ChatColor.GREEN + " join" + ChatColor.AQUA + " ~Join a channel.");
		player.sendMessage(ChatColor.RED + "/catchat channel" + ChatColor.GREEN + " leave" + ChatColor.AQUA + " ~Leave a channel.");
		player.sendMessage(ChatColor.RED + "/catchat channel" + ChatColor.GREEN + " create" + ChatColor.AQUA + " ~create a channel.");
		player.sendMessage(ChatColor.RED + "/catchat channel" + ChatColor.GREEN + " ban" + ChatColor.AQUA + " ~ban a player.");
		player.sendMessage(ChatColor.RED + "/catchat channel" + ChatColor.GREEN + " unban" + ChatColor.AQUA + " ~unban a player.");
	}

	private void showUserHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "~~" + ChatColor.GOLD + "CatChat" + ChatColor.GREEN + "~~");
		sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "channel " + ChatColor.AQUA + "~Show channel help.");
	}

	private void showAdminHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "~~" + ChatColor.GOLD + "CatChat" + ChatColor.GREEN + "~~");
		sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "reload " + ChatColor.AQUA + "~Reload configuration.");
		sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "prefix " + ChatColor.AQUA + "~Change prefix.");
		sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "suffix " + ChatColor.AQUA + "~Change suffix.");
		sender.sendMessage(ChatColor.RED + "/catchat " + ChatColor.GREEN + "priority " + ChatColor.AQUA + "~Change priority.");
	}

}
