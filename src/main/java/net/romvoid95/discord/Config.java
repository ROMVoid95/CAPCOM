package net.romvoid95.discord;

public class Config {

	private Long clientId;
	private String token;
	private String prefix;
	private Boolean useWebhook;
	private String webhookUrl;
	private String owner;
	private Long issuesChannel;
	private String pattern = "[%d{HH:mm:ss}] [%level] %logger{0}: %msg%n";
	private String bossRegex;

	public Config() {}
	
	public String getPattern() {
		return pattern;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getToken() {
		return token;
	}

	public void setBossRegex(String bossRegex) {
		this.bossRegex = bossRegex;
	}
	
	public String getBossRegex() {
		return bossRegex;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Boolean getUseWebhook() {
		return useWebhook;
	}

	public void setUseWebhook(Boolean useWebhook) {
		this.useWebhook = useWebhook;
	}
	
	public String getWebhookUrl() {
		return webhookUrl;
	}

	public void setWebhookUrl(String webhookUrl) {
		this.webhookUrl = webhookUrl;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Long getIssuesChannel() {
		return issuesChannel;
	}

	public void setIssuesChannel(Long issuesChannel) {
		this.issuesChannel = issuesChannel;
	}
}
