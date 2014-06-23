/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRArrayLoadExpression extends IRExpression {
  private IRExpression _root;
  private IRExpression _index;
  private IRType _componentType;

  public IRArrayLoadExpression(IRExpression root, IRExpression index, IRType componentType) {
    _root = root;
    _index = index;
    _componentType = componentType;

    root.setParent( this );
    index.setParent( this );
  }

  public IRExpression getRoot() {
    return _root;
  }

  public IRExpression getIndex() {
    return _index;
  }

  public IRType getComponentType() {
    return _componentType;
  }

  @Override
  public IRType getType() {
    return _componentType;
  }
}
