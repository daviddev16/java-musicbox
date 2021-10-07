package org.musicbox.core.managers;

import org.musicbox.core.player.soundcloud.SoundCloudAudioSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public final class BotAudioManager {

  private static Logger logger = LoggerFactory.getLogger(BotAudioManager.class);
  private static BotAudioManager botAudioManager;

  private AudioPlayerManager playerManager;
  
  private BotAudioManager() {
	playerManager = new DefaultAudioPlayerManager();
	playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
	playerManager.registerSourceManager(new YoutubeAudioSourceManager());
	playerManager.registerSourceManager(new HttpAudioSourceManager());
	logger.info("BotAudioManager loaded.");
	botAudioManager = this;
  }

  public AudioPlayerManager getAudioPlayerManager() {
	return playerManager;
  }
  
  public static BotAudioManager getBotAudioManager() {
	return botAudioManager;
  }

  public static void setup() {
	new BotAudioManager();
  }

}
