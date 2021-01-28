package net.romvoid95.discord.util;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class FormatUtil {

	private final static String MULTIPLE_FOUND = "**Multiple %s found matching \"%s\":**";
	private final static String CMD_EMOJI = "\uD83D\uDCDC"; // 📜

	public static String capitalize(String input) {
		if (input == null || input.isEmpty())
			return "";
		if (input.length() == 1)
			return input.toUpperCase();
		return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
	}

	public static String join(String delimiter, char... items) {
		if (items == null || items.length == 0)
			return "";
		StringBuilder sb = new StringBuilder().append(items[0]);
		for (int i = 1; i < items.length; i++)
			sb.append(delimiter).append(items[i]);
		return sb.toString();
	}

	public static <T> String join(String delimiter, Function<T, String> function, T... items) {
		if (items == null || items.length == 0)
			return "";
		StringBuilder sb = new StringBuilder(function.apply(items[0]));
		for (int i = 1; i < items.length; i++)
			sb.append(delimiter).append(function.apply(items[i]));
		return sb.toString();
	}

	public static String listOfText(List<TextChannel> list, String query) {
		String out = String.format(MULTIPLE_FOUND, "text channels", query);
		for (int i = 0; i < 6 && i < list.size(); i++)
			out += "\n - " + list.get(i).getName() + " (" + list.get(i).getAsMention() + ")";
		if (list.size() > 6)
			out += "\n**And " + (list.size() - 6) + " more...**";
		return out;
	}

	public static Message formatHelp(CommandEvent event) {
		event.getMessage().delete().queue();
		EmbedBuilder builder = new EmbedBuilder()
				.setColor(!event.isFromType(ChannelType.TEXT) ? Color.LIGHT_GRAY : event.getSelfMember().getColor());

		List<Command> commandsInCategory;
		String content;
		if (event.getArgs().isEmpty()) {
			commandsInCategory = Collections.emptyList();
			content = event.getClient().getSuccess() + " **Command Categories:**";
		} else {
			commandsInCategory = event.getClient().getCommands().stream().filter(cmd -> {
				if (cmd.isHidden() || cmd.isOwnerCommand())
					return false;
				if (cmd.getCategory() == null)
					return "general".startsWith(event.getArgs().toLowerCase());
				return cmd.getCategory().getName().toLowerCase().startsWith(event.getArgs().toLowerCase());
			}).collect(Collectors.toList());
			if (commandsInCategory.isEmpty()) {
				content = event.getClient().getWarning() + " No Category `" + event.getArgs() + "` found.";
			} else {
				content = event.getClient().getSuccess() + " **" + event.getSelfUser().getName() + "** "
						+ (commandsInCategory.get(0).getCategory() == null ? "General"
								: commandsInCategory.get(0).getCategory().getName())
						+ " Commands:";
			}
		}

		if (commandsInCategory.isEmpty()) {
			event.getClient().getCommands().stream().filter(cmd -> cmd.getCategory() != null)
					.map(cmd -> cmd.getCategory().getName()).distinct()
					.forEach(cat -> builder.addField(CMD_EMOJI + " " + cat + " Commands",
							"**" + event.getClient().getPrefix() + "help " + cat.toLowerCase() + "**\n\u200B",
							false));
		} else {
			commandsInCategory.forEach(cmd -> builder.addField(
					event.getClient().getPrefix() + cmd.getName()
							+ (cmd.getArguments() == null ? "" : " " + cmd.getArguments()),
					(cmd.getCategory() == null ? "general" : cmd.getCategory().getName().toLowerCase())
							+ "\n\u200B",
					false));
		}

		return new MessageBuilder().append(filterEveryone(content)).setEmbed(builder.build()).build();
	}

	public static String filterEveryone(String input) {
		return input.replace("\u202E", "") // RTL override
				.replace("@everyone", "@\u0435veryone") // cyrillic e
				.replace("@here", "@h\u0435re") // cyrillic e
				.replace("discord.gg/", "discord\u2024gg/") // one dot leader
				.replace("@&", "\u0DB8&"); // role failsafe
	}
}
