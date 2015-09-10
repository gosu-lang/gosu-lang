/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.util.StringPool;

public class StringCache {
  public static String get(String rawString) {
    if (rawString == null) {
      return null;
    }
    return StringPool.get( rawString );
  }
}
