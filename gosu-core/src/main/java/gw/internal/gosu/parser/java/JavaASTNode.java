/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.internal.ext.org.antlr.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class JavaASTNode extends AbstractJavaASTNode {
  protected String _name;
  protected List<IJavaASTNode> children = new ArrayList<IJavaASTNode>();

  public JavaASTNode(String name) {
    _name = name;
  }

  @Override
  public void setTextRange(Token token1, Token token2) {
  }

  @Override
  public void add(IJavaASTNode node) {
    children.add(node);
  }

  @Override
  public List<IJavaASTNode> getChildren() {
    return children;
  }

  @Override
  public String getText() {
    return _name;
  }

  public String getSource() {
    StringBuilder sb = new StringBuilder();
    for( IJavaASTNode child : getChildren() ) {
      sb.append( child.getSource() );
    }
    return sb.toString();
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  @Override
  public boolean isOfType(String name) {
    return getText().equals(name);
  }

  public String toString() {
    return _name;
  }
}
