package org.musicbox.models;

import org.musicbox.core.command.CommandSupply;
import org.musicbox.core.command.IFallible;
import org.musicbox.core.command.Received;
import org.musicbox.core.utils.Constants;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.PlaceholderBuilder;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class CommandFailHandler implements IFallible {

	private static CommandFailHandler failHandler;

	private CommandFailHandler() {
		failHandler = this;
	}

	@Override
	public void onNotFound(MessageReceivedEvent event, Received received, CommandSupply commandSupply) {
		Messages.translatedMessage(event, Messages.COMMAND_NOT_FOUND, 
				PlaceholderBuilder.createBy(event, false).build());
	}

	@Override
	public void onTypeMissmatch(MessageReceivedEvent event, Received received, CommandSupply commandSupply) {
		Messages.translatedMessage(event, Messages.COMMAND_TYPE_MISSMATCH,
				PlaceholderBuilder.createBy(event, false).build());
	}

	@Override
	public void onWrongArgumentCount(MessageReceivedEvent event, Received received, CommandSupply commandSupply) {
		Messages.translatedMessage(event, Messages.COMMAND_SYNTAX_ERROR, 
				PlaceholderBuilder.createBy(event, false).build());
	}

	@Override
	public void onThrowException(MessageReceivedEvent event, Received received, CommandSupply commandSupply,
			Exception e) {
		Messages.translatedMessage(event, Messages.COMMAND_FAILED,
				new PlaceholderBuilder(false).event(event).add(Constants.KEY_EXCEPTION_MESSAGE, e.getMessage()).build());
	}

	public static CommandFailHandler getFailHandler() {
		return failHandler;
	}

	public static void setup() {
		new CommandFailHandler();
	}

}
