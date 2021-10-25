package org.musicbox.models;

import javax.management.InstanceAlreadyExistsException;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.command.CommandSupply;
import org.musicbox.core.command.Received;
import org.musicbox.core.models.IFallible;
import org.musicbox.miscs.Constants;
import org.musicbox.miscs.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class FailHandler implements IFallible {

   private static Logger logger = LoggerFactory.getLogger(FailHandler.class);
   private static FailHandler failHandler;

   private FailHandler() throws InstanceAlreadyExistsException {
      
      if (failHandler != null)
         throw new InstanceAlreadyExistsException("FailHandler instance already exists.");
      
      failHandler = this;
   }

   @Override
   public void onNotFound(MessageReceivedEvent event, Received received, CommandSupply commandSupply) {
      Messages.translatedMessage(event, Messages.COMMAND_NOT_FOUND,
            PlaceholderBuilder.createBy(event, true).build());
   }

   @Override
   public void onTypeMissmatch(MessageReceivedEvent event, Received received, CommandSupply commandSupply) {
      Messages.translatedMessage(event, Messages.COMMAND_TYPE_MISSMATCH,
            PlaceholderBuilder.createBy(event, false).add(Constants.KEY_COMMAND_USAGE, commandSupply.getUsage())
            .build());
   }

   @Override
   public void onWrongArgumentCount(MessageReceivedEvent event, Received received, CommandSupply commandSupply) {
      Messages.translatedMessage(event, Messages.COMMAND_SYNTAX_ERROR,
            PlaceholderBuilder.createBy(event, false).add(Constants.KEY_COMMAND_USAGE, commandSupply.getUsage())
            .build());
   }

   @Override
   public void onThrowException(MessageReceivedEvent event, Received received, CommandSupply commandSupply,
         Exception e) {
      Messages.translatedMessage(event, Messages.COMMAND_FAILED,
            new PlaceholderBuilder(false).event(event).add(Constants.KEY_EXCEPTION_MESSAGE, e.getMessage()).build());
   }

   public static FailHandler getFailHandler() {
      return failHandler;
   }

   public static void setup() {
      try {
         new FailHandler();
      } catch (InstanceAlreadyExistsException e) {
         logger.warn(e.getLocalizedMessage());
      }
   }

}
