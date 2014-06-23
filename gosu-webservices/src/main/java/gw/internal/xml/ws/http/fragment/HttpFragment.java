/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http.fragment;

import gw.internal.xml.ws.http.HttpException;
import gw.internal.xml.ws.http.HttpParseContext;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class HttpFragment {

  private static final Set<Byte> _separators = makeCharacterSet( "()<>@,;:\\\"/[]?={} \t" );

  protected void consumeOptionalWhitespace( HttpParseContext context ) {
    Byte ch = context.get();
    if ( ch != null ) {
      // first, consume optional leading CRLF
      if ( ch == 13 ) {
        context.next();
        consumeChar( context, (byte) 10 );
      }
      while ( true ) {
        ch = context.get();
        if ( ch == null || ( ch != '\t' &&  ch != ' ' ) ) {
          break;
        }
        context.next();
      }
    }
  }

  protected void consumeChar( HttpParseContext context, byte c ) {
    if ( context.get() != c ) {
      throw new HttpException( "Expected " + c + " but found " + context.get() );
    }
    context.next();
  }

  protected boolean consumeOptionalChar( HttpParseContext context, byte c ) {
    Byte ch = context.get();
    if ( ch == null || ch != c ) { // ch != c would throw an NPE if ch is null due to unboxing, which is why the null check is needed separately
      return false;
    }
    context.next();
    return true;
  }

  protected boolean isSeparator( byte ch ) {
    return _separators.contains( ch );
  }

  protected boolean isCtl( byte ch ) {
    return ch < 32 || ch == 127;
  }

  private static Set<Byte> makeCharacterSet( String chars ) {
    byte[] bytes;
    try {
      bytes = chars.getBytes( "US-ASCII" );
    }
    catch ( UnsupportedEncodingException ex ) {
      throw new RuntimeException( ex ); // should never happen with US-ASCII
    }
    Set<Byte> ret = new HashSet<Byte>();
    for ( byte b : bytes ) {
      ret.add( b );
    }
    return Collections.unmodifiableSet( ret );
  }

}
