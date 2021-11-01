package org.musicbox.core.translation;

/* translation field */
public enum TranslationKeys {

   /*embeds */
   SELECT_COMMAND("select_command"),
   PAUSE_COMMAND("pause_command"),
   RESUME_COMMAND("resume_command"),
   SKIP_COMMAND("skip_command"),
   STOP_COMMAND("stop_command"),
   REPEAT_COMMAND("repeat_command"),
   
   PLAYLIST_QUEUED("playlist_queued"),
   TRACK_QUEUED("track_queued"),
   
   INVALID_VOICE_CHANNEL("invalid_voice_channel"),
   MEMBER_IS_NOT_TOGETHER("member_is_not_together"),
   
   NO_MATCHES("no_matches"),
   WRONG_REPEAT_MODE("wrong_repeat_mode"),
   GENERIC_ERROR("generic_error"),
   COMMAND_MISSMATCH("command_missmatch"),
   COMMAND_OUT_OF_BOUNDS("command_out_of_bounds"),
   COMMAND_NOT_FOUND("command_not_found"),
   MISSING_BOT("missing_bot"),
   
   /*labels*/
   LABEL_EMPTY_LIST("label_empty_list"),
   LABEL_INVALID_POSITION("label_invalid_position"),
   LABEL_ALREADY_PAUSED("label_already_paused"),
   LABEL_ALREADY_RESUMED("label_already_resumed"),
   LABEL_UNSKIPPABLE("label_unskippable");
   
   private String key;

   private TranslationKeys(String key) {
      this.key = key;
   }
   
   public String getKey() {
      return key;
   }

}
