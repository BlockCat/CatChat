package me.blockcat.catchat;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CatChat extends JavaPlugin {

	public static Permission permission = null;
	public String format;
	public static FileConfiguration config;

	public void onEnable() {		
		setupPermissions();
		config = this.getConfig();
		
		format = config.getString("Format");
		getServer().getPluginManager().registerEvents(new CatListener(this),this);
	}
	public void onDissable() {
		this.saveConfig();
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
