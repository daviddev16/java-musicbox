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
   RESTART_COMMAND("restart_command"),
   BACK_COMMAND("back_command"),
   NOW_COMMAND("now_command"),
   
   QUEUE_PAGE("queue_page"),
   
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
   LABEL_NOT_PLAYING("label_not_playing"),
   
   LABEL_EMPTY_LIST("label_empty_list"),
   LABEL_INVALID_POSITION("label_invalid_position"),
   LABEL_ALREADY_PAUSED("label_already_paused"),
   LABEL_ALREADY_RESUMED("label_already_resumed"),
   LABEL_UNABLE_ACTION("label_unable_action"),
   LABEL_UNSKIPPABLE("label_unskippable"),
   
   LABEL_PLAY_DESCRIPTION("label_play_description"),
   LABEL_STOP_DESCRIPTION("label_stop_description"),
   LABEL_SKIP_DESCRIPTION("label_skip_description"),
   LABEL_RESUME_DESCRIPTION("label_resume_description"),
   LABEL_PAUSE_DESCRIPTION("label_pause_description"),
   LABEL_SELECT_DESCRIPTION("label_select_description"),
   LABEL_REPEAT_DESCRIPTION("label_repeat_description"),
   LABEL_RESTART_DESCRIPTION("label_restart_description"),
   LABEL_NOW_DESCRIPTION("label_now_description"),
   LABEL_BACK_DESCRIPTION("label_back_description"),
   LABEL_LEAVE_DESCRIPTION("label_leave_description"),
   LABEL_QUEUE_DESCRIPTION("label_queue_description");
   
   private String key;

   private TranslationKeys(String key) {
      this.key = key;
   }
   
   public String getKey() {
      return key;
   }

}
