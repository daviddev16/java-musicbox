package org.musicbox.utils;

import java.net.URL;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.musicbox.MusicBox;
import org.musicbox.models.Placeholder;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public final class Utils {

  public static String getTimestamp(long milliseconds) {
	int seconds = (int) (milliseconds / 1000) % 60;
	int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
	int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

	if (hours > 0)
	  return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	else
	  return String.format("%02d:%02d", minutes, seconds);
  }

  public static void send(TextChannel channel, String key, List<Placeholder> placeholders) {
	JsonObject jsonMessage = MusicBox.getConfiguration().getMessages(I18n.DEFAULT_LANGUAGE, key);
	channel.sendMessage(EmbedTranslator.translate(jsonMessage, placeholders)).queue();
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

  public static boolean isNumeric(String strNum) {
	if (strNum == null) {
	  return false;
	}
	try {
	  @SuppressWarnings("unused")
	  double d = Double.parseDouble(strNum);
	} catch (NumberFormatException nfe) {
	  return false;
	}
	return true;
  }

  public static boolean isURL(String url) {
	try {
	  new URL(url);
	  return true;
	} catch (Exception e) {
	  return false;
	}
  }

  public static boolean isBoolean(String str) {
	if (str.equals("true") || str.equals("false") || str.equals("yes") || str.equals("no") || str.equals("sim")
		|| str.equals("nao")) {
	  return true;
	}
	return false;
  }

  public static boolean getBoolean(String str) {
	if (str.endsWith("true") || str.equals("sim") || str.equals("yes")) {
	  return true;
	} else {
	  return false;
	}
  }

}
