/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http.fragment;

import gw.internal.xml.ws.http.HttpParseContext;

public class HttpQuotedString extends HttpFragment {

  private final String _text;

  public HttpQuotedString( HttpParseContext context ) {
    consumeChar( context, (byte) '"' );
    StringBuilder sb = new StringBuilder();
    WHILE:
    while ( true ) {
      Byte ch = context.get();
      if ( ch == null ) {
        throw new IllegalArgumentException( "Unterminated quoted string" );
      }
      switch ( ch ) {
        case '\\': {
          // quoted-pair
          context.next();
          ch = context.get();
          if ( ch == null ) {
            throw new IllegalArgumentException( "Unterminated quoted character" );
          }
          // TODO dlank - CHAR following backslash should be in the range 0..127 - see RFC 2616
          //noinspection RedundantCast
          sb.append( (char) (byte) ch );
          break;
        }
        case '"': {
          break WHILE;
        }
        default: {
          //noinspection RedundantCast
          sb.append( (char) (byte) ch );
          break;
        }
      }
      context.next();
    }
    consumeChar( context, (byte) '"' );
    _text = sb.toString();
  }

  public String getText() {
    return _text;
  }

}
