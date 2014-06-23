/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IParseTree;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import gw.lang.parser.SourceCodeReader;
import gw.util.GosuEscapeUtil;

/**
*/
public class Token implements IToken
{
  int _iType;
  String _strValue;
  int _iInvalidCharPos;
  int _iDocPosition;
  int _iDocLength;
  int _iColumn;
  int _iLine;
  int _iLineOffset;
  boolean _bUnterminated;
  boolean _bAnalyzingSeparately;
  boolean _bAnalyzingDirective;
  DocCommentBlock _turd;

  IParseTree _after;


  public Token()
  {
    _iInvalidCharPos = -1;
  }

  Token init( int iType,
              int iInvalidCharPos,
              int iTokenStart,
              int iTokenEnd,
              int iTokenColumn,
              int iLine,
              int iLineOffset,
              boolean bUnterminatedString,
              String strValue,
              boolean bAnalyzingSeparately,
              boolean bAnalyzingDirective,
              SourceCodeReader document,
              DocCommentBlock turd )
  {
    _iType = iType;
    _iInvalidCharPos = iInvalidCharPos;
    _iDocPosition = iTokenStart;
    _iDocLength = iTokenEnd - _iDocPosition;
    _iColumn = iTokenColumn;
    _bUnterminated = bUnterminatedString;
    _bAnalyzingSeparately = bAnalyzingSeparately;
    _bAnalyzingDirective = bAnalyzingDirective;
    _iLine = iLine;
    _iLineOffset = iLineOffset;
    _turd = turd;
    assignContent( strValue, document );
    return this;
  }

  @Override
  public IToken copy()
  {
    return copyInto( create() );
  }

  @Override
  public <E extends IToken> E copyInto( E t )
  {
    Token copy = (Token)t;
    copy._iType = _iType;
    copy._strValue = _strValue;
    copy._iInvalidCharPos = _iInvalidCharPos;
    copy._iDocPosition = _iDocPosition;
    copy._iDocLength = _iDocLength;
    copy._iColumn = _iColumn;
    copy._iLine = _iLine;
    copy._iLineOffset = _iLineOffset;
    copy._bUnterminated = _bUnterminated;
    copy._bAnalyzingSeparately = _bAnalyzingSeparately;
    copy._bAnalyzingDirective = _bAnalyzingDirective;
    copy._turd = _turd;

    copy._after = _after;

    return t;
  }

  Token create()
  {
    return new Token();
  }

  @Override
  public void collapse()
  {
    _iDocPosition = _iDocPosition + _iDocLength;
  }

  protected void assignContent( String strValue, SourceCodeReader document )
  {
    _strValue = strValue == null ? null : getMyTextFromSource( document );
  }

  public int getTokenStart()
  {
    return _iDocPosition;
  }

  public int getTokenEnd()
  {
    return _iDocPosition + _iDocLength;
  }

  @Override
  public int getTokenColumn()
  {
    return _iColumn;
  }

  @Override
  public int getLine()
  {
    return _iLine;
  }

  @Override
  public int getLineOffset()
  {
    return _iLineOffset;
  }

  public String getStringValue()
  {
    return _strValue;
  }

  public int getInvalidCharPos()
  {
    return _iInvalidCharPos;
  }

  public int getType()
  {
    return _iType;
  }

  public String getText()
  {
    return _strValue != null
           ? _iType == '"' || _iType == '\''
             ? GosuEscapeUtil.escapeForGosuStringLiteral( _strValue, 0, _strValue.length() )
             : _strValue
           : _iType == ISourceCodeTokenizer.TT_INTEGER || _iType == ISourceCodeTokenizer.TT_NUMBER
             ? _strValue
             : String.valueOf( (char)_iType );
  }

  @Override
  public void setAfter( IParseTree after )
  {
    _after = after;
  }

  @Override
  public IParseTree getAfter()
  {
    return _after;
  }

  @Override
  public String toString()
  {
    return "Token : [" + getText() + "]";
  }

  protected String getMyTextFromSource( SourceCodeReader document )
  {
    String strText = "";
    if( _iDocLength > 0 )
    {
      int iTokenEnd = Math.min( _iDocPosition + _iDocLength, document.getLength() );
      strText = document.subsequence( _iDocPosition, iTokenEnd ).toString();
      strText = StringCache.get(strText);
    }
    return strText;
  }

  @Override
  public boolean isAnalyzingSeparately()
  {
    return _bAnalyzingSeparately;
  }

  @Override
  public boolean isAnalyzingDirective()
  {
    return _bAnalyzingDirective;
  }

  public DocCommentBlock getTurd()
  {
    return _turd;
  }
}
