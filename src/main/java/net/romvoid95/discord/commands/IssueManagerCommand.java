package net.romvoid95.discord.commands;

import static net.dv8tion.jda.api.Permission.*;

import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.romvoid95.discord.commands.impl.CapComCommand;
import net.romvoid95.discord.util.FormatUtil;

public class IssueManagerCommand extends CapComCommand {
	
    public static Permission[] REQUIRED_PERMS = {MESSAGE_READ, MESSAGE_WRITE, MESSAGE_EMBED_LINKS, MESSAGE_HISTORY, MESSAGE_MANAGE};
    public static String REQUIRED_ERROR = "I am missing the necessary message related permissions (Read, Send, Read History, Embed, and Manage) in %s!";

    public IssueManagerCommand() {
    	this.name = "manager";
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
        this.guildOnly = true;
        this.category = new Category("Manager");
        this.children = new CapComCommand[] {new ChannelCommand(), new IssueCommand()};
    }
    
    @Override
    protected void execute(CommandEvent event)
    {
        if(event.getArgs().isEmpty())
        {
            event.reply(this.getHelp());
            return;
        }
        List<TextChannel> list = FinderUtil.findTextChannels(event.getArgs(), event.getGuild());
        if(list.isEmpty())
        {
            event.replyError("I couldn't find any text channel called `"+event.getArgs()+"`.");
            return;
        }
        if(list.size()>1)
        {
            event.replyWarning(FormatUtil.listOfText(list, event.getArgs()));
            return;
        }
        
        TextChannel textChannel = list.get(0);
        if(!event.getSelfMember().hasPermission(textChannel, REQUIRED_PERMS))
        {
            event.replyError(String.format(REQUIRED_ERROR, textChannel.getAsMention()));
            return;
        }

    }
}
