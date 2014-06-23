/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.ITypeVariableDefinitionExpression;
import gw.lang.parser.expressions.ITypeVariableListClause;

import java.util.List;


/**
 */
public class TypeVariableListClause extends Expression implements ITypeVariableListClause
{
  private List<ITypeVariableDefinitionExpression> _vars;

  public TypeVariableListClause( List<ITypeVariableDefinitionExpression> vars )
  {
    _vars = vars;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder( "<" );
    for( ITypeVariableDefinitionExpression def : _vars )
    {
      if( sb.length() > 1 )
      {
        sb.append( ", " );
      }
      sb.append( def.toString() );
    }
    return sb.toString();
  }

  @Override
  public List<ITypeVariableDefinitionExpression> getTypeVariabledDefinitions()
  {
    return _vars;
  }

}
