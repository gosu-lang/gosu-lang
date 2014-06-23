/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IParseTree;

/**
 */
public final class PositionToken extends Token
{
  private IParseTree _pos;
  private int _iTokenStart;
  private int _iTokenEnd;

  public PositionToken( IParseTree pos, int iStartOffset, int iEndOffset )
  {
    _pos = pos;
    _iTokenStart = iStartOffset;
    _iTokenEnd = iEndOffset;
  }

  public IParseTree getPos()
  {
    return _pos;
  }

  @Override
  public int getType()
  {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public int getTokenStart()
  {
    return _iTokenStart;
  }

  @Override
  public int getTokenEnd()
  {
    return _iTokenEnd;
  }
}
