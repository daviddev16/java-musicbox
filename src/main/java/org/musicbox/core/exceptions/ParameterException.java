package org.musicbox.core.exceptions;

import org.musicbox.core.translation.TranslationKeys;

public class ParameterException extends RuntimeException {

   private static final long serialVersionUID = -2277469739389226310L;

   public static final int EMPTY_CONTENT = 2;
   public static final int TYPE_MISSMATCH = 1;
   public static final int OUT_OF_BOUNDS = 0;

   private int exceptionType;

   public ParameterException(String message, int exceptionType) {
      super(message);
      this.exceptionType = exceptionType;
   }

   public ParameterException(ParameterException subException) {
      this(subException.getMessage(), subException.getExceptionType());
   }

   public int getExceptionType() {
      return exceptionType;
   }

   public TranslationKeys getError() {
      if(getExceptionType() == EMPTY_CONTENT)
         return TranslationKeys.COMMAND_MISSMATCH;
      if(getExceptionType() == OUT_OF_BOUNDS)
         return TranslationKeys.COMMAND_OUT_OF_BOUNDS;
      if(getExceptionType() == TYPE_MISSMATCH)
         return TranslationKeys.COMMAND_MISSMATCH;
      return null;
   }

}
