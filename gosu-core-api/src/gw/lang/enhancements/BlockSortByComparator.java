/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.enhancements;

import gw.lang.function.IBlock;
import java.util.Comparator;

@SuppressWarnings({"UnusedDeclaration"}) // Used in Gosu core enhancements
public class BlockSortByComparator implements Comparator
{
  private final Object[] args = new Object[1];
  private boolean _ascending;
  private IBlock _bytecodeBlock;
  private Comparator _comparator;

  public BlockSortByComparator( Object block, boolean ascending )
  {
    this(block, ascending, null);
  }

  public BlockSortByComparator (Object block, boolean ascending, Comparator comparator) {
    _bytecodeBlock = (IBlock)block;
    _ascending = ascending;
    _comparator = comparator;
  }

  public int compare( Object o1, Object o2 )
  {
    args[0] = o1;
    Comparable c1 = invoke( this.args );
    this.args[0] = o2;
    Comparable c2 = invoke( this.args );
    if( c1 == null && c2 == null )
    {
      return 0;
    }
    else if( c1 == null )
    {
      return _ascending ? -1 : 1;
    }
    else if( c2 == null )
    {
      return _ascending ? 1 : -1;
    }
    //noinspection unchecked
    int cmp = _comparator != null
              ? _comparator.compare(c1, c2)
              : c1.compareTo( c2 );
    return _ascending ? cmp : (cmp < 0 ? 1 : - cmp);
  }

  private Comparable invoke( Object[] args )
  {
    return (Comparable)_bytecodeBlock.invokeWithArgs( args );
  }
}