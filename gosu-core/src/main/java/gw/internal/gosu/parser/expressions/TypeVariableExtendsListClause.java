/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.statements.ITypeVariableExtendsListClause;
import gw.lang.reflect.IType;

public class TypeVariableExtendsListClause extends SuperTypeClause implements ITypeVariableExtendsListClause
{
  public TypeVariableExtendsListClause( IType superType )
  {
    super( superType );
  }
}