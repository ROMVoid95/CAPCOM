package net.romvoid95.discord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.romvoid95.discord.data.JsonDataManager;
import net.romvoid95.discord.data.cache.issue.Cache;
import net.romvoid95.discord.data.cache.issue.IssueCache;

public class Data {
	
    private static JsonDataManager<Config> config;
    private static JsonDataManager<IssueCache> issueCache;
    
    private static File lockFile = new File("cacheLock");
    
    public void initializeConfig() {
    	Data.config().get();
    }
    
    public static void initializeIssueCache() {
    	if(!lockFile.exists()) {
    		try {
				lockFile.createNewFile();
		    	Data.issueCache().get();
		    	Data.issueCache().get().setCache(new ArrayList<Cache>());
		    	Data.issueCache().save();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
 
    public static JsonDataManager<Config> config() {
        if (config == null) {
            config = new JsonDataManager<>(Config.class, "config.json", Config::new);
        }

        return config;
    }
    
    public static JsonDataManager<IssueCache> issueCache() {
        if (issueCache == null) {
        	issueCache = new JsonDataManager<>(IssueCache.class, "cache.json", IssueCache::new);
        }

        return issueCache;
    }
}
