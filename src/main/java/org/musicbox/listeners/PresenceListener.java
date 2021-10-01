package org.musicbox.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PresenceListener extends ListenerAdapter {

  public static volatile int guildCount = 0;

  public void onReady(ReadyEvent event) {

	guildCount = event.getGuildTotalCount();

	event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE,
		Activity.of(ActivityType.LISTENING, "musica em " + guildCount + " servidores."));
  }

  @Override
  public void onGuildJoin(GuildJoinEvent event) {
	guildCount++;
	event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE,
		Activity.of(ActivityType.LISTENING, "musica em " + guildCount + " servidores."));
  }

  @Override
  public void onGuildLeave(GuildLeaveEvent event) {
	guildCount--;
	event.getJDA().getPresence().setPresence(OnlineStatus.ONLINE,
		Activity.of(ActivityType.LISTENING, "musica em " + guildCount + " servidores."));
  }

}
