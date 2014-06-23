/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

public abstract class TokenizerCallback implements Runnable {
  
  int _position;

  public TokenizerCallback(int position) {
    _position = position;
  }
  
  public int getPosition() {
    return _position;
  }
  
  
  
}
