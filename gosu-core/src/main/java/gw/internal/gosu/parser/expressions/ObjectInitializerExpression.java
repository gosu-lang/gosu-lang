/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;

import java.util.ArrayList;
import java.util.List;

import gw.lang.parser.expressions.IObjectInitializerExpression;

/**
 * @author cgross
 */
public class ObjectInitializerExpression extends Expression implements IObjectInitializerExpression
{
  private List<InitializerAssignment> _assignments = new ArrayList<InitializerAssignment>();

  public Object evaluate()
  {
    return null; //do nothing
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "{" );
    if( _assignments != null )
    {
      for( int i = 0; i < _assignments.size(); i++ )
      {
        if( i != 0 )
        {
          sb.append( ", " );
        }
        InitializerAssignment entries = _assignments.get( i );
        sb.append( entries.toString() );
      }
    }
    sb.append( "}" );
    return sb.toString();
  }

  public void initialize( Object rootValue )
  {
    if( _assignments != null )
    {
      for( InitializerAssignment entries : _assignments )
      {
        entries.execute( rootValue );
      }
    }
  }

  public void add( InitializerAssignment expression )
  {
    _assignments.add( expression );
  }

  public List<InitializerAssignment> getInitializers()
  {
    return _assignments;
  }
}