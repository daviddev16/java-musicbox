package org.musicbox.core.guild.modules;

import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.VoiceChannel;

public final class InspectorModule extends GuildModule {

   private volatile Timer waitingStateTimer;

   @Override
   public void load() {}
   
   /* wait until someone join the voice channel again */
   public void waitToQuitIfNecessary(VoiceChannel channel) {

      if(waitingStateTimer != null)
         return;

      waitingStateTimer = new Timer("[" + getGuild().getIdLong() + "-timer]", true);
      waitingStateTimer.schedule(new TimerTask() {
         public void run() {

            synchronized (getModule(ScheduleModule.class)) {
               getGuild().getAudioManager().closeAudioConnection();
               getModule(ScheduleModule.class).stop();
               destroyWaitingStateTimer();
            }

         }
      }, 60 * 2 * 1000);
   }

   /* cancel the waiting state */
   public void cancelIfNecessary() {
      if(waitingStateTimer != null) {
         waitingStateTimer.cancel();
         destroyWaitingStateTimer();
      }
   }

   /* destroy it */
   private void destroyWaitingStateTimer() {
      waitingStateTimer = null;
   }
   
   public boolean isWaiting() {
      return waitingStateTimer != null;
   }

}
