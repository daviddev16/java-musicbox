package org.musicbox.tables;

import org.musicbox.core.GuildInstance;
import org.musicbox.core.command.CommandCategory;
import org.musicbox.core.command.Link;
import org.musicbox.core.command.Usage;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.managers.YoutubeSearchManager;
import org.musicbox.core.utils.Messages;
import org.musicbox.core.utils.Placeholder;
import org.musicbox.core.utils.PlaceholderBuilder;
import org.musicbox.models.PlayTrackResult;
import org.musicbox.utils.Constants;
import org.musicbox.utils.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static org.musicbox.core.utils.Messages.translatedMessage;

import java.util.List;

public class MusicCommands {
  
  @Usage(usage = "play/p <track title or url>")
  @Link(commandId = 0, names = { "play", "p" }, category = CommandCategory.MUSIC, argumentsSplit = false)
  private void play(MessageReceivedEvent event, String content) {

	Guild guild = event.getGuild();
	GuildInstance guildInstance = GuildManager.getGuildManager().getGuildInstance(guild);
	
	List<Placeholder> placeholders = PlaceholderBuilder.createBy(event, true)
		.add(Constants.KEY_USER_INPUT, content).build();
	
	/* check if the bot is in any voice channel */
	if (!Utils.isPresentOnGuild(guild)) {
	  
	  /* check if the member is on voice channel */
	  if (!Utils.isOnVoiceChannel(event.getMember())) {
		translatedMessage(event, Messages.ABSENT_ON_VOICE_CHANNEL, placeholders);
		return;
	  }

	  /* connect to the member's voice channel */
	  VoiceChannel memberChannel = event.getMember().getVoiceState().getChannel();
	  event.getGuild().getAudioManager().openAudioConnection(memberChannel);
	} 

	/* check if the member is present in the same voice channel channel */
	else if (!Utils.isTogetherWith(event.getMember(), guild)) { 
	  translatedMessage(event, Messages.NOT_SAME_VOICE_CHANNEL, placeholders);
	  return;
	}

	if (!Utils.isURL(content)) {
	  content = YoutubeSearchManager.getSearchManager().getUrlBasedOnText(content);
	}

	/* load and play track if is not already playing */
	guildInstance.load(content, new PlayTrackResult(event, guildInstance, placeholders));
  }


}
