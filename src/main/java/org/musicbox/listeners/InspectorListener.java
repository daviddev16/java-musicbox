package org.musicbox.listeners;

import java.util.List;

import org.musicbox.core.guild.GuildWrapper;
import org.musicbox.core.guild.controllers.Inspector;
import org.musicbox.core.managers.GuildManager;
import org.musicbox.core.managers.ListenerManager.Listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;

public class InspectorListener extends Listener {

   @Override
   public void onGenericGuildVoice(GenericGuildVoiceEvent event) {

      Guild guild = event.getGuild();
      VoiceChannel channel = null;
      GuildWrapper wrapper = GuildManager.getGuildManager().getWrapper(guild);
      Inspector inspector = wrapper.getInspector();

      if (event instanceof GuildVoiceLeaveEvent) {
         channel = ((GuildVoiceLeaveEvent) event).getChannelLeft();
         if (isBotPresent(channel) && isAlone(channel.getMembers())) {
            if (!inspector.isWaiting()) {
               inspector.waitToQuitIfNecessary(channel);
            }
         }
      } else if (event instanceof GuildVoiceJoinEvent) {
         channel = ((GuildVoiceJoinEvent) event).getChannelJoined();
         if (isBotPresent(channel) && !isAlone(channel.getMembers())) {
            inspector.cancelIfNecessary();
         }
      }
   }

   public boolean isBotPresent(VoiceChannel voiceChannel) {
      return voiceChannel.getMembers().stream()
            .filter(member -> member.getUser().getIdLong() == voiceChannel.getJDA().getSelfUser().getIdLong()).findFirst()
            .isPresent();
   }

   public static boolean isAlone(List<Member> member) {
      return member.size() == 1;
   }
}
