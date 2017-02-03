/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.util.Stack;
import java.io.Reader;

public interface ISourceCodeTokenizer
{
  int TT_EOL = '\n';
  int TT_EOF = -1;
  int TT_WHITESPACE = -2;
  int TT_COMMENT = -3;
  int TT_NUMBER = -4;
  int TT_WORD = -5;
  int TT_OPERATOR = -6;
  int TT_KEYWORD = -7;
  int TT_NOTHING = -8;
  int TT_INTEGER = -9;

  IToken copy();

  IToken copyInto( IToken t );

  ISourceCodeTokenizer lightweightRestore();

  boolean isPositioned();

  void reset();

  void reset( Reader reader );

  void reset( SourceCodeReader reader );

  SourceCodeReader getReader();

  String getSource();

  ITokenizerInstructor getInstructor();

  void setInstructor( ITokenizerInstructor instructor );

  boolean isWhitespaceSignificant();

  void setWhitespaceSignificant( boolean bWhitespaceSignificant );

  boolean isCommentsSignificant();

  void setCommentsSignificant( boolean bCommentsSignificant );

  int getLineNumber();

  int getLineOffset();

  int getTokenColumn();

  public IToken getCurrentToken();

  IToken getTokenAt( int iTokenIndex );

  IToken getTokenAtPosition( int docPosition );

  void wordChars( int iLow, int iHigh );

  void whitespaceChars( int iLow, int iHigh );

  void ordinaryChars( int iLow, int iHigh );

  void ordinaryChar( int ch );

  void operators( String[] astrOperators );

  void operatorChars( int iLow, int iHigh );

  boolean isOperator( String strOperator );

  void commentChar( int ch );

  void quoteChar( int ch );

  void parseNumbers();

  void eolIsSignificant( boolean bFlag );

  void lowerCaseMode( boolean bLowerCaseMode );

  boolean isUnterminatedString();

  boolean isUnterminatedComment();

  void setParseDotsAsOperators( boolean parseDotsAsOperators );

  boolean isParseDotsAsOperators();

  int getTokenStart();

  int getTokenEnd();

  String getTokenAsString();

  int mark();

  void restoreToMark( int markedState );

  int getRestoreState();

  void nextToken();

  void pushOffsetMarker( ITokenizerOffsetMarker offsetMarker );
  void popOffsetMarker( ITokenizerOffsetMarker offsetMarker );

  int getState();

  int countMatches( String s );

  int countMatches( String s, int tokenType );

  int getType();

  String getStringValue();

  boolean isEOF();

  Stack<? extends IToken> getTokens();

  boolean isAnalyzingSeparately();
  boolean isAnalyzingDirective();

  boolean isSupportsKeywords();
  void setSupportsKeywords( boolean supportsKeywords );
}
