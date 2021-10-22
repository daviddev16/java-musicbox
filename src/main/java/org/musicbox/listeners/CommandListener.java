package org.musicbox.listeners;


import org.musicbox.config.DefaultConfig;
import org.musicbox.core.Permissions;
import org.musicbox.core.managers.CommandManager;
import org.musicbox.core.models.Listener;
import org.musicbox.core.utils.Utils;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandListener extends Listener {

   @Override
   public void onMessageReceived(MessageReceivedEvent event) {

      if (!event.isFromType(ChannelType.TEXT) || event.getAuthor().isBot())
         return;

      /* check if the bot can send embeds in the text channel */
      if (!Permissions.canWrite(event.getTextChannel(), event.getGuild().getSelfMember())) {
         return;
      }

      /* kernel's id */
      if (DefaultConfig.DEBUG_MODE && event.getMember().getUser().getIdLong() != 339978701297156098L) {
         event.getTextChannel().sendMessage("Você não tem permissão de usar o bot em modo de desenvolvimento.")
               .queue(Utils.deleteAfter(20L));
         return;
      }

      CommandManager.getCommandManager().perform(DefaultConfig.PREFIX, event);
   }

}
