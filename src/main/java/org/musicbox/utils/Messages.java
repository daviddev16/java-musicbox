package org.musicbox.utils;

import java.util.List;

import org.musicbox.core.GuildInstance;
import org.musicbox.core.LanguageManager;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.models.Placeholder;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class Messages {

  public static final int TRACK_SKIPPED = 1;
  public static final int TRACK_STOPPED = 2;
  public static final int TRACK_PAUSED = 3;
  public static final int TRACK_RESUMED = 4;
  public static final int TRACK_REPEAT_MODE = 5;
  public static final int TRACK_RESTARTED = 6;
  public static final int TRACK_PLAYING_NOW = 9;

  public static final int COMMAND_FAILED = 0;
  public static final int COMMAND_NOT_FOUND = 1;
  public static final int COMMAND_SYNTAX_ERROR = 2;
  public static final int COMMAND_TYPE_MISSMATCH = 3;

  public static final int COMMAND_MISSING_PERMISSION = 5;

  public static final int TRACK_ADDED = 4;

  public static void send(TextChannel channel, String languageId, int messageId, List<Placeholder> placeholders) {
	JsonObject jsonMessage = LanguageManager.getLanguageManager().getMessage(languageId, messageId);
	channel.sendMessage(EmbedTranslator.translate(jsonMessage, placeholders)).queue();
  }

  public static void translatedMessage(MessageReceivedEvent event, int messageId, List<Placeholder> placeholders) {

	GuildInstance guildInstance = GuildManager.getGuildManager().getGuildInstance(event.getGuild());
	send(event.getTextChannel(), guildInstance.getUsedLanguage(), messageId, placeholders);
  }

}
