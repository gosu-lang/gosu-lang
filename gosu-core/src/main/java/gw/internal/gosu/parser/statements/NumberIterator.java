/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import java.util.Iterator;

/**
 */
class NumberIterator implements Iterator
{
  private int _iIndex;
  private final int _iNum;

  public NumberIterator( Number numObj )
  {
    _iNum = numObj.intValue();
  }

  public void remove()
  {
    throw new UnsupportedOperationException( "Sorry, the integer iterator does not support remove()." );
  }

  public boolean hasNext()
  {
    return _iIndex < _iNum;
  }

  public Object next()
  {
    return _iIndex++;
  }
}
