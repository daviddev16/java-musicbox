package org.musicbox.modules.paginator;

import java.util.List;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface IPaginator {

   List<MessageEmbed> getPages();

   int getCurrentIndex();

   void open(GuildChannel channel);
   
   void close();

   Message getMessage();
   
   void next();

   void back();
   
   void moveToPage(MessageEmbed page);

}
