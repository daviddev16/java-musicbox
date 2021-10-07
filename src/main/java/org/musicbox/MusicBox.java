package org.musicbox;

import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.config.ThreadingConfig;
import okhttp3.OkHttpClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import org.musicbox.config.Configs;
import org.musicbox.core.LanguageManager;
import org.musicbox.core.managers.BotAudioManager;
import org.musicbox.core.managers.CommandManager;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.managers.YoutubeSearchManager;
import org.musicbox.core.models.CommandHelpers;
import org.musicbox.core.models.Listeners;
import org.musicbox.listeners.CommandListener;
import org.musicbox.listeners.PresenceListener;
import org.musicbox.listeners.WatcherListener;
import org.musicbox.tables.MusicCommandTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicBox {

  private static MusicBox musicBox;

  private static Logger logger = LoggerFactory.getLogger(MusicBox.class);

  private final ShardManager shardManager;

  public MusicBox() throws LoginException {

	
	Configs.setup("musicbox-app");

	LanguageManager.setup();
	BotAudioManager.setup();
	GuildManager.setup();
	YoutubeSearchManager.setup();
	CommandHelpers.setup();
	
	CommandManager.setup();
	CommandManager.getCommandManager().handle(new MusicCommandTable());
	
	Listeners.setup();

	Listeners.register(
		new WatcherListener(), 
		new CommandListener(), 
		new PresenceListener());

	RestAction.setDefaultFailure(null);

	/* application intents */
	EnumSet<GatewayIntent> intents = EnumSet.of(
		GatewayIntent.GUILD_MESSAGES, 
		GatewayIntent.GUILD_EMOJIS,
		GatewayIntent.GUILD_VOICE_STATES
		);

	/* cache flags*/
	Collection<CacheFlag> cacheFlags = Arrays.asList(
		CacheFlag.MEMBER_OVERRIDES,
		CacheFlag.ROLE_TAGS,
		CacheFlag.CLIENT_STATUS,
		CacheFlag.ACTIVITY,
		CacheFlag.EMOTE
		);

	shardManager = DefaultShardManagerBuilder.create(
		Configs.TOKEN, intents)
		.disableCache(cacheFlags)
		.setGatewayEncoding(GatewayEncoding.ETF)
		.setChunkingFilter(ChunkingFilter.NONE)
		.setMemberCachePolicy(MemberCachePolicy.VOICE)
		.addEventListeners(Listeners.getAllListeners())
		.setRawEventsEnabled(true)
		.setBulkDeleteSplittingEnabled(false)
		.setEventPool(ThreadingConfig.newScheduler(1, () -> "MusicBox", "EventPool"), true)
		.setHttpClient(new OkHttpClient())
		.build();

	logger.info("MusicBox is ready to be used.");
	musicBox = this;
  }

  public ShardManager getShardManager() {
	return shardManager;
  }

  public static MusicBox getMusicBox() {
	return musicBox;
  }

}
