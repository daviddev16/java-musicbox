package org.musicbox.commands;

import java.util.Arrays;
import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.command.GuildCommand;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.translation.PlaceholderKeys;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.SelfPermissions;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SelectCommand extends GuildCommand {

   public SelectCommand() {
      super("select", Arrays.asList("select", "slct", "slc", "sl"), false);
      nextRequiredAs(Integer.class);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {

      Member member = event.getMember();
      Integer position = (Integer)params[0];

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

      if(!wrapper.getScheduler().validPosition(position.intValue())) {
         throw new FriendlyException(wrapper.getLanguage().getLabel(
               TranslationKeys.LABEL_INVALID_POSITION), Severity.COMMON, null);
      }
      
      wrapper.getScheduler().select(position.intValue(), true);

      PlaceholderBuilder.putOrReplace(placeholders, Placeholder.create(PlaceholderKeys.TRACK_TITLE, 
            wrapper.getScheduler().getCurrentName()));
      
      Messages.Embed.send(event.getTextChannel(), placeholders, 
            TranslationKeys.SELECT_COMMAND, null);
      
   }

   @Override
   public String toUsageString() {
      return "<title or url>";
   }

}
