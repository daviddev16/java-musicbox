package org.musicbox.commands;


import java.util.Arrays;

import org.musicbox.core.command.GuildCommand;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.SelfPermissions;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LeaveCommand extends GuildCommand {

   public LeaveCommand() {
      super("leave", Arrays.asList("leave", "l", "lv"), false);
      description(TranslationKeys.LABEL_LEAVE_DESCRIPTION);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      if(!SelfPermissions.canWrite(event.getTextChannel()))
         return;
      
      wrapper.getInspector().disconnect();
      
   }

}
