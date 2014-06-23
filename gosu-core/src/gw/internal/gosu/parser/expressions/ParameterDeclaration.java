/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IParameterDeclaration;

public class ParameterDeclaration extends LocalVarDeclaration implements IParameterDeclaration
{
  public ParameterDeclaration( String strParameterName )
  {
    super( strParameterName );
  }
}
