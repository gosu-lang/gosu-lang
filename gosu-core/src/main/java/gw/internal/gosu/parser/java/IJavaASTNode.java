/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.internal.ext.org.antlr.runtime.Token;

import java.util.List;

public interface IJavaASTNode {
  public void setTextRange(Token token1, Token token2);
  void add(IJavaASTNode node);
  List<IJavaASTNode> getChildren();
  String getText();
  String getSource();
  boolean isLeaf();

  IJavaASTNode getChild(int index);

  int getChildOfTypeIndex(String name);
  int getChildOfTypeIndex(int tokettype);
  int getChildOfTypesIndex(int... tokenTypes);
  IJavaASTNode getChildOfTypes(String... name);

  IJavaASTNode getChildOfType(int tokenType);
  IJavaASTNode getChildOfType(String name);
  List<IJavaASTNode> getChildrenOfTypes(String... name);

  boolean isOfType(String name);
}
