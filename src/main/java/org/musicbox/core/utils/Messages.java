package org.musicbox.core.utils;

import java.util.List;

import org.musicbox.core.GuildInstance;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.managers.LanguageManager;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class Messages {

	public static final int COMMAND_FAILED = 0;
	public static final int COMMAND_NOT_FOUND = 1;
	public static final int COMMAND_SYNTAX_ERROR = 2;
	public static final int COMMAND_TYPE_MISSMATCH = 3;
	public static final int COMMAND_MISSING_PERMISSION = 4;

	public static final int ABSENT_ON_VOICE_CHANNEL = 5;
	public static final int NOT_SAME_VOICE_CHANNEL = 6;
	public static final int NO_MATCHES = 7;

	public static final int TRACK_ADDED = 8;
	public static final int PLAYLIST_ADDED = 9;

	private static void send(TextChannel channel, String languageId, int messageId, List<Placeholder> placeholders) {
		JsonObject jsonMessage = LanguageManager.getLanguageManager().getMessage(languageId, messageId);
		channel.sendMessage(EmbedUtils.translate(jsonMessage, placeholders)).queue();
	}

	public static void translatedMessage(MessageReceivedEvent event, int messageId, List<Placeholder> placeholders) {
		GuildInstance guildInstance = GuildManager.getGuildManager().getGuildInstance(event.getGuild());
		send(event.getTextChannel(), guildInstance.getUsedLanguage(), messageId, placeholders);
	}

}
