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

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ResumeCommand extends GuildCommand {

   public ResumeCommand() {
      super("resume", Arrays.asList("resume", "rs"), true/* ignoring arguments */);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {

      Member member = event.getMember();
      List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
            .event(event).command(this).build();

      if (SelfPermissions.isAlreadyConnect(wrapper)) {
         if (!SelfPermissions.isTogether(member)) {
            Messages.Embed.send(event.getTextChannel(), placeholders, 
                  TranslationKeys.MEMBER_IS_NOT_TOGETHER, null);
            return;
         }
      } else {
         Messages.Embed.send(event.getTextChannel(), placeholders, 
               TranslationKeys.MISSING_BOT, null);
         return;
      }

      if(!wrapper.getScheduler().isPaused()) {
         return;
      }
      
      wrapper.getScheduler().setPauseState(false);
   }

}
