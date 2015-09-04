/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

/**
 */
public abstract class IToken
{
  public abstract int getType();
  public abstract int getTokenStart();
  public abstract int getTokenEnd();
  public abstract int getTokenColumn();
  public abstract String getStringValue();
  public abstract boolean isValueKeyword();
  public abstract Keyword getKeyword();
  public abstract int getInvalidCharPos();
  public abstract String getText();

  public abstract int getLine();
  public abstract int getLineOffset();

  public abstract boolean isAnalyzingSeparately();
  public abstract boolean isAnalyzingDirective();

  public abstract void setAfter( IParseTree after );

  public abstract IParseTree getAfter();

  public abstract IToken copy();
  public abstract <E extends IToken> E copyInto( E t );

  public abstract void collapse();
}
