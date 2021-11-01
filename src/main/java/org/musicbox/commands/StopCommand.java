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

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StopCommand extends GuildCommand {

   public StopCommand() {
      super("stop", Arrays.asList("stop", "st", "stp"), false);
      description(TranslationKeys.LABEL_STOP_DESCRIPTION);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      if(!SelfPermissions.canInteract(event.getMember(), wrapper))
         return;
      
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .event(event).command(this).build();

      if(wrapper.getScheduler().isEmptyOrDone()) {
         throw new FriendlyException(wrapper.getLanguage().getLabel(
               TranslationKeys.LABEL_EMPTY_LIST), Severity.COMMON, null);
      }

      Messages.Embed.send(event.getTextChannel(), placeholders, 
            TranslationKeys.STOP_COMMAND, null);

      wrapper.getScheduler().stopSchedule();
   }

}
