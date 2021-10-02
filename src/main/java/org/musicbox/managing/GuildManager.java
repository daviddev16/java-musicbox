package org.musicbox.managing;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

import org.musicbox.MusicBox;
import org.musicbox.models.LoadingResult;
import org.musicbox.models.PlayInfo;
import org.musicbox.models.TrackChunk;
import org.musicbox.models.VoiceStateResult;
import org.musicbox.player.AudioPlayerSendHandler;
import org.musicbox.player.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;

public class GuildManager {

  private final AudioPlayer player;
  private final TrackScheduler scheduler;
  private final AudioPlayerSendHandler sendHandler;
  private volatile Timer presenceWaiter;
  private final Guild guild;

  public GuildManager(AudioPlayerManager manager, Guild guild) {
	this.guild = guild;
	player = manager.createPlayer();
	scheduler = new TrackScheduler(player);
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

  public void play(final Message triggerMessage, String url, BiConsumer<TrackChunk, Exception> playResult) {

	final String trackUrl = stripUrl(url);
	MusicBox.getAudioPlayerManager().loadItemOrdered(this, trackUrl, new AudioLoadResultHandler() {

	  @Override
	  public void trackLoaded(AudioTrack track) {

		VoiceStateResult voiceStateResult = connectToVoiceChannel(triggerMessage);
		switch (voiceStateResult) {

		/* joined the voice channel */
		case JOINED:
		  LoadingResult loadingResult = addTrack(track, triggerMessage, false);
		  PlayInfo playInfo = new PlayInfo(track.getInfo().title, track.getInfo().length, false);
		  playResult.accept(new TrackChunk(playInfo, loadingResult), null);
		  return;

		  /* denied */
		case REJECTED:
		  playResult.accept(new TrackChunk(null, LoadingResult.MISSING_PERMISSION), null);
		  return;

		  /* invalid voice channel */
		case FAILED:
		  playResult.accept(new TrackChunk(null, LoadingResult.MISSING_VOICE_CHANNEL), null);
		  return;
		}
	  }

	  @Override
	  public void playlistLoaded(AudioPlaylist playlist) {

		VoiceStateResult voiceStateResult = connectToVoiceChannel(triggerMessage);
		switch (voiceStateResult) {
		case JOINED:

		  LoadingResult loadingResult = null;
		  PlayInfo playInfo = new PlayInfo(playlist.getName(), playlist.getTracks().size(), true);
		  for (AudioTrack track : playlist.getTracks()) {
			LoadingResult currentResult = addTrack(track, triggerMessage, true);
			if (loadingResult == null) {
			  loadingResult = currentResult;
			}
		  }
		  playResult.accept(new TrackChunk(playInfo, loadingResult), null);
		  return;
		case REJECTED:

		  playResult.accept(new TrackChunk(null, LoadingResult.MISSING_PERMISSION), null);
		  return;
		case FAILED:

		  playResult.accept(new TrackChunk(null, LoadingResult.MISSING_VOICE_CHANNEL), null);
		  return;
		}
	  }

	  @Override
	  public void noMatches() {
		playResult.accept(new TrackChunk(null, LoadingResult.NO_MATCHES), null);
	  }

	  @Override
	  public void loadFailed(FriendlyException exception) {
		playResult.accept(new TrackChunk(null, LoadingResult.FAILED_LOAD), exception);
	  }
	});
  }

  private LoadingResult addTrack(final AudioTrack track, final Message triggerMessage, boolean playlist) {
	try {
	  getSchedule().addToQueue(track);

	  if (!playlist)
		return LoadingResult.QUEUED_SINGLE;
	  else
		return LoadingResult.QUEUED_PLAYLIST;

	} catch (Exception e) {
	  return LoadingResult.FAILED_QUEUE;
	}
  }

  private VoiceStateResult connectToVoiceChannel(Message message) {
	VoiceChannel voiceChannel = message.getMember().getVoiceState().getChannel();
	if (voiceChannel != null) {
	  try {
		message.getGuild().getAudioManager().openAudioConnection(voiceChannel);
		return VoiceStateResult.JOINED;
	  } catch (PermissionException e) {
		if (e.getPermission() == Permission.VOICE_CONNECT) {
		  System.out.println("sem permissão");
		  return VoiceStateResult.REJECTED;
		}
	  }
	}
	return VoiceStateResult.FAILED;
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
		synchronized (scheduler) {
		  quitVoiceChannel();
		  getSchedule().closeSchedule();
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
