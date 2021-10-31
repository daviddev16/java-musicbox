package org.musicbox.core.module;

import java.util.Set;

public abstract class CoreModule {

   public abstract void onEnabled();

   public Set<Class<? extends CoreModule>> getDependecies(){
      return null;
   }
   
   @Override
   public String toString() {
      return "(" + getClass().getSimpleName() + ")"; 
   }
}
