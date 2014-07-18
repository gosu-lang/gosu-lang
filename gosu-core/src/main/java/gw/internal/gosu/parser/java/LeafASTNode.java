/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.internal.ext.org.antlr.runtime.Token;

import java.util.Collections;
import java.util.List;

public class LeafASTNode extends AbstractJavaASTNode {
  protected String _name;
  protected int _tokenType;

  public LeafASTNode(int type, String text) {
    _name = text;
    _tokenType = type;
  }

  @Override
  public void setTextRange(Token token1, Token token2) {
  }

  @Override
  public void add(IJavaASTNode node) {
    throw new RuntimeException("Cannot add nodes to leaf elements");
  }

  @Override
  public List<IJavaASTNode> getChildren() {
    return Collections.EMPTY_LIST;
  }

  @Override
  public String getText() {
    return _name;
  }

  @Override
  public String getSource() {
    return _name;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public boolean isOfType(String name) {
    return false;
  }

  public int getTokenType() {
    return _tokenType;
  }

  public String toString() {
    return _name + " - " + _tokenType;
  }
}
