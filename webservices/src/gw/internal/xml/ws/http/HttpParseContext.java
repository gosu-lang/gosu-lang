/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http;

public class HttpParseContext {

  private byte[] _input;
  private int _position;
  private int _mark = 0;

  public HttpParseContext( byte[] input ) {
    _input = input;
  }

  public void next() {
    if ( _position < _input.length ) {
      _position++;
    }
  }

  public Byte get() {
    if ( _position >= _input.length ) {
      return null;
    }
    return _input[ _position ];
  }

  public int position() {
    return _position;
  }

  public void mark() {
    _mark = _position;
  }

  public void reset() {
    _position = _mark;
  }

}
