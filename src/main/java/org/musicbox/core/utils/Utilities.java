package org.musicbox.core.utils;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.musicbox.core.builders.PlaceholderBuilder.Placeholder;
import org.musicbox.miscs.Constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.VoiceChannel;

public final class Utilities {

   public static MessageEmbed translate(JsonObject jsonObject, List<Placeholder> placeholders) {
      EmbedBuilder builder = new EmbedBuilder();
      if (jsonObject.has("author")) {
         builder.setAuthor(
               translatePlaceholders(jsonObject.get("author").getAsJsonObject().get("name").getAsString(), placeholders),
               translatePlaceholders(jsonObject.get("author").getAsJsonObject().get("url").getAsString(), placeholders),
               translatePlaceholders(jsonObject.get("author").getAsJsonObject().get("iconUrl").getAsString(), placeholders));
      }
      if (jsonObject.has("footer")) {
         builder.setFooter(
               translatePlaceholders(jsonObject.get("footer").getAsJsonObject().get("text").getAsString(), placeholders),
               translatePlaceholders(jsonObject.get("footer").getAsJsonObject().get("iconUrl").getAsString(), placeholders));
      }
      if (jsonObject.has("description")) {
         builder.setDescription(translatePlaceholders(jsonObject.get("description").getAsString(), placeholders));
      }
      if (jsonObject.has("color")) {
         builder.setColor(Color.decode("#" + translatePlaceholders(jsonObject.get("color").getAsString(), placeholders)));
      }
      if (jsonObject.has("image")) {
         builder.setImage(translatePlaceholders(jsonObject.get("image").getAsString(), placeholders));
      }
      if (jsonObject.has("thumbnail") && Constants.ENABLE_EMBED_THUMBNAILS) {
         builder.setThumbnail(translatePlaceholders(jsonObject.get("thumbnail").getAsString(), placeholders));
      }
      if (jsonObject.has("title")) {
         builder.setTitle(translatePlaceholders(jsonObject.get("title").getAsString(), placeholders));
      }
      if (jsonObject.has("fields")) {
         JsonArray fieldArray = jsonObject.get("fields").getAsJsonArray();
         fieldArray.forEach(fieldElement -> {
            JsonObject fieldObject = fieldElement.getAsJsonObject();
            if (fieldObject.has("#blank")) {
               builder.addBlankField(fieldObject.get("#black").getAsBoolean());
            } else {
               builder.addField(translatePlaceholders(fieldObject.get("name").getAsString(), placeholders),
                     translatePlaceholders(fieldObject.get("value").getAsString(), placeholders),
                     fieldObject.get("inline").getAsBoolean());
            }
         });
      }

      return builder.build();
   }

   public static String translatePlaceholders(String text, List<Placeholder> placeholders) {
      String translatedCode = text;
      for (Placeholder placeholder : placeholders) {
         translatedCode = translatedCode.replace(placeholder.getCode(), placeholder.getReplacement());
      }
      return translatedCode;
   }

   public static boolean isTogetherWith(Member member, Guild guild) {
      VoiceChannel voiceChannel = guild.getSelfMember().getVoiceState().getChannel();
      for (Member connectedMember : voiceChannel.getMembers()) {
         if (connectedMember.getIdLong() == member.getIdLong()) {
            return true;
         }
      }
      return false;
   }

   public static String stripUrl(String url) {
      if (url.startsWith("<") && url.endsWith(">")) {
         return url.substring(1, url.length() - 1);
      } else {
         return url;
      }
   }
   
   @SafeVarargs
   public static List<Permission> allOf(Collection<Permission>... permissions) {
      List<Permission> collectPermissions = new ArrayList<>();
      for (Collection<Permission> permission : permissions) {
         permission.forEach(perm -> collectPermissions.add(perm));
      }
      return collectPermissions;
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
   
   public static boolean parseBoolean(String value) {
      if(Utilities.isBoolean(value)) {
         return Utilities.getBoolean(value);
      }
      throw new IllegalArgumentException("Not a boolean value.");
   }

   public static String toString(List<?> list) {
      StringJoiner joiner = new StringJoiner(", ");
      list.forEach(object -> {

         if (object instanceof Permission)
            joiner.add(((Permission) object).getName().toUpperCase());

      });
      return joiner.toString().trim();
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
