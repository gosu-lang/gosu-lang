/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.http.fragment;

import gw.internal.xml.ws.http.HttpParseContext;

import java.util.ArrayList;
import java.util.List;

public class HttpMediaType extends HttpFragment {

  private final HttpToken _type;
  private final HttpToken _subtype;
  private final List<HttpParameter> _parameters;

  public HttpMediaType( HttpToken type, HttpToken subtype, List<HttpParameter> parameters ) {
    _type = type;
    _subtype = subtype;
    _parameters = new ArrayList<HttpParameter>( parameters );
  }

  public HttpMediaType( HttpParseContext context ) {
    _type = new HttpToken( context );
    consumeChar( context, (byte) '/' );
    _subtype = new HttpToken( context );
    consumeOptionalWhitespace( context );
    _parameters = new ArrayList<HttpParameter>();
    while ( consumeOptionalChar( context, (byte) ';' ) ) {
      consumeOptionalWhitespace( context );
      _parameters.add( new HttpParameter( context ) );
      consumeOptionalWhitespace( context );
    }
  }

  public HttpToken getType() {
    return _type;
  }

  public HttpToken getSubtype() {
    return _subtype;
  }

  public List<HttpParameter> getParameters() {
    return _parameters;
  }

  public String getMediaType() {
    return _type.getText() + "/" + _subtype.getText();
  }

  public String getFirstParameter( String key ) {
    String value = null;
    for ( HttpParameter httpParameter : getParameters() ) {
      if ( httpParameter.getAttribute().getText().equals( key ) ) {
        value = httpParameter.getValue().getText();
        break;
      }
    }
    return value;
  }

}
