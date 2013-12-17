package me.blockcat.catchat;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CatChat extends JavaPlugin {

	public static Permission permission = null;
	public static FileConfiguration config;
	public static String denyKind;
	
	public ChannelHandler channelHandler;
	public CatListener listener;
	public String channelFormat = "";
	public String format = "";

	public void onEnable() {		
		setupPermissions();

		this.loadConfig();
		this.getCommand("catchat").setExecutor(new ChatCommands(this));

		listener = new CatListener(this);
		channelHandler = new ChannelHandler(this);
		getServer().getPluginManager().registerEvents(listener, this);

	}

	public void loadConfig() {
		config = this.getConfig();

		if(!config.contains("Format")) {
			config.set("Format", "<+prefix+name+suffix&f> +msg");
		}
		format = config.getString("Format");
		
		if(!config.contains("Channel-format")) {
			config.set("Channel-format", "&f<+prefix &f+name> &7 +msg");
		}
		channelFormat = config.getString("Channel-format");

		if(!config.contains("censor-link")) {
			config.set("censor-link", "<dots>");
		}
		denyKind = config.getString("censor-link");
		config.addDefault("Channel-format", "&f<+prefix &f+name> &7 +msg");
		this.saveDefaultConfig();
	}

	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}


	public String getFormat() {
		return format;
	}
	
	public String getChannelFormat() {
		return channelFormat;
	}
	
	public ChannelHandler getChannelHandler() {
		return this.channelHandler;
	}
	
	public static boolean hasPerms(Player player, String string) {
		if (player.isOp()) return true;
		return permission.has(player, string);
	}

	public static boolean hasPerms(String player, String permission) {
		String w = null;
		return CatChat.permission.has(w, player, permission);
	}
}
