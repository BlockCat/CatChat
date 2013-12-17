package me.blockcat.catchat.JSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonMessage extends JsonComponent{
	
	String text = "";
	JSONObject object;
	JSONArray extraArray;
	
	@SuppressWarnings("unchecked")
	public JsonMessage(String text) {
		object = new JSONObject();
		extraArray = new JSONArray();
		
		this.text = text;
		object.put("text", text);
	}
	
	@SuppressWarnings("unchecked")
	public void addText(String text) {
		this.text += " " + text;
		object.put("text", this.text);
	}
	
	@SuppressWarnings("unchecked")
	public void append(JsonComponent component) {
		extraArray.add(component.getObject());
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getObject() {
		object.put("extra", extraArray);
		return object;
	}

}
