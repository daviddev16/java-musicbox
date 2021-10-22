package org.musicbox.core.guild;

import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.VoiceChannel;

public final class Inspector implements GuildWrapperPart {

   private volatile Timer waitingStateTimer;
  
   private final GuildWrapper guildWrapper;
   
   public Inspector(GuildWrapper guildWrapper) {
      this.guildWrapper = guildWrapper;
   }

   /* wait until someone join the voice channel again */
   public void waitToQuitIfNecessary(VoiceChannel channel) {

      if(waitingStateTimer != null)
         return;

      waitingStateTimer = new Timer("[" + getGuild().getIdLong() + "-timer]", true);
      waitingStateTimer.schedule(new TimerTask() {
         @Override
         public void run() {

            synchronized (getWrapper().getScheduler()) {
               getGuild().getAudioManager().closeAudioConnection();
               getWrapper().getScheduler().stopSchedule();
               destroyTimer();
            }

         }
      }, 60 * 2 * 1000);
   }

   /* cancel the waiting state */
   public void cancelIfNecessary() {
      if(waitingStateTimer != null) {
         waitingStateTimer.cancel();
         destroyTimer();
      }
   }

   /* destroy it */
   private void destroyTimer() {
      waitingStateTimer = null;
   }

   public boolean isWaiting() {
      return waitingStateTimer != null;
   }

   @Override
   public GuildWrapper getWrapper() {
      return guildWrapper;
   }

}
