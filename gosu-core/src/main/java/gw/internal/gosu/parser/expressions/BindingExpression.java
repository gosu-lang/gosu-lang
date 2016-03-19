/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IBindingExpression;
import gw.lang.reflect.IType;


/**
 */
public final class BindingExpression extends Expression implements IBindingExpression
{
  private final Expression _lhsExpr;
  private final Expression _rhsExpr;
  private final IType _bindForType;
  private final boolean _bPrefix;
  private final int _mark;


  public BindingExpression( Expression lhsExpr, Expression rhsExpr, IType bindForType, IType type, int mark, boolean bPrefix )
  {
    _lhsExpr = lhsExpr;
    _rhsExpr = rhsExpr;
    _type = type;
    _bindForType = bindForType;
    _bPrefix = bPrefix;
    _mark = mark;
  }

  public Expression getLhsExpr()
  {
    return _lhsExpr;
  }

  public Expression getRhsExpr()
  {
    return _rhsExpr;
  }

  public IType getBindForType()
  {
    return _bindForType;
  }

  public boolean isPrefix()
  {
    return _bPrefix;
  }

  public int getMark()
  {
    return _mark;
  }

  public Object evaluate()
  {
    throw new CannotExecuteGosuException();
  }

  @Override
  public String toString()
  {
    return getLhsExpr() + " " + getRhsExpr();
  }
}
