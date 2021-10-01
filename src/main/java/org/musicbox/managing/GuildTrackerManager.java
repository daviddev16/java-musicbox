package org.musicbox.managing;

import java.util.HashMap;
import java.util.Map;
import org.musicbox.MusicBox;
import net.dv8tion.jda.api.entities.Guild;

public class GuildTrackerManager {

  private final Map<Long, GuildManager> guildManagers;

  public GuildTrackerManager() {
	guildManagers = new HashMap<Long, GuildManager>();
  }

  public GuildManager getGuildManager(Guild guild) {
	long guildId = guild.getIdLong();
	GuildManager guildManager = guildManagers.get(guildId);
	if (guildManager == null) {
	  synchronized (guildManagers) {
		guildManager = guildManagers.get(guildId);
		if (guildManager == null) {
		  guildManager = new GuildManager(MusicBox.getAudioPlayerManager(), guild);
		  guildManager.getAudioPlayer().setVolume(MusicBox.getMainVolume());
		  guildManagers.put(guildId, guildManager);
		}
	  }
	}
	return guildManager;
  }

  public Map<Long, GuildManager> getGuildManagers() {
	return guildManagers;
  }

}
