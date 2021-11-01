package org.musicbox.core.command;

import java.util.LinkedList;

import java.util.List;

import org.musicbox.core.exceptions.ParameterException;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.utils.Utilities;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class GenericCommand {

   private String name;
   private List<String> usages;
   private boolean contentArgument;
   private List<Class<?>> arguments;

   public GenericCommand(String name, List<String> usages, boolean contentArgument) {
      this.name = name;
      this.usages = usages;
      this.contentArgument = contentArgument;
      this.arguments = new LinkedList<>();
   }

   public abstract void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params);
   
   public boolean isMine(final String command) {
      return usages.stream().anyMatch(usage -> usage.equals(command));
   }

   public void tryoutCommand(CommandTranslator translator) throws ParameterException {
      if(!isContentArgument()) {
         if(arguments.size() != translator.getArguments().length) {
            throw new ParameterException("Out of bounds.", 
                  ParameterException.OUT_OF_BOUNDS);
         }
         try {
            for(int i = 0; i < translator.getArguments().length; i++) {
               Utilities.parse(translator.getArguments()[i], arguments.get(i));
            }
         }catch(Exception e) {
            throw new ParameterException("Type missmatch.", 
                  ParameterException.TYPE_MISSMATCH);
         }
      }
      if(translator.getArguments().length < 1) {
         throw new ParameterException("Empty content.", 
               ParameterException.EMPTY_CONTENT);  
      }
   }

   public void nextRequiredAs(Class<?> nextArgumentClass) {
      arguments.add(nextArgumentClass);
   }
   
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<String> getUsages() {
      return usages;
   }

   public void setUsages(List<String> usages) {
      this.usages = usages;
   }

   public List<Class<?>> getRequiredArguments(){
      return arguments;
   }

   public abstract String toUsageString();

   public boolean isContentArgument() {
      return contentArgument;
   }

}
