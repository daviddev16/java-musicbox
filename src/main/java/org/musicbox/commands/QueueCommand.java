package org.musicbox.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.musicbox.core.command.GuildCommand;
import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.guild.controllers.TrackScheduler;
import org.musicbox.core.module.Modules;
import org.musicbox.core.translation.TranslationKeys;
import org.musicbox.core.utils.SelfPermissions;
import org.musicbox.modules.paginator.PaginatorModule;
import org.musicbox.modules.paginator.templates.TracklistTemplate;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QueueCommand extends GuildCommand {

   public QueueCommand() {
      super("queue", Arrays.asList("queue", "q", "list"), false);
      description(TranslationKeys.LABEL_QUEUE_DESCRIPTION);
   }

   @Override
   public void onExecute(GuildWrapper wrapper, MessageReceivedEvent event, Object[] params) {
      if(!SelfPermissions.canWrite(event.getTextChannel()))
         return;
      
      if(!wrapper.getScheduler().isPlaying()) {
         throw new FriendlyException(wrapper.getLanguage().getLabel(
               TranslationKeys.LABEL_EMPTY_LIST), Severity.COMMON, null);
      }

      TracklistTemplate template = new TracklistTemplate(wrapper);
      template.createPages(getTracklistPages(wrapper.getScheduler()), event);
      
      Modules.getModule(PaginatorModule.class).createPaginator(template.getAllPages(), 
            event.getTextChannel());
      
   }

   private List<String> getTracklistPages(TrackScheduler scheduler){
      
      List<String> descriptions = new ArrayList<>();
      
      int tracksPerPage = 7;
      StringBuilder builder = new StringBuilder();
      
      for(int count = 0; count < scheduler.getTracklist().size(); count++) {
         
         AudioTrack track = scheduler.getTracklist().get(count);
         String trackName = "`"+count+"` - " + track.getInfo().title;
         
         if(scheduler.isCurrent(count)) {
            trackName = "`"+count+"` - 🔊 *" + track.getInfo().title + "*"; 
         }
         
         if(count % tracksPerPage == 0 && tracksPerPage != 0) {
            descriptions.add(builder.toString());
            builder.delete(0, builder.length());
         }
         
         builder.append(trackName).append('\n');
         
      }
      
      return descriptions;
   }
   
}
