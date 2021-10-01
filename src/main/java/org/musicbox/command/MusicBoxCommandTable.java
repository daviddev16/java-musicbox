package org.musicbox.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.musicbox.MusicBox;
import org.musicbox.annotations.Command;
import org.musicbox.annotations.Description;
import org.musicbox.command.CommandController.CommandInfo;
import org.musicbox.managing.GuildManager;
import org.musicbox.models.LoadingResult;
import org.musicbox.models.Placeholder;
import org.musicbox.models.Placeholders;
import org.musicbox.utils.I18n;
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
  "p" }, usage = "play/p <url>", category = CommandCategory.MUSIC, aliasSplit = false, order = 0)
  private void commandPlay(MessageReceivedEvent event, final String userInput) {

	Guild guild = event.getGuild();

	String url = userInput; /* ok let's just suppose the user typed a valid url lol */
	if (!Utils.isURL(url)) {
	  url = MusicBox.getSearchManager().getUrlBaseOnText(url);
	}

	Message triggerMessage = event.getMessage();
	GuildManager guildManager = getGuildManager(guild);

	guildManager.play(triggerMessage, url, (trackChunk, exception) -> {

	  List<Placeholder> placeholders = Placeholders.ofTrackAdded(event, userInput, trackChunk);

	  if (trackChunk.getLoadingResult() == LoadingResult.QUEUED_SINGLE) {
		Utils.send(event.getTextChannel(), "trackAdded", placeholders);
	  } else if (trackChunk.getLoadingResult() == LoadingResult.QUEUED_PLAYLIST) {
		Utils.send(event.getTextChannel(), "playlistAdded", placeholders);
	  } else {
		System.out.println(trackChunk.getLoadingResult().name());

		String reason = MusicBox.getConfiguration().getFailedReason(I18n.DEFAULT_LANGUAGE,
			I18n.getReason(trackChunk.getLoadingResult()), placeholders);

		Utils.send(event.getTextChannel(), "failed", Placeholders.ofFailed(event, reason));
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
	Utils.send(event.getTextChannel(), "trackStopped", Placeholders.ofTrackStopped(event));

  }

  @Description(text = "Passa para a proxima musica.")
  @Command(name = { "skip" }, usage = "skip", category = CommandCategory.MUSIC, aliasSplit = true, order = 2)
  private void commandSkip(MessageReceivedEvent event) {

	GuildManager guildManager = getGuildManager(event.getGuild());
	guildManager.getSchedule().nextTrack();
	Utils.send(event.getTextChannel(), "trackSkipped", Placeholders.ofTrackSkipped(event));
  }



  @Description(text = "Começa a musica do zero.")
  @Command(name = { "restart" }, usage = "restart", category = CommandCategory.MUSIC, aliasSplit = true, order = 3)
  private void commandRestart(MessageReceivedEvent event) {

	GuildManager guildManager = getGuildManager(event.getGuild());

	AudioTrack track = guildManager.getAudioPlayer().getPlayingTrack();
	if (track == null)
	  track = guildManager.getSchedule().getLastTrack();

	if (track != null) {
	  Utils.send(event.getTextChannel(), "trackRestarted", Placeholders.ofTrackRestarted(event));
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
	Utils.send(event.getTextChannel(), "trackRepeatMode", Placeholders.ofTrackRepeatMode(event));
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

		long queueLength = 0;
		int index = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("Musica atual: " + guildManager.getAudioPlayer().getPlayingTrack().getInfo().title + " | Lista: ")
		.append(songQueue.size()).append("\n");
		for (AudioTrack track : songQueue) {
		  if(index < 10) {
			queueLength += track.getDuration();
			sb.append(index + ": " + "`[").append(Utils.getTimestamp(track.getDuration())).append("]` ");
			sb.append(track.getInfo().title).append("\n");
		  }
		  index++;
		}
		sb.append("\n").append("Tempo total: ").append(Utils.getTimestamp(queueLength));
		event.getChannel().sendMessage(sb.toString()).queue(Utils.deleteAfter(120L));
	  }
	}
  }

  @Description(text = "Pausa a musica atual.")
  @Command(name = { "pause" }, usage = "pause", category = CommandCategory.MUSIC, aliasSplit = true, order = 7)
  private void commandPause(MessageReceivedEvent event) {

	Guild guild = event.getGuild();
	GuildManager guildManager = getGuildManager(guild);

	if (guildManager.getAudioPlayer().getPlayingTrack() == null) {
	  return;
	}

	guildManager.getAudioPlayer().setPaused(true);
	Utils.send(event.getTextChannel(), "trackPaused", Placeholders.ofTrackPaused(event));
  }

  @Description(text = "Lista de comandos.")
  @Command(name = { "help" }, usage = "help", category = CommandCategory.MUSIC, aliasSplit = true, order = 8)
  private void commandHelp(MessageReceivedEvent event) {

	StringBuffer buffer = new StringBuffer();

	List<CommandInfo> sortedCommands = MusicBox.getCommandController().getCommandMap().values().stream()
		.collect(Collectors.toCollection(ArrayList::new));

	sortedCommands.sort(new Comparator<CommandInfo>() {
	  public int compare(CommandInfo o1, CommandInfo o2) {
		return (o1.getOrder() == o2.getOrder()) ? 0 : (o1.getOrder() > o2.getOrder()) ? 1 : -1;
	  }
	});

	sortedCommands.forEach((commandInfo) -> {
	  buffer.append("**").append(Utils.getJoinedString(commandInfo.getNames())).append("**: ");
	  buffer.append("`!" + commandInfo.getUsage() + "`").append(" **|** ");
	  buffer.append("*").append(commandInfo.getDescription().text()).append("*").append('\n');
	});

	Utils.send(event.getTextChannel(), "helpCommand", Placeholders.ofHelpCommand(event, buffer.toString().trim()));

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
	Utils.send(event.getTextChannel(), "trackResumed", Placeholders.ofTrackResumed(event));
  }

  @Description(text = "Playlist do Kernel.")
  @Command(name = { "kernel" }, usage = "kernel", category = CommandCategory.MUSIC, aliasSplit = true, order = 10)
  private void commandKernel(MessageReceivedEvent event) {
	commandPlay(event, "https://soundcloud.com/musicbykernel/sets/only-bangers");
	event.getTextChannel().sendMessage("enjoy 🙂 !").queue(Utils.deleteAfter(5L));
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

  @Description(text = "Mostra a musica atual.")
  @Command(name = { "playing", "pn" }, usage = "playing/pn", category = CommandCategory.MUSIC, aliasSplit = true, order = 12)
  private void commandPlayingNow(MessageReceivedEvent event) {

	GuildManager guildManager = getGuildManager(event.getGuild());

	AudioTrack track = guildManager.getAudioPlayer().getPlayingTrack();
	if (track != null) {
	  Utils.send(event.getTextChannel(), "playingNow", Placeholders.ofPlayingNow(event, track.getInfo().title));
	}
  }

  @Description(text = "Seleciona uma musica pelo numero na lista.")
  @Command(name = { "select" }, usage = "select <index>", category = CommandCategory.MUSIC, aliasSplit = true, order = 12)
  private void commandSelect(MessageReceivedEvent event, int index) {

	GuildManager guildManager = getGuildManager(event.getGuild());

	if(guildManager.getSchedule().isValidIndex(index)) {
	  String name = "Indo para a musica " + index;
	  Utils.send(event.getTextChannel(), "select", Placeholders.ofSelect(event, name));
	  guildManager.getSchedule().select(index);
	}
	else {
	  Utils.send(event.getTextChannel(), "failed", Placeholders.ofFailed(event, "Valor inválido."));
	}
  }

}