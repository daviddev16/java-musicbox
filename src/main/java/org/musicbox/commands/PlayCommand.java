package org.musicbox.commands;

import java.util.Arrays;
import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.command.GuildCommand;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.SelfPermissions;
import org.musicbox.models.QueuedTrackResult;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayCommand extends GuildCommand {

   public PlayCommand() {
      super("play", Arrays.asList("play", "p"), true);
      description(TranslationKeys.LABEL_PLAY_DESCRIPTION);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      Member member = event.getMember();
      String titleOrUrl = params[0].toString();

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .event(event).command(this).build();

      if (SelfPermissions.isAlreadyConnect(wrapper)) {
         if (!SelfPermissions.isTogether(member)) {
            Messages.Embed.send(event.getTextChannel(), placeholders, 
                  TranslationKeys.MEMBER_IS_NOT_TOGETHER, null);
            return;
         }
      } else {
         if (member.getVoiceState().inVoiceChannel())
            wrapper.getInspector().connect(member.getVoiceState().getChannel());
         else {
            Messages.Embed.send(event.getTextChannel(), placeholders, 
                  TranslationKeys.INVALID_VOICE_CHANNEL, null);
            return;
         }
      }
      
      wrapper.getScheduler().queue(titleOrUrl, 
            new QueuedTrackResult(event, wrapper, placeholders));
   }

   @Override
   public String toUsageString() {
      return "<title or url>";
   }

}
