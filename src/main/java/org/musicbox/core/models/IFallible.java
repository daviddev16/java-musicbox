package org.musicbox.core.models;

import org.musicbox.core.command.GenericCommand;
import org.musicbox.core.translation.TranslationKeys;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public interface IFallible {
   
   void onGenericError(GenericCommand command, TextChannel channel, User sender, TranslationKeys translationKey);
   void onThrownException(GenericCommand command, TextChannel channel, User sender, Exception exception);
   
}
