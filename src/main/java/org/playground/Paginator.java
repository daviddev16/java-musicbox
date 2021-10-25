package org.playground;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.musicbox.core.managers.ListenerManager;
import org.musicbox.core.managers.ListenerManager.Listener;
import org.musicbox.core.utils.SelfPermissions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;

public class Paginator extends Listener implements IPaginator {

   private static final String NEXT_EMOJI = "👉🏻";
   private static final String BACK_EMOJI = "👈🏻";

   private List<Page> pages;
   private AtomicReference<Message> messageReference;
   private int currentPage = 0;

   protected Paginator() {
      pages = Collections.synchronizedList(new LinkedList<>());
      messageReference = new AtomicReference<Message>(null);
      currentPage = 0;
   }

   @Override
   public void onGenericGuildMessageReaction(GenericGuildMessageReactionEvent event) {
      if(event.getMessageIdLong() == getMessage().getIdLong() && !SelfPermissions.isSelf(event.getUserIdLong())) {
         switch (event.getReactionEmote().getEmoji()) {
            case BACK_EMOJI -> back();
            case NEXT_EMOJI -> next();
         }
      }
   }

   @Override
   public void open(GuildChannel channel) {
      ((TextChannel) channel).sendMessageEmbeds(pages.get(currentPage).build()).queue(msg -> {
         messageReference.set(msg);
         msg.addReaction(BACK_EMOJI).queue();
         msg.addReaction(NEXT_EMOJI).queue(); /* back | next */    
         ListenerManager.addListener(this);
      });
   }

   @Override
   public synchronized void moveToPage(Page page) {
      if (getMessage() != null) {
         getMessage().editMessageEmbeds(page.build()).queue(msg -> messageReference.set(msg));
         return;
      }
      throw new NullPointerException("Null paginator message.");
   }

   @Override
   public synchronized void close() {
      ListenerManager.removeListener(this);
      if (getMessage() != null)
         getMessage().delete().queue();
   }

   @Override
   public synchronized void next() {
      if(currentPage++ >= pages.size())
         currentPage = 0;

      moveToPage(pages.get(currentPage));
   }

   @Override
   public synchronized void back() {
      if(currentPage-- < 0)
         currentPage = pages.size() - 1;

      moveToPage(pages.get(currentPage));
   }

   @Override
   public Message getMessage() {
      return messageReference.get();
   }

   @Override
   public int getCurrentIndex() {
      return currentPage;
   }

   @Override
   public List<Page> getPages() {
      return pages;
   }
   
   public static final class Page extends EmbedBuilder {}


}
