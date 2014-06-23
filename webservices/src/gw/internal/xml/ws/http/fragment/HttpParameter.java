/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http.fragment;

import gw.internal.xml.ws.http.HttpParseContext;

public class HttpParameter extends HttpFragment {

  private final HttpToken _attribute;
  private final HttpTokenOrQuotedString _value;

  public HttpParameter( HttpToken attribute, HttpTokenOrQuotedString value ) {
    _attribute = attribute;
    _value = value;
  }

  public HttpParameter( HttpParseContext context ) {
    _attribute = new HttpToken( context );
    consumeOptionalWhitespace( context );
    consumeChar( context, (byte) '=' );
    consumeOptionalWhitespace( context );
    _value = new HttpTokenOrQuotedString( context );
  }

  public HttpToken getAttribute() {
    return _attribute;
  }

  public HttpTokenOrQuotedString getValue() {
    return _value;
  }

}
