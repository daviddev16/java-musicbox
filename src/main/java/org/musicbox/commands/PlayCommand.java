package org.musicbox.commands;

import java.util.Arrays;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.command.GuildCommand;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.SelfPermissions;
import org.musicbox.miscs.Messages;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayCommand extends GuildCommand {

   public PlayCommand() {
      super("play", Arrays.asList("play", "p"), true);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {

      Member member = event.getMember();
      PlaceholderBuilder builder = PlaceholderBuilder.createBy(event, true);

      Messages.Embed.send(event, TranslationKeys.PLAY_COMMAND, builder);

      if(SelfPermissions.isAlreadyConnect(wrapper)) {
         if(!SelfPermissions.isTogether(member)) {
          //  Messages.Embed.send(event.getTextChannel(), TranslationKeys.GENERIC_ERROR, 
           //       Placeholder.of(event.getGuild(), TranslationKeys.INVALID_VOICE_CHANNEL));
            return;
         }
      } else {
         if(member.getVoiceState().inVoiceChannel()) {
            wrapper.getInspector().connect(member.getVoiceState().getChannel());
         } else {
          //  Messages.Embed.send(event.getTextChannel(), TranslationKeys.GENERIC_ERROR, 
           //       Placeholder.of(event.getGuild(), TranslationKeys.INVALID_VOICE_CHANNEL));
            return;
         }
      }
      
      wrapper.getScheduler().load(null, null);
   }

   @Override
   public String toUsageString() {
      return "play <title or url>";
   }

}
