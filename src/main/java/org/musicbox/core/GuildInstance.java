package org.musicbox.core;

import java.util.Timer;
import java.util.TimerTask;

import org.musicbox.core.managers.BotAudioManager;
import org.musicbox.player.AudioPlayerSendHandler;
import org.musicbox.player.TrackScheduler;
import org.musicbox.utils.Constants;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public final class GuildInstance {

  private final AudioPlayer player;
  private final TrackScheduler scheduler;
  private final AudioPlayerSendHandler sendHandler;
  private volatile Timer presenceWaiter;
  private final Guild guild;

  public GuildInstance(AudioPlayerManager manager, Guild guild) {
	this.guild = guild;
	player = manager.createPlayer();
	scheduler = new TrackScheduler(player, this);
	sendHandler = new AudioPlayerSendHandler(player);
	player.addListener(scheduler);
	guild.getAudioManager().setSendingHandler(getSendHandler());
  }

  public TrackScheduler getSchedule() {
	return scheduler;
  }

  public AudioPlayer getAudioPlayer() {
	return player;
  }

  public AudioPlayerSendHandler getSendHandler() {
	return sendHandler;
  }

  public Guild getGuild() {
	return guild;
  }

  public String getUsedLanguage() {
	return Constants.PT_BR;
  }
  
  public void load(final String url, TextChannel channel) {
	
	BotAudioManager.getBotAudioManager().getAudioPlayerManager().loadItemOrdered(this, url, new AudioLoadResultHandler() {
	  
	  public void trackLoaded(AudioTrack track) {
		channel.sendMessage("playing>" + track.getInfo().title).queue();
		getSchedule().queue(track);
	  }
	  
	  public void playlistLoaded(AudioPlaylist playlist) {
	  }
	  
	  public void noMatches() {
		System.out.println("soso");
	  }
	  
	  @Override
	  public void loadFailed(FriendlyException exception) {
		System.out.println("failed");
		
	  }
	});
	
  }

  @SuppressWarnings("unused")
  private String stripUrl(String url) {
	if (url.startsWith("<") && url.endsWith(">")) {
	  return url.substring(1, url.length() - 1);
	} else {
	  return url;
	}
  }

  /* wait until someone join the voice channel again */
  public void waitToQuitIfNecessary(VoiceChannel channel) {
	presenceWaiter = new Timer("[" + getGuild().getIdLong() + "-waiter]");
	getPresenceWaiter().schedule(new TimerTask() {
	  @Override
	  public void run() {
		synchronized (scheduler) {
		  quitVoiceChannel();
		  getSchedule().stop();
		  presenceWaiter = null;
		}
	  }
	}, 60 * 2 * 1000);
  }

  public void cancelWaiter() {
	if (getPresenceWaiter() != null) {
	  System.out.println("waiter cancelado");
	  getPresenceWaiter().cancel();
	  presenceWaiter = null;
	}
  }

  public void quitVoiceChannel() {
	guild.getAudioManager().closeAudioConnection();
  }

  public Timer getPresenceWaiter() {
	return presenceWaiter;
  }

}
