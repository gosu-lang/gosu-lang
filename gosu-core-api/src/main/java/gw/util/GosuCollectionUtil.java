/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class GosuCollectionUtil
{
  //----------------------------------------------------------------------------------------
  // Collection utilities
  //----------------------------------------------------------------------------------------
  /**
   * Returns a compacted and locked map representing the map passed in.  This method can freely change the
   * implementation type of the map.  I.e. it can return an emptyMap, singletonMap, or even a completely different map implementation.
   */
  public static <S, T> Map<S, T> compactAndLockHashMap( HashMap<S, T> map )
  {
    if( map == null || map.isEmpty() )
    {
      return Collections.emptyMap();
    }

    if( map.size() == 1 )
    {
      Map.Entry<S, T> stEntry = map.entrySet().iterator().next();
      return Collections.singletonMap( stEntry.getKey(), stEntry.getValue() );
    }

    Map<S, T> newMap = new HashMap<S, T>( map.size(), 1 );
    newMap.putAll( map );
    return Collections.unmodifiableMap( newMap );
  }

  /**
   * Returns a compacted and locked list representing the list passed in.
   */
  public static <T> List<T> compactAndLockList( List<T> list )
  {
    if( list == null || list.isEmpty() )
    {
      return Collections.emptyList();
    }

    if( list.size() == 1 )
    {
      return Collections.singletonList( list.get( 0 ) );
    }

    if( list instanceof ArrayList )
    {
      ((ArrayList<T>)list).trimToSize();
    }

    return Collections.unmodifiableList( list );
  }

  /**
   * {@link String#startsWith(String)} for Lists.
   *
   * @return true iff list is at least as big as prefix, and if the first prefix.size() elements
   *         are element-wise equal to the elements of prefix.
   */
  public static boolean startsWith( List<?> list, List<?> prefix )
  {
    if( list.size() < prefix.size() )
    {
      return false;
    }
    Iterator<?> listIter = list.iterator();
    for( Object prefixElement : prefix )
    {
      boolean b = listIter.hasNext();
      assert b :
        "list claims to have at least as many elements as prefix, but its iterator is exhausted first";
      if( !GosuObjectUtil.equals( prefixElement, listIter.next() ) )
      {
        return false;
      }
    }
    return true;
  }
}
