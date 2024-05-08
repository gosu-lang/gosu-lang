package gw.util.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Faster than String.intern()
 * <p/>
 * @deprecated This class is going away soon, do not use this.
 */
public class StringPool
{
  private static final StringPool INSTANCE = new StringPool();

  private ConcurrentHashMap<String, String> _map;
  private int _misses;
  private int _total;
  private long _size;

  private StringPool() {
    _map = new ConcurrentHashMap<>();
  }

  @Deprecated
  public static String get( String value ) {
    return value;
  }

//  public static String get( String value ) {
//    if( value == null ) {
//      return null;
//    }
//    String existing = INSTANCE._map.get( value );
//    if( existing != null ) {
//      return existing;
//    }
//    INSTANCE._map.put( value, value );
//    return value;
//  }
//
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
