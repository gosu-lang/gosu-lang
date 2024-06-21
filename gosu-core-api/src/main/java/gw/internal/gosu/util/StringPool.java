package gw.internal.gosu.util;

import gw.lang.UnstableAPI;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Faster than String.intern()
 * <p/>
 * WARNING: This API is unstable and intended for internal use only.
 */
@UnstableAPI
public class StringPool
{
  private static final StringPool INSTANCE = new StringPool();

  private final ConcurrentHashMap<String, String> _map;

  private int _misses;
  private int _total;
  private long _size;

  private StringPool() {
    _map = new ConcurrentHashMap<>();
  }

  /**
   * This method is intended for internal use only.
   */
  @UnstableAPI
  public static String get( String value ) {
    if( value == null ) {
      return null;
    }
    String existing = INSTANCE._map.get( value );
    if( existing != null ) {
      return existing;
    }
    INSTANCE._map.put( value, value );
    return value;
  }

//  public static String get( String value ) {
//    String existing = INSTANCE._map.get( value );
//    INSTANCE._total++;
//    if( existing != null ) {
//      return existing;
//    }
//    INSTANCE._misses++;
//    INSTANCE._size += value.length();
//    INSTANCE._map.put( value, value );
//    return value;
//  }

  public static void printStats() {
    System.out.println( "MISSES: " + INSTANCE._misses );
    System.out.println( "TOTAL: " + INSTANCE._total );
    System.out.println( "SIZE: " + INSTANCE._size );
  }
}
