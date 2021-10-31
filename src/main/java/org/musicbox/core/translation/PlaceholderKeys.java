package org.musicbox.core.translation;

public enum PlaceholderKeys {

   
   GLOBAL_PREFIX("global_prefix"),
   OWNER("owner_name"),
   
   SENDER_NAME("sender_name"),
   SENDER_AVATAR("sender_avatar"),
   
   COMMAND_USAGE("command_usage");
   
   
   private String tag;
   
   private PlaceholderKeys(String tag) {
      this.tag = tag;
   }
   
   public String getTag() {
      return tag;
   }
   
}
