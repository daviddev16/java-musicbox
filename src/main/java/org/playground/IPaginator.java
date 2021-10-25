package org.playground;

import java.util.List;

import org.playground.Paginator.Page;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;

public interface IPaginator {

   List<Page> getPages();

   int getCurrentIndex();

   void open(GuildChannel channel);
   
   void close();

   Message getMessage();
   
   void next();

   void back();
   
   void moveToPage(Page page);

}
