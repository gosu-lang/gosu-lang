/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.lang.parser.IToken;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class GosucSdk {
  private List<String> _paths;

  public GosucSdk( List<String> paths ) {
    _paths = paths;
  }

  public List<String> getPaths() {
    return _paths;
  }

  public String write() {
    StringBuilder sb = new StringBuilder()
    .append( "sdk {\n" );
    for( String path : getPaths() ) {
      sb.append( "  " ).append( "\"" ).append( path ).append( "\",\n" );
    }
    sb.append( "}" );
    return sb.toString();
  }

  public static GosucSdk parse( GosucProjectParser parser ) {
    parser.verify( parser.matchWord( "sdk", false ), "Expecting 'sdk' keyword" );
    parser.verify( parser.match( null, '{', false ), "Expecting '{' to begin sdk path list" );
    List<String> paths = new ArrayList<String>();
    for( IToken t = parser.getTokenizer().getCurrentToken(); parser.match( null, '"', false ); t = parser.getTokenizer().getCurrentToken() ) {
      paths.add( t.getStringValue() );
      if( !parser.match( null, ',', false ) ) {
        break;
      }
    }
    parser.verify( parser.match( null, '}', false ), "Expecting '}' to close sdk path list" );
    return new GosucSdk( paths );
  }

  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( o == null || getClass() != o.getClass() ) {
      return false;
    }

    GosucSdk gosucSdk = (GosucSdk)o;

    return _paths.equals( gosucSdk._paths );
  }

  @Override
  public int hashCode() {
    return _paths.hashCode();
  }
}
