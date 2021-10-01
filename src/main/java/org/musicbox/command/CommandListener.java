package org.musicbox.command;

import org.musicbox.MusicBox;
import org.musicbox.signature.MusicBoxMessages;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

  public void onMessageReceived(MessageReceivedEvent event) {

	if (!event.isFromType(ChannelType.TEXT) || event.getAuthor().isBot())
	  return;

	if (!MusicBox.getConfiguration().isDebugMode()) {
	  MusicBox.getCommandController().handle(event, MusicBox.getConfiguration().getDefaultCommandPrefix());
	  return;
	}

	/* my id lol */
	if (event.getMember().getUser().getIdLong() != 339978701297156098L) {
	  event.getTextChannel().sendMessage("Você não tem permissão de usar o bot em modo de desenvolvimento.")
		  .queue(MusicBoxMessages.deleteAfter(20L));
	  return;
	}

	MusicBox.getCommandController().handle(event, MusicBox.getConfiguration().getDebugCommandPrefix());
  }
}
