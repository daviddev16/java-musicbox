package org.musicbox.core.managers;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.musicbox.MusicBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class ListenerManager {

   private static Logger logger = LoggerFactory.getLogger(LanguageManager.class);
   private static ListenerManager listenerManager;

   private List<Listener> listeners;

   public ListenerManager() throws InstanceAlreadyExistsException {

      if (listenerManager != null)
         throw new InstanceAlreadyExistsException("ListenerManager instance already exists.");
      
      this.listeners = Collections.synchronizedList(new LinkedList<>());
      
      logger.info("ListenerManager loaded.");
      listenerManager = this;
   }

   public static void setup() { 
      try {
         new ListenerManager();
      } catch (InstanceAlreadyExistsException e) {
         logger.warn(e.getLocalizedMessage());
      } 
   }
   
   public static void register(Listener... listeners) {
      Arrays.stream(listeners).filter(listener -> listener != null)
            .forEach(listener -> getListenerManager().getListeners().add(listener));
   }

   public static void addListener(Listener listener) {
      MusicBox.getMusicBox().getShardManager().addEventListener(listener);
   }

   public static void removeListener(Listener listener) {
      MusicBox.getMusicBox().getShardManager().removeEventListener(listener);
   }
   
   public static Listener[] getAllListeners() {
      return listenerManager.getListeners().toArray(new Listener[listenerManager.getListeners().size()]);
   }
   
   private List<Listener> getListeners() {
      return listeners;
   }

   private static ListenerManager getListenerManager() {
      return listenerManager;
   }

   public static abstract class Listener extends ListenerAdapter {}
}
