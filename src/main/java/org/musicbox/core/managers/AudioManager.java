package org.musicbox.core.managers;

import javax.management.InstanceAlreadyExistsException;

import org.musicbox.core.miscs.SoundCloudAudioSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public final class AudioManager {

   private static Logger logger = LoggerFactory.getLogger(AudioManager.class);
   private static AudioManager audioManager;

   private AudioPlayerManager playerManager;

   private AudioManager() throws InstanceAlreadyExistsException {
      
      if (audioManager != null)
         throw new InstanceAlreadyExistsException("AudioManager instance already exists.");
      
      playerManager = new DefaultAudioPlayerManager();
      playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
      playerManager.registerSourceManager(new YoutubeAudioSourceManager());
      playerManager.registerSourceManager(new HttpAudioSourceManager());

      logger.info("AudioManager loaded.");
      audioManager = this;
   }

   public static void setup() { 
      try {
         new AudioManager();
      } catch (InstanceAlreadyExistsException e) {
         logger.warn(e.getLocalizedMessage());
      } 
   }
   
   public AudioPlayerManager getPlayerManager() {
      return playerManager;
   }

   public static AudioManager getAudioManager() {
      return audioManager;
   }

}
