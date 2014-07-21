/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJavaASTNode implements IJavaASTNode {

  @Override
  public IJavaASTNode getChild(int index) {
    List<IJavaASTNode> children = getChildren();
    return children.get(index);
  }

  @Override
  public IJavaASTNode getChildOfType(int tokenType) {
    List<IJavaASTNode> children = getChildren();
    for (int i = 0; i < children.size(); i++) {
      IJavaASTNode child = children.get(i);
      if (child instanceof LeafASTNode && ((LeafASTNode) child).getTokenType() == tokenType) {
        return child;
      }
    }
    return null;
  }

  @Override
  public int getChildOfTypeIndex(int tokenType) {
    List<IJavaASTNode> children = getChildren();
    for (int i = 0; i < children.size(); i++) {
      IJavaASTNode child = children.get(i);
      if (child instanceof LeafASTNode && ((LeafASTNode) child).getTokenType() == tokenType) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int getChildOfTypesIndex(int... tokenTypes) {
    List<IJavaASTNode> children = getChildren();
    for (int i = 0; i < children.size(); i++) {
      IJavaASTNode child = children.get(i);
      if (child instanceof LeafASTNode) {
        int childTokenType = ((LeafASTNode) child).getTokenType();
        for (int tokenType : tokenTypes) {
          if (childTokenType == tokenType) {
            return i;
          }
        }
      }
    }
    return -1;
  }

  @Override
  public IJavaASTNode getChildOfTypes(String... types) {
    List<IJavaASTNode> children = getChildren();
    for (int i = 0; i < children.size(); i++) {
      IJavaASTNode child = children.get(i);
      if (!(child instanceof LeafASTNode)) {
        String childText = child.getText();
        for (String type : types) {
          if (childText.equals(type)) {
            return child;
          }
        }
      }
    }
    return null;
  }

  @Override
  public int getChildOfTypeIndex(String name) {
    List<IJavaASTNode> children = getChildren();
    for (int i = 0; i < children.size(); i++) {
      IJavaASTNode child = children.get(i);
      if (!(child instanceof LeafASTNode) && child.getText().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public IJavaASTNode getChildOfType(String name) {
    int childIndex = getChildOfTypeIndex(name);
    if (childIndex != -1) {
      return getChild(childIndex);
    }
    return null;
  }

  @Override
  public List<IJavaASTNode> getChildrenOfTypes(String... types) {
    List<IJavaASTNode> result = new ArrayList<IJavaASTNode>();
    for (IJavaASTNode child : getChildren()) {
      if (!(child instanceof LeafASTNode)) {
        String childText = child.getText();
        for (String type : types) {
          if (childText.equals(type)) {
            result.add(child);
          }
        }
      }
    }
    return result;
  }
}
