/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.function.IBlock;
import gw.lang.parser.Keyword;
import gw.util.GosuEscapeUtil;

import javax.script.SimpleBindings;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 */
public class Expando extends SimpleBindings implements IExpando
{
  private static final Map<String, String> ALT_MAP = new HashMap<>();
  private static final Map<String, String> ALT_MAP_REV = new HashMap<>();

  static
  {
    // Capitalize/Uncapitalize reserved keywords to avoid parse errors.
    // Internally we perserve the case of the keys, but in structure types
    // we expose them as alternate versions of the reserved words.

    for( String kw : Keyword.getAll() )
    {
      if( Keyword.isReservedKeyword( kw ) )
      {
        String altKw;
        char c = kw.charAt( 0 );
        if( Character.isLowerCase( c ) )
        {
          altKw = Character.toUpperCase( c ) + kw.substring( 1 );
        }
        else
        {
          altKw = Character.toLowerCase( c ) + kw.substring( 1 );
        }
        ALT_MAP.put( altKw, kw );
        ALT_MAP_REV.put( kw, altKw );
      }
    }
  }


  public static String getAltKey( String key )
  {
    return ALT_MAP_REV.getOrDefault( key, key );
  }

  public Expando()
  {
    super( new LinkedHashMap<>() );
  }

  public Expando( Map<String, Object> map )
  {
    super( map );
  }

  @Override
  public Object getFieldValue( String field )
  {
    return get( field );
  }
  @Override
  public Object get( Object field )
  {
    field = ALT_MAP.getOrDefault( field, (String)field );
    return super.get( field );
  }

  @Override
  public void setFieldValue( String field, Object value )
  {
    put( field, value );
  }
  @Override
  public Object put( String field, Object value )
  {
    field = ALT_MAP.getOrDefault( field, field );
    return super.put( field, value );
  }

  @Override
  public Object invoke( String methodName, Object... args )
  {
    Object f = get( methodName );
    if( f instanceof IBlock )
    {
      return ((IBlock)f).invokeWithArgs( args );
    }
    return IPlaceholder.UNHANDLED;
  }

  @Override
  public void setDefaultFieldValue( String name )
  {
    setFieldValue( name, new Expando() );
  }

  @Override
  public boolean containsKey( Object field )
  {
    field = ALT_MAP.getOrDefault( field, (String)field );
    return super.containsKey( field );
  }

  @Override
  public Object remove( Object field )
  {
    field = ALT_MAP.getOrDefault( field, (String)field );
    return super.remove( field );
  }

  @Override
  public Set<String> keySet()
  {
    return super.keySet().stream().map( (k) -> ALT_MAP_REV.getOrDefault( k, k ) ).collect( Collectors.toSet() );
  }

  @Override
  public Set<Entry<String, Object>> entrySet()
  {
    return super.entrySet().stream().map(
      e -> e.getKey().equals( ALT_MAP_REV.getOrDefault( e.getKey(), e.getKey() ) )
           ? e
           : new AbstractMap.SimpleEntry<>( ALT_MAP_REV.get( e.getKey() ), e.getValue() ) ).collect( Collectors.toSet() );
  }

  public String toGosu()
  {
    StringBuilder sb = new StringBuilder();
    toGosu( true, sb, 0 );
    return sb.toString();
  }

  public void toGosu( boolean bWithDynamic, StringBuilder sb, int indent )
  {
    int iKey = 0;
    indent( sb, indent );
    sb.append( "new" ).append( bWithDynamic ? " dynamic.Dynamic()" : "()" );
    if( size() > 0 )
    {
      sb.append( " {\n" );
      for( String key : keySet() )
      {
        indent( sb, indent + 2 );
        sb.append( ":" ).append( key ).append( " = " );
        Object value = get( key );
        if( value instanceof Expando )
        {
          ((Expando)value).toGosu( false, sb, indent + 2 );
        }
        else if( value instanceof List )
        {
          handleGosuList( sb, indent, (List)value );
        }
        else
        {
          appendGosuValue( sb, value );
        }
        appendCommaNewLine( sb, iKey++ < size() - 1 );
      }
    }
    indent( sb, indent );
    sb.append( "}\n" );
  }

  private void handleGosuList( StringBuilder sb, int indent, List list )
  {
    int length = list.size();
    sb.append( '{' );
    if( length > 0 )
    {
      sb.append( "\n" );
      int iSize = list.size();
      for( int i = 0; i < iSize; i++ )
      {
        Object comp = list.get( i );
        if( comp instanceof Expando )
        {
          ((Expando)comp).toGosu( false, sb, indent + 4 );
        }
        else if( comp instanceof List )
        {
          handleGosuList( sb, indent + 4, (List)comp );
        }
        else
        {
          indent( sb, indent + 4 );
          appendGosuValue( sb, comp );
        }
        appendCommaNewLine( sb, i < iSize - 1 );
      }
    }
    indent( sb, indent + 2 );
    sb.append( "}" );
  }

  public String toJson()
  {
    StringBuilder sb = new StringBuilder();
    toJson( sb, 0 );
    return sb.toString();
  }

  public void toJson( StringBuilder sb, int indent )
  {
    int iKey = 0;
    indent( sb, indent );
    if( size() > 0 )
    {
      sb.append( " {\n" );
      for( String key : keySet() )
      {
        indent( sb, indent + 2 );
        sb.append( '"' ).append( key ).append( '"' ).append( ": " );
        Object value = get( key );
        if( value instanceof Expando )
        {
          ((Expando)value).toJson( sb, indent + 2 );
        }
        else if( value instanceof List )
        {
          handleJsonList( sb, indent, (List)value );
        }
        else
        {
          appendGosuValue( sb, value );
        }
        appendCommaNewLine( sb, iKey++ < size() - 1 );
      }
    }
    indent( sb, indent );
    sb.append( "}\n" );
  }

  private void handleJsonList( StringBuilder sb, int indent, List value )
  {
    int length = value.size();
    sb.append( '[' );
    if( length > 0 )
    {
      sb.append( "\n" );
      int iSize = value.size();
      for( int i = 0; i < iSize; i++ )
      {
        Object comp = value.get( i );
        if( comp instanceof Expando )
        {
          ((Expando)comp).toJson( sb, indent + 4 );
        }
        else if( comp instanceof List )
        {
          handleJsonList( sb, indent + 4, value );
        }
        else
        {
          indent( sb, indent + 4 );
          appendGosuValue( sb, comp );
        }
        appendCommaNewLine( sb, i < iSize - 1 );
      }
    }
    indent( sb, indent + 2 );
    sb.append( "]" );
  }

  private void appendCommaNewLine( StringBuilder sb, boolean bComma )
  {
    if( bComma )
    {
      sb.append( ',' );
    }
    sb.append( "\n" );
  }

  private StringBuilder appendGosuValue( StringBuilder sb, Object comp )
  {
    if( comp instanceof String )
    {
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
             comp instanceof Byte )
    {
      sb.append( comp );
    }
    else if( comp == null )
    {
      sb.append( "null" );
    }
    else
    {
      throw new IllegalStateException( "Unsupported expando type: " + comp.getClass() );
    }
    return sb;
  }

  private String getTypeFrom( Object comp )
  {
    String typeName;
    if( comp instanceof String )
    {
      typeName = "String";
    }
    else if( comp instanceof Integer ||
             comp instanceof Long ||
             comp instanceof Double ||
             comp instanceof Float ||
             comp instanceof Short ||
             comp instanceof Character ||
             comp instanceof Byte )
    {
      typeName = comp.getClass().getSimpleName();
    }
    else if( comp == null )
    {
      typeName = null;
    }
    else
    {
      throw new IllegalStateException( "Unsupported expando type: " + comp.getClass() );
    }
    return typeName;
  }

  public String toXml()
  {
    return toXml( "object" );
  }

  public String toXml( String name )
  {
    StringBuilder sb = new StringBuilder();
    toXml( name, sb, 0 );
    return sb.toString();
  }

  public void toXml( String name, StringBuilder sb, int indent )
  {
    indent( sb, indent );
    sb.append( '<' ).append( name );
    if( size() > 0 )
    {
      sb.append( ">\n" );
      for( String key : keySet() )
      {
        Object value = get( key );
        if( value instanceof Expando )
        {
          ((Expando)value).toXml( key, sb, indent + 2 );
        }
        else if( value instanceof List )
        {
          int length = ((List)value).size();
          indent( sb, indent + 2 );
          sb.append( "<" ).append( key );
          if( length > 0 )
          {
            sb.append( ">\n" );
            for( Object comp : ((List)value) )
            {
              if( comp instanceof Expando )
              {
                ((Expando)comp).toXml( "li", sb, indent + 4 );
              }
              else
              {
                indent( sb, indent + 4 );
                sb.append( "<li>" ).append( comp ).append( "</li>\n" );
              }
            }
            indent( sb, indent + 2 );
            sb.append( "</" ).append( key ).append( ">\n" );
          }
          else
          {
            sb.append( "/>\n" );
          }
        }
        else
        {
          indent( sb, indent + 2 );
          sb.append( '<' ).append( key ).append( ">" );
          sb.append( value );
          sb.append( "</" ).append( key ).append( ">\n" );
        }
      }
      indent( sb, indent );
      sb.append( "</" ).append( name ).append( ">\n" );
    }
    else
    {
      sb.append( "/>\n" );
    }
  }

  private void indent( StringBuilder sb, int indent )
  {
    for( int i = 0; i < indent; i++ )
    {
      sb.append( ' ' );
    }
  }

  @Override
  public boolean equals( Object o )
  {
    if( o == this )
    {
      return true;
    }

    if( !(o instanceof Map) )
    {
      return false;
    }
    Map<?, ?> m = (Map<?, ?>)o;
    if( m.size() != size() )
    {
      return false;
    }

    try
    {
      Iterator<Entry<String, Object>> i = entrySet().iterator();
      while( i.hasNext() )
      {
        Entry<String, Object> e = i.next();
        String key = e.getKey();
        Object value = e.getValue();
        if( value == null )
        {
          if( !(m.get( key ) == null && m.containsKey( key )) )
          {
            return false;
          }
        }
        else
        {
          if( !value.equals( m.get( key ) ) )
          {
            return false;
          }
        }
      }
    }
    catch( ClassCastException unused )
    {
      return false;
    }
    catch( NullPointerException unused )
    {
      return false;
    }

    return true;
  }


  @Override
  public String toString()
  {
    Iterator<Entry<String, Object>> i = entrySet().iterator();
    if( !i.hasNext() )
    {
      return "{}";
    }

    StringBuilder sb = new StringBuilder();
    sb.append( '{' );
    while( true )
    {
      Entry<String, Object> e = i.next();
      String key = e.getKey();
      Object value = e.getValue();
      sb.append( key );
      sb.append( " -> " );
      sb.append( value == this ? "(this Expando)" : value ).append( "\n" );
      if( !i.hasNext() )
      {
        return sb.append( '}' ).toString();
      }
    }
  }
}
