package org.musicbox.tables;

import org.musicbox.core.GuildInstance;
import org.musicbox.core.command.CommandCategory;
import org.musicbox.core.command.Link;
import org.musicbox.core.command.Usage;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.managers.YoutubeSearchManager;
import org.musicbox.utils.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicCommandTable {

  @Usage(usage = "play/p <track title or url>")
  @Link(commandId = 0, names = { "play", "p" }, category = CommandCategory.MUSIC, argumentsSplit = false)
  private void play(MessageReceivedEvent event, String content) {

	Guild guild = event.getGuild();

	if (!Utils.isPresentOnGuild(guild)) {

	  if (!Utils.isOnVoiceChannel(event.getMember())) {
		System.out.println("Você não está presente em nenhum canal de voz.");
		return;
	  }

	  VoiceChannel memberChannel = event.getMember().getVoiceState().getChannel();
	  event.getGuild().getAudioManager().openAudioConnection(memberChannel);

	} else if (!Utils.isTogetherWith(event.getMember(), guild)) {
	  System.out.println("Você não pode usar o bot, conecte-se no mesmo canal.");
	  return;
	}

	GuildInstance guildInstance = GuildManager.getGuildManager().getGuildInstance(guild);

	String url = content.trim();
	if (!Utils.isURL(url)) {
	  url = YoutubeSearchManager.getSearchManager().getUrlBaseOnText(url);
	}

	guildInstance.load(url, event.getTextChannel());

  }
  
  /*
   * @Usage(usage = "pause")
   * 
   * @Link(commandId = 1, names = { "pause" }, category = CommandCategory.MUSIC,
   * argumentsSplit = true) private void pause(MessageReceivedEvent event) {
   * 
   * Guild guild = event.getGuild(); GuildInstance guildInstance =
   * GuildManager.getGuildManager().getGuildInstance(guild);
   * guildInstance.getSchedule().setPauseState(true); }
   * 
   * 
   * @Usage(usage = "resume")
   * 
   * @Link(commandId = 2, names = { "resume" }, category = CommandCategory.MUSIC,
   * argumentsSplit = true) private void resume(MessageReceivedEvent event) {
   * 
   * Guild guild = event.getGuild(); GuildInstance guildInstance =
   * GuildManager.getGuildManager().getGuildInstance(guild);
   * guildInstance.getSchedule().setPauseState(false); }
   * 
   * @Usage(usage = "stop")
   * 
   * @Link(commandId = 3, names = { "stop" }, category = CommandCategory.MUSIC,
   * argumentsSplit = true) private void stop(MessageReceivedEvent event) {
   * 
   * Guild guild = event.getGuild(); GuildInstance guildInstance =
   * GuildManager.getGuildManager().getGuildInstance(guild);
   * guildInstance.getSchedule().stop(); }
   * 
   * 
   * 
   * 
   * @Usage(usage = "queue")
   * 
   * @Link(commandId = 4, names = { "queue", "q" }, category =
   * CommandCategory.MUSIC, argumentsSplit = true) private void
   * queue(MessageReceivedEvent event) {
   * 
   * Guild guild = event.getGuild(); GuildInstance guildInstance =
   * GuildManager.getGuildManager().getGuildInstance(guild);
   * 
   * String str = "-"; for(AudioTrack track :
   * guildInstance.getSchedule().tracklist) { str += track.getInfo().title + "\n";
   * } event.getTextChannel().sendMessage(str).queue();
   * 
   * }
   * 
   * @Usage(usage = "select <position>")
   * 
   * @Link(commandId = 5, names = { "select", "slc" }, category =
   * CommandCategory.MUSIC, argumentsSplit = true) private void
   * select(MessageReceivedEvent event, int position) {
   * 
   * Guild guild = event.getGuild(); GuildInstance guildInstance =
   * GuildManager.getGuildManager().getGuildInstance(guild);
   * guildInstance.getSchedule().select(position);
   * 
   * }
   * 
   * @Usage(usage = "skip")
   * 
   * @Link(commandId = 6, names = { "skip" }, category = CommandCategory.MUSIC,
   * argumentsSplit = true) private void skip(MessageReceivedEvent event) {
   * 
   * Guild guild = event.getGuild(); GuildInstance guildInstance =
   * GuildManager.getGuildManager().getGuildInstance(guild);
   * guildInstance.getSchedule().next(); }
   * 
   * @Usage(usage = "back")
   * 
   * @Link(commandId = 7, names = { "back" }, category = CommandCategory.MUSIC,
   * argumentsSplit = true) private void back(MessageReceivedEvent event) {
   * 
   * Guild guild = event.getGuild(); GuildInstance guildInstance =
   * GuildManager.getGuildManager().getGuildInstance(guild);
   * guildInstance.getSchedule().back(); }
   */

}
