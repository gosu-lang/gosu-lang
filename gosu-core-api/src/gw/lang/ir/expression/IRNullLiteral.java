/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRNullLiteral extends IRExpression {

  public IRNullLiteral() { }

  @Override
  public IRType getType() {
    // This is Object instead of void because we need to be clear that A) this isn't primitive and B) it implies a value on
    // the stack, as opposed to void, which is primitive and implies the absence of anything on the stack
    return IRTypeConstants.OBJECT();
  }
}
