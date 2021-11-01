package org.musicbox.commands;

import java.util.Arrays;

import org.musicbox.core.command.GenericCommand;
import org.musicbox.core.command.GuildCommand;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.managers.CommandManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends GuildCommand {

   public HelpCommand() {
      super("help", Arrays.asList("help", "hlp", "h"), false);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      final EmbedBuilder builder = new EmbedBuilder();

      final int rows = 3;
      int cmdCount = 0;
      for(GenericCommand cmd : CommandManager.getCommandManager().getCommands()) {
         if(cmd.getDescription() != null) {
            cmdCount++;
            boolean inline = (cmdCount % rows) == 1;
            System.out.println(cmdCount + ":" + inline);
            builder.addField(cmd.getName(), wrapper.getLanguage().getLabel(cmd.getDescription()), inline);
         }
      }
      event.getTextChannel().sendMessageEmbeds(builder.build()).queue();
   }

}
