package net.romvoid95.discord.data;

import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.entities.TextChannel;
import net.romvoid95.discord.CapCom;
import net.romvoid95.discord.Config;
import net.romvoid95.discord.Data;
import net.romvoid95.discord.data.cache.issue.Cache;
import net.romvoid95.discord.data.cache.issue.IssueCache;

public class ManagerInterface {
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(ManagerInterface.class);
	
	private static JsonDataManager<Config> config = Data.config();
	private static JsonDataManager<IssueCache> issueCache = Data.issueCache();
	
	public static void setChannel(TextChannel channel) {
		config.get().setIssuesChannel(channel.getIdLong());
		config.save();
	}
	
	public static TextChannel getChannel() {
		return CapCom.getJda().getTextChannelById(config.get().getIssuesChannel());
	}
	
	public static Cache findPost(String uid) {
		List<Cache> cacheList = issueCache.get().getCache();
		log.info("Looking for PostID: " + uid);
		for(Cache cache : cacheList) {
			if(cache.getPostId().equalsIgnoreCase(uid)) {
				log.info("Found PostID: " + uid);
				return cache;
			}
		}
		return null;
	}
	
	public static void deleteFromCache(Cache cache) {

		issueCache.get().getCache().remove(cache);
		Data.issueCache().save();
		
//		List<Cache> cacheList = issueCache.get().getCache();
//		cacheList.remove(tempCache);
//		
//		issueCache.get().setCache(cacheList);
//		issueCache.save();
	}
	
}
