package org.musicbox.commands;

import static org.musicbox.miscs.Messages.translatedMessage;

import java.util.List;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.command.CommandCategory;
import org.musicbox.core.command.Link;
import org.musicbox.core.command.Usage;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.guild.controllers.TrackScheduler;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.utils.SelfPermissions;
import org.musicbox.core.utils.Utilities;
import org.musicbox.miscs.Constants;
import org.musicbox.miscs.Messages;
import org.musicbox.models.QueuedTrackResult;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicCommands {

   /*
    * PLAY COMMAND
    */
   @Usage(usage = "play/p <track title or url>")
   @Link(commandId = 0, names = { "play", "p" }, category = CommandCategory.MUSIC, argumentsSplit = false)
   private void play(MessageReceivedEvent event, String content) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, content)
            .add(Constants.KEY_MISSING_PERMISSIONS, Utilities.toString(SelfPermissions.VOICE_PERMISSIONS))
            .build();

      if(SelfPermissions.isAlreadyConnect(guildWrapper)) {
         if(!SelfPermissions.isTogether(event.getMember())) {
            translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
            return;
         }
      } else if(!event.getMember().getVoiceState().inVoiceChannel()) {
         translatedMessage(event, Messages.ABSENT_FROM_VOICE_CHANNEL, placeholders);
         return;
      }

      VoiceChannel memberChannel = event.getMember().getVoiceState().getChannel();

      if(!SelfPermissions.canSpeak(memberChannel)) {
         translatedMessage(event, Messages.COMMAND_MISSING_PERMISSION, placeholders);
         return;
      }

      if(!SelfPermissions.getSelfMember(memberChannel).getVoiceState().inVoiceChannel()) {
         guildWrapper.getInspector().connect(memberChannel);
      }
      
      guildWrapper.getScheduler().queue(content, new QueuedTrackResult(event, guildWrapper, placeholders));
   }

   /*
    * STOP COMMAND
    */
   @Usage(usage = "stop")
   @Link(commandId = 1, names = { "stop" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void stop(MessageReceivedEvent event) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, event.getMessage().getContentDisplay())
            .build();

      if (SelfPermissions.isAlreadyConnect(guildWrapper) && !SelfPermissions.isTogether(event.getMember())) {
         translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
         return;
      }

      guildWrapper.getScheduler().stopSchedule();
   }



   /*
    * PAUSE COMMAND
    */
   @Usage(usage = "pause")
   @Link(commandId = 2, names = { "pause" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void pause(MessageReceivedEvent event) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, event.getMessage().getContentDisplay())
            .add(Constants.KEY_MISSING_PERMISSIONS, Utilities.toString(SelfPermissions.VOICE_PERMISSIONS))
            .build();

      if (SelfPermissions.isAlreadyConnect(guildWrapper)) {
         if (!SelfPermissions.isTogether(event.getMember())) {
            translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
            return;
         }

         guildWrapper.getScheduler().setPauseState(true);
      }
   }



   /*
    * RESUME COMMAND
    */
   @Usage(usage = "resume")
   @Link(commandId = 3, names = { "resume" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void resume(MessageReceivedEvent event) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, event.getMessage().getContentDisplay())
            .add(Constants.KEY_MISSING_PERMISSIONS, Utilities.toString(SelfPermissions.VOICE_PERMISSIONS))
            .build();

      if (SelfPermissions.isAlreadyConnect(guildWrapper)) {
         if (!SelfPermissions.isTogether(event.getMember())) {
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
            .add(Constants.KEY_MISSING_PERMISSIONS, Utilities.toString(SelfPermissions.VOICE_PERMISSIONS))
            .build();

      if (SelfPermissions.isAlreadyConnect(guildWrapper)) {
         if (!SelfPermissions.isTogether(event.getMember())) {
            translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
            return;
         }

         if(guildWrapper.getScheduler().validPosition(position)) {
            guildWrapper.getScheduler().select(position, true);
         }
      }
   }

   @Usage(usage = "queue")
   @Link(commandId = 3, names = { "queue" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void queue(MessageReceivedEvent event) {

      TrackScheduler scheduler = GuildManager.getGuildManager().getWrapper(event.getGuild()).getScheduler();

      StringBuffer buffer = new StringBuffer();
      scheduler.getTracklist().forEach(e -> {
         buffer.append(e.getInfo().title).append("\n");
      });
      event.getTextChannel().sendMessage(buffer.toString()).queue();

   }

   @Usage(usage = "skip")
   @Link(commandId = 3, names = { "skip" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void skip(MessageReceivedEvent event) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, event.getMessage().getContentDisplay())
            .add(Constants.KEY_MISSING_PERMISSIONS, Utilities.toString(SelfPermissions.VOICE_PERMISSIONS))
            .build();

      if (SelfPermissions.isAlreadyConnect(guildWrapper)) {
         if (!SelfPermissions.isTogether(event.getMember())) {
            translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
            return;
         }

         guildWrapper.getScheduler().nextTrack();
      }

   }

   @Usage(usage = "back")
   @Link(commandId = 3, names = { "back" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void back(MessageReceivedEvent event) {

      GuildWrapper guildWrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, event.getMessage().getContentDisplay())
            .add(Constants.KEY_MISSING_PERMISSIONS, Utilities.toString(SelfPermissions.VOICE_PERMISSIONS))
            .build();

      if (SelfPermissions.isAlreadyConnect(guildWrapper)) {
         if (!SelfPermissions.isTogether(event.getMember())) {
            translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
            return;
         }

         guildWrapper.getScheduler().previousTrack();
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
