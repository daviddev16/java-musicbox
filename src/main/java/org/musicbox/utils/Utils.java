package org.musicbox.utils;

import java.net.URL;

public final class Utils {

  public static boolean isNumeric(String strNum) {
	if (strNum == null) {
	  return false;
	}
	try {
	  @SuppressWarnings("unused")
	  double d = Double.parseDouble(strNum);
	} catch (NumberFormatException nfe) {
	  return false;
	}
	return true;
  }

  public static boolean isURL(String url) {
	try {
	  new URL(url);
	  return true;
	} catch (Exception e) {
	  return false;
	}
  }

  public static boolean isBoolean(String str) {
	if (str.equals("true") || str.equals("false") || str.equals("yes") || str.equals("no") || str.equals("sim")
		|| str.equals("nao")) {
	  return true;
	}
	return false;
  }

  public static boolean getBoolean(String str) {
	if (str.endsWith("true") || str.equals("sim") || str.equals("yes")) {
	  return true;
	} else {
	  return false;
	}
  }

  public static String getTimestamp(long milliseconds) {
	int seconds = (int) (milliseconds / 1000) % 60;
	int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
	int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

	if (hours > 0)
	  return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	else
	  return String.format("%02d:%02d", minutes, seconds);
  }

}
