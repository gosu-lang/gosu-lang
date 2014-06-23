/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http.fragment;

import gw.internal.xml.ws.http.HttpParseContext;
import gw.util.Pair;
import gw.xml.ws.WebServiceException;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HttpMultipartRelatedContent extends HttpFragment {

  private final List<HttpHeadersAndBody> _parts = new ArrayList<HttpHeadersAndBody>();
  private final String _start;

  public HttpMultipartRelatedContent( HttpParseContext context, HttpMediaType contentType ) throws EOFException {
    String boundary = contentType.getFirstParameter( "boundary" );
    if ( boundary == null ) {
      throw new WebServiceException( "Boundary not specified." );
    }
    byte[] boundaryBytes;
    try {
      boundaryBytes = boundary.getBytes( "US-ASCII" );
    }
    catch ( UnsupportedEncodingException e ) {
      throw new RuntimeException( e ); // shouldn't happen with US-ASCII
    }
    _start = contentType.getFirstParameter( "start" );
    ByteArrayOutputStream currentHttpHeadersAndBody = null;
    while ( true ) {
      if ( context.get() == null ) {
        throw new EOFException( "Unexpected end of file" );
      }
      if ( boundaryStart( context, currentHttpHeadersAndBody == null ) ) {
        boolean boundaryFound = true;
        for ( byte boundaryByte : boundaryBytes ) {
          if ( context.get() == null || context.get() != boundaryByte ) {
            boundaryFound = false;
            break;
          }
          context.next();
        }
        if ( boundaryFound ) {
          if ( currentHttpHeadersAndBody != null ) {
            byte[] currentHttpHeadersAndBodyBytes = currentHttpHeadersAndBody.toByteArray();
            HttpHeadersAndBody message = new HttpHeadersAndBody( new HttpParseContext( currentHttpHeadersAndBodyBytes ) );
            _parts.add( message );
          }
          if ( consumeOptionalChar( context, (byte) '-' ) ) {
            consumeChar( context, (byte) '-' );
            break; // final boundary found
          }
          consumeChar( context, (byte) 13 );
          consumeChar( context, (byte) 10 );
          context.mark();
          currentHttpHeadersAndBody = new ByteArrayOutputStream();
          continue;
        }
        context.reset();
      }
      currentHttpHeadersAndBody.write( context.get() );
      context.next();
    }

  }

  private boolean boundaryStart( HttpParseContext context, boolean firstBoundary ) {
    if ( context.get() == 13 || context.get() == '-' ) {
      // possible boundary start
      context.mark();
      if ( context.get() == 13 ) {
        context.next();
        if ( context.get() == 10 ) {
          context.next();
        }
        else {
          context.reset();
          return false;
        }
      }
      else if ( ! firstBoundary ) {
        context.reset(); // CRLF is required before boundary, unless first boundary
        return false;
      }
      if ( context.get() == '-' ) {
        context.next();
        if ( context.get() == '-' ) {
          context.next();
          return true;
        }
      }
      context.reset();
    }
    return false;
  }

  public HttpHeadersAndBody getRootPart() {
    if ( _start == null ) {
      return _parts.get( 0 ); // TODO - check the spec to make sure this is right
    }
    for ( HttpHeadersAndBody part : _parts ) {
      for ( Pair<HttpToken, HttpFieldValue> header : part.getHttpHeaders()){
        if ( header.getFirst().getText().equalsIgnoreCase( "content-id" ) ) {
          if ( header.getSecond().getText().equals( _start ) ) {
            return part;
          }
        }
      }
    }
    throw new WebServiceException( "Root part not found with content-id '" + _start + "'" );
  }

  public List<HttpHeadersAndBody> getParts() {
    return _parts;
  }

}
