/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class StringCache {
  private static Map<StringSoftReference, StringSoftReference> _cache = new HashMap<StringSoftReference, StringSoftReference>();
  private static Key _key = new Key("");

  public static String get(String rawString) {
    if (rawString == null) {
      return null;
    }
    StringSoftReference cachedReference;
    synchronized (_cache) {
      _key.thisString = rawString;
      cachedReference = _cache.get(_key);
      if (cachedReference == null || cachedReference.get() == null) {
        cachedReference = new StringSoftReference(new String(rawString));
        _cache.put(cachedReference, cachedReference);
      }
    }
    return cachedReference.get();
  }

  private static class Key {
    private String thisString;

    public Key(String string) {
      thisString = string;
    }

    @Override
    public boolean equals(Object o) {
      String thatString = ((SoftReference<String>) o).get();
      if (thatString == null) {
        return false;
      }
      if (thisString.hashCode() != thatString.hashCode()) {
        return false;
      }
      return thisString.equals(thatString);
    }

    @Override
    public int hashCode() {
      return thisString.hashCode();
    }
  }

  private static class StringSoftReference extends SoftReference<String> {

    public StringSoftReference(String referent) {
      super(referent);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;

      String thisString = get();
      String thatString = ((StringSoftReference) o).get();

      if (thisString == null) {
        return thatString == null;
      }

      if (thatString == null) {
        return false;
      }

      if (thisString.hashCode() != thatString.hashCode()) {
        return false;
      }

      return thisString.equals(thatString);
    }

    @Override
    public int hashCode() {
      String thisString = get();
      return thisString != null ? thisString.hashCode() : 0;
    }

    public String toString() {
      return get();
    }

  }
}
