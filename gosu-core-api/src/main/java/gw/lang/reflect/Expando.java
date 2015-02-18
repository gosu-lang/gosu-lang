/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.function.IBlock;
import gw.util.GosuEscapeUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class Expando implements IExpando {
  private Map<String, Object> _map = new LinkedHashMap<String, Object>();

  @Override
  public Object getFieldValue( String field ) {
    return _map.get( field );
  }

  @Override
  public void setFieldValue( String field, Object value ) {
    _map.put( field, value );
  }

  @Override
  public Object invoke( String methodName, Object... args ) {
    Object f = _map.get( methodName );
    if( f instanceof IBlock ) {
      return ((IBlock)f).invokeWithArgs( args );
    }
    return IPlaceholder.UNHANDLED;
  }

  @Override
  public void setDefaultFieldValue( String name ) {
    setFieldValue( name, new Expando() );
  }

  @Override
  public Map getMap() {
    return _map;
  }

  public String toGosu() {
    StringBuilder sb = new StringBuilder();
    toGosu( true, sb, 0 );
    return sb.toString();
  }
  public void toGosu( boolean bWithDynamic, StringBuilder sb, int indent ) {
    int iKey = 0;
    indent( sb, indent );
    sb.append( "new" ).append( bWithDynamic ? " dynamic.Dynamic()" : "()" );
    if( _map.size() > 0 ) {
      sb.append( " {\n" );
      for( String key : _map.keySet() ) {
        indent( sb, indent + 2 );
        sb.append( ":" ).append( key ).append( " = " );
        Object value = _map.get( key );
        if( value instanceof Expando ) {
          ((Expando)value).toGosu( false, sb, indent+2 );
        }
        else if( value instanceof List )
        {
          int length = ((List)value).size();
          sb.append( '{' );
          if( length > 0 ) {
            sb.append( "\n" );
            int iSize = ((List)value).size();
            for( int i = 0; i < iSize; i++ ) {
              Object comp = ((List)value).get( i );
              if( comp instanceof Expando ) {
                ((Expando)comp).toGosu( false, sb, indent+4 );
              }
              else {
                indent( sb, indent+4 );
                appendGosuValue( sb, comp );
              }
              appendCommaNewLine( sb, i < iSize - 1 );
            }
          }
          indent( sb, indent + 2 );
          sb.append( "}" );
        }
        else {
          appendGosuValue( sb, value );
        }
        appendCommaNewLine( sb, iKey++ < _map.size() - 1 );
      }
    }
    indent( sb, indent );
    sb.append( "}" );

  }

  private void appendCommaNewLine( StringBuilder sb, boolean bComma ) {
    if( bComma ) {
      sb.append( ',' );
    }
    sb.append( "\n" );
  }

  private StringBuilder appendGosuValue( StringBuilder sb, Object comp ) {
    if( comp instanceof String ) {
      sb.append( '"' );
      sb.append( GosuEscapeUtil.escapeForGosuStringLiteral( (String)comp ) );
      sb.append( '"' );
    }
    else if( comp instanceof Integer ||
             comp instanceof Long ||
             comp instanceof Double ||
             comp instanceof Float ||
             comp instanceof Short ||
             comp instanceof Character ||
             comp instanceof Byte ) {
      sb.append( comp );
    }
    else if( comp == null ) {
      sb.append( "null" );
    }
    else {
      throw new IllegalStateException( "Unsupported expando type: " + comp.getClass() );
    }
    return sb;
  }

  public String toXml( String name ) {
    StringBuilder sb = new StringBuilder();
    toXml( name, sb, 0 );
    return sb.toString();
  }
  public void toXml( String name, StringBuilder sb, int indent ) {
    indent( sb, indent );
    sb.append( '<' ).append( name );
    if( _map.size() > 0 ) {
      sb.append( ">\n" );
      for( String key : _map.keySet() ) {
        Object value = _map.get( key );
        if( value instanceof Expando ) {
          ((Expando)value).toXml( key, sb, indent+2 );
        }
        else if( value instanceof List )
        {
          int length = ((List)value).size();
          indent( sb, indent+2 );
          sb.append( "<" ).append( key );
          if( length > 0 ) {
            sb.append( ">\n" );
            for( Object comp : ((List)value) ) {
              if( comp instanceof Expando ) {
                ((Expando)comp).toXml( "li", sb, indent+4 );
              }
              else {
                indent( sb, indent+4 );
                sb.append( "<li>" ).append( comp ).append( "</li>\n" );
              }
            }
            indent( sb, indent+2 );
            sb.append( "</" ).append( key ).append( ">\n" );
          }
          else {
            sb.append( "/>\n");
          }
        }
        else {
          indent( sb, indent+2 );
          sb.append( '<' ).append( key ).append( ">" );
          sb.append( value );
          sb.append( "</" ).append( key ).append( ">\n" );
        }
      }
      indent( sb, indent );
      sb.append( "</").append( name ).append( ">\n" );
    }
    else {
      sb.append( "/>\n" );
    }
  }

  private void indent( StringBuilder sb, int indent ) {
    for( int i = 0; i < indent; i++ ) {
      sb.append( ' ' );
    }
  }

  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( !(o instanceof Expando) ) {
      return false;
    }

    Expando expando = (Expando)o;

    if( !_map.equals( expando._map ) ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return _map.hashCode();
  }

  @Override
  public String toString() {
    return "Expando{" +
           "_map=" + _map +
           '}';
  }
}
