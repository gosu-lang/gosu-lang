/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;

import gw.util.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.expressions.ICollectionInitializerExpression;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class CollectionInitializerExpression extends Expression implements ICollectionInitializerExpression
{
  private ArrayList<IExpression> _values;

  public boolean isCompileTimeConstant()
  {
    for( IExpression expr : getValues() )
    {
      if( !expr.isCompileTimeConstant() )
      {
        return false;
      }
    }
    return true;
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    // Convert to an array for compile-time constant e.g., {a,b,c} is legal array expr for Annotation args
    Class<?> arrayClass = getArrayClass( getType() );
    List<IExpression> values = getValues();
    Object instance = Array.newInstance( arrayClass.getComponentType(), values.size() );
    for( int i = 0; i < Array.getLength( instance ); i++ )
    {
      IExpression expr = values.get( i );
      Object value = expr.evaluate();
      if( value instanceof BigDecimal || value instanceof BigInteger )
      {
        value = value.toString();
      }
      Array.set( instance, i, value );
    }
    return instance;
  }

  public static Class<?> getArrayClass( IType type )
  {
    if( JavaTypes.COLLECTION().isAssignableFrom( type ) )
    {
      return Array.newInstance(getArrayClass(type.getTypeParameters()[0]), 0).getClass();
    }
    if( StandardCoercionManager.isBoxed( type ) )
    {
      type = TypeSystem.getPrimitiveType( type );
    }
    else if( type == JavaTypes.BIG_DECIMAL() || type == JavaTypes.BIG_INTEGER())
    {
      type = JavaTypes.STRING();
    }
    else if( type.isEnum() )
    {
      // An enum evaluates as the name of the enum constant field (for compile-time constant evaluation)
      type = JavaTypes.STRING();
    }
    if( !type.isPrimitive() && type != JavaTypes.STRING() )
    {
      throw new IllegalStateException( "A compile-time constant expression must be either primitive, String, or Enum" );
    }
    return ((IJavaType)type).getBackingClass();
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "{" );
    if( _values != null )
    {
      for( int i = 0; i < _values.size(); i++ )
      {
        IExpression expression = _values.get( i );
        sb.append( expression.toString() );
        if( i < _values.size() - 1 )
        {
          sb.append( ", " );
        }
      }
    }
    sb.append( "}" );
    return sb.toString();
  }

  public void initialize( Object newObject )
  {
    if( _values != null )
    {
      Collection collection = (Collection)newObject;
      for( int i = 0; i < _values.size(); i++ )
      {
        IExpression expression = _values.get( i );
        Object val = expression.evaluate();
        collection.add( val );
      }
    }
  }

  public void add( Expression expression )
  {
    if( _values == null )
    {
      _values = new ArrayList<IExpression>();
    }
    _values.add( expression );
  }

  public void addFirst( Expression expression )
  {
    if( _values == null )
    {
      _values = new ArrayList<IExpression>();
    }
    _values.add( 0, expression );
  }

  public List<IExpression> getValues()
  {
    return _values == null ? Collections.<IExpression>emptyList() : _values;
  }
}