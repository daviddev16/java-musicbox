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

   public boolean isMine(String command) {
      for(String usage : usages) {
         if(usage.equals(command)) {
            return true;
         }
      }
      return false;
   }

   public abstract void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params);

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
   
   public void nextRequiredAs(Class<?> nextArgumentClass) {
      arguments.add(nextArgumentClass);
   }

   public void tryoutCommand(CommandTranslator translator) throws ParameterException {
      if(arguments.size() != translator.getArguments().length) {
         throw new ParameterException("Out of bounds.", ParameterException.OUT_OF_BOUNDS);
      }
      for(int i = 0; i < translator.getArguments().length; i++) {
         parse(translator.getArguments()[i], arguments.get(i));
      }
   }

   public Object parse(String argument, Class<?> parameter) {
      try {
         if (parameter == Integer.class || parameter == int.class) {
            return Integer.parseInt(argument);
         } else if (parameter == Byte.class || parameter == byte.class) {
            return Byte.parseByte(argument);
         } else if (parameter == Short.class || parameter == short.class) {
            return Short.parseShort(argument);
         } else if (parameter == Float.class || parameter == float.class) {
            return Float.parseFloat(argument);
         } else if (parameter == Double.class || parameter == double.class) {
            return Double.parseDouble(argument);
         } else if (parameter == Boolean.class || parameter == boolean.class) {
            return Utilities.parseBoolean(argument);
         } else if (parameter == Long.class || parameter == long.class) {
            return Long.parseLong(argument);
         } else if (parameter == String.class) {
            return argument.trim();
         } else {
            throw new ParameterException("Type missmatch.", ParameterException.TYPE_MISSMATCH);
         }
      }catch (Exception e) {
         throw new ParameterException("Type missmatch.", ParameterException.TYPE_MISSMATCH);
      }
   }

   public abstract String toUsageString();

   public boolean isContentArgument() {
      return contentArgument;
   }

}
