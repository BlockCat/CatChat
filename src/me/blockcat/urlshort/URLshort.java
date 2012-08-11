package me.blockcat.urlshort;

import java.io.File;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class URLshort extends JavaPlugin {

	public static Permission permission = null;
	public String format;
	public static FileConfiguration config;

	public void onEnable() {		
		setupPermissions();
		config = this.getConfig();
		File f = new File(this.getDataFolder(), "config.yml");
		if (f.exists()) {
			f.mkdir();
		}

		if (!config.contains("Format"))
			config.set("Format", "&f<+prefix&f>+suffix +name&f: +msg");

		if (!config.contains("Prefix.group"))
			config.set("Prefix.group", "test");

		if (!config.contains("Suffix.group")) 
			config.set("Suffix.group", "test");

		format = config.getString("Format");
		this.saveConfig();

		getServer().getPluginManager().registerEvents(new URLListener(this),this);
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


}
