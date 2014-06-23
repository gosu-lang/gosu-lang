/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.Keyword;
import gw.lang.parser.statements.ISuperTypeClause;
import gw.lang.reflect.IType;

public class SuperTypeClause extends Expression implements ISuperTypeClause
{
  private IType _superType;

  public SuperTypeClause( IType superType )
  {
    _superType = superType;
  }

  public IType getSuperType()
  {
    return _superType;
  }

  public Object evaluate()
  {
    return null; // Nothing to do
  }

  @Override
  public String toString()
  {
    return Keyword.KW_extends + " " + (_superType == null ? "" : _superType.getName());
  }

}