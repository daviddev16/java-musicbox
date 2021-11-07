package org.musicbox.commands;

import java.util.Arrays;
import java.awt.Color;

import org.musicbox.core.command.GenericCommand;
import org.musicbox.core.command.GuildCommand;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.managers.CommandManager;
import org.musicbox.core.utils.SelfPermissions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends GuildCommand {

   public HelpCommand() {
      super("help", Arrays.asList("help", "hlp", "h"), false);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      
      if(!SelfPermissions.canWrite(event.getTextChannel()))
         return;
      
      final StringBuilder strBuilder = new StringBuilder();
      for(GenericCommand cmd : CommandManager.getCommandManager().getCommands()) {
         if(cmd.getDescription() != null) {
            strBuilder.append( "**" + ((GuildCommand)cmd).getFullUsage() + "**" ).append('\n');
            strBuilder.append( wrapper.getLanguage().getLabel(cmd.getDescription()) ).append('\n');
         }
      }

      strBuilder.trimToSize();
      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setColor(Color.decode("#bb72da"));
      embedBuilder.setDescription(strBuilder.toString());
      event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
   }

}
