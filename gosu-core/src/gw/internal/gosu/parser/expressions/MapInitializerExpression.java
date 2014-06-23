/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import gw.lang.parser.expressions.IMapInitializerExpression;
import gw.lang.parser.IExpression;

/**
 * @author cgross
 */
public class MapInitializerExpression extends Expression implements IMapInitializerExpression
{
  private ArrayList<IExpression> _keys;
  private ArrayList<IExpression> _values;

  public Object evaluate()
  {
    return null; //do nothing
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "{" );
    if( _keys != null )
    {
      for( int i = 0; i < _keys.size(); i++ )
      {
        IExpression key = _keys.get( i );
        sb.append( key.toString() );
        sb.append( "->" );
        IExpression value = _values.get( i );
        sb.append( value.toString() );
        if( i < _keys.size() - 1 )
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
    if( _keys != null )
    {
      Map map = (Map)newObject;
      for( int i = 0; i < _keys.size(); i++ )
      {
        IExpression keyExpr = _keys.get( i );
        Object key = keyExpr.evaluate();
        IExpression valueExpr = _values.get( i );
        Object value = valueExpr.evaluate();
        map.put( key, value );
      }
    }
  }

  public void add( Expression key, Expression value )
  {
    if( _keys == null )
    {
      _keys = new ArrayList<IExpression>();
      _values = new ArrayList<IExpression>();
    }
    _keys.add( key );
    _values.add( value );
  }

  public void addFirst( Expression key, Expression value )
  {
    if( _keys == null )
    {
      _keys = new ArrayList<IExpression>();
      _values = new ArrayList<IExpression>();
    }
    _keys.add(0, key );
    _values.add(0, value );
  }

  public List<IExpression> getKeys()
  {
    return _keys;
  }

  public List<IExpression> getValues()
  {
    return _values;
  }
}