/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.internal.ext.org.antlr.runtime.Token;

import java.util.List;

public class TypeASTNode extends JavaASTNode {
  private String _typeName = "";
  private List<IJavaASTNode> _typeArguments;

  public TypeASTNode() {
    super("type");
  }

  @Override
  public void setTextRange(Token token1, Token token2) {
  }

  @Override
  public void add(IJavaASTNode node) {
    if (node instanceof LeafASTNode) {
      _typeName += node.getText();
    } else {
      super.add(node);
    }
  }

  public String toString() {
    return _name + " - " + _typeName;
  }

  public String getTypeName() {
    return _typeName;
  }

  public List<IJavaASTNode> getTypeArguments() {
    if (_typeArguments == null) {
      _typeArguments = getChildrenOfTypes(JavaASTConstants.typeArgument);
    }
    return _typeArguments;
  }

  public boolean isParameterized() {
    return getTypeArguments().size() > 0;
  }

  public boolean isParameterizedArrayType() {
    return _typeName.endsWith("[]") && isParameterized();
  }
}
