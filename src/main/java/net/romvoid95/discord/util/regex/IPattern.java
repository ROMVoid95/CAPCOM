package net.romvoid95.discord.util.regex;

public interface IPattern<T> {

	public T check(String toCheck);
	
	Boolean check();
	
}
