package org.musicbox;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.entities.SelfUser;

import java.io.IOException;
import java.util.EnumSet;
import java.util.logging.Level;

import org.musicbox.command.CommandController;
import org.musicbox.config.MusicBoxConfiguration;
import org.musicbox.listeners.CommandListener;
import org.musicbox.listeners.PresenceListener;
import org.musicbox.listeners.WatcherListener;
import org.musicbox.managing.GuildTrackerManager;
import org.musicbox.managing.YoutubeSearchManager;
import org.musicbox.models.MusicBoxCommandTable;
import org.musicbox.player.soundcloud.SoundCloudAudioSourceManager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public class MusicBox {

  private volatile static MusicBox instance;

  private MusicBoxConfiguration musicBoxConfiguration;
  private YoutubeSearchManager youtubeSearchManager;
  private GuildTrackerManager guildTrackerManager;
  private CommandController commandController;

  private AudioPlayerManager playerManager;

  private final int volume = 150 / 2;
  private SelfUser selfUser;

  public static void main(String[] args) throws IOException {
	new MusicBox();
  }

  public MusicBox() {

	instance = this;
	java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);

	try {
	  /* bot json configurations */
	  musicBoxConfiguration = new MusicBoxConfiguration("musicbox-app");

	  /* setup managers */
	  youtubeSearchManager = new YoutubeSearchManager();
	  guildTrackerManager = new GuildTrackerManager();
	  playerManager = new DefaultAudioPlayerManager();
	  commandController = new CommandController();

	  commandController.register(MusicBoxCommandTable.class);

	  /* LavaPlayer register all source managers */
	  playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
	  playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
	  playerManager.registerSourceManager(new BandcampAudioSourceManager());
	  playerManager.registerSourceManager(new YoutubeAudioSourceManager());
	  playerManager.registerSourceManager(new VimeoAudioSourceManager());
	  playerManager.registerSourceManager(new HttpAudioSourceManager());
	  playerManager.registerSourceManager(new LocalAudioSourceManager());

	  /* bot intents */
	  EnumSet<GatewayIntent> intents = EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS,
		  GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_PRESENCES);

	  String token = null;

	  if (getConfiguration().isDebugMode())
		token = getConfiguration().getDebugToken(); /* the 2nd bot application for debugging stuff */
	  else
		token = getConfiguration().getMainToken();

	  /* loading JDA */
	  JDA jda = JDABuilder.create(token, intents)
		  .addEventListeners(new CommandListener(), new PresenceListener(), new WatcherListener()).build();

	  selfUser = jda.getSelfUser();

	  System.out.println("MusicBox application is ready to be used.");
	  System.gc();

	} catch (Exception e) {
	  e.printStackTrace();
	  // System.exit(-1);
	}
  }

  public static AudioPlayerManager getAudioPlayerManager() {
	return getInstance().playerManager;
  }

  public static GuildTrackerManager getTrackerManager() {
	return getInstance().guildTrackerManager;
  }

  public static YoutubeSearchManager getSearchManager() {
	return getInstance().youtubeSearchManager;
  }

  public static CommandController getCommandController() {
	return getInstance().commandController;
  }

  public static MusicBoxConfiguration getConfiguration() {
	return getInstance().musicBoxConfiguration;
  }

  public static int getMainVolume() {
	return getInstance().getVolume();
  }

  public static MusicBox getInstance() {
	return instance;
  }

  public SelfUser getSelfUser() {
	return selfUser;
  }

  public JDA getJDA() {
	return getSelfUser().getJDA();
  }

  public int getVolume() {
	return volume;
  }

}
