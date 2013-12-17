package me.blockcat.catchat.JSON;

import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

public class JsonUrlEvent extends JsonComponent {
	
	private String text;
	private String value;
	
	public JsonUrlEvent(String text, String value) {
		this.text = text;
		
		if (!value.startsWith("http://")) value = "http://" + value;
		
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getObject() {
		JSONObject object = new JSONObject();
		JSONObject clickObject = new JSONObject();
		JSONObject hoverObject = new JSONObject();

		clickObject.put("action", "open_url");
		clickObject.put("value", value);
		
		hoverObject.put("action", "show_text");
		hoverObject.put("value", ChatColor.LIGHT_PURPLE + value);
		
		object.put("text", text);
		object.put("clickEvent", clickObject);
		object.put("hoverEvent", hoverObject);
		
		return object;
	}

}
