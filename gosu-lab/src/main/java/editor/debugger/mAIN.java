package editor.debugger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 */
public class mAIN
{
  public static void main( String[] args )
  {
//    TreeMap map = new TreeMap();
//    map.put( "abc", 123 );
//    map.put( "def", 123 );
//    System.out.println( map );

    MyList x = new MyList();
    x.put( "SDF",  3 );
    System.out.println( x );
  }

  static class MyList extends HashMap
  {
    @Override
    public int size()
    {
      return 3;
    }

    @Override
    public Set<Entry> entrySet()
    {
      throw new RuntimeException(  );
    }
  }
}

