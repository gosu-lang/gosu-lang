/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;

import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;
import gw.lang.parser.ITokenizerInstructor;
import gw.lang.parser.ITokenizerOffsetMarker;
import gw.lang.parser.SourceCodeReader;
import gw.util.Stack;

import java.io.IOException;
import java.io.Reader;


/**
 */
final public class SourceCodeTokenizer implements ISourceCodeTokenizer
{
  private int _state;
  private SourceCodeTokenizerInternal _internal;
  private Stack<ITokenizerOffsetMarker> _offsetMarkers;

  private SourceCodeTokenizer()
  {
    _state = -1;
    _offsetMarkers = new Stack<>();
  }

  private SourceCodeTokenizer( boolean initForCopy )
  {
    this();
    _internal = new SourceCodeTokenizerInternal( initForCopy );
  }

  public SourceCodeTokenizer( CharSequence sourceCode )
  {
    this();
    _internal = new SourceCodeTokenizerInternal( new SourceCodeReader( sourceCode ), null );
  }

  public SourceCodeTokenizer( Reader reader )
  {
    this();
    _internal = new SourceCodeTokenizerInternal( reader );
  }

  public SourceCodeTokenizer( SourceCodeReader reader )
  {
    this();
    _internal = new SourceCodeTokenizerInternal( reader );
  }

  public SourceCodeTokenizer( SourceCodeReader reader, ITokenizerInstructor instructor )
  {
    this();
    _internal = new SourceCodeTokenizerInternal( reader, instructor );
    if( instructor != null )
    {
      instructor.setTokenizer( this );
    }
  }

  public IToken copy()
  {
    return getCurrentToken().copy();
  }

  public IToken copyInto( IToken t )
  {
    return getCurrentToken().copyInto( t );
  }

  public SourceCodeTokenizerInternal getInternal() {
    return _internal;
  }

  /**
   * @return a very lightweight restored copy of this tokenizer, without deep cloning anything.  This method
   * should be used with care, as it does not clone internal state and cannot actually tokenize.  It should be
   * used <b>only</b> for offset information.
   */
  public SourceCodeTokenizer lightweightRestore()
  {
    SourceCodeTokenizer copy = new SourceCodeTokenizer( true );
    copy._state = _state;
    copy._internal.setTokens( getTokens() );
    return copy;
  }

  public boolean isPositioned()
  {
    return _state >= 0;
  }

  public void reset()
  {
    _state = -1;
    if( getOffsetMarker() != null )
    {
      int iOffsetMark = getOffsetMarker().getOffsetMark();
      if( iOffsetMark >= 0 )
      {
        restoreToMark( iOffsetMark );
      }
    }
  }

  public void reset( Reader reader )
  {
    reset( SourceCodeReader.makeSourceCodeReader( reader ), false );
  }

  public void reset( SourceCodeReader reader )
  {
    reset( reader, false );
  }

  private void reset( SourceCodeReader reader, boolean bResetReader )
  {
    if( reader != _internal.getReader() )
    {
      _internal.reset( reader, bResetReader );
    }
    _state = -1;
    _offsetMarkers.clear();
  }

  public SourceCodeReader getReader()
  {
    return _internal.getReader();
  }

  public String getSource()
  {
    return getReader() == null ? "" : getReader().getSource();
  }

  public ITokenizerInstructor getInstructor()
  {
    return _internal.getInstructor();
  }

  public void setInstructor( ITokenizerInstructor instructor )
  {
    _internal.setInstructor( instructor );
  }

  public boolean isWhitespaceSignificant()
  {
    return _internal.isWhitespaceSignificant();
  }
  public void setWhitespaceSignificant( boolean bWhitespaceSignificant )
  {
    _internal.setWhitespaceSignificant( bWhitespaceSignificant );
  }

  public boolean isCommentsSignificant()
  {
    return _internal.isCommentsSignificant();
  }
  public void setCommentsSignificant( boolean bCommentsSignificant )
  {
    _internal.setCommentsSignificant( bCommentsSignificant );
  }

  final public Token getCurrentToken()
  {
    Stack<Token> tokens = _internal.getTokens();
    int count = tokens.size();
    if( count == 0 )
    {
      return new Token();
    }
    if( _state == count )
    {
      return _internal.getEofToken();
    }
    if( _state == -1 )
    {
      return new Token();
    }
    return tokens.get( _state );
  }

  public Token getTokenAt( int iTokenIndex )
  {
    int iTokenCount = getTokens().size();
    if( iTokenCount == 0 )
    {
      return null;
    }
    if( iTokenIndex >= iTokenCount )
    {
      return null;
    }
    if( _state == -1 )
    {
      return null;
    }
    return getTokens().get( iTokenIndex );
  }

  public int getLineNumber()
  {
    return getCurrentToken().getLine();
  }

  public int getLineOffset()
  {
    return getCurrentToken().getLineOffset();
  }

  public int getTokenColumn()
  {
    return getCurrentToken().getTokenColumn();
  }

  public void wordChars( int iLow, int iHigh )
  {
    _internal.wordChars( iLow, iHigh );
  }

  public void whitespaceChars( int iLow, int iHigh )
  {
    _internal.whitespaceChars( iLow, iHigh );
  }

  public void ordinaryChars( int iLow, int iHigh )
  {
    _internal.ordinaryChars( iLow, iHigh );
  }

  public void ordinaryChar( int ch )
  {
    _internal.ordinaryChar( ch );
  }

  public void operators( String[] astrOperators )
  {
    _internal.operators( astrOperators );
  }

  public void operatorChars( int iLow, int iHigh )
  {
    _internal.operatorChars( iLow, iHigh );
  }

  public boolean isOperator( String strOperator )
  {
    return _internal.isOperator( strOperator );
  }

  public void commentChar( int ch )
  {
    _internal.commentChar( ch );
  }

  public void quoteChar( int ch )
  {
    _internal.quoteChar( ch );
  }

  public void parseNumbers()
  {
    _internal.parseNumbers();
  }

  public void eolIsSignificant( boolean bFlag )
  {
    _internal.eolIsSignificant( bFlag );
  }

  public void lowerCaseMode( boolean bLowerCaseMode )
  {
    _internal.lowerCaseMode( bLowerCaseMode );
  }

  public boolean isUnterminatedString()
  {
    return _internal.isUnterminatedString();
  }

  public boolean isUnterminatedComment()
  {
    return _internal.isUnterminatedComment();
  }

  public void setParseDotsAsOperators( boolean parseDotsAsOperators )
  {
    _internal.setParseDotsAsOperators( parseDotsAsOperators );
  }
  public boolean isParseDotsAsOperators()
  {
    return _internal.isParseDotsAsOperators();
  }

  public DocCommentBlock popLastComment()
  {
    Stack<Token> tokens = _internal.getTokens();
    for( int i = _state, j = 0; i >= 0 && j < 5; i--, j++ )
    {
      Token token = tokens.get( i );
      DocCommentBlock turd = token.getTurd();
      if( turd != null )
      {
        return turd;
      }
    }
    return null;
  }

  public int getTokenStart()
  {
    return getCurrentToken().getTokenStart();
  }

  public int getTokenEnd()
  {
    return getCurrentToken().getTokenEnd();
  }

  public String getTokenAsString()
  {
    String ret;
    Token token = getCurrentToken();
    switch( token.getType() )
    {
      case TT_WORD:
      case TT_KEYWORD:
      case TT_NUMBER:
      case TT_INTEGER:
        ret = token.getText();
        break;

      case TT_EOF:
        ret = "EOF";
        break;

      case TT_EOL:
        ret = "EOL";
        break;

      case TT_NOTHING:
        ret = "NOTHING";
        break;

      default:
      {
        if( token.getType() > 0 )
        {
          char s[] = new char[3];
          s[0] = s[2] = '\'';
          s[1] = (char)token.getType();
          ret = new String( s );
        }
        else
        {
          ret = "'" + token.getText() + "'";
        }
        break;
      }
    }

    return ret;
  }

  public int mark()
  {
    return _state;
  }

  public void restoreToMark( int iMarkedOffset )
  {
    _state = iMarkedOffset;
    _internal.rip();
  }

  public int getRestoreState()
  {
    return _state;
  }

  public Stack<Token> getTokens()
  {
    return _internal.getTokens();
  }

  public void nextToken() {
    int iType = nextTokenImpl();
    while( (!isCommentsSignificant() && iType == TT_COMMENT) ||
           (!isWhitespaceSignificant() && iType == TT_WHITESPACE) )
    {
      iType = nextTokenImpl();
    }
  }

  private int nextTokenImpl()
  {
    if( _state < 0 )
    {
      _internal.rip();
    }

    if( _state == getTokens().size() )
    {
      return TT_EOF;
    }

    _state++;

    if( _state == getTokens().size() )
    {
      return TT_EOF;
    }

    return getTokens().get( _state ).getType();
  }

  public String getStringValue()
  {
    return getCurrentToken().getStringValue();
  }

  @Override
  public String toString()
  {
    return "Token[" + getTokenAsString() + "], line " + getLineNumber();
  }

  public ITokenizerOffsetMarker getOffsetMarker()
  {
    if( _offsetMarkers.isEmpty() )
    {
      return null;
    }
    return _offsetMarkers.peek();
  }
  public void pushOffsetMarker( ITokenizerOffsetMarker offsetMarker )
  {
    _offsetMarkers.push( offsetMarker );
  }
  public void popOffsetMarker( ITokenizerOffsetMarker offsetMarker )
  {
    if( _offsetMarkers.pop() != offsetMarker )
    {
      throw new IllegalStateException( "Unbalanced push/pop OffsetMarker" );
    }
  }

  public int getState()
  {
    return _state;
  }

  public int countMatches( String s )
  {
    int matches = 0;
    nextToken();
    Token token = getCurrentToken();
    while( token.getType() != SourceCodeTokenizer.TT_EOF )
    {
      switch( token.getType() )
      {
        case TT_EOF:
          break;
        case TT_EOL:
          matches += s.equals( "\n" ) ? 1 : 0;
          break;
        case TT_WORD:
        case TT_KEYWORD:
          matches += s.equals( token.getStringValue() ) ? 1 : 0;
          break;

        case TT_INTEGER:
        case TT_NUMBER:
          matches += s.equals( token.getText() ) ? 1 : 0;
          break;

        case TT_NOTHING:
          break;

        default:
        {
          if( token instanceof StringToken )
          {
            matches += s.equals( token.getText() ) ? 1 : 0;
          }
          else if( token.getStringValue() != null )
          {
            matches += s.equals( token.getStringValue() ) ? 1 : 0;
          }
          else
          {
            matches += s.equals( String.valueOf( (char)token.getType() )) ? 1 : 0;
          }
          break;
        }
      }
      nextToken();
      token = getCurrentToken();
    }
    reset();
    return matches;
  }

  public int countMatches( String s, int tokenType )
  {
    int matches = 0;
    nextToken();
    Token token = getCurrentToken();
    while( token.getType() != SourceCodeTokenizer.TT_EOF )
    {
      if( token.getType() == tokenType )
      {
        matches += s.equals( token.getStringValue() ) ? 1 : 0;
      }
      nextToken();
      token = getCurrentToken();
    }
    reset();
    return matches;
  }

  public int getType()
  {
    return isEOF()
           ? TT_EOF
           : isNOTHING()
             ? TT_NOTHING
             : getCurrentToken().getType();
  }

  public boolean isEOF()
  {
    return _state == getTokens().size();
  }
  public boolean isNOTHING()
  {
    return _state < 0;
  }

  void goToPosition( int iOffset ) throws IOException
  {
    if( getTokenStart() > iOffset )
    {
      reset();
      goToPosition( iOffset );
    }
    else
    {
      while( getTokenStart() < iOffset )
      {
        popLastComment();
        nextToken();
        if( isEOF() || isNOTHING() )
        {
          throw new IOException( "Unexpected EOF" );
        }
      }
    }
  }

  public void resetButKeepTokens()
  {
    _state = -1;
  }

  @Override
  public boolean isAnalyzingSeparately()
  {
    if( _internal.getTokens().size() < 2 || _state < 1 )
    {
      return false;
    }

    return getPriorToken( false ).isAnalyzingSeparately();
  }

  @Override
  public boolean isAnalyzingDirective()
  {
    if( _internal.getTokens().size() < 2 || _state < 1 )
    {
      return false;
    }

    return getPriorToken( false ).isAnalyzingDirective();
  }

  @Override
  public boolean isSupportsKeywords() {
    return _internal.isSupportsKeywords();
  }
  @Override
  public void setSupportsKeywords(boolean supportsKeywords) {
    _internal.setSupportsKeywords( supportsKeywords );
  }

  public Token getPriorToken()
  {
    return getPriorToken( true );
  }
  public Token getPriorToken( boolean bSkipWhitespace )
  {
    return getPriorToken( bSkipWhitespace, bSkipWhitespace );
  }
  public Token getPriorToken( boolean bSkipWhitespace, boolean bSkipComments )
  {
    if( getTokens().size() == 0 )
    {
      return new Token();
    }
    for( int i = _state-1; i >= 0; i-- )
    {
      Token t = getTokens().get( i );
      if( (!bSkipWhitespace || t.getType() != TT_WHITESPACE) &&
          (!bSkipComments || t.getType() != TT_COMMENT))
      {
        return t;
      }
    }
    return getTokens().get( 0 );
  }

  public int lookaheadType( int iTokens, boolean bSkipSpaces )
  {
    int iPos = _state;

    if( bSkipSpaces )
    {
      int i = 0;
      while( i < iTokens && iPos != -1 )
      {
        iPos = skipSpaces( iPos );
        i++;
      }
      iPos = iPos == -1 ?  getTokens().size() : iPos;
    }
    else
    {
      iPos = _state + iTokens;
    }
    if( iPos < getTokens().size() )
    {
      return getTokens().get( iPos ).getType();
    }

    return TT_EOF;
  }

  private int skipSpaces(int iPos)
  {
    int type = TT_WHITESPACE;
    iPos++;
    while( iPos < getTokens().size() && type == TT_WHITESPACE )
    {
      type = getTokens().get( iPos ).getType();
      iPos++;
    }
    return type != TT_WHITESPACE ?  iPos-1 : -1;
  }
}
