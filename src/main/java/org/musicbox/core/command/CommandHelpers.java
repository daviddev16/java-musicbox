package org.musicbox.core.command;

import org.musicbox.core.utils.PlaceholderBuilder;
import org.musicbox.utils.Constants;
import org.musicbox.utils.Messages;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class CommandHelpers implements IFallible {

  private static CommandHelpers commandHelpers;

  public CommandHelpers() {
	commandHelpers = this;
  }

  public static CommandHelpers getHelpers() {
	return commandHelpers;
  }

  public static void setup() {
	new CommandHelpers();
  }

  @Override
  public void onNotFound(MessageReceivedEvent event, Received received, CommandSupply commandSupply) {
	Messages.translatedMessage(event, Messages.COMMAND_NOT_FOUND, new PlaceholderBuilder(false).event(event).build());
  }

  @Override
  public void onTypeMissmatch(MessageReceivedEvent event, Received received, CommandSupply commandSupply) {
	Messages.translatedMessage(event, Messages.COMMAND_TYPE_MISSMATCH, new PlaceholderBuilder(false).event(event).build());
  }

  @Override
  public void onWrongArgumentCount(MessageReceivedEvent event, Received received, CommandSupply commandSupply) {
	Messages.translatedMessage(event, Messages.COMMAND_SYNTAX_ERROR, new PlaceholderBuilder(false).event(event).build());
  }

  @Override
  public void onThrowException(MessageReceivedEvent event, Received received, CommandSupply commandSupply,
	  Exception e) {
	Messages.translatedMessage(event, Messages.COMMAND_FAILED,
		new PlaceholderBuilder(false).event(event).add(Constants.KEY_EXCEPTION_MESSAGE, e.getMessage()).build());
  }

}
