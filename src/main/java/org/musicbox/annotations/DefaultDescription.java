package org.musicbox.annotations;

import java.lang.annotation.Annotation;

public final class DefaultDescription {

  public static Description get() {
	return new Description() {
	  public Class<? extends Annotation> annotationType() {
		return Description.class;
	  }

	  public String text() {
		return "Nenhuma descrição informada.";
	  }
	};
  }
}
