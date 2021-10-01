package org.musicbox.signature;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.musicbox.I18n;
import org.musicbox.MusicBox;
import org.musicbox.command.CommandController.CommandInfo;
import org.musicbox.utils.EmbedTranslator;
import org.musicbox.utils.Placeholder;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicBoxMessages {

	public static void commandNotFound(TextChannel textChannel) {
		System.out.println("1");
	}

	public static void commandWrongAlias(TextChannel textChannel) {
		System.out.println("2");

	}

	public static void internalError(TextChannel textChannel, Throwable e) {
		System.out.println("3");
		e.printStackTrace();
	}

	public static void send(TextChannel channel, String key, List<Placeholder> placeholders) {
		JsonObject jsonMessage = MusicBox.getConfiguration().getMessages(I18n.DEFAULT_LANGUAGE, key);		
		channel.sendMessage(EmbedTranslator.translate(jsonMessage, placeholders)).queue();
	}

	public static void help(MessageReceivedEvent event) {

		StringBuffer buffer = new StringBuffer();

		List<CommandInfo> sortedCommands = MusicBox.getCommandController().getCommandMap().values().stream()
				.collect(Collectors.toCollection(ArrayList::new));

		sortedCommands.sort(new Comparator<CommandInfo>() {
			public int compare(CommandInfo o1, CommandInfo o2) {
				return (o1.getOrder() == o2.getOrder()) ? 0 : (o1.getOrder() > o2.getOrder()) ? 1 : -1;
			}
		});

		sortedCommands.forEach((commandInfo) -> {
			buffer.append("**").append(getJoinedString(commandInfo.getNames())).append("**: ");
			buffer.append("`!" + commandInfo.getUsage() + "`").append(" **|** ");
			buffer.append("*").append(commandInfo.getDescription().text()).append("*").append('\n');
		});

		event.getTextChannel()
				.sendMessage(new EmbedBuilder().setTitle("Lista de comandos")
						.setFooter("Made by: Kernel#5096 | Lista requisitada por " + event.getAuthor().getAsTag(),
								event.getAuthor().getEffectiveAvatarUrl())
						.setDescription(buffer.toString()).setThumbnail(I18n.ADDED)
						.setColor(Color.decode("#FFF9D0")).build())
				.queue();

	}

	public static String getJoinedString(String[] strs) {
		StringJoiner joiner = new StringJoiner(",");
		for (String str : strs) {
			joiner.add(str);
		}
		return joiner.toString().trim();
	}

	public static Consumer<Message> deleteAfter(long sec) {
		return (msg) -> msg.delete().queueAfter(sec, TimeUnit.SECONDS);
	}

}
