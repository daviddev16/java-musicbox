package org.musicbox.core.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface IFallible {

  void onTypeMissmatch(MessageReceivedEvent event, Received received, CommandSupply commandSupply);
  void onWrongArgumentCount(MessageReceivedEvent event, Received received, CommandSupply commandSupply);
  void onNotFound(MessageReceivedEvent event, Received received, CommandSupply commandSupply);
  void onThrowException(MessageReceivedEvent event, Received received, CommandSupply commandSupply, Exception e);
  
}
