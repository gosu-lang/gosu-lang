/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;

public class IRCharacterLiteral extends IRExpression
{
  private char _char;

  public IRCharacterLiteral( char c )
  {
    _char = c;
  }

  public char getValue()
  {
    return _char;
  }

  @Override
  public IRType getType()
  {
    return IRTypeConstants.pCHAR();
  }
}