package net.romvoid95.discord.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import net.romvoid95.discord.Data;

public class BossPattern implements IPattern<BossPattern> {
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(BossPattern.class);

	private final String bossRegex = Data.config().get().getBossRegex();

	private String contentToCheck;

	private Boolean hasGroupOne; // BOSS
	private String groupOne;

	private Boolean hasGroupTwo; // SPAWNING
	private String groupTwo;

	private Boolean hasGroupThree; // DUNGEON
	private String groupThree;

	private Matcher matcher;

	public BossPattern() {
	}

	@Override
	public BossPattern check(String toCheck) {
		this.contentToCheck = toCheck;
		if (!toCheck.isEmpty()) {
			checkForAll();
		}
		return this;
	}

	public final String getContentToCheck() {
		return contentToCheck;
	}

	private void checkForAll() {
		Pattern pattern = Pattern.compile(bossRegex);
		matcher = pattern.matcher(getContentToCheck());

		StringBuilder b1 = new StringBuilder();
		StringBuilder b2 = new StringBuilder();
		StringBuilder b3 = new StringBuilder();
		while(check()) {
			b1.append(matcher.group(1));
			b2.append(matcher.group(2));
    		b3.append(matcher.group(3));
		}
		groupOne = b1.toString().replace("null", "");
		groupTwo = b2.toString().replace("null", "");
		groupThree = b3.toString().replace("null", "");
		
		hasGroupOne = groupOne.isEmpty() ? false : true;
		hasGroupTwo = groupTwo.isEmpty() ? false : true;
		hasGroupThree = groupThree.isEmpty() ? false : true;
	}

	@Override
	public Boolean check() {
		return matcher.find();
	}

	public boolean hasOne() {
		return hasGroupOne;
	}

	public boolean hasTwo() {
		return hasGroupTwo;
	}

	public boolean hasThree() {
		return hasGroupThree;
	}

	public String getOne() {
		return groupOne;
	}

	public String getTwo() {
		return groupTwo;
	}

	public String getThree() {
		return groupThree;
	}
}
