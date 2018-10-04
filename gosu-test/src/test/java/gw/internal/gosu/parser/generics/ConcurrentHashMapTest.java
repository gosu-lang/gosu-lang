package gw.internal.gosu.parser.generics;

import gw.testharness.Disabled;
import java.util.concurrent.ConcurrentHashMap;
import junit.framework.TestCase;

public class ConcurrentHashMapTest //extends TestCase
{
  @Disabled(assignee = "smckinney", reason = "it demonstrates a hang in ConcurrentHashMap")
  public void testReentrantHang()
  {
    ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
    for( int i = 0; i < 100; i++ )
    {
      map.computeIfAbsent( i, key -> findValue( map, key ) );
    }
  }

  private Integer findValue( ConcurrentHashMap<Integer, Integer> map, Integer key )
  {
    if( key % 5 == 0 )
    {
      return key;
    }
    return map.computeIfAbsent( key + 1, k -> findValue( map, k ) );
  }
}
