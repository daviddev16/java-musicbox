package org.musicbox.core.guild;

import net.dv8tion.jda.api.entities.Guild;

public final class GuildWrapper {

   private final Guild guild;

   private final Inspector inspector;
   private final TrackScheduler scheduler;
   private final Language language;
   
   public GuildWrapper(Guild guild) {
      this.scheduler = new TrackScheduler(this);
      this.inspector = new Inspector(this);
      this.language = new Language(this);
      this.guild = guild;
   }
   
   public Inspector getInspector() {
      return inspector;
   }

   public TrackScheduler getScheduler() {
      return scheduler;
   }

   public Language getLanguage() {
      return language;
   }

   public Guild getGuild() {
      return guild;
   }
}
