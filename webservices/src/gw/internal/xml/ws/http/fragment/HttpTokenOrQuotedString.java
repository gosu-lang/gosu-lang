/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http.fragment;

import gw.internal.xml.ws.http.HttpParseContext;

public class HttpTokenOrQuotedString extends HttpFragment {

  private final String _text;

  public HttpTokenOrQuotedString( String text ) {
    _text = text;
  }

  public HttpTokenOrQuotedString( HttpParseContext context ) {
    // reuse existing parsers
    if ( context.get() == '"' ) {
      // quoted string
      _text = new HttpQuotedString( context ).getText();
    }
    else {
      // token
      _text = new HttpToken( context ).getText();
    }
  }

  public String getText() {
    return _text;
  }

}
