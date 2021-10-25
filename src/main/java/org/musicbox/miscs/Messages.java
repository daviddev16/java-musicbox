package org.musicbox.miscs;

import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.managers.LanguageManager;
import org.musicbox.core.utils.Utilities;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class Messages {

   public static final int COMMAND_FAILED = 0;
   public static final int COMMAND_NOT_FOUND = 1;
   public static final int COMMAND_SYNTAX_ERROR = 2;
   public static final int COMMAND_TYPE_MISSMATCH = 3;
   public static final int COMMAND_MISSING_PERMISSION = 4;

   public static final int ABSENT_FROM_VOICE_CHANNEL = 5;
   public static final int NOT_SAME_VOICE_CHANNEL = 6;
   public static final int NO_MATCHES = 7;

   public static final int TRACK_ADDED = 8;
   public static final int PLAYLIST_ADDED = 9;

   public static final int QUEUE_PAGE = 20;
   
   private static void send(TextChannel channel, String languageId, int messageId, List<Placeholder> placeholders) {
      JsonObject jsonMessage = LanguageManager.getLanguageManager().getMessage(languageId, messageId);
      channel.sendMessageEmbeds(Utilities.translate(jsonMessage, placeholders)).queue();
   }

   public static void translatedMessage(MessageReceivedEvent event, int messageId, List<Placeholder> placeholders) {
      GuildWrapper wrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());
      send(event.getTextChannel(), wrapper.getLanguage().getUsedLanguage(), messageId,
            placeholders);
   }

}
