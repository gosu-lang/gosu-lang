/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IToken;

import java.io.Serializable;

/**
 * module-name [: exported]
 */
public class GosucDependency implements Serializable {
  private String _modName;
  private boolean _exported;

  public GosucDependency( String modName, boolean exported ) {
    _exported = exported;
    _modName = modName;
  }

  public String getModuleName() {
    return _modName;
  }

  public boolean isExported() {
    return _exported;
  }

  public String write() {
    StringBuilder out = new StringBuilder();
    out.append( _modName );
    if( _exported ) {
      out.append( ": exported" );
    }
    return out.toString();
  }

  public static GosucDependency parse( GosucProjectParser parser ) {
    IToken t = parser.getTokenizer().getCurrentToken();
    parser.verify( parser.match( null, ISourceCodeTokenizer.TT_WORD, false ), "Expecting module name for dependency" );
    String name = t.getStringValue();
    boolean exported = false;
    if( parser.matchOperator( ":", false ) ) {
      parser.verify( exported = parser.matchWord( "exported", false ), "Expecting 'exported' keyword" );
    }
    return new GosucDependency( name, exported );
  }

  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( o == null || getClass() != o.getClass() ) {
      return false;
    }

    GosucDependency that = (GosucDependency)o;

    if( _exported != that._exported ) {
      return false;
    }
    if( !_modName.equals( that._modName ) ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = _modName.hashCode();
    result = 31 * result + (_exported ? 1 : 0);
    return result;
  }
}
