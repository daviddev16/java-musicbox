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

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SelectCommand extends GuildCommand {

   public SelectCommand() {
      super("select", Arrays.asList("select", "slct", "slc", "sl"), false);
      nextRequiredAs(Integer.class);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      if(!SelfPermissions.canInteract(event.getMember(), wrapper))
         return;
      
      Integer position = (Integer)params[0];         
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .event(event).command(this).build();

      if(!wrapper.getScheduler().validPosition(position)) {
         throw new FriendlyException(wrapper.getLanguage().getLabel(
               TranslationKeys.LABEL_INVALID_POSITION), Severity.COMMON, null);
      }

      wrapper.getScheduler().select(position.intValue(), true);

      placeholders.add(Placeholder.create(PlaceholderKeys.TRACK_TITLE, 
            wrapper.getScheduler().getCurrentName()));
      placeholders.add(Placeholder.create(PlaceholderKeys.TRACK_POSITION, 
            wrapper.getScheduler().getCurrentPosition()+""));
      
      Messages.Embed.send(event.getTextChannel(), placeholders, 
            TranslationKeys.SELECT_COMMAND, null);
   }

   @Override
   public String toUsageString() {
      return "<position>";
   }

}
