/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.generator;

import gw.plugin.ij.framework.MarkerType;

public class GosuTestingResource {
  private static char[] CHARS = {';', '<', ' ', '\n', '{'};
  public String content;
  public String fileName;
  public String qualifiedName;

  public GosuTestingResource(String _fileName, String _content) {
    fileName = _fileName;
    content = _content;
    qualifiedName = _fileName.replace('/', '.');
    qualifiedName = qualifiedName.substring(0, qualifiedName.lastIndexOf('.'));
  }

  public static int wordEnd(String text, int offset) {
    int min = Integer.MAX_VALUE;
    for (char c : CHARS) {
      int i = text.indexOf(c, offset);
      if (i >= 0 && i < min) {
        min = i;
      }
    }
    return min;
  }

  public static String removeMarkers(String text) {
    MarkerType[] values = MarkerType.values();
    for (MarkerType v : values) {
      String s = v.markerText;
      text = text.replace(s, "");
    }
    return text;
  }
}
