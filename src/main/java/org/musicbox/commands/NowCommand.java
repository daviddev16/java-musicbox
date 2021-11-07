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

public class NowCommand extends GuildCommand {

   public NowCommand() {
      super("now", Arrays.asList("now", "playingnow", "pn"), false);
      description(TranslationKeys.LABEL_NOW_DESCRIPTION);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      
      if(!SelfPermissions.canWrite(event.getTextChannel()))
         return;
      
      if(!wrapper.getScheduler().isPlaying()) {
         throw new FriendlyException(wrapper.getLanguage().getLabel(
               TranslationKeys.LABEL_NOT_PLAYING), Severity.COMMON, null);
      }
      
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .event(event).command(this).build();

      placeholders.add(Placeholder.create(PlaceholderKeys.TRACK_TITLE, 
            wrapper.getScheduler().getCurrentName()));
      placeholders.add(Placeholder.create(PlaceholderKeys.TRACK_POSITION, 
            wrapper.getScheduler().getCurrentPosition()+""));
      
      Messages.Embed.send(event.getTextChannel(), placeholders, 
            TranslationKeys.NOW_COMMAND, null);
      
   }

}
