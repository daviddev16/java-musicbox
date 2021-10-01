package org.musicbox.signature;

import java.util.List;
import java.util.Queue;

import org.musicbox.I18n;
import org.musicbox.MusicBox;
import org.musicbox.annotations.Command;
import org.musicbox.annotations.Description;
import org.musicbox.command.CommandCategory;
import org.musicbox.command.CommandTable;
import org.musicbox.managing.GuildManager;
import org.musicbox.managing.LoadingResult;
import org.musicbox.utils.Placeholder;
import org.musicbox.utils.Utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

public class MusicBoxCommandTable extends CommandTable {

  @Description(text = "Adiciona a musica na lista.")
  @Command(name = { "play",
	  "p" }, usage = "play|p <url>", category = CommandCategory.MUSIC, aliasSplit = false, order = 0)
  private void commandPlay(MessageReceivedEvent event, final String userInput) {

	Guild guild = event.getGuild();

	String url = userInput; /* ok let's just supposed the user typed an url lol */
	if (!Utils.isURL(url)) {
	  url = MusicBox.getSearchManager().getUrlBaseOnText(url);
	}

	Message triggerMessage = event.getMessage();
	GuildManager guildManager = getGuildManager(guild);

	List<Placeholder> placeholders = Placeholder.of(Placeholder.defaultPlaceholders(), Placeholder.owner());

	guildManager.play(triggerMessage, url, (trackChunk, exception) -> {

	  Placeholder.messageEventPlaceholders(event, placeholders);
	  Placeholder.trackPlaceholder(trackChunk, placeholders);
	  Placeholder.userInputPlaceholder(userInput, placeholders);

	  if (trackChunk.getLoadingResult() == LoadingResult.QUEUED_SINGLE) {
		MusicBoxMessages.send(event.getTextChannel(), "trackAdded", placeholders);
	  } else if (trackChunk.getLoadingResult() == LoadingResult.QUEUED_PLAYLIST) {
		MusicBoxMessages.send(event.getTextChannel(), "playlistAdded", placeholders);
	  } else {
		System.out.println(trackChunk.getLoadingResult().name());

		Placeholder.addReason(placeholders, MusicBox.getConfiguration().getFailedReason(I18n.DEFAULT_LANGUAGE,
			I18n.getReason(trackChunk.getLoadingResult()), placeholders));

		MusicBoxMessages.send(event.getTextChannel(), "failed", placeholders);
	  }

	  if (exception != null) {
		System.out.println(exception.getMessage());
	  }
	});
  }

  @Description(text = "Encerra a lista de reprodução.")
  @Command(name = { "stop" }, usage = "stop", category = CommandCategory.MUSIC, aliasSplit = true, order = 1)
  private void commandStop(MessageReceivedEvent event) {

	GuildManager guildManager = getGuildManager(event.getGuild());

	guildManager.getSchedule().getQueue().clear();
	guildManager.getAudioPlayer().stopTrack();
	guildManager.getAudioPlayer().setPaused(false);
	// MusicBoxMessages.trackStopped(event);
  }

  @Description(text = "Passa para a proxima musica.")
  @Command(name = { "skip" }, usage = "skip", category = CommandCategory.MUSIC, aliasSplit = true, order = 2)
  private void commandSkip(MessageReceivedEvent event) {

	GuildManager guildManager = getGuildManager(event.getGuild());
	guildManager.getSchedule().nextTrack();
	// MusicBoxMessages.trackSkipped(event);
  }

  @Description(text = "Começa a musica do zero.")
  @Command(name = { "restart" }, usage = "restart", category = CommandCategory.MUSIC, aliasSplit = true, order = 3)
  private void commandRestart(MessageReceivedEvent event) {

	GuildManager guildManager = getGuildManager(event.getGuild());

	AudioTrack track = guildManager.getAudioPlayer().getPlayingTrack();
	if (track == null)
	  track = guildManager.getSchedule().getLastTrack();

	if (track != null) {
	  // MusicBoxMessages.trackRestarted(event);
	  guildManager.getAudioPlayer().playTrack(track.makeClone());
	} else {
	  event.getChannel().sendMessage("Não há musica para recomeçar!").queue();
	}
  }

  @Description(text = "Repete a musica atual.")
  @Command(name = { "repeat" }, usage = "repeat", category = CommandCategory.MUSIC, aliasSplit = true, order = 4)
  private void commandRepeat(MessageReceivedEvent event) {

	GuildManager guildManager = getGuildManager(event.getGuild());

	if (guildManager.getSchedule().isRepeating()) {
	  guildManager.getSchedule().setRepeating(false);
	  event.getMessage().addReaction("👍").queue();
	  return;
	}
	// MusicBoxMessages.trackRepeatMode(event);
	guildManager.getSchedule().setRepeating(true);
  }

  @Description(text = "Ver lista de reprodução.")
  @Command(name = { "queue" }, usage = "queue", category = CommandCategory.MUSIC, aliasSplit = true, order = 5)
  private void commandQueue(MessageReceivedEvent event) {

	GuildManager guildManager = getGuildManager(event.getGuild());

	Queue<AudioTrack> songQueue = guildManager.getSchedule().getQueue();
	synchronized (songQueue) {
	  if (songQueue.isEmpty()) {
		event.getChannel().sendMessage("a lista está vazia!").queue();
	  } else {

		int trackCount = 0;
		long queueLength = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("Musica atual: " + guildManager.getAudioPlayer().getPlayingTrack().getInfo().title + " | Lista: ")
			.append(songQueue.size()).append("\n");
		for (AudioTrack track : songQueue) {
		  queueLength += track.getDuration();
		  if (trackCount < 10) {
			sb.append("`[").append(Utils.getTimestamp(track.getDuration())).append("]` ");
			sb.append(track.getInfo().title).append("\n");
			trackCount++;
		  }
		}
		sb.append("\n").append("Tempo total: ").append(Utils.getTimestamp(queueLength));
		event.getChannel().sendMessage(sb.toString()).queue();
	  }
	}
  }

  /*
   * @Description(text = "Adiciona uma playlist.")
   * 
   * @Command(name = { "playlist", "pl" }, usage = "playlist|pl <url>", category =
   * CommandCategory.MUSIC, aliasSplit = false, order = 6) private void
   * commandPlaylist(MessageReceivedEvent event, String url) {
   * 
   * Guild guild = event.getGuild(); GuildManager guildManager =
   * getGuildManager(guild); guildManager.play(event, url, true);
   * 
   * VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
   * 
   * if (voiceChannel != null) {
   * guild.getAudioManager().setSendingHandler(guildManager.getSendHandler()); try
   * { guild.getAudioManager().openAudioConnection(voiceChannel); } catch
   * (PermissionException e) { if (e.getPermission() == Permission.VOICE_CONNECT)
   * { event.getChannel().sendMessage(
   * "Eu não tenho permissão de entrar nesse canal de voz :( [" +
   * voiceChannel.getName() + "].") .queue(); } } } }
   */

  @Description(text = "Pausa a musica atual.")
  @Command(name = { "pause" }, usage = "pause", category = CommandCategory.MUSIC, aliasSplit = true, order = 7)
  private void commandPause(MessageReceivedEvent event) {

	Guild guild = event.getGuild();
	GuildManager guildManager = getGuildManager(guild);

	if (guildManager.getAudioPlayer().getPlayingTrack() == null) {
	  return;
	}

	guildManager.getAudioPlayer().setPaused(true);
	// MusicBoxMessages.trackPaused(event);
  }

  @Description(text = "Lista de comandos.")
  @Command(name = { "help" }, usage = "help", category = CommandCategory.MUSIC, aliasSplit = true, order = 8)
  private void commandHelp(MessageReceivedEvent event) {
	MusicBoxMessages.help(event);
  }

  @Description(text = "Despausa a musica.")
  @Command(name = { "resume" }, usage = "resume", category = CommandCategory.MUSIC, aliasSplit = true, order = 9)
  private void commandResume(MessageReceivedEvent event) {

	Guild guild = event.getGuild();
	GuildManager guildManager = getGuildManager(guild);

	if (guildManager.getAudioPlayer().getPlayingTrack() == null) {
	  return;
	}

	guildManager.getAudioPlayer().setPaused(false);
	// MusicBoxMessages.trackResumed(event);
  }

  @Description(text = "Playlist do Kernel.")
  @Command(name = { "kernel" }, usage = "kernel", category = CommandCategory.MUSIC, aliasSplit = true, order = 10)
  private void commandKernel(MessageReceivedEvent event) {
	// commandPlaylist(event,
	// "https://soundcloud.com/musicbykernel/sets/only-bangers");
	event.getTextChannel().sendMessage("enjoy 🙂 !").queue(MusicBoxMessages.deleteAfter(20L));
  }

  @Description(text = "Faz o bot entrar na sua call.")
  @Command(name = { "join" }, usage = "join", category = CommandCategory.MUSIC, aliasSplit = true, order = 11)
  private void commandJoin(MessageReceivedEvent event) {

	Guild guild = event.getGuild();
	GuildManager guildManager = getGuildManager(guild);
	VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();

	if (voiceChannel != null) {
	  guild.getAudioManager().setSendingHandler(guildManager.getSendHandler());
	  try {
		guild.getAudioManager().openAudioConnection(voiceChannel);
	  } catch (PermissionException e) {
		if (e.getPermission() == Permission.VOICE_CONNECT) {
		  event.getChannel()
			  .sendMessage("Eu não tenho permissão de entrar nesse canal de voz :( [" + voiceChannel.getName() + "].")
			  .queue();
		}
	  }
	}
  }

}