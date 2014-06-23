/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;

public interface ITypeLiteralExpression extends ILiteralExpression, Cloneable
{
  IMetaType getType();
  void setType( IType strFqn );

  IExpression getPackageExpression();
}
