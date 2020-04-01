/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.util.StreamUtil;

import java.io.IOException;
import java.io.Reader;

public class SourceCodeReader
{
  private String _source;
  private int _iLength;
  private int _iPosition;

  public SourceCodeReader( String source )
  {
    _source = source;
    _iLength = _source == null ? -1 : _source.length();
    _iPosition = 0;
  }

  public int read()
  {
    return _iLength > _iPosition
           ? _source.charAt( _iPosition++ )
           : -1;
  }

  public int peek()
  {
    return peek( 1 );
  }

  public int peek( int n )
  {
    n -= 1; // already positioned +1 ahead

    return _iLength > _iPosition + n
           ? _source.charAt( _iPosition + n )
           : -1;
  }

  public int getPosition()
  {
    return _iPosition;
  }
  public void setPosition( int iPosition ) throws IOException
  {
    if( iPosition < 0 )
    {
      throw new IOException( iPosition + " < 0" );
    }
    _iPosition = iPosition;
  }

  public String getSource()
  {
    return _source;
  }

  public String subsequence( int iStart, int iEnd )
  {
    return _source.substring( iStart, iEnd );
  }

  public static SourceCodeReader makeSourceCodeReader( Reader reader )
  {
    try
    {
      return new SourceCodeReader( StreamUtil.getContent( reader ) );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public int getLength()
  {
    return _iLength;
  }
}
