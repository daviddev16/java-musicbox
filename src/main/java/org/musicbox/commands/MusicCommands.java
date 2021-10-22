package org.musicbox.commands;

import static org.musicbox.core.utils.Messages.translatedMessage;

import java.util.List;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.Permissions;
import org.musicbox.core.command.CommandCategory;
import org.musicbox.core.command.Link;
import org.musicbox.core.command.Usage;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.managers.YoutubeSearchManager;
import org.musicbox.core.utils.Constants;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.Placeholder;
import org.musicbox.core.utils.PlaceholderBuilder;
import org.musicbox.core.utils.Utils;
import org.musicbox.models.PlayTrackResult;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicCommands {

   @Usage(usage = "play/p <track title or url>")
   @Link(commandId = 0, names = { "play", "p" }, category = CommandCategory.MUSIC, argumentsSplit = false)
   private void play(MessageReceivedEvent event, String content) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, content)
            .add(Constants.KEY_MISSING_PERMISSIONS, Utils.toString(Permissions.VOICE_CHANNEL_PERMISSIONS))
            .build();

      if (!Utils.isSpeakingOnGuild(event.getGuild())) {

         if (!Utils.isOnVoiceChannel(event.getMember())) {
            translatedMessage(event, Messages.ABSENT_FROM_VOICE_CHANNEL, placeholders);
            return;
         }

         VoiceChannel memberChannel = event.getMember().getVoiceState().getChannel();

         if(!Permissions.canConnect(memberChannel)) {
            translatedMessage(event, Messages.COMMAND_MISSING_PERMISSION, placeholders);
            return;
         }

         event.getGuild().getAudioManager().openAudioConnection(memberChannel);
      }

      else if (!Utils.isTogetherWith(event.getMember(), event.getGuild())) {
         translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
         return;
      }

      if (!Utils.isURL(content)) {
         content = YoutubeSearchManager.getSearchManager().getUrlBasedOnText(content);
      }
      /* load and play track if is not already playing */
      guildWrapper.getScheduler().load(content, new PlayTrackResult(event, guildWrapper, placeholders));
   }

   @Usage(usage = "stop")
   @Link(commandId = 1, names = { "stop" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void stop(MessageReceivedEvent event) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, event.getMessage().getContentDisplay())
            .add(Constants.KEY_MISSING_PERMISSIONS, Utils.toString(Permissions.VOICE_CHANNEL_PERMISSIONS))
            .build();

      if (Utils.isSpeakingOnGuild(event.getGuild())) {

         if (!Utils.isTogetherWith(event.getMember(), event.getGuild())) {
            translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
            return;
         }

         guildWrapper.getScheduler().stop();
      }
   }

   @Usage(usage = "pause")
   @Link(commandId = 2, names = { "pause" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void pause(MessageReceivedEvent event) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, event.getMessage().getContentDisplay())
            .add(Constants.KEY_MISSING_PERMISSIONS, Utils.toString(Permissions.VOICE_CHANNEL_PERMISSIONS))
            .build();

      if (Utils.isSpeakingOnGuild(event.getGuild())) {

         if (!Utils.isTogetherWith(event.getMember(), event.getGuild())) {
            translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
            return;
         }

         guildWrapper.getScheduler().setPauseState(true);
      }
   }

   @Usage(usage = "resume")
   @Link(commandId = 3, names = { "resume" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void resume(MessageReceivedEvent event) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, event.getMessage().getContentDisplay())
            .add(Constants.KEY_MISSING_PERMISSIONS, Utils.toString(Permissions.VOICE_CHANNEL_PERMISSIONS))
            .build();

      if (Utils.isSpeakingOnGuild(event.getGuild())) {

         if (!Utils.isTogetherWith(event.getMember(), event.getGuild())) {
            translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
            return;
         }

         guildWrapper.getScheduler().setPauseState(false);
      }
   }

   @Usage(usage = "select <position>")
   @Link(commandId = 4, names = { "select" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void select(MessageReceivedEvent event, int position) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, event.getMessage().getContentDisplay())
            .add(Constants.KEY_MISSING_PERMISSIONS, Utils.toString(Permissions.VOICE_CHANNEL_PERMISSIONS))
            .build();

      if (Utils.isSpeakingOnGuild(event.getGuild())) {

         if (!Utils.isTogetherWith(event.getMember(), event.getGuild())) {
            translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
            return;
         }

         if(guildWrapper.getScheduler().isQueuePosition(position)) {
            guildWrapper.getScheduler().select(position);
         }
      }
   }


   @Usage(usage = "shutdown")
   @Link(commandId = 3, names = { "shutdown" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void shutdown(MessageReceivedEvent event) {
      if(event.getAuthor().getIdLong() == 339978701297156098L && !DefaultConfig.DEBUG_MODE) {
         event.getJDA().shutdown();
      }
   }


}
