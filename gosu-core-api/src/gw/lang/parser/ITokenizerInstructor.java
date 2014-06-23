/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface ITokenizerInstructor
{
  public void getInstructionFor( int c );

  public void reset();

  public ITokenizerInstructor createNewInstance(ISourceCodeTokenizer tokenizer);

  void setTokenizer( ISourceCodeTokenizer tokenizer );

  boolean isAtIgnoredPos();

  boolean isAnalyzingSeparately();

  boolean isAnalyzingDirective();
}
