/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http.fragment;

import gw.internal.xml.ws.http.HttpException;
import gw.internal.xml.ws.http.HttpParseContext;

public class HttpToken extends HttpFragment {

  private final String _text;

  public HttpToken( String text ) {
    _text = text;
  }

  public HttpToken( HttpParseContext context ) {
    StringBuilder sb = new StringBuilder();
    while ( true ) {
      Byte ch = context.get();
      if ( ch == null || isCtl( ch ) || isSeparator( ch ) ) {
        _text = sb.toString();
        if ( _text.length() == 0 ) {
          throw new HttpException( "Expected length of 'token' to be greater than zero" );
        }
        return;
      }
      //noinspection RedundantCast
      sb.append( (char) (byte) ch );
      context.next();
    }
  }

  public String getText() {
    return _text;
  }

  @Override
  public String toString() {
    return _text;
  }

}
