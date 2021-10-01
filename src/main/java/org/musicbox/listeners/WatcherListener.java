package org.musicbox.listeners;

import java.util.List;

import org.musicbox.MusicBox;
import org.musicbox.managing.GuildManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WatcherListener extends ListenerAdapter {

  @Override
  public void onGenericGuildVoice(GenericGuildVoiceEvent event) {

	Guild guild = event.getGuild();
	GuildManager guildManager = MusicBox.getTrackerManager().getGuildManager(guild);
	VoiceChannel channel = null;

	if (event instanceof GuildVoiceLeaveEvent) {
	  channel = ((GuildVoiceLeaveEvent) event).getChannelLeft();

	  if (isBotPresent(channel) && isAlone(channel.getMembers())) {
		if (guildManager.getPresenceWaiter() == null) {
		  guildManager.waitToQuitIfNecessary(channel);
		}
	  }
	} else if (event instanceof GuildVoiceJoinEvent) {
	  channel = ((GuildVoiceJoinEvent) event).getChannelJoined();
	  if (isBotPresent(channel) && !isAlone(channel.getMembers())) {
		guildManager.cancelWaiter();
	  }
	}
  }

  public boolean isBotPresent(VoiceChannel voiceChannel) {
	return voiceChannel.getMembers().stream()
		.filter(member -> member.getUser().getIdLong() == MusicBox.getInstance().getSelfUser().getIdLong()).findFirst()
		.isPresent();
  }

  public static boolean isAlone(List<Member> member) {
	return member.size() == 1;
  }

}
