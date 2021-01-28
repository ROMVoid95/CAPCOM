package net.romvoid95.discord.util;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import net.romvoid95.discord.commands.impl.CapComCommand;

public class Reflection {
	
	private static String[] packages = new String[] {"net.romvoid95.discord.commands", "net.romvoid95.discord.commands.subcommands"};
	private static Set<Class<? extends CapComCommand>> scannersSet = new HashSet<Class<? extends CapComCommand>>();
	
	public static Set<Class<? extends CapComCommand>> getCommands() {
		ConfigurationBuilder configBuilder = new ConfigurationBuilder().forPackages(packages);
		Reflections reflections = new Reflections(configBuilder);
		scannersSet.addAll(reflections.getSubTypesOf(CapComCommand.class));
		return scannersSet;
	}
}
