package org.musicbox.core.module;

public abstract class CoreModule {
   
   public void onRegistered() {}
   
   public void onDisabling() {}

   @Override
   public String toString() {
      return "(" + getClass().getSimpleName() + ")"; 
   }
   
}
