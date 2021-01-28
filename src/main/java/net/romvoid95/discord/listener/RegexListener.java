package net.romvoid95.discord.listener;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.romvoid95.discord.data.ManagerInterface;
import net.romvoid95.discord.data.cache.CacheMessage;
import net.romvoid95.discord.data.cache.issue.Cache;
import net.romvoid95.discord.util.regex.BossPattern;

public class RegexListener extends ListenerAdapter {
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(RegexListener.class);
	
	static Cache bossesPost;
	static CacheMessage messageToLink;
	
	private Boolean shouldRespond;
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		if(event.getChannel().getIdLong() == 495688158458019843L) {
			String messageContent = event.getMessage().getContentRaw();
			BossPattern boss = new BossPattern().check(messageContent);
			runChecks(boss);
			if(shouldRespond) {
				event.getChannel().sendMessage(event.getAuthor().getAsMention() + "\n If I am correct, this Post has the fix to your issue!\n" + messageToLink.getLink()).queue();
				return;
			}
		}
	}
	
	private void runChecks(BossPattern pattern) {
		int passes = 0;
		if(pattern.hasOne()) {
			passes++;
		}
		if(pattern.hasTwo()) {
			passes++;
			passes++;
		}
		if(pattern.hasThree()) {
			passes++;
		}
		if(passes >= 3) {
			this.shouldRespond = true;
		} else {
			this.shouldRespond = false;
		}
	}
	
	public static void setChannel(JDA jda) {
		bossesPost =  ManagerInterface.findPost("f7fbdf2a23fa");
		messageToLink = new CacheMessage(bossesPost.getMessageId(), bossesPost.getChannelId());
	}
}
