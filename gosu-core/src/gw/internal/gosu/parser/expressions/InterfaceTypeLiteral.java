/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IInterfaceTypeLiteralExpression;
import gw.lang.reflect.IType;

public class InterfaceTypeLiteral extends TypeLiteral implements IInterfaceTypeLiteralExpression {

  public InterfaceTypeLiteral() {
    super();
  }

  public InterfaceTypeLiteral(IType type, boolean ignoreTypeDeprecation) {
    super(type, ignoreTypeDeprecation);
  }

  public InterfaceTypeLiteral(IType type) {
    super(type);
  }

}
