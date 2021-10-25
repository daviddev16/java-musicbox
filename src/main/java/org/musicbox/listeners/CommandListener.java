package org.musicbox.listeners;


import org.musicbox.config.DefaultConfig;
import org.musicbox.core.managers.CommandManager;
import org.musicbox.core.managers.ListenerManager.Listener;
import org.musicbox.core.utils.SelfPermissions;
import org.musicbox.core.utils.Utilities;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandListener extends Listener {

   @Override
   public void onMessageReceived(MessageReceivedEvent event) {

      if (!event.isFromType(ChannelType.TEXT) || event.getAuthor().isBot())
         return;

      /* check if the bot can send embeds in the text channel */
      if (!SelfPermissions.canWrite(event.getTextChannel())) {
         return;
      }

      /* temporary/debugging 
      if(event.getGuild().getIdLong() != 893011039468273705L) {
         event.getTextChannel().sendMessage("Não posso ser usado aqui, estou em fase de teste para a proxima versão.\nCaso queira testar, basta entrar no servidor: https://discord.gg/RjxMwhMwwF.")
         .queue(Utilities.deleteAfter(20L));
         return;
      }*/
      
      /* kernel's id */
      if (DefaultConfig.DEBUG_MODE && event.getMember().getUser().getIdLong() != 339978701297156098L) {
         event.getTextChannel().sendMessage("Você não tem permissão de usar o bot em modo de desenvolvimento.")
               .queue(Utilities.deleteAfter(20L));
         return;
      }

      CommandManager.getCommandManager().perform(DefaultConfig.PREFIX, event);
   }

}
