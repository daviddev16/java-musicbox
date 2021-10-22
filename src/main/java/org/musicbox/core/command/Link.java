package org.musicbox.core.command;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Link {

   int commandId();
   String[] names();
   CommandCategory category();
   boolean argumentsSplit() default true;

}
