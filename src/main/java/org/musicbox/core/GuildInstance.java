package org.musicbox.core;

import java.util.Timer;
import java.util.TimerTask;

import org.musicbox.core.managers.BotAudioManager;
import org.musicbox.core.models.IAudioLoadResult;
import org.musicbox.core.player.TrackScheduleManager;
import org.musicbox.utils.Constants;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public final class GuildInstance {

  private final AudioPlayer player;
  private final TrackScheduleManager scheduleManager;
  private volatile Timer presenceWaiter;
  private final Guild guild;

  public GuildInstance(AudioPlayerManager manager, Guild guild) {
	this.guild = guild;
	player = manager.createPlayer();
	scheduleManager = new TrackScheduleManager(player, this);
	player.addListener(scheduleManager);
	guild.getAudioManager().setSendingHandler(scheduleManager);
  }

 

  public void load(final String url, IAudioLoadResult result) {
	BotAudioManager.getBotAudioManager().getAudioPlayerManager().loadItemOrdered(this, stripUrl(url),
		new AudioLoadResultHandler() {
		  public void trackLoaded(AudioTrack track) {
			try {
			  getSchedule().queue(track);
			  result.onQueuedSingle(track);
			} catch (Exception e) {
			  result.onFailed(e);
			}
		  }

		  public void playlistLoaded(AudioPlaylist playlist) {
			try {
			  for (AudioTrack audioTrack : playlist.getTracks()) {
				getSchedule().queue(audioTrack);
			  }
			  result.onQueuedPlaylist(playlist);
			} catch (Exception e) {
			  result.onFailed(e);
			}
		  }

		  public void noMatches() {
			result.noMatches();
		  }

		  public void loadFailed(FriendlyException exception) {
			result.onFailed(exception);
		  }
		});
  }

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
		synchronized (scheduleManager) {
		  getGuild().getAudioManager().closeAudioConnection();
		  getSchedule().stop();
		  presenceWaiter = null;
		}
	  }
	}, 60 * 2 * 1000);
  }

  public void cancelWaiter() {
	if (getPresenceWaiter() != null) {
	  getPresenceWaiter().cancel();
	  presenceWaiter = null;
	}
  }

  public TrackScheduleManager getSchedule() {
 	return scheduleManager;
   }

   public AudioPlayer getAudioPlayer() {
 	return player;
   }

   public Guild getGuild() {
 	return guild;
   }

   public String getUsedLanguage() {
 	return Constants.PT_BR;
   }

  public Timer getPresenceWaiter() {
	return presenceWaiter;
  }

}
