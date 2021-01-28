package net.romvoid95.discord.commands;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.jagrosh.jdautilities.command.CommandEvent;

import net.romvoid95.discord.CapCom;
import net.romvoid95.discord.commands.impl.CapComCommand;

public class EvalCommand extends CapComCommand {

	public EvalCommand() {
		this.name = "eval";
		this.help = "evaluates nashorn code";
		this.ownerCommand = true;
		this.guildOnly = false;
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent event) {
        event.getChannel().sendTyping().queue();
        event.async(() ->
        {
            ScriptEngine se = new ScriptEngineManager().getEngineByName("Nashorn");
            se.put("bot", CapCom.get());
            se.put("event", event);
            se.put("jda", event.getJDA());
            se.put("guild", event.getGuild());
            se.put("channel", event.getChannel());
            String args = event.getArgs().replaceAll("([^(]+?)\\s*->", "function($1)");
            try
            {
                event.replySuccess("Evaluated Successfully:\n```\n"+se.eval(args)+" ```");
            } 
            catch(Exception e)
            {
                event.replyError("An exception was thrown:\n```\n"+e+" ```");
            }
        });
    }

}
