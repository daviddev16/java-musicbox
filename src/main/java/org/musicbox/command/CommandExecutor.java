package org.musicbox.command;

import org.musicbox.MusicBox;
import org.musicbox.managing.GuildManager;
import org.musicbox.player.AudioPlayerSendHandler;
import org.musicbox.player.TrackScheduler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandExecutor {

	private String[] command;
	private Class<?>[] parameterClasses;

	public CommandExecutor(String[] command, Class<?>[] parameterClasses) {
		this.setCommand(command);
		this.setParameterClasses(parameterClasses);
	}

	public void execute(MessageReceivedEvent event, Object[] arguments) {}

	public boolean validateArguments(Object[] arguments) {
		if(parameterClasses == null) {
			return true;
		}
		if(arguments.length == parameterClasses.length) {
			for(int i = 0; i < parameterClasses.length; i++) {
				if(!arguments[i].getClass().isAssignableFrom(parameterClasses[i])) {
					return false;
				}
			}
		}else {
			return false;
		}
		return true;
	}
	
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

	public Class<?>[] getParameterClasses() {
		return parameterClasses;
	}

	public String[] getCommand() {
		return command;
	}
	
	public void setParameterClasses(Class<?>[] parameterClasses) {
		this.parameterClasses = parameterClasses;
	}

	public void setCommand(String[] command) {
		this.command = command;
	}

}
