package org.musicbox.models;

import javax.management.InstanceAlreadyExistsException;

import org.musicbox.core.builders.PlaceholderBuilder;
import org.musicbox.core.command.GenericCommand;
import org.musicbox.core.models.IFallible;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public final class GuildFailHandler implements IFallible {

   private static Logger logger = LoggerFactory.getLogger(GuildFailHandler.class);
   private static GuildFailHandler failHandler;

   private GuildFailHandler() throws InstanceAlreadyExistsException {
      if (failHandler != null)
         throw new InstanceAlreadyExistsException("FailHandler instance already exists.");

      failHandler = this;
   }
   
   @Override
   public void onGenericError(GenericCommand command, TextChannel channel, User sender, TranslationKeys translation) {
      PlaceholderBuilder builder = PlaceholderBuilder.createDefault(true)
            .command(command)
            .user(sender);
      
      Messages.Embed.send(channel, builder, translation, null);
   }

   public static void setup() {
      try {
         new GuildFailHandler();
      } catch (InstanceAlreadyExistsException e) {
         logger.warn(e.getLocalizedMessage());
      }
   }

   public static GuildFailHandler getFailHandler() {
      return failHandler;
   }

}
