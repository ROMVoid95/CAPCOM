package net.romvoid95.discord.data.cache.issue;

public class Cache {

	private String postId;

	private long channelId;

	private long messageId;

	public Cache() {
	}

	/**
	 *
	 * @param messageId
	 * @param postId
	 * @param channelId
	 */
	public Cache(String postId, long channelId, long messageId) {
		super();
		this.postId = postId;
		this.channelId = channelId;
		this.messageId = messageId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public Cache withPostId(String postId) {
		this.postId = postId;
		return this;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public Cache withChannelId(long channelId) {
		this.channelId = channelId;
		return this;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public Cache withMessageId(long messageId) {
		this.messageId = messageId;
		return this;
	}
	
	@Override
	public String toString() {
		return String.format("Cache [\n\tpostId=%s\n\tchannelId=%s\n\tmessageId=%s\n]", postId, channelId, messageId);
	}
}
