package org.playground;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PaginatorManager {
   
   private static Logger logger = LoggerFactory.getLogger(PaginatorManager.class);
   
   private static PaginatorManager paginatorManager;
   
   private final List<Paginator> paginators;

   private PaginatorManager() {
      this.paginators = new ArrayList<Paginator>();
      logger.info("PaginatorManager loaded.");
      paginatorManager = this;
   }

   public static void setup() {
      new PaginatorManager();
   }

   
   
   public static PaginatorManager getPaginatorManager() {
      return paginatorManager;
   }

   public List<Paginator> getPaginators() {
      return paginators;
   }
   
}
