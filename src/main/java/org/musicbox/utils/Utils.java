package org.musicbox.utils;

import java.net.URL;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;

public final class Utils {

  public static boolean isTogetherWith(Member member, Guild guild) {
	VoiceChannel voiceChannel = guild.getSelfMember().getVoiceState().getChannel();
	for (Member connectedMember : voiceChannel.getMembers()) {
	  if (connectedMember.getIdLong() == member.getIdLong()) {
		return true;
	  }
	}
	return false;
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
  
  public static String toString(List<Permission> perm) {
	StringJoiner joiner = new StringJoiner(", ");
	perm.forEach(p -> joiner.add(p.getName()));
	return joiner.toString().trim();
  }

  public static boolean isOnVoiceChannel(Member member) {
	return member.getVoiceState().getChannel() != null;
  }

  public static boolean isPresentOnGuild(Guild guild) {
	return guild.getSelfMember().getVoiceState().inVoiceChannel();
  }

  public static String getTimestamp(long milliseconds) {
 	int seconds = (int) (milliseconds / 1000) % 60;
 	int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
 	int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

 	if (hours > 0)
 	  return String.format("%02d:%02d:%02d", hours, minutes, seconds);
 	else
 	  return String.format("%02d:%02d", minutes, seconds);
   }
  
  public static String getJoinedString(String[] strs) {
	StringJoiner joiner = new StringJoiner(",");
	for (String str : strs) {
	  joiner.add(str);
	}
	return joiner.toString().trim();
  }

  public static boolean isNumeric(String strNum) {
	if (strNum == null || strNum.isEmpty()) {
	  return false;
	}
	try {
	  Double.parseDouble(strNum);
	} catch (NumberFormatException nfe) {
	  return false;
	}
	return true;
  }

  public static Consumer<Message> deleteAfter(long sec) {
	return (msg) -> msg.delete().queueAfter(sec, TimeUnit.SECONDS);
  }
  
  public static boolean isURL(String url) {
	try {
	  new URL(url);
	  return true;
	} catch (Exception e) {
	  return false;
	}
  }

}
