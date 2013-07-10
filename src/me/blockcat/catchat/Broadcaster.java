package me.blockcat.catchat;

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
	private CatChat plugin;
	private Player player;
	private String[] msga;
	private String oldURL;


	public Broadcaster(URL url, CatChat instance, Player player, String[] msga, String x) {
		pageURL = url;
		plugin = instance;
		this.player = player;
		this.msga = msga;
		this.oldURL = x;
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
			//all = "okij.in/" +all.split(d)[7];
			all = all.replace("http://", "");
			nMsg = nMsg + all;
			in.close();
			r.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			return;
		}

		{
			String msg = "";
			for (String x : msga) {
				if (x.equalsIgnoreCase(oldURL)) {
					x = nMsg;
				}
				msg += x +" ";
			}
			player.chat(msg);
		}


	}

}
