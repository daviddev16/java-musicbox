package org.musicbox;

import org.musicbox.managing.LoadingResult;

public final class I18n {

	/* the reason why it's disabled: thumbnails are getting too much time on loading */
	public static final boolean DISABLE_EMBED_THUMBNAILS = true;
	
	/* lang */
	public static final String DEFAULT_LANGUAGE = "pt_br";

	/* common placeholders key */
	public static final String SENDER_TAG                = "senderTag";
	public static final String SENDER_AVATAR             = "senderAvatar";
	public static final String TRACK_TITLE 			     = "trackTitle";
	public static final String TRACK_TIMESTAMP 			 = "trackTimestamp";
	public static final String TRACK_ADDED_ICON 		 = "trackAddedIcon";
	public static final String TRACK_SKIPPED_ICON 		 = "trackSkippedIcon";
	public static final String TRACK_STOPPED_ICON 		 = "trackStoppedIcon";
	public static final String TRACK_PAUSED_ICON 	     = "trackPausedIcon";
	public static final String TRACK_RESUMED_ICON 		 = "trackResumedIcon";
	public static final String TRACK_RESTARTED_ICON 	 = "trackRestartedIcon";
	public static final String TRACK_REPEAT_MODE 	     = "trackRepeatModeIcon";
	public static final String SLEEPING_ICON 		     = "sleepingIcon";
	public static final String FAILED_ICON 				 = "failedIcon";
	public static final String FAIL_REASON 		         = "failReason";
	public static final String COMMAND_LIST 	         = "commandList";
	public static final String OWNER_TAG 				 = "musicBoxOwner";
	public static final String USER_INPUT_TAG            = "userInput";
	
	/*fail reasons*/
	public static final int MISSING_PERMISSION_REASON    = 0;
	public static final int NO_MATCHES_REASON 		     = 1;
	public static final int MISSING_VOICECHANNEL_REASON  = 2;
	public static final int FAILED_QUEUE_REASON          = 3;
	public static final int FAILED_LOAD_REASON 			 = 4;
	
	/* CDN icons */
	public static final String ADDED 					 = "https://cdn-icons-png.flaticon.com/64/5622/5622170.png";
	public static final String REMOVED 					 = "https://cdn-icons-png.flaticon.com/64/5622/5622228.png";
	public static final String SKIPPED 					 = "https://cdn-icons-png.flaticon.com/64/5622/5622057.png";
	public static final String DONE 					 = "https://cdn-icons-png.flaticon.com/64/5622/5622199.png";
	public static final String PAUSED 					 = "https://cdn-icons-png.flaticon.com/64/5622/5622133.png";
	public static final String REPEAT 					 = "https://cdn-icons-png.flaticon.com/64/5622/5622266.png";
	public static final String SLEEPING 				 = "https://cdn-icons-png.flaticon.com/64/5622/5622244.png";
	public static final String FAILED 	   				 = "https://cdn-icons-png.flaticon.com/64/5622/5622223.png";
	public static final String RESUMED 					 = "https://cdn-icons-png.flaticon.com/64/5622/5622141.png";
	public static final String RESTARTED 				 = "https://cdn-icons-png.flaticon.com/64/5622/5622235.png";

	
	public static int getReason(LoadingResult loadingResult) {
		
		if(loadingResult == LoadingResult.FAILED_LOAD) {
			return FAILED_LOAD_REASON;
		}
		else if(loadingResult == LoadingResult.FAILED_QUEUE) {
			return FAILED_QUEUE_REASON;
		}
		else if(loadingResult == LoadingResult.MISSING_PERMISSION) {
			return MISSING_PERMISSION_REASON;
		}
		else if(loadingResult == LoadingResult.NO_MATCHES) {
			return NO_MATCHES_REASON;
		}
		else if(loadingResult == LoadingResult.MISSING_VOICE_CHANNEL) {
			return MISSING_VOICECHANNEL_REASON;
		}
		return NO_MATCHES_REASON;
	}
}
