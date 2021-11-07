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

public class RestartCommand extends GuildCommand {

   public RestartCommand() {
      super("restart", Arrays.asList("restart"), false);
      description(TranslationKeys.LABEL_RESTART_DESCRIPTION);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      if(!SelfPermissions.canInteract(event.getMember(), wrapper))
         return;
      
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .event(event).command(this).build();
      
      if(!wrapper.getScheduler().isPlaying()) {
         throw new FriendlyException(wrapper.getLanguage().getLabel(
               TranslationKeys.LABEL_EMPTY_LIST), Severity.COMMON, null);
      }
      
      placeholders.add(Placeholder.create(PlaceholderKeys.TRACK_TITLE, 
            wrapper.getScheduler().getCurrentName()));
      placeholders.add(Placeholder.create(PlaceholderKeys.TRACK_POSITION, 
            wrapper.getScheduler().getCurrentPosition()+""));
      
      wrapper.getScheduler().restart();
      
      Messages.Embed.send(event.getTextChannel(), placeholders, 
            TranslationKeys.RESTART_COMMAND, null);
   }

}
