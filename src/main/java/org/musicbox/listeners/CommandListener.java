package org.musicbox.listeners;

import java.util.List;

import org.musicbox.config.Configs;
import org.musicbox.core.Permissions;
import org.musicbox.core.managers.CommandManager;
import org.musicbox.core.models.Listener;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.Placeholder;
import org.musicbox.core.utils.PlaceholderBuilder;
import org.musicbox.utils.Utils;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandListener extends Listener {

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
	
	if (!event.isFromType(ChannelType.TEXT) || event.getAuthor().isBot())
	  return;

	List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
		.permission().build();
	
	/* check if the bot can send embeds in the text channel */
	if(!Permissions.canWrite(event.getTextChannel(), event.getMember())) {
	  Messages.translatedMessage(event, Messages.COMMAND_MISSING_PERMISSION, placeholders);
	  return;
	}

	if (!Configs.DEBUG_MODE) {
	  CommandManager.getCommandManager().perform(Configs.PREFIX, event);
	  return;
	}

	/* kernel's id */
	if (event.getMember().getUser().getIdLong() != 339978701297156098L) {
	  event.getTextChannel().sendMessage("Você não tem permissão de usar o bot em modo de desenvolvimento.")
	  .queue(Utils.deleteAfter(20L));
	  return;
	}

	CommandManager.getCommandManager().perform(Configs.PREFIX, event);
  }

  
}
