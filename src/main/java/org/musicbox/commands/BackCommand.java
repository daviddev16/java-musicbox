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

public class BackCommand extends GuildCommand {

   public BackCommand() {
      super("back", Arrays.asList("back", "bck", "bc", "previous"), false);
      description(TranslationKeys.LABEL_BACK_DESCRIPTION);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      if(!SelfPermissions.canInteract(event.getMember(), wrapper))
         return;

      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .event(event).command(this).build();

      if(!wrapper.getScheduler().isSkippable()) {
         throw new FriendlyException(wrapper.getLanguage().getLabel(
               TranslationKeys.LABEL_UNSKIPPABLE), Severity.COMMON, null);
      }

      final String previousTrack = wrapper.getScheduler().getCurrentName();

      wrapper.getScheduler().previousTrack();

      placeholders.add(Placeholder.create(PlaceholderKeys.TRACK_TITLE_PREVIOUS, 
            previousTrack));

      Messages.Embed.send(event.getTextChannel(), placeholders, 
            TranslationKeys.BACK_COMMAND, null);
   }

}
