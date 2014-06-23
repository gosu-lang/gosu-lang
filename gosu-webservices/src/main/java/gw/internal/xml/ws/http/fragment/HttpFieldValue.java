/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http.fragment;

import gw.internal.xml.ws.http.HttpParseContext;

public class HttpFieldValue extends HttpFragment {

  private final String _text;

  public HttpFieldValue( HttpParseContext context ) {
    StringBuilder sb = new StringBuilder();
    while ( true ) {
      byte b = context.get();
      if ( b == 13 ) {
        // check for [CRLF]-prefixed LWS
        context.mark();
        context.next();
        if ( context.get() == 10 ) {
          context.next();
          b = context.get();
          if ( b == ' ' || b == '\t' ) {
            // LWS within field value
            sb.append( ' ' );
          }
          else {
            // end of field value
            break;
          }
          continue;
        }
        else {
          context.reset();
        }
      }
      //noinspection RedundantCast
      sb.append( (char) b );
      context.next();
    }
    _text = sb.toString();
  }

  public String getText() {
    return _text;
  }

  @Override
  public String toString() {
    return _text;
  }

}
