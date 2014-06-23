/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRClassLiteral extends IRExpression {

  private IRType _literalType;

  public IRClassLiteral(IRType literalType) {
    _literalType = literalType;
  }

  public IRType getLiteralType() {
    return _literalType;
  }

  @Override
  public IRType getType() {
    return GosuShop.getIRTypeResolver().getDescriptor( Class.class );
  }
}
