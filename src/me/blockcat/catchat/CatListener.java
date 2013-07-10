package me.blockcat.catchat;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CatListener implements Listener {

	private static List<String> endings = new ArrayList<String>();
	private CatChat plugin;
	private ExecutorService exec = Executors.newCachedThreadPool();

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
		String msg = event.getMessage();
		String[] msga = msg.split(" ");
		String nMsg = "";

		for (String x : msga) {
			if(this.isUrl(x)) {
				if (!CatChat.hasPerms(event.getPlayer(), "catchat.links")) {
					if (CatChat.denyKind.equalsIgnoreCase("<dots>")) {
						x = x.replace(".", " ");
					} else {
						x = CatChat.denyKind;
					} 
					nMsg = nMsg + x + " ";
					//x = "";
					continue;
				}else {
				}
				try {

					//URL pageURL = new URL("http://www.okij.in/api/s/"+x);
					URL pageURL = new URL("http://is.gd/create.php?format=simple&url="+x);
					Broadcaster bc = new Broadcaster(pageURL, plugin, event.getPlayer(), msga, x);

					exec.execute(bc);


					event.setCancelled(true);
					return;
				} catch (Exception e) {
					event.setMessage(msg);
					return;
				}

			} else {
				nMsg = nMsg + x + " ";
				continue;
			}
		}

		Player player = event.getPlayer();
		event.setFormat(format(player, nMsg));
	}

	public String format(Player player, String msg) {
		String prefix = "";
		String suffix = "";
		String format = plugin.getFormat();
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

		for (String s : endings) {
			if (url.contains(s)) return true;
		}
		return false;
	}

}
