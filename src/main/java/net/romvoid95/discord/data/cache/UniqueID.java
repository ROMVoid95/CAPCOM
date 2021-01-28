package net.romvoid95.discord.data.cache;

import java.util.UUID;

public class UniqueID {
	private String id;
	
	public UniqueID() {
		String[] s = UUID.randomUUID().toString().split("-");
		id = s[0] + s[1];
	}
	
	public UniqueID(String id) {
		this.id = id;
	}
	
	public static String generate() {
		return new UniqueID().getId();
	}
	
	public static UniqueID of(String id) {
		return new UniqueID(id);
	}
	
	public String getId() {
		return id;
	}
}
