package org.musicbox.core.command;

import java.util.List;

import org.musicbox.config.DefaultConfig;

public abstract class GuildCommand extends GenericCommand {
   
   public GuildCommand(String name, List<String> usages, boolean contentArgument) {
      super(name, usages, contentArgument);
   }

   @Override
   public String toUsageString() {
      return "";
   }
   
   public String getFullUsage() {
      return DefaultConfig.PREFIX + getName() + " " + toUsageString();
   }
   
}
