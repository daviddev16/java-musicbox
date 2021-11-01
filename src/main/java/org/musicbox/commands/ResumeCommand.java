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

public class ResumeCommand extends GuildCommand {

   public ResumeCommand() {
      super("resume", Arrays.asList("resume", "rs"), true);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      if(!SelfPermissions.canInteract(event.getMember(), wrapper))
         return;
      
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .event(event).command(this).build();

      if(!wrapper.getScheduler().isPaused()) {
         throw new FriendlyException(wrapper.getLanguage().getLabel(
               TranslationKeys.LABEL_ALREADY_RESUMED), Severity.COMMON, null);
      }
      
      wrapper.getScheduler().setPauseState(false);
    
      placeholders.add(Placeholder.create(PlaceholderKeys.TRACK_TITLE, 
            wrapper.getScheduler().getCurrentName()));
      placeholders.add(Placeholder.create(PlaceholderKeys.TRACK_POSITION, 
            wrapper.getScheduler().getCurrentPosition()+""));
      
      Messages.Embed.send(event.getTextChannel(), placeholders, 
            TranslationKeys.RESUME_COMMAND, null);
   }

}
