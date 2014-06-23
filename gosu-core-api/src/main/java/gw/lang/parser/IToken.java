/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

/**
 */
public interface IToken
{
  int getType();
  int getTokenStart();
  int getTokenEnd();
  int getTokenColumn();
  String getStringValue();
  int getInvalidCharPos();
  String getText();

  int getLine();
  int getLineOffset();

  boolean isAnalyzingSeparately();
  boolean isAnalyzingDirective();

  void setAfter( IParseTree after );

  IParseTree getAfter();

  IToken copy();
  <E extends IToken> E copyInto( E t );

  void collapse();
}
