/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.perf.objectsize;

import java.text.DecimalFormat;

/**
 */
public class ObjectSize {
  private long length;
  private boolean complete;
  private static String[] units = {"b", "Kb", "Mb", "Gb", "Tb"};
  private static DecimalFormat format = new DecimalFormat("#.#");

  public ObjectSize(long length, boolean complete) {
    this.length = length;
    this.complete = complete;
  }

  public String toString() {
    return toString(length) + " (" + (complete ? "complete" : "incomplete")+ ")";
  }

  public static String toString(double value) {
    int unit = 0;
    while (value >= 1024) {
      value /= 1024;
      unit ++;
    }
    return format.format(value) + " " + units[unit];
  }

  public void add(ObjectSize size) {
    length += size.length;
    complete &= size.complete;
  }

  public boolean isIncomplete() {
    return !complete;
  }

  public long size() {
    return length;
  }
}
