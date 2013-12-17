package me.blockcat.catchat;

import java.util.HashMap;
import java.util.Map.Entry;

public enum Symbols {
	
	NOSMILE('\u2639', ":(", ":-("),
	SMILE('\u263B', ":)", ":-)"),
	HEART('\u2764', "<3"),
	ARROWR('\u2794', "->", "-->", "=>");
	
	public static HashMap<String, Character> index = new HashMap<String, Character>();
	
	public char c;
	public String[] vars;
	
	Symbols(char smiley, String...vars) {
		c = smiley;
		this.vars = vars;
	}
	
	static {
		for (Symbols s : Symbols.values()) {
			for (String str : s.vars) {
				index.put(str, s.c);
			}
		}
	}
	
	public static String getSmiley(String msg) {
		for (Entry<String, Character> entry : index.entrySet()) {
			
			if (!msg.contains(entry.getKey())) continue;
			
			msg = msg.replace(entry.getKey(), Character.toString(entry.getValue()));
		}
		return msg;
	}

}
