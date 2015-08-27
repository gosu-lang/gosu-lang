/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public class StringSource implements ISource {
  private String _strSource;
  private ISourceCodeTokenizer _tokenizer;

  public StringSource( String strSource ) {
    _strSource = strSource;
  }

  public String getSource() {
    return _strSource;
  }

  @Override
  public void stopCachingSource() {
    // do nothing for now
  }

  @Override
  public ISourceCodeTokenizer getTokenizer() {
    return _tokenizer;
  }
  @Override
  public void setTokenizer( ISourceCodeTokenizer tokenizer ) {
    _tokenizer = tokenizer;
  }
}
