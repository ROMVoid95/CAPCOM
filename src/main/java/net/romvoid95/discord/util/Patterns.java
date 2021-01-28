package net.romvoid95.discord.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns {
	
	private final String issueUrl = "(?<!\\]\\()https://github\\.com/[TeamGalacticraft]+/[Galacticraft]+/(?:issues)/(\\d+)";
	private final String subst = "[Issue #$1]($0)";
	private final String NA = "N/A";
	
	private String urlToCheck;
	private String result;
	
	private Boolean isValid;
	
	public Patterns() {}
	
	public Patterns checkUrl(String url) {
		this.urlToCheck = url;
		if(url.equalsIgnoreCase(NA.replaceAll("/", ""))) {
			result = NA;
		} else {
			this.isValid = check();
		}
		return this;
	}

	/**
	 * @return the urlToCheck
	 */
	public final String getUrlToCheck() {
		return urlToCheck;
	}
	
	/**
	 * @param urlToCheck the urlToCheck to set
	 */
	public final void setUrlToCheck(String urlToCheck) {
		this.urlToCheck = urlToCheck;
	}
	
	private Boolean check() {
		Pattern pattern = Pattern.compile(issueUrl);
		Matcher matcher = pattern.matcher(getUrlToCheck());
		if(matcher.find()) {
			this.result = matcher.replaceAll(subst);
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean isValid() {
		return isValid;
	}
	
	public String get() {
		return (result.equals(NA)) ? NA : result;
	}
	
}
