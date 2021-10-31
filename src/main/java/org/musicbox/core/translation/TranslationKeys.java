package org.musicbox.core.translation;

/* translation field */
public enum TranslationKeys {

   /* embed keys */
   PLAY_COMMAND("play_command"),
   
   COMMAND_MISSMATCH("command_missmatch"),
   COMMAND_OUT_OF_BOUNDS("command_out_of_bounds"),
   COMMAND_NOT_FOUND("command_not_found");

   private String key;

   private TranslationKeys(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }

}
