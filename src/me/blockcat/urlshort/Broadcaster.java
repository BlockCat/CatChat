package me.blockcat.urlshort;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Broadcaster extends Thread {

	private URL pageURL;
	private URLshort plugin;
	private Player player;


	public Broadcaster(URL url, URLshort instance, Player player) {
		pageURL = url;
		plugin = instance;
		this.player = player;
	}

	@Override
	public void run() {
		String nMsg = "";
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) pageURL.openConnection();

			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			Reader r = new InputStreamReader(in);
			int c;
			String all = "";

			while ((c = r.read()) != -1) {
				String u = String.valueOf((char) c);
				all += u;

			}
			String d = Character.toString('"');
			all = "okij.in/" +all.split(d)[7];
			nMsg = nMsg + all;
			in.close();
			r.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			return;
		}

		Server server = plugin.getServer();

		server.broadcastMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.YELLOW +" has shared a link:");
		server.broadcastMessage(ChatColor.GOLD + nMsg);


	}

}
