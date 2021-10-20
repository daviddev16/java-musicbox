package org.musicbox.core;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public final class Permissions {

   public static List<Permission> WRITING_PERMISSIONS = Arrays.asList(
         Permission.MESSAGE_EMBED_LINKS,
         Permission.MESSAGE_ADD_REACTION,
         Permission.MESSAGE_WRITE
         );

   public static List<Permission> VOICE_CHANNEL_PERMISSIONS = Arrays.asList(
         Permission.VOICE_CONNECT,
         Permission.VOICE_SPEAK
         );

   public static boolean canWrite(TextChannel textChannel, Member sender) {
      return sender.hasPermission(textChannel, WRITING_PERMISSIONS);
   }

   public static boolean canSelfConnect(VoiceChannel channel) {
      Member selfMember = channel.getGuild().getSelfMember();
      return selfMember.hasPermission(channel, VOICE_CHANNEL_PERMISSIONS);
   }

}
