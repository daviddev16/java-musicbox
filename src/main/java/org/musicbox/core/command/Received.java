package org.musicbox.core.command;

import java.util.Arrays;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Received {

   private final String name;
   private final MessageReceivedEvent event;
   private final String[] arguments;
   private final String content;

   private Received(String name, String content, String[] arguments, MessageReceivedEvent event) {
      this.name = name;
      this.event = event;
      this.arguments = arguments;
      this.content = content;
   }

   public MessageReceivedEvent getEvent() {
      return this.event;
   }

   public String getJoinedArguments() {
      return Arrays.stream(getArguments()).collect(Collectors.joining(" ")).trim();
   }

   public String[] getArguments() {
      return arguments;
   }

   public String getName() {
      return name;
   }

   public String getContent() {
      return this.content;
   }

   public static Received read(String prefix, MessageReceivedEvent event) {
      String content = event.getMessage().getContentRaw().trim();
      String[] contentList = content.split("\\s+");
      String[] arguments = Arrays.copyOfRange(contentList, 1, contentList.length);
      String commandName = contentList[0].substring(prefix.length());
      return new Received(commandName, content, arguments, event);
   }

}
