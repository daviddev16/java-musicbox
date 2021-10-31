package org.musicbox.modules.paginator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.musicbox.core.module.CoreModule;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class PaginatorModule extends CoreModule {

   private List<Paginator> paginators;

   @Override
   public void onEnabled() {
      paginators = Collections.synchronizedList(new LinkedList<>());
   }

   protected void clearPaginator(Paginator paginator) {
      paginators.remove(paginator);
   }
   
   public List<Paginator> getPaginators() {
      return paginators;
   }

   public void createPaginator(EmbedBuilder[] pages, TextChannel textChannel) {
      if (pages == null)
         throw new NullPointerException("Pages are null.");

      paginators.add(new Paginator() 
      {{
            Stream.of(pages).forEach(getPages()::add);
            open(textChannel);
      }});
   }

}
