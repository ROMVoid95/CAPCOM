package net.romvoid95.discord.commands;

import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;

import net.dv8tion.jda.api.entities.TextChannel;
import net.romvoid95.discord.commands.impl.CapComCommand;
import net.romvoid95.discord.data.ManagerInterface;
import net.romvoid95.discord.util.FormatUtil;

public class ChannelCommand extends CapComCommand {

	public ChannelCommand() {
        this.category = new Category("Manager");
		this.arguments = "<show | set #channel>";
		this.name = "channel";
		this.help = "Displays the currently set channel for Known-Issues post, or sets a new channel ";
	}

	@Override
	protected void execute(CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			event.reply(this.getHelp());
			return;
		}
		String args = event.getArgs();
		if (args.equals("show")) {
			showCurrentChannel(event);
			return;
		}
		if (args.startsWith("set")) {
			List<TextChannel> list = FinderUtil.findTextChannels(event.getArgs(), event.getGuild());
			if (list.isEmpty()) {
				event.replyError("I couldn't find any text channel called `" + event.getArgs() + "`.");
				return;
			}
			if (list.size() > 1) {
				event.replyWarning(FormatUtil.listOfText(list, event.getArgs()));
				return;
			}
			TextChannel textChannel = list.get(0);
			if (!event.getSelfMember().hasPermission(textChannel, IssueManagerCommand.REQUIRED_PERMS)) {
				event.replyError(String.format(IssueManagerCommand.REQUIRED_ERROR, textChannel.getAsMention()));
				return;
			}
			setChannel(event, textChannel);
		}
	}

	private void showCurrentChannel(CommandEvent event) {
		TextChannel textChannel = ManagerInterface.getChannel();
		if (textChannel == null) {
			event.replyWarning("the Issue Post channel is not set on the server! . Please include a channel name.");
		} else {
			event.replySuccess("Known-Issue Posts are currently set to post in " + textChannel.getAsMention()
					+ (event.getSelfMember().hasPermission(textChannel, IssueManagerCommand.REQUIRED_PERMS) ? ""
							: "\n" + event.getClient().getWarning()
									+ String.format(IssueManagerCommand.REQUIRED_ERROR, textChannel.getAsMention())));
		}
		event.getMessage().delete().queue();
	}

	private void setChannel(CommandEvent event, TextChannel textChannel) {
		ManagerInterface.setChannel(textChannel);
		if (textChannel == null)
			event.replyError("Something went wrong! Either the mentioned channel does not exist or it cannot be set");
		else
			event.replySuccess("Known-Issue Posts will now be sent in " + textChannel.getAsMention());

		event.getMessage().delete().queue();
	}
}
