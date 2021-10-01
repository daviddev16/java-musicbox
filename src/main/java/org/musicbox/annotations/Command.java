package org.musicbox.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.musicbox.command.CommandCategory;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	String[] name();
	String usage();
	CommandCategory category();
	boolean aliasSplit();
	int order();
}
