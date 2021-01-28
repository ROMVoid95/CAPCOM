/*
 * Copyright 2017 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.romvoid95.discord;

import java.util.Arrays;

import javax.security.auth.login.LoginException;

import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.romvoid95.discord.commands.impl.CapComCommand;
import net.romvoid95.discord.listener.RegexListener;
import net.romvoid95.discord.logging.WebhookAppender;
import net.romvoid95.discord.util.FormatUtil;
import net.romvoid95.discord.util.Reflection;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class CapCom {

	private static final Logger log = (Logger) LoggerFactory.getLogger(CapCom.class);
	
	// STATIC
	private static CapCom capcom;
	private static JDA jda;
	private static EventWaiter waiter;

	// NON STATIC
	private CommandClientBuilder builder;
	private Config config = Data.config().get();

	private CapCom() throws LoginException {
		capcom = this;
		waiter = new EventWaiter();

		Data.initializeIssueCache();

		this.builder = new CommandClientBuilder();
		builder.useDefaultGame();
		builder.setOwnerId(config.getOwner());
		builder.setEmojis("\u2611", "\u26a0", "\uD83D\uDED1");
		builder.setPrefix(config.getPrefix());
		builder.setHelpConsumer(event -> event.reply(FormatUtil.formatHelp(event)));
		autoRegisterCommands(builder);

		GatewayIntent[] toEnable = { 
				GatewayIntent.GUILD_MESSAGES, // Recieve guild messages, needed to, well operate at all.
				GatewayIntent.GUILD_MESSAGE_REACTIONS, // Receive message reactions, used for reaction menus.
				GatewayIntent.GUILD_MEMBERS
		};
		
		CacheFlag[] toDisable = {
				CacheFlag.ACTIVITY,
				CacheFlag.VOICE_STATE,
				CacheFlag.EMOTE,
				CacheFlag.CLIENT_STATUS
		};
		
		if (config.getUseWebhook()) {
			log.info("Webhook Used: " + config.getUseWebhook());
			LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

			ThresholdFilter filter = new ThresholdFilter();
			filter.setLevel("all");
			filter.setContext(lc);
			filter.start();

			PatternLayoutEncoder encoder = new PatternLayoutEncoder();
			encoder.setPattern("**" + config.getPattern() + "**");
			encoder.setContext(lc);
			encoder.start();

			WebhookAppender appender = new WebhookAppender();
			appender.setEncoder(encoder);
			appender.addFilter(filter);
			appender.setWebhookUrl(config.getWebhookUrl());
			appender.setName("ERROR_WH");
			appender.setContext(lc);
			appender.start();

			Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
			root.addAppender(appender);
		}

		jda = JDABuilder.create(config.getToken(), Arrays.asList(toEnable))
				.setStatus(OnlineStatus.DO_NOT_DISTURB)
				.setActivity(Activity.playing("loading..."))
				.addEventListeners(waiter, builder.build(), new RegexListener())
				.disableCache(Arrays.asList(toDisable))
				.build();

	}

	public static void main(String[] args) throws LoginException {
		new CapCom();
		
		RegexListener.setChannel(getJda());
	}

	private void autoRegisterCommands(CommandClientBuilder builder) {
		CapComCommand commandClass;
		for (Class<? extends CapComCommand> clazz : Reflection.getCommands()) {
			log.info("Registered Command: " + clazz.getSimpleName().replace("Command", ""));
			try {
				commandClass = clazz.newInstance();
				builder.addCommand(commandClass);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static CapCom get() {
		return capcom;
	}

	public static JDA getJda() {
		return jda;
	}

	public static EventWaiter getEventWaiter() {
		return waiter;
	}
}
