/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IModifierListClause;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class ModifierListClause extends Expression implements IModifierListClause
{
  public ModifierListClause()
  {
    setType( JavaTypes.pVOID() );
  }

  public IType evaluate()
  {
    throw new IllegalStateException();
  }

  @Override
  public String toString()
  {
    return "";
  }
}
