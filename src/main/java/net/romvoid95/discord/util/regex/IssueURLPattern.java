package net.romvoid95.discord.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IssueURLPattern implements IPattern<IssueURLPattern>{
	
	private final String issueUrl = "(?<!\\]\\()https://github\\.com/[TeamGalacticraft]+/[Galacticraft]+/(?:issues)/(\\d+)";
	private final String subst = "[Issue #$1]($0)";
	private final String NA = "N/A";
	
	private String urlToCheck;
	private String result;
	
	private Boolean isValid;
	
	public IssueURLPattern() {}
	
	@Override
	public IssueURLPattern check(String toCheck) {
		this.urlToCheck = toCheck;
		if(toCheck.equalsIgnoreCase(NA.replaceAll("/", ""))) {
			result = NA;
		} else {
			this.isValid = check();
		}
		return this;
	}

	public final String getUrlToCheck() {
		return urlToCheck;
	}
	
	@Override
	public Boolean check() {
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
