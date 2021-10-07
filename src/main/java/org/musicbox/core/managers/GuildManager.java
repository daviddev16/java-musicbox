package org.musicbox.core.managers;

import java.util.HashMap;
import java.util.Map;

import org.musicbox.config.Configs;
import org.musicbox.core.GuildInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Guild;

public final class GuildManager {

  private static Logger logger = LoggerFactory.getLogger(GuildManager.class);

  private static GuildManager guildManager;
  private final Map<Long, GuildInstance> guildManagers;

  private GuildManager() {
	guildManagers = new HashMap<Long, GuildInstance>();
	guildManager = this;
	logger.info("GuildManager loaded.");
  }

  public GuildInstance getGuildInstance(Guild guild) {
	long guildId = guild.getIdLong();
	GuildInstance guildManager = guildManagers.get(guildId);
	if (guildManager == null) {
	  synchronized (guildManagers) {
		guildManager = guildManagers.get(guildId);
		if (guildManager == null) {
		  guildManager = new GuildInstance(BotAudioManager.getBotAudioManager().getAudioPlayerManager(), guild);
		  guildManager.getAudioPlayer().setVolume(Configs.VOLUME);
		  guildManagers.put(guildId, guildManager);
		}
	  }
	}
	return guildManager;
  }

  public Map<Long, GuildInstance> getGuildManagers() {
	return guildManagers;
  }
  
  public static GuildManager getGuildManager() {
 	return guildManager;
   }

  public static void setup() {
	new GuildManager();
  }

}
