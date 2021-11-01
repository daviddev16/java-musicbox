package org.musicbox.core.translation;

/* translation field */
public enum TranslationKeys {

   /*embeds */
   SELECT_COMMAND("select_command"),
   
   PLAYLIST_QUEUED("playlist_queued"),
   TRACK_QUEUED("track_queued"),
   
   INVALID_VOICE_CHANNEL("invalid_voice_channel"),
   MEMBER_IS_NOT_TOGETHER("member_is_not_together"),
   
   NO_MATCHES("no_matches"),
   GENERIC_ERROR("generic_error"),
   COMMAND_MISSMATCH("command_missmatch"),
   COMMAND_OUT_OF_BOUNDS("command_out_of_bounds"),
   COMMAND_NOT_FOUND("command_not_found"),
   MISSING_BOT("missing_bot"),
   
   /*labels*/
   LABEL_INVALID_POSITION("invalid_position");
   
   private String key;

   private TranslationKeys(String key) {
      this.key = key;
   }
   
   public String getKey() {
      return key;
   }

}
