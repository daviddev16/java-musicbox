package org.musicbox.core.guild;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.musicbox.core.guild.modules.GuildModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Guild;

public final class GuildInstance {

   private static Logger logger = LoggerFactory.getLogger(GuildInstance.class);
   
   private final Guild guild;

   private Set<GuildModule> modules;

   public GuildInstance(Guild guild) {
      modules = Collections.synchronizedSet(new LinkedHashSet<>());
      this.guild = guild;
   }

   @SuppressWarnings("unchecked")
   public <M extends GuildModule> M getModule(Class<M> moduleClass) {
      return (M) getModules().stream()
            .takeWhile(module -> module.getClass().isAssignableFrom(moduleClass))
            .findFirst()
            .orElse(null);
   }

   public boolean containsModule(Class<? extends GuildModule> moduleClass) {
      return getModules().stream(
            ).anyMatch(module -> module.getClass().isAssignableFrom(moduleClass));
   }

   public void addModule(GuildModule... modules) {
      for(GuildModule module : modules) {
         if(module != null) {
            injectGuildInstance(module);
            getModules().add(module);
            module.load();
         }
      }
   }
   
   public void injectGuildInstance(GuildModule guildModule) {
      try {
         Field guildInstanceField = GuildModule.class.getField("guildInstance");
         guildInstanceField.setAccessible(true); 
         guildInstanceField.set(guildModule, this);
      } catch (Exception e) {
         logger.info("guildInstance field not found.");
         System.exit(-1);
      }
   }

   public Set<GuildModule> getModules() {
      return modules;
   }

   public Guild getGuild() {
      return guild;
   }
}
