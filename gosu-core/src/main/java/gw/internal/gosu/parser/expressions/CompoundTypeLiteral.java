/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.ICompoundTypeLiteral;
import gw.lang.parser.expressions.ITypeLiteralExpression;

import java.util.List;

/**
 */
public class CompoundTypeLiteral extends TypeLiteral implements ICompoundTypeLiteral
{
  List<TypeLiteral> _types;

  public List<TypeLiteral> getTypes()
  {
    return _types;
  }
  public void setTypes( List<? extends ITypeLiteralExpression> types )
  {
    //noinspection unchecked
    _types = (List<TypeLiteral>)types;
  }
}
