package org.musicbox.core.guild.controllers;

import java.util.Timer;
import java.util.TimerTask;

import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.models.GuildWrapperPart;

import net.dv8tion.jda.api.entities.VoiceChannel;

public final class Inspector implements GuildWrapperPart {

   private volatile Timer waitingStateTimer;

   private final GuildWrapper guildWrapper;

   public Inspector(GuildWrapper guildWrapper) {
      this.guildWrapper = guildWrapper;
   }

   /* wait until someone join the voice channel again */
   public void waitToQuitIfNecessary(VoiceChannel channel) {

      if (waitingStateTimer != null)
         return;

      waitingStateTimer = new Timer("[" + getGuild().getIdLong() + "-timer]", true);
      waitingStateTimer.schedule(new TimerTask() {
         @Override
         public void run() {
            synchronized (getWrapper().getScheduler()) {
               disconnect();
               getWrapper().getScheduler().stopSchedule();
               destroyTimer();
            }
         }
      }, 60 * 5 * 1000);
   }

   /* connect to the voice channel */
   public void connect(VoiceChannel voiceChannel) {
      getGuild().getAudioManager().openAudioConnection(voiceChannel);
      getGuild().getAudioManager().setSendingHandler(getWrapper().getScheduler());
   }
   
   /* leave the current voice channel */
   public void disconnect() {
      getGuild().getAudioManager().closeAudioConnection();
      getGuild().getAudioManager().setSendingHandler(null);
   }

   /* cancel the waiting state */
   public void cancelIfNecessary() {
      if (waitingStateTimer != null) {
         waitingStateTimer.cancel();
         destroyTimer();
      }
   }

   /* destroy the waiting timer */
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
