package me.blockcat.catchat.JSON;

import org.json.simple.JSONObject;

public class JsonText extends JsonComponent {
	
	private String text;
	
	public JsonText(String text) {
		this.text = text;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getObject() {
		JSONObject object = new JSONObject();
		object.put("text", text);
		return object;
	}

}
