package net.romvoid95.discord.commands.impl;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.ChannelType;

public abstract class CapComCommand extends Command {

	protected String[] requiredRoles = new String[0];

	public void runOverride(CommandEvent event) {

		if (requiredRoles.length >= 1) {
			for (String role : requiredRoles) {
				if (!event.isFromType(ChannelType.TEXT)
						|| event.getMember().getRoles().stream().noneMatch(r -> r.getName().equalsIgnoreCase(role))) {
					terminate(event,
							event.getClient().getError() + " You must have a role called `" + role + "` to use that!");
					return;
				}
			}
		}
		super.run(event);
	}

	private void terminate(CommandEvent event, String message) {
		if (message != null)
			event.reply(message);
		if (event.getClient().getListener() != null)
			event.getClient().getListener().onTerminatedCommand(event, this);
	}

}
