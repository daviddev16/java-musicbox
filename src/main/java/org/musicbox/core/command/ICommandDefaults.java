package org.musicbox.core.command;

import org.musicbox.core.GuildInstance;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.player.AudioPlayerSendHandler;
import org.musicbox.player.TrackScheduler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.entities.Guild;

public interface ICommandDefaults {

  public default GuildInstance getGuildInstance(Guild guild) {
	return GuildManager.getGuildManager().getGuildInstance(guild);
  }

  public default TrackScheduler getSchedule(Guild guild) {
	return getGuildInstance(guild).getSchedule();
  }

  public default AudioPlayer getAudioPlayer(Guild guild) {
	return getGuildInstance(guild).getAudioPlayer();
  }

  public default AudioPlayerSendHandler getAudioSendHandler(Guild guild) {
	return getGuildInstance(guild).getSendHandler();
  }

}
