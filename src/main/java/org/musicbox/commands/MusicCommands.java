package org.musicbox.commands;

import static org.musicbox.core.utils.Messages.translatedMessage;

import java.util.List;

import org.musicbox.core.GuildInstance;
import org.musicbox.core.command.CommandCategory;
import org.musicbox.core.command.Link;
import org.musicbox.core.command.Usage;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.managers.YoutubeSearchManager;
import org.musicbox.core.utils.Constants;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.Placeholder;
import org.musicbox.core.utils.PlaceholderBuilder;
import org.musicbox.core.utils.Utils;
import org.musicbox.models.PlayTrackResult;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicCommands {

   @Usage(usage = "play/p <track title or url>")
   @Link(commandId = 0, names = { "play", "p" }, category = CommandCategory.MUSIC, argumentsSplit = false)
   private void play(MessageReceivedEvent event, String content) {

      GuildInstance guildInstance = GuildManager.getGuildManager().getGuildInstance(event.getGuild());

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .add(Constants.KEY_USER_INPUT, content).build();

      /* check if the bot is in any voice channel */
      if (!Utils.isPresentOnGuild(event.getGuild())) {

         /* check if the member is on voice channel */
         if (!Utils.isOnVoiceChannel(event.getMember())) {
            translatedMessage(event, Messages.ABSENT_FROM_VOICE_CHANNEL, placeholders);
            return;
         }

         /* connect to the member's voice channel */
         VoiceChannel memberChannel = event.getMember().getVoiceState().getChannel();
         event.getGuild().getAudioManager().openAudioConnection(memberChannel);
      }

      /* check if the member is present in the same voice channel channel */
      else if (!Utils.isTogetherWith(event.getMember(), event.getGuild())) {
         translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
         return;
      }

      if (!Utils.isURL(content)) {
         content = YoutubeSearchManager.getSearchManager().getUrlBasedOnText(content);
      }

      /* load and play track if is not already playing */
      guildInstance.load(content, new PlayTrackResult(event, guildInstance, placeholders));
   }

   @Usage(usage = "queue/q")
   @Link(commandId = 1, names = { "queue", "q" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void queue(MessageReceivedEvent event) {

      GuildInstance guildInstance = GuildManager.getGuildManager().getGuildInstance(event.getGuild());

      StringBuffer buffer = new StringBuffer();
      for(AudioTrack track : guildInstance.getSchedule().getTracklist()) {
         buffer.append(track.getInfo().title).append("\n");
      }

      event.getTextChannel().sendMessage(buffer.toString().trim()).queue();

   }

   @Usage(usage = "stop")
   @Link(commandId = 2, names = { "stop" }, category = CommandCategory.MUSIC, argumentsSplit = true)
   private void stop(MessageReceivedEvent event) {

		GuildInstance guildInstance = GuildManager.getGuildManager().getGuildInstance(event.getGuild());
		guildInstance.getSchedule().stop();

   }


}
