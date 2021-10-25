package org.musicbox.modules.paginator;

import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;

public interface IPaginator {

   List<EmbedBuilder> getPages();

   int getCurrentIndex();

   void open(GuildChannel channel);
   
   void close();

   Message getMessage();
   
   void next();

   void back();
   
   void moveToPage(EmbedBuilder page);

}
