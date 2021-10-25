package org.musicbox.listeners;

import org.musicbox.config.DefaultConfig;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.managers.ListenerManager.Listener;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

public class PresenceListener extends Listener {

   public static volatile int guildCount = 0;

   @Override
   public void onReady(ReadyEvent event) {

      String shard = event.getJDA().getShardInfo().getShardString();

      guildCount = event.getGuildTotalCount();
      
      if(DefaultConfig.DEBUG_MODE) {
        
         event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE,
               Activity.of(ActivityType.LISTENING, shard));

      } else {
         event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE,
               Activity.of(ActivityType.LISTENING, "musica em " + guildCount + " servidores."));
      }
   }

   @Override
   public void onGuildJoin(GuildJoinEvent event) {
      guildCount++;
      event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE,
            Activity.of(ActivityType.LISTENING, "musica em " + guildCount + " servidores."));
   }

   @Override
   public void onGuildLeave(GuildLeaveEvent event) {
      guildCount--;
      event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE,
            Activity.of(ActivityType.LISTENING, "musica em " + guildCount + " servidores."));
   }

   @Override
   public void onShutdown(ShutdownEvent event) {
      GuildManager.getGuildManager().getWrappers().values().forEach(guildInstance -> {
         guildInstance.getGuild().getAudioManager().closeAudioConnection();
      });
   }

}
