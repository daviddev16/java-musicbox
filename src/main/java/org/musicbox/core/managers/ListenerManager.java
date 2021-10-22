package org.musicbox.core.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class ListenerManager {

   private static ListenerManager globalListenerManager;

   private List<Listener> listeners;

   public ListenerManager() {
      this.listeners = new ArrayList<>();
      globalListenerManager = this;
   }

   public static void register(Listener... listeners) {
      Arrays.stream(listeners).filter(listener -> listener != null)
            .forEach(listener -> getListenersList().add(listener));
   }

   private static List<Listener> getListenersList() {
      return globalListenerManager.listeners;
   }

   public static Listener[] getAllListeners() {
      return globalListenerManager.listeners.toArray(new Listener[globalListenerManager.listeners.size()]);
   }

   public static void setup() {
      new ListenerManager();
   }

   public static abstract class Listener extends ListenerAdapter {}
}
