package me.blockcat.catchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.blockcat.catchat.ChannelHandler.Channel;
import me.blockcat.catchat.JSON.JsonMessage;
import me.blockcat.catchat.JSON.JsonText;
import me.blockcat.catchat.JSON.JsonUrlEvent;
import net.minecraft.server.v1_7_R1.ChatSerializer;
import net.minecraft.server.v1_7_R1.IChatBaseComponent;
import net.minecraft.server.v1_7_R1.PacketPlayOutChat;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CatListener implements Listener {

	private static List<String> endings = new ArrayList<String>();
	private CatChat plugin;

	public CatListener(CatChat p) {
		plugin = p;
	}

	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().equalsIgnoreCase("/chat reload") && CatChat.hasPerms(event.getPlayer(), "catchat.admin")) {
			plugin.reloadConfig();
			plugin.loadConfig();
			System.out.println("Chat Reloaded");
			event.getPlayer().sendMessage(ChatColor.GOLD + "[CatChat]" + ChatColor.GREEN + " has reloaded!");
			event.setCancelled(true);
		}
	}

	@EventHandler 
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event){

		String msg = event.getMessage().replaceAll("^[ ]*\\*", "*");
		String[] messageArray = format(event.getPlayer(), msg, msg.startsWith("*")).split(" ");
		String newMessage = "";

		JsonMessage message = new JsonMessage("");
		for (String x : messageArray) {
			
			if(this.isUrl(x)) {
				
				if (!CatChat.hasPerms(event.getPlayer(), "catchat.links")) {
				
					newMessage = newMessage + ChatColor.RED + "<censored> ";
					continue;
				}else {
					
					message.append(new JsonUrlEvent(ChatColor.GREEN + "<link> ", x));
				}
			} else {

				newMessage += x + " ";
				message.append(new JsonText(Symbols.getSmiley(x) + " "));
				continue;
			}
		}

		Player player = event.getPlayer();
		System.out.println(newMessage);
		//if starts with *, then channel chat
		if (newMessage.startsWith("^[ ]*\\*")) {
			Channel channel = plugin.getChannelHandler().getChannel(player);

			if (channel == null) {
				newMessage.replaceAll("^[ ]*\\*", "");
			} else {
				event.getRecipients().retainAll(plugin.getChannelHandler().getPlayers(player));
			}
		}
		
		//System.out.println(message.getObject().toJSONString());
		for (Player recepient : event.getRecipients()) {
			IChatBaseComponent comp = ChatSerializer.a(message.getObject().toJSONString());
	        PacketPlayOutChat packet = new PacketPlayOutChat(comp, true);
	        ((CraftPlayer) recepient).getHandle().playerConnection.sendPacket(packet);
		}
		event.setCancelled(true);
	}

	public String format(Player player, String msg) {
		return format(player, msg, false);
	}

	public String format(Player player, String msg, boolean channel) {
		String prefix = "";
		String suffix = "";
		String format = (channel) ? plugin.getChannelFormat() : plugin.getFormat();
		World w = null;
		String[] groups = CatChat.permission.getPlayerGroups(w, player.getName());	

		if (format.contains("+prefix")) {
			try {
				int i = Integer.MAX_VALUE;
				for (Entry<String, Object> map : plugin.getConfig().getConfigurationSection("Prefix").getValues(false).entrySet()) {
					for (String g : groups) {
						if (g.equalsIgnoreCase(map.getKey())) {
							if (CatChat.config.getInt("Priority." + g) < i) {
								prefix = CatChat.config.getString("Prefix." + g);
								i = CatChat.config.getInt("Priority." + g);
							}
						}
					}
				}
				format = format.replace("+prefix", prefix);
			} catch(Exception e) {
				e.printStackTrace();
				format = format.replace("+prefix","");
			}
		}
		if (format.contains("+suffix")) {
			try {
				int i = Integer.MAX_VALUE;
				for (Entry<String, Object> map : plugin.getConfig().getConfigurationSection("Suffix").getValues(false).entrySet()) {
					for (String g : groups) {
						if (g.equalsIgnoreCase(map.getKey())) {
							if (CatChat.config.getInt("Priority." + g) < i) {
								suffix = CatChat.config.getString("Suffix." + g);
								i = CatChat.config.getInt("Priority." + g);
							}
						}
					}
				}
				format = format.replace("+suffix", suffix);
			} catch (Exception e) {
				format = format.replace("+suffix", "");
			}
		}
		if (format.contains("+name")) {
			format = format.replace("+name", player.getName());
		}

		if (format.contains("+nick")) {
			format = format.replace("+nick", (player.getDisplayName() != null) ? player.getDisplayName() : (player.getCustomName() != null) ? player.getCustomName() : player.getName());
		}

		format = ChatColor.translateAlternateColorCodes('&', format);
		if (format.contains("+msg")) {
			format = format.replace("+msg", msg);
			if (CatChat.hasPerms(player, "catchat.colors")) {
				format = ChatColor.translateAlternateColorCodes('&', format);
			}
		}
		return format;
	}

	private boolean isUrl(String url) {
		String regex = "((([A-Za-z]{3,9}:(?://)?)(?:[\\-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9\\.\\-]+|(?:www\\.|[\\-;:&=\\+\\$,\\w]+@)[A-Za-z0-9\\.\\-]+)((?:/[\\+~%/\\.\\w\\-_]*)?\\??(?:[\\-\\+=&;%@\\.\\w_]*)#?(?:[\\.\\!/\\\\\\w]*))?)";
		
		if (Pattern.compile(regex).matcher(url).find()) return true;
		
		for (String s : endings) {
			if (url.contains(s)) return true;
		}
		return false;
	}

	static {
		endings.add(".com");
		endings.add(".co");
		endings.add(".info");
		endings.add(".net");
		endings.add(".org");
		endings.add(".me");
		endings.add(".mobi");
		endings.add(".us");
		endings.add(".biz");
		endings.add(".tv");
		endings.add(".ca");
		endings.add(".au");
		endings.add(".mx");
		endings.add(".ws");
		endings.add(".ag");
		endings.add(".ws");
		endings.add(".am");
		endings.add(".asia");
		endings.add(".at");
		endings.add(".be");
		endings.add(".br");
		endings.add(".bz");
		endings.add(".at");
		endings.add(".cc");
		endings.add(".co");
		endings.add(".de");
		endings.add(".es");
		endings.add(".de");
		endings.add(".eu");
		endings.add(".fm");
		endings.add(".fr");
		endings.add(".gs");
		endings.add(".in");
		endings.add(".it");
		endings.add(".jobs");
		endings.add(".in");
		endings.add(".jp");
		endings.add(".ms");
		endings.add(".mx");
		endings.add(".nl");
		endings.add(".nu");
		endings.add(".nz");
		endings.add(".se");
		endings.add(".tk");
		endings.add(".tw");
		endings.add(".uk");
		endings.add(".cz");
		endings.add(".la");
		endings.add(".xxx");
	}

}
