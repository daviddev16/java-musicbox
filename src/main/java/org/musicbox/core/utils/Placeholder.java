package org.musicbox.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Placeholder {

   public String suffix;
   private String replacement;

   private Placeholder(String suffix, String replacement) {
      this.suffix = suffix;
      this.replacement = replacement;
   }

   @Deprecated
   public static List<Placeholder> of(Placeholder... phs) {
      List<Placeholder> placeholders = new ArrayList<>();
      if (phs != null) {
         Arrays.stream(phs).forEach(placeholders::add);
      }
      return placeholders;
   }

   @Deprecated
   public static List<Placeholder> of(List<Placeholder> otherPlaceholders, Placeholder... phs) {
      List<Placeholder> placeholders = of(phs);
      if (otherPlaceholders != null) {
         otherPlaceholders.forEach(placeholders::add);
      }
      return placeholders;
   }

   public static Placeholder create(String suffix, String replacement) {
      return new Placeholder(suffix, replacement);
   }

   public String getSuffix() {
      return suffix;
   }

   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }

   public String getReplacement() {
      return replacement;
   }

   public void setReplacement(String replacement) {
      this.replacement = replacement;
   }

   public String getCode() {
      return ("$[" + getSuffix() + "]").trim();
   }

}
