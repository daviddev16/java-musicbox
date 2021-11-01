package org.musicbox.core.translation;

public enum PlaceholderKeys {

   GLOBAL_PREFIX("global_prefix"),
   OWNER("owner_name"),
   
   SENDER_NAME("sender_name"),
   SENDER_AVATAR("sender_avatar"),
   
   COMMAND_USAGE("command_usage"),
   COMMAND_NAME("command_name"),
   
   TRACK_TITLE("track_title"),
   TRACK_TITLE_PREVIOUS("previous::track_title"),
   TRACK_DURATION("track_duration"),
   TRACK_POSITION("track_position"),
   TIME_LEFT("time_left"),

   PLAYLIST_LENGTH("playlist_length"),
   PLAYLIST_NAME("playlist_title"),
   
   REPEAT_MODE("repeat_mode"),
   
   GENERIC_ERROR_MESSAGE("generic_error_message"),
   ERROR_LEVEL("error_level");
   
   private String tag;
   
   private PlaceholderKeys(String tag) {
      this.tag = tag;
   }
   
   public String getTag() {
      return tag;
   }
   
}
