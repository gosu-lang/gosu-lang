/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting.samples;

import java.util.Scanner;

public class GosuCodeSamples {
  public static final String SPACING_SAMPLE = load("SpacingSample.txt");
  public static final String BLANK_LINES_SAMPLE = load("BlankLinesSample.txt");
  public static final String WRAPPING_AND_BRACES_SAMPLE = load("WrappingAndBracesSample.txt");

  private static String load(String fileName) {
    final String s = new Scanner(GosuCodeSamples.class.getResourceAsStream(fileName), "UTF-8").useDelimiter("\\A").next();
    return s.replaceAll("\\r", "");
  }
}
