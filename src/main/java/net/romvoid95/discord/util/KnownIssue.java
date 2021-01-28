package net.romvoid95.discord.util;

import java.awt.Color;
import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

@Data
@Builder(builderClassName = "Builder", builderMethodName = "build", buildMethodName = "makeFinal", setterPrefix = "set")
public class KnownIssue {
	
	private String tite;
	private String postId;
	private OffsetDateTime time;
	private Patterns issueUrl;
	private Field descriptionField;
	private Field workaroundField;

	public MessageEmbed toEmbed() {
		String irPrefix = "\n**Issue Report Link: **";
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(getTite());
		embed.setDescription(irPrefix + getIssueUrl().get());
		embed.setColor(new Color(15158332));
		embed.setTimestamp(time);
		embed.setFooter("PostID: " + postId, null);
		embed.setThumbnail("https://i.imgur.com/xodG6di.png");
		embed.addField(getDescriptionField());
		embed.addField(getWorkaroundField());
		return embed.build();
	}
}
