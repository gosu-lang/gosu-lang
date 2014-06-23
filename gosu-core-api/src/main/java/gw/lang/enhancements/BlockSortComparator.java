/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.enhancements;

import gw.lang.function.IBlock;

import java.util.Comparator;

@SuppressWarnings({"UnusedDeclaration"}) // Used in Gosu core enhancements
public class BlockSortComparator implements Comparator
{
  private IBlock _isBeforeBlock;
  private final Object[] _args = new Object[2];

  public BlockSortComparator( Object isBefore )
  {
    _isBeforeBlock = (IBlock)isBefore;
  }

  public int compare( Object o1, Object o2 )
  {
    _args[0] = o1;
    _args[1] = o2;
    if( invokeIsBefore( _args ) )
    {
      return -1;
    }

    _args[0] = o2;
    _args[1] = o1;
    if( invokeIsBefore( _args ) )
    {
      return 1;
    }

    return 0;
  }

  private boolean invokeIsBefore( Object[] args )
  {
    return (Boolean)_isBeforeBlock.invokeWithArgs( args );
  }
}