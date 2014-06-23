/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.INameInDeclaration;

public class NameInDeclaration extends Expression implements INameInDeclaration
{
  private String _strName;

  public NameInDeclaration( String strName )
  {
    super();
    _strName = strName;
  }

  @Override
  public String toString()
  {
    return _strName;
  }

  @Override
  public String getName()
  {
    return _strName;
  }

}
