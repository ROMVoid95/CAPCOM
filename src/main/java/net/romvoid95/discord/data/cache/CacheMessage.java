package net.romvoid95.discord.data.cache;

import java.util.function.Consumer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.RestAction;
import net.romvoid95.discord.CapCom;

public class CacheMessage {
	
	public long msgId;
	public long channelId;
	private JDA jda = CapCom.getJda();
	private String jumpUrl;

	public CacheMessage(long msgId, long channelId) {
		this.msgId = msgId;
		this.channelId = channelId;
	}
	
	public void edit(String content) {
		jda.getTextChannelById(channelId).editMessageById(msgId, content).queue();
	}
	
	public void delete() {
		jda.getTextChannelById(channelId).deleteMessageById(msgId).queue();
	}
	
	public void delete(Consumer<? super Void> onSuccess) {
		jda.getTextChannelById(channelId).deleteMessageById(msgId).queue(onSuccess);
	}
	
	public String getLink() {
		RestAction<Message> message = jda.getTextChannelById(channelId).retrieveMessageById(msgId);
		jumpUrl = message.complete().getJumpUrl();
		message.submit();
		return jumpUrl;
	}
}
