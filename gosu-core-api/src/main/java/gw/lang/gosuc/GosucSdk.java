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
  private List<String> _backingSource;

  public GosucSdk( List<String> paths, List<String> backingSource ) {
    _paths = paths;
    _backingSource = backingSource;
  }

  public List<String> getPaths() {
    return _paths;
  }

  public List<String> getBackingSourcePath() {
    return _backingSource;
  }

  public String write() {
    StringBuilder sb = new StringBuilder()
    .append( "sdk {\n" );
    for( String path : getPaths() ) {
      sb.append( "  " ).append( "\"" ).append( path ).append( "\",\n" );
    }
    sb.append( "}\n" )
    .append( "backingsource {\n" );
    for( String path : getBackingSourcePath() ) {
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

    List<String> backingSource = new ArrayList<>();
    if( parser.matchWord( "backingsource", false ) )
    {
      parser.verify( parser.match( null, '{', false ), "Expecting '{' to begin backingsource path list" );
      for( IToken t = parser.getTokenizer().getCurrentToken(); parser.match( null, '"', false ); t = parser.getTokenizer().getCurrentToken() )
      {
        paths.add( t.getStringValue() );
        if( !parser.match( null, ',', false ) )
        {
          break;
        }
      }
      parser.verify( parser.match( null, '}', false ), "Expecting '}' to close sdk path list" );
    }
    return new GosucSdk( paths, backingSource );
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    GosucSdk gosucSdk = (GosucSdk)o;

    if( !_backingSource.equals( gosucSdk._backingSource ) )
    {
      return false;
    }
    return _paths.equals( gosucSdk._paths );
  }

  @Override
  public int hashCode()
  {
    int result = _paths.hashCode();
    result = 31 * result + _backingSource.hashCode();
    return result;
  }
}
