package org.musicbox.core;

import java.util.Arrays;
import java.util.List;


import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public final class Permissions {

  public static List<Permission> WRITING_PERMISSION = Arrays.asList(
	  Permission.MESSAGE_EMBED_LINKS,
	  Permission.MESSAGE_ADD_REACTION,
	  Permission.MESSAGE_WRITE
	  );  

  public static boolean canWrite(TextChannel textChannel, Member sender) {
	return sender.hasPermission(textChannel, WRITING_PERMISSION);
  }

}
