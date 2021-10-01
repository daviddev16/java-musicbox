package org.musicbox.models;

import java.util.List;

import org.musicbox.MusicBox;
import org.musicbox.utils.I18n;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import static org.musicbox.models.Placeholder.create;

/* otimization */
public final class Placeholders {

  public static List<Placeholder> ofTrackAdded(MessageReceivedEvent event, String userInput, TrackChunk trackChunk){
	return Placeholder.of(
		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
		create(I18n.TRACK_TITLE, trackChunk.getTitle()),
		create(I18n.TRACK_TIMESTAMP, trackChunk.getTiming()),
		create(I18n.TRACK_ADDED_ICON, I18n.ADDED),
		create(I18n.USER_INPUT_TAG, userInput)
		);
  }
  
  public static List<Placeholder> ofTrackSkipped(MessageReceivedEvent event){
	return Placeholder.of(
		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
		create(I18n.TRACK_SKIPPED_ICON, I18n.SKIPPED)
		);
  }
  
  public static List<Placeholder> ofTrackStopped(MessageReceivedEvent event){
	return Placeholder.of(
		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
		create(I18n.GLOBAL_PREFIX, MusicBox.getConfiguration().getDefaultCommandPrefix()),
		create(I18n.TRACK_STOPPED_ICON, I18n.DONE)
		);
  }
  
  public static List<Placeholder> ofTrackPaused(MessageReceivedEvent event){
 	return Placeholder.of(
 		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
 		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
 		create(I18n.GLOBAL_PREFIX, MusicBox.getConfiguration().getDefaultCommandPrefix()),
 		create(I18n.TRACK_PAUSED_ICON, I18n.PAUSED)
 		);
   }
  
  public static List<Placeholder> ofTrackResumed(MessageReceivedEvent event){
 	return Placeholder.of(
 		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
 		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
 		create(I18n.GLOBAL_PREFIX, MusicBox.getConfiguration().getDefaultCommandPrefix()),
 		create(I18n.TRACK_RESUMED_ICON, I18n.RESUMED)
 		);
   }
  
  public static List<Placeholder> ofTrackRepeatMode(MessageReceivedEvent event){
 	return Placeholder.of(
 		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
 		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
 		create(I18n.GLOBAL_PREFIX, MusicBox.getConfiguration().getDefaultCommandPrefix()),
 		create(I18n.TRACK_REPEAT_MODE, I18n.REPEAT)
 		);
   }
  
  public static List<Placeholder> ofFailed(MessageReceivedEvent event, String reason){
 	return Placeholder.of(
 		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
 		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
 		create(I18n.FAIL_REASON, reason),
 		create(I18n.FAILED_ICON, I18n.FAILED)
 		);
   }
  
  public static List<Placeholder> ofSelect(MessageReceivedEvent event, String name){
 	return Placeholder.of(
 		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
 		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
 		create(I18n.TRACK_TITLE, name),
 		create(I18n.TRACK_REPEAT_MODE, I18n.REPEAT)
 		);
   }
  
  public static List<Placeholder> ofTrackRestarted(MessageReceivedEvent event){
 	return Placeholder.of(
 		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
 		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
 		create(I18n.GLOBAL_PREFIX, MusicBox.getConfiguration().getDefaultCommandPrefix()),
 		create(I18n.TRACK_RESTARTED_ICON, I18n.RESTARTED)
 		);
   }
  
  public static List<Placeholder> ofWrongCommandAlias(MessageReceivedEvent event, String usage){
 	return Placeholder.of(
 		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
 		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
 		create(I18n.COMMAND_USAGE, usage),
 		create(I18n.FAILED_ICON, I18n.FAILED)
 		);
   }
  
  public static List<Placeholder> ofPlayingNow(MessageReceivedEvent event, String currentTrack){
 	return Placeholder.of(
 		create(I18n.TRACK_TITLE, currentTrack),
 		create(I18n.TRACK_ADDED_ICON, I18n.ADDED)
 		);
   }
  
  public static List<Placeholder> ofHelpCommand(MessageReceivedEvent event, String commandList){
 	return Placeholder.of(
 		create(I18n.SENDER_TAG, event.getAuthor().getAsTag()),
 		create(I18n.SENDER_AVATAR, event.getAuthor().getEffectiveAvatarUrl()),
 		create(I18n.OWNER_TAG, MusicBox.getConfiguration().getOwnerTag()),
 		create(I18n.TRACK_ADDED_ICON, I18n.ADDED),
 		create(I18n.COMMAND_LIST, commandList)
 		);
   }
  
}
