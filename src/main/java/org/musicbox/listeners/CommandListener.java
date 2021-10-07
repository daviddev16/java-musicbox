package org.musicbox.listeners;

import java.util.List;
import java.util.StringJoiner;

import org.musicbox.config.Configs;
import org.musicbox.core.Permissions;
import org.musicbox.core.managers.CommandManager;
import org.musicbox.core.models.Listener;
import org.musicbox.core.utils.PlaceholderBuilder;
import org.musicbox.utils.Constants;
import org.musicbox.utils.Messages;
import org.musicbox.utils.Utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandListener extends Listener {

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
	if (!event.isFromType(ChannelType.TEXT) || event.getAuthor().isBot())
	  return;

	if(!Permissions.canWrite(event.getTextChannel(), event.getMember())) {
	  Messages.translatedMessage(event, Messages.COMMAND_MISSING_PERMISSION, new PlaceholderBuilder()
		  .event(event)
		  .add(Constants.KEY_MISSING_PERMISSIONS, toString(Permissions.WRITING_PERMISSION))
		  .build());
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

  public static String toString(List<Permission> perm) {
	StringJoiner joiner = new StringJoiner(", ");
	perm.forEach(p -> joiner.add(p.getName()));
	return joiner.toString().trim();
  }
}
