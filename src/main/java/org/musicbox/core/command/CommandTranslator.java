package org.musicbox.core.command;

import java.util.Arrays;

import org.musicbox.config.DefaultConfig;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class CommandTranslator {

   private final String command;
   private final String[] arguments;
   
   protected CommandTranslator(String command, String[] arguments) {
      this.command = command;
      this.arguments = arguments;
   }
   
   public static CommandTranslator getByEvent(MessageReceivedEvent event) {
      String contentDisplay = event.getMessage().getContentDisplay();
      contentDisplay = contentDisplay.substring(DefaultConfig.PREFIX.length(), 
            contentDisplay.length());
      
      String[] arguments = contentDisplay.split("\\s+");
      String[] commandArguments = Arrays.copyOfRange(arguments, 1, arguments.length);
      return new CommandTranslator(arguments[0], commandArguments);
   }
   
   public String getCommand() {
      return command;
   }

   public String[] getArguments() {
      return arguments;
   }

   public boolean isValid() {
      return (command != null && !command.isBlank());
   }
   
}
