/*
 * Copyright 2017 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.romvoid95.discord.commands;

import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.romvoid95.discord.CapCom;
import net.romvoid95.discord.Config;
import net.romvoid95.discord.Data;
import net.romvoid95.discord.commands.impl.CapComCommand;
import net.romvoid95.discord.data.ManagerInterface;
import net.romvoid95.discord.data.cache.CacheMessage;
import net.romvoid95.discord.data.cache.UniqueID;
import net.romvoid95.discord.data.cache.issue.Cache;
import net.romvoid95.discord.data.cache.issue.IssueCache;
import net.romvoid95.discord.util.KnownIssue;
import net.romvoid95.discord.util.Patterns;

public class IssueCommand extends CapComCommand {

    public static Permission[] REQUIRED_PERMS = {Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY};
    public static String REQUIRED_ERROR = "I am missing the necessary permissions (Read Messages, Send Messages, Read Message History, and Embed Links) in %s!";

	KnownIssue.Builder ki = KnownIssue.build();
	private CommandEvent event;
	private CacheMessage questionMessage;
	private CacheMessage headerMessage;
	
	private final IssueCache issueCache = Data.issueCache().get();
	private final Config config = Data.config().get();
	private Cache cachedPost;
	
	private String UUID;

	public IssueCommand() {
		this.name = "issue";
		this.arguments = "<new | delete | test>";
		this.category = new Category("Manager");
		this.help = "Known-Issue Interface";
		this.requiredRoles = new String[] { "Moderator", "Mission Control" };
		this.botPermissions = new Permission[] {Permission.MESSAGE_MANAGE};
	}

	@Override
	protected void execute(CommandEvent event) {
		if(!event.getArgs().isEmpty()) {
			switch (event.getArgs()) {
			case "test":
				test(event);
				break;
			case "new":
				start(event);
				break;
			case "delete":
				delete(event);
				break;
			}
		}
	}
	
	private void delete(CommandEvent event) {
		EmbedBuilder header = new EmbedBuilder().setTitle("__Known-Issue Manager Interface__")
				.setDescription("\n**Max User Input  Wait Time:**\n *3 Minutes*\n\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		this.event = event;
		event.getEvent().getMessage().delete().queue();
		event.getChannel().sendMessage(header.build()).queue(m -> {
			this.headerMessage = new CacheMessage(m.getIdLong(), m.getChannel().getIdLong());
		});
		
		this.Stage1_Delete(event);
	}
	
	private void Stage1_Delete(CommandEvent event) {
		event.reply("**\nProvide Post-ID to Delete**", m -> {
			this.questionMessage = new CacheMessage(m.getIdLong(), m.getChannel().getIdLong());
		});
		
		CapCom.getEventWaiter().waitForEvent(MessageReceivedEvent.class, 
				e -> validate(e), 
				e -> {
					cachedPost = ManagerInterface.findPost(e.getMessage().getContentStripped());
					UUID = e.getMessage().getContentStripped();
					e.getMessage().delete().queue();
					Stage2_Delete();
				}, 3, TimeUnit.MINUTES, () -> event.reply("TimeOut Time Exceeded, Terminating Known Issue Creation"));
	}
	
	private void Stage2_Delete() {
		if(cachedPost!=null) {
			questionMessage.edit("**\nConfirm Deletion**\n\t-> Reply With *delete* to confirm");

			CapCom.getEventWaiter().waitForEvent(MessageReceivedEvent.class, 
					e -> repliedWithDelete(e), 
					e -> {
						e.getMessage().delete().queue();
						DeletePost();
					}, 3, TimeUnit.MINUTES, () -> event.reply("TimeOut Time Exceeded, Terminating Known Issue Creation"));
		}
		
	}
	
	private Boolean repliedWithDelete(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().equalsIgnoreCase("delete");
	}
	
	private void DeletePost() {
		CacheMessage messageToDelete = new CacheMessage(cachedPost.getMessageId(), cachedPost.getChannelId());
		messageToDelete.delete(s -> {
			questionMessage.edit("**\nConfirmation**\n\t-> Known-Issue(ID: " +  UUID + ") has been Deleted");
		});

		ManagerInterface.deleteFromCache(cachedPost);
		
		try {
			Thread.sleep(8 * 1000);
			
			headerMessage.delete();
			questionMessage.delete();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	private void test(CommandEvent event) {
		event.getEvent().getMessage().delete().queue();
		UUID = new UniqueID().getId();
		ki.setPostId(UUID);
		ki.setTime(event.getMessage().getTimeCreated());
		ki.setTite("Test Issue");
		ki.setIssueUrl(new Patterns().checkUrl("NA"));
		ki.setDescriptionField(new Field("__Description__", "NO CONTENT, JUST A TEST", false));
		ki.setWorkaroundField(new Field("__Workaround / Fix__", "NO CONTENT, JUST A TEST", false));
		TextChannel channel = CapCom.getJda().getTextChannelById(config.getIssuesChannel());
		channel.sendMessage(ki.makeFinal().toEmbed()).queue(s -> {
			Cache cache = new Cache(UUID, s.getChannel().getIdLong(), s.getIdLong());
			issueCache.getCache().add(cache);
			Data.issueCache().save();
		});;
	}
	
	private void start(CommandEvent event) {
		EmbedBuilder header = new EmbedBuilder().setTitle("__Known-Issue Manager Interface__")
				.setDescription("\n**Max User Input  Wait Time:**\n *3 Minutes*\n\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

		this.event = event;
		UUID = new UniqueID().getId();
		ki.setPostId(UUID);
		ki.setTime(event.getMessage().getTimeCreated());
		event.getEvent().getMessage().delete().queue();
		event.getChannel().sendMessage(header.build()).queue(m -> {
			this.headerMessage = new CacheMessage(m.getIdLong(), m.getChannel().getIdLong());
		});
		
		this.Stage1_GetTitle();
	}

	private void Stage1_GetTitle() {

		event.reply("**\nProvide The Known Issue Title To Use**", m -> {
			this.questionMessage = new CacheMessage(m.getIdLong(), m.getChannel().getIdLong());
		});

		CapCom.getEventWaiter().waitForEvent(MessageReceivedEvent.class, e -> validate(e), e -> {
			ki.setTite(e.getMessage().getContentStripped());
			e.getMessage().delete().queue();
			Stage2_GetIssueLink();
		}, 3, TimeUnit.MINUTES, () -> event.reply("TimeOut Time Exceeded, Terminating Known Issue Creation"));

	}

	private void Stage2_GetIssueLink() {
		questionMessage.edit("**\nPost Github Issue Link**\n*Reply with* **NA** *if one does not exist**");
		CapCom.getEventWaiter().waitForEvent(MessageReceivedEvent.class, e -> validate(e), e -> {
			ki.setIssueUrl(new Patterns().checkUrl(e.getMessage().getContentStripped()));
			e.getMessage().delete().queue();
			Stage3_GetDescription();
		}, 3, TimeUnit.MINUTES, () -> event.reply("TimeOut Time Exceeded, Terminating Known Issue Creation"));
	}

	private void Stage3_GetDescription() {
		questionMessage.edit("**\nProvide an imformative and detailed description of the issue or bug**");
		CapCom.getEventWaiter().waitForEvent(MessageReceivedEvent.class, e -> validate(e), e -> {
			ki.setDescriptionField(new Field("__Description__", e.getMessage().getContentStripped(), false));
			e.getMessage().delete().queue();
			Stage3_GetWorkAround();
		}, 3, TimeUnit.MINUTES, () -> event.reply("TimeOut Time Exceeded, Terminating Known Issue Creation"));
	}

	private void Stage3_GetWorkAround() {
		questionMessage.edit(
				"\n*(If One Exists)*\n**Provide an imformative and detailed description of how to fix the problem**");
		CapCom.getEventWaiter().waitForEvent(MessageReceivedEvent.class, e -> validate(e), e -> {
			ki.setWorkaroundField(new Field("__Workaround / Fix__", e.getMessage().getContentStripped(), false));
			e.getMessage().delete().queue();
			FinaleStage();
		}, 3, TimeUnit.MINUTES, () -> event.reply("TimeOut Time Exceeded, Terminating Known Issue Creation"));
	}

	private void FinaleStage() {
		headerMessage.delete();
		questionMessage.delete();
		TextChannel channel = CapCom.getJda().getTextChannelById(config.getIssuesChannel());
		channel.sendMessage(ki.makeFinal().toEmbed()).queue(s -> {
			Cache cache = new Cache(UUID, s.getChannel().getIdLong(), s.getIdLong());
			issueCache.getCache().add(cache);
			Data.issueCache().save();
		});;
	}

	private <T> Boolean validate(MessageReceivedEvent e) {
		return e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel())
				&& !e.getMessage().equals(event.getMessage());
	}
}
