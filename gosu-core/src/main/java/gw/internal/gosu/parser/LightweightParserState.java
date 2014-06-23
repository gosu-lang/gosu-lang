/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IParserState;

public class LightweightParserState implements IParserState
{
  private int _lineNum;
  private int _lineOffset;
  private int _offsetShift;
  private int _tokenColumn;
  private String _src;
  private int _tokenStart;
  private int _tokenEnd;


  public LightweightParserState( SourceCodeTokenizer tokenizer, int offsetShift, int lineShift )
  {
    _offsetShift = offsetShift;
    init( tokenizer.getSource(),
          tokenizer.getLineNumber() + lineShift,
          tokenizer.getLineOffset(),
          tokenizer.getTokenColumn(),
          tokenizer.getTokenStart() + offsetShift,
          tokenizer.getTokenEnd() + offsetShift );
  }

  protected LightweightParserState()
  {
    //subtypes should call init() explicitly
  }

  void init( String src, int lineNum, int lineOffset, int tokenColumn, int tokenStart, int tokenEnd )
  {
    _lineNum = lineNum;
    _lineOffset = lineOffset;
    _tokenColumn = tokenColumn;
    _src = src;
    _tokenStart = tokenStart;
    _tokenEnd = tokenEnd;
  }

  public int getLineNumber()
  {
    return _lineNum;
  }

  public int getLineOffset()
  {
    return _lineOffset;
  }

  public int getTokenColumn()
  {
    return _tokenColumn;
  }

  public String getSource()
  {
    return _src;
  }

  public int getTokenStart()
  {
    return _tokenStart;
  }

  public LightweightParserState cloneWithNewTokenStartAndTokenEnd(int newTokenStart, int newLength) {
    LightweightParserState clone = new LightweightParserState();
    clone.init(_src, _lineNum, _lineOffset, _tokenColumn, newTokenStart + _offsetShift, newTokenStart + newLength - 1 + _offsetShift);
    return clone;
  }

  public int getTokenEnd()
  {
    return _tokenEnd;
  }
}
