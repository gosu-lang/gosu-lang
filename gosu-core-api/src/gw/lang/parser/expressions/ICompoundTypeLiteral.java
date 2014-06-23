/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import java.util.List;

public interface ICompoundTypeLiteral
{
  List<? extends ITypeLiteralExpression> getTypes();
  void setTypes( List<? extends ITypeLiteralExpression> types );
}
