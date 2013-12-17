package me.blockcat.catchat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChannelHandler {

	private File file;
	private CatChat plugin;
	
	//TODO: save and load channels 

	/** Player, Channel Name*/
	private HashMap<String, String> playerData = new HashMap<String, String>();
	private HashMap<String, Channel> channelData = new HashMap<String, Channel>();

	public ChannelHandler(CatChat plugin) {
		this.plugin = plugin;
		file = new File(plugin.getDataFolder(), "channel.data");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
	}
	
	public Channel getChannel(Player player) {
		if (playerData.containsKey(player.getName())) {
			if (channelData.containsKey(playerData.get(player.getName()).toLowerCase())) {
				return channelData.get(playerData.get(player.getName()).toLowerCase());
			}
		}
		return null;
	}
	
	public List<Player> getPlayers(Player player) {
		return getPlayers(getChannel(player));
	}
	
	public List<Player> getPlayers(Channel channel) {
		List<Player> list = new ArrayList<Player>();
		
		for (Entry<String, String> ent : playerData.entrySet()) {
			if (ent.getValue().toLowerCase().equalsIgnoreCase(channel.getName().toLowerCase())) {
				Player temp = Bukkit.getPlayerExact(ent.getKey());
				list.add(temp);
			}
		}
		return list;
	}
	
	public boolean joinChannel(Player player, String name) {
		return joinChannel(player, name, "");
	}

	public boolean joinChannel(Player player, String name, String password) {
		
		if (!channelData.containsKey(name.toLowerCase())) {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Channel not found.");
			return false;
		}
		
		Channel channel = channelData.get(name.toLowerCase());
		if (!channel.password.equals(password)) {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Wrong password.");
			return false;
		}
		
		if (channel.banList.contains(player.getName())) {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " Banned from this channel.");
			return false;
		}
		
		if (playerData.containsKey(player.getName()) && getChannel(player) != null) {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Left: " + ChatColor.AQUA + getChannel(player).getName());
		}
		player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Joined: " + ChatColor.AQUA + channel.getName());
		
		playerData.put(player.getName(), name.toLowerCase());
		return true;
	}
	
	public void leaveChannel(Player player) {
		if (getChannel(player) == null) {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " You are not in a channel.");
		} else {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Left: " + ChatColor.AQUA + getChannel(player).getName());
			leaveChannel(player.getName());
		}
	}
	
	public void leaveChannel(String player) {
		playerData.remove(player);
	}

	
	public boolean createChannel(Player player, String name) {
		return createChannel(player, name, "");
	}
	
	public boolean createChannel(Player player, String name, String password) {
		if (channelData.containsKey(name.toLowerCase())) {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.RED + " This channel already exists.");
			return false;
		}
		
		Channel channel = new Channel(player.getName(), name, password);
		
		if (playerData.containsKey(player.getName()) && getChannel(player) != null) {
			player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Left: " + ChatColor.AQUA + getChannel(player).getName());
		}
		player.sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " Created: " + ChatColor.AQUA + channel.getName());
		
		playerData.put(player.getName(), name.toLowerCase());
		channelData.put(name.toLowerCase(), channel);
		
		
		return true;
	}

	public class Channel {
		
		public String owner;
		private String name;
		private String password;
		public List<String> banList = new ArrayList<String>();

		public Channel(String owner, String name, String password) {
			this.owner = owner;
			this.setName(name);
			this.password = password;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
	}
}
