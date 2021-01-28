package net.romvoid95.discord.data.cache.issue;

import java.util.ArrayList;
import java.util.List;

public class IssueCache {

	private List<Cache> cache = new ArrayList<Cache>();

	public IssueCache() {
	}

	/**
	 *
	 * @param cache
	 */
	public IssueCache(List<Cache> cache) {
		super();
		this.cache = cache;
	}

	public List<Cache> getCache() {
		return cache;
	}

	public void setCache(List<Cache> cache) {
		this.cache = cache;
	}

	public IssueCache withCache(List<Cache> cache) {
		this.cache = cache;
		return this;
	}
}
