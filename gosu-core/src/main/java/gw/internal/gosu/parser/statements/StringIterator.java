/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 */
class StringIterator implements Iterator
{
  int iCsr = 0;
  private final String _strObj;

  public StringIterator( String strObj )
  {
    _strObj = strObj;
  }

  public boolean hasNext()
  {
    return iCsr < _strObj.length();
  }

  public Object next()
  {
    if( !hasNext() )
    {
      throw new NoSuchElementException( "No element at index [" + iCsr + "] for character iterator" );
    }

    return String.valueOf( _strObj.charAt( iCsr++ ) );
  }

  public void remove()
  {
    throw new UnsupportedOperationException( "Sorry, String character iterator does not support remove()." );
  }
}
