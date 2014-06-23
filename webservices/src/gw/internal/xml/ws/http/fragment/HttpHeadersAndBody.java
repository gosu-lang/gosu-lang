/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http.fragment;

import gw.internal.xml.ws.http.HttpParseContext;
import gw.util.Pair;
import gw.util.StreamUtil;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HttpHeadersAndBody extends HttpFragment {

  private final List<Pair<HttpToken,HttpFieldValue>> _httpHeaders = new ArrayList<Pair<HttpToken, HttpFieldValue>>();
  private final byte[] _body;

  public HttpHeadersAndBody( HttpParseContext context ) {
    while ( true ) {
      Byte b = context.get();
      if ( b == 13 ) {
        context.mark();
        context.next();
        b = context.get();
        context.next();
        if ( b == 10 ) {
          // \r\n
          break;
        }
        else {
          context.reset();
        }
      }
      HttpToken httpHeaderName = new HttpToken( context );
      consumeOptionalWhitespace( context );
      consumeChar( context, (byte) ':' );
      consumeOptionalWhitespace( context );
      HttpFieldValue httpHeaderValue = new HttpFieldValue( context );
      _httpHeaders.add( new Pair<HttpToken, HttpFieldValue>( httpHeaderName, httpHeaderValue ) );
    }
    // everything left is the body
    ByteArrayOutputStream body = new ByteArrayOutputStream();
    while ( true ) {
      Byte b = context.get();
      if ( b == null ) {
        break;
      }
      body.write( b );
      context.next();
    }
    _body = body.toByteArray();
  }

  public List<Pair<HttpToken, HttpFieldValue>> getHttpHeaders() {
    return _httpHeaders;
  }

  public byte[] getBody() {
    return _body;
  }

}
