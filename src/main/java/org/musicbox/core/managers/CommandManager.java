package org.musicbox.core.managers;

import java.util.LinkedHashSet;

import java.util.Set;
import java.util.stream.Stream;

import javax.management.InstanceAlreadyExistsException;

import org.musicbox.core.command.CommandTranslator;
import org.musicbox.core.command.GenericCommand;
import org.musicbox.core.exceptions.ParameterException;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.Utilities;
import org.musicbox.models.GuildFailHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

   private static Logger logger = LoggerFactory.getLogger(CommandManager.class);
   private static CommandManager commandManager;

   private final Set<GenericCommand> commands;

   private CommandManager() throws InstanceAlreadyExistsException {

      if (commandManager != null)
         throw new InstanceAlreadyExistsException("CommandManager instance already exists.");

      commands = new LinkedHashSet<>();

      logger.info("CommandManager loaded.");
      commandManager = this;
   }

   public static void setup() {
      try {
         new CommandManager();
      } catch (InstanceAlreadyExistsException e) {
         logger.warn(e.getLocalizedMessage());
      }
   }

   public void perform(String prefix, MessageReceivedEvent event) {

      GuildWrapper wrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      if (!event.getMessage().getContentDisplay().startsWith(prefix))
         return;

      CommandTranslator translator = CommandTranslator.getByEvent(event);

      if (translator.isValid()) {

         GenericCommand command = (GenericCommand) getCommandManager()
               .getCommandByName(translator.getCommand());

         if (command == null) {
            GuildFailHandler.getFailHandler().onGenericError(command, event.getTextChannel(), 
                  event.getAuthor(), TranslationKeys.COMMAND_NOT_FOUND);
            return;
         }
         try {
            /* check the compatibility */
            command.tryoutCommand(translator);

         }catch(ParameterException exception) {
            GuildFailHandler.getFailHandler().onGenericError(command, event.getTextChannel(), 
                  event.getAuthor(), exception.getError());
            return;
         }

         Object[] params = getParameters(command, translator);

         if (event.isFromGuild()) {
            try {
               command.onExecute(wrapper, event, params);
            }catch(Exception e) {
               GuildFailHandler.getFailHandler().onThrownException(command, 
                     event.getTextChannel(), event.getAuthor(), e);
            }
         }
      }
   }

   private Object[] getParameters(GenericCommand command, CommandTranslator translator) {
      if(command != null) {
         if(command.isContentArgument()) {   
            return new Object[] { Utilities.getWholeContent(translator.getArguments()) };
         }
         Object[] parameters = new Object[translator.getArguments().length];
         for(int i = 0; i < parameters.length; i++) {
            parameters[i] = Utilities.parse(translator.getArguments()[i], 
                  command.getRequiredArguments().get(i));
         }
         return parameters;
      }
      return new Object[] {};
   }


   public GenericCommand getCommandByName(String name) {
      return getCommands().stream().filter(cmd -> cmd.isMine(name))
            .findAny().orElse(null);
   }

   public static void register(GenericCommand... commands) {
      Stream.of(commands).forEach(cmd -> getCommandManager().getCommands().add(cmd));
   }

   public Set<GenericCommand> getCommands() {
      return commands;
   }

   public static CommandManager getCommandManager() {
      return commandManager;
   }

}
