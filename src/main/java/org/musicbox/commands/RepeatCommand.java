package org.musicbox.commands;

import java.util.Arrays;
import java.util.List;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.core.command.GuildCommand;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.guild.controllers.TrackScheduler.RepeatMode;
import org.musicbox.core.translation.PlaceholderKeys;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.SelfPermissions;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RepeatCommand extends GuildCommand {

   public RepeatCommand() {
      super("repeat", Arrays.asList("repeat", "rp"), true);
      description(TranslationKeys.LABEL_REPEAT_DESCRIPTION);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      if(!SelfPermissions.canInteract(event.getMember(), wrapper)) {
         return;
      }
      String repeatModeString = params[0].toString().toUpperCase();
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .event(event).command(this).build();
      
      if(!RepeatMode.isValid(repeatModeString)) {
         Messages.Embed.send(event.getTextChannel(), placeholders, 
               TranslationKeys.WRONG_REPEAT_MODE, null);
         return;
      }
      wrapper.getScheduler().setRepeatMode(RepeatMode.valueOf(repeatModeString));
      
      placeholders.add(Placeholder.create(PlaceholderKeys.REPEAT_MODE, 
            repeatModeString));
      
      Messages.Embed.send(event.getTextChannel(), placeholders, 
            TranslationKeys.REPEAT_COMMAND, null);
   }

   @Override
   public String toUsageString() {
      return "<All | Single | None>";
   }

}
