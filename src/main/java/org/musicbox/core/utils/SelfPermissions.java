package org.musicbox.core.utils;

import java.util.Arrays;
import java.util.List;

import org.musicbox.core.guild.GuildWrapper;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import static org.musicbox.core.utils.Utilities.allOf;

public final class SelfPermissions {

   public static List<Permission> WRITE_PERMISSIONS = Arrays.asList(
         Permission.MESSAGE_EMBED_LINKS,
         Permission.MESSAGE_ADD_REACTION,
         Permission.MESSAGE_WRITE
         );

   public static List<Permission> VOICE_PERMISSIONS = Arrays.asList(
         Permission.VOICE_CONNECT,
         Permission.VOICE_SPEAK
         );
   
   public static List<Permission> ALL_PERMISSIONS = allOf(WRITE_PERMISSIONS,VOICE_PERMISSIONS);

   public static boolean canWrite(TextChannel textChannel) {
      return textChannel != null && getSelfMember(textChannel)
            .hasPermission(textChannel, WRITE_PERMISSIONS);
   }

   public static boolean canSpeak(VoiceChannel voiceChannel) {
      return voiceChannel != null && getSelfMember(voiceChannel)
            .hasPermission(voiceChannel, VOICE_PERMISSIONS);
   }

   public static boolean isAlreadyConnect(Guild guild) {
      return guild != null && guild.getSelfMember().getVoiceState().inVoiceChannel();
   }

   public static boolean isConnected(VoiceChannel channel) {
      return channel.getMembers().stream()
            .anyMatch(member -> member.getIdLong() == getSelfMember(channel).getIdLong());
   }

   public static boolean isAlreadyConnect(GuildWrapper wrapper) {
      return wrapper != null && isAlreadyConnect(wrapper.getGuild());
   }
   
   public static Member getSelfMember(GuildChannel channel) {
      return channel.getGuild().getSelfMember();
   }

   public static boolean isTogether(Member member) {

      if(member.getVoiceState().inVoiceChannel())
         return isConnected(member.getVoiceState().getChannel());

      return false;
   }

}
