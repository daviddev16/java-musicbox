package org.musicbox.modules;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.musicbox.core.module.CoreModule;
import org.playground.Paginator;

public class PaginatorModule extends CoreModule {
   
   private List<Paginator> paginators;
   
   @Override
   public void onRegistered() {
      paginators = Collections.synchronizedList(new LinkedList<>());
   }

   @Override
   public void onDisabling() {}

   public List<Paginator> getPaginators() {
      return paginators;
   }
   
}
