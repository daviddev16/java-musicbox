package org.musicbox.core.managers;

import java.util.LinkedHashSet;
import java.util.Set;

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

   public void perform(String prefix, MessageReceivedEvent event) {

      GuildWrapper wrapper = GuildManager.getGuildManager().getWrapper(event.getGuild());

      if (!event.getMessage().getContentDisplay().startsWith(prefix))
         return;

      CommandTranslator translator = CommandTranslator.getByEvent(event);

      if (!translator.isValid())
         return;

      GenericCommand command = (GenericCommand) getCommandManager()
            .getCommandByName(translator.getCommand());

      if (command == null) {
         GuildFailHandler.getFailHandler().onGenericError(command, event.getTextChannel(), 
               event.getAuthor(), TranslationKeys.COMMAND_NOT_FOUND);
         return;
      }

      try {
         command.tryoutCommand(translator);
      }catch(ParameterException exception) {
         GuildFailHandler.getFailHandler().onGenericError(command, event.getTextChannel(), 
               event.getAuthor(), TranslationKeys.COMMAND_MISSMATCH);
         return;
      }

      Object[] params = new Object[1];

      if(!command.isContentArgument()) {
         params = new Object[command.getRequiredArguments().size()];
         for(int i = 0; i < params.length; i++) {
            params[i] = command.parse(translator.getArguments()[i], command.getRequiredArguments().get(i));
         }
      } else {
         params[0] = Utilities.getWholeContent(translator.getArguments());
      }

      if (event.isFromGuild())
         command.onExecute(wrapper, event, params);
   }

   public static void setup() {
      try {
         new CommandManager();
      } catch (InstanceAlreadyExistsException e) {
         logger.warn(e.getLocalizedMessage());
      }
   }

   public GenericCommand getCommandByName(String name) {
      return getCommands().stream().filter(cmd -> cmd.isMine(name))
            .findAny().orElse(null);
   }

   public Set<GenericCommand> getCommands() {
      return commands;
   }

   public static CommandManager getCommandManager() {
      return commandManager;
   }

}
