package gw.lang.reflect.gs.gen;

import gw.config.CommonServices;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuEscapeUtil;
import java.lang.reflect.Array;

/**
 */
public abstract class SrcElement
{
  public static final int INDENT = 2;
  private SrcAnnotated _owner;

  public SrcElement() {}
  public SrcElement( SrcAnnotated owner )
  {
    _owner = owner;
  }

  public abstract StringBuilder render( StringBuilder sb, int indent );

  public SrcAnnotated getOwner()
  {
    return _owner;
  }
  public void setOwner( SrcAnnotated owner )
  {
    _owner = owner;
  }

  public String indent( StringBuilder sb, int indent )
  {
    for( int i = 0; i < indent; i++ )
    {
      sb.append( ' ' );
    }
    return "";
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    render( sb, 0 );
    return sb.toString();
  }

  public static String makeCompileTimeConstantValue( IType type, Object value )
  {
    String result;

    if( value == null )
    {
      result = "null";
    }
    else if( value instanceof IType )
    {
      result = ((IType)value).getName();
    }
    else if( value instanceof String )
    {
      if( JavaTypes.STRING() == type )
      {
        result = "\"" + GosuEscapeUtil.escapeForJava( value.toString() ) + "\"";
      }
      else if( type.isEnum() )
      {
        result = type.getName() + '.' + value;
      }
      else if( type == JavaTypes.pCHAR() )
      {
        result = "'" + GosuEscapeUtil.escapeForJava( value.toString() ) + "'";
      }
      else
      {
        result = (String)value;
      }
    }
    else if( value instanceof Character )
    {
      result = "'" + GosuEscapeUtil.escapeForJava( value.toString() ) + "'";
    }
    else if( value.getClass().isArray() )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( "{" );
      int len = Array.getLength( value );
      for( int i = 0; i < len; i++ )
      {
        Object v = Array.get( value, i );
        sb.append( i > 0 ? ", " : "" ).append( makeCompileTimeConstantValue( type.getComponentType(), v ) );
      }
      sb.append( "}" );
      result = sb.toString();
    }
    else
    {
      result = value.toString();
    }
    return result;
  }

  public static String makeDefaultPrimitiveValue( IType type )
  {
    return makeCompileTimeConstantValue( type, CommonServices.getCoercionManager().convertNullAsPrimitive( type, false ) );
  }
}
