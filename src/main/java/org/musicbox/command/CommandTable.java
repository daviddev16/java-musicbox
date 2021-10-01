package org.musicbox.command;

import org.musicbox.MusicBox;
import org.musicbox.managing.GuildManager;
import org.musicbox.player.AudioPlayerSendHandler;
import org.musicbox.player.TrackScheduler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.entities.Guild;

public class CommandTable {

  public GuildManager getGuildManager(Guild guild) {
	return MusicBox.getTrackerManager().getGuildManager(guild);
  }

  public TrackScheduler getSchedule(Guild guild) {
	return getGuildManager(guild).getSchedule();
  }

  public AudioPlayer getAudioPlayer(Guild guild) {
	return getGuildManager(guild).getAudioPlayer();
  }

  public AudioPlayerSendHandler getAudioSendHandler(Guild guild) {
	return getGuildManager(guild).getSendHandler();
  }

}
