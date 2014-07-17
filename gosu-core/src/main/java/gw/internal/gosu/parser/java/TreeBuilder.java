/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.internal.ext.org.antlr.runtime.Token;
import gw.internal.gosu.parser.StringCache;

import java.util.HashSet;
import java.util.Stack;

/**
 * Helper class to build trees representing the parsing process of a grammar.
 *
 * @author Yang Jiang (yang.jiang.z@gmail.com)
 */
public class TreeBuilder {
  Stack<IJavaASTNode> stack = new Stack<IJavaASTNode>();
  private IJavaASTNode _node = new JavaASTNode("root");
  private int _ignoreCount = 0;
  private IJavaASTNode IGNORE_NODE = new JavaASTNode("IGNORE");
  private static HashSet<String> ignore = new HashSet<String>();
  private static HashSet<String> ignore_recursive = new HashSet<String>();
  static {
    ignore.add("classOrInterfaceDeclaration");
    ignore.add("typeDeclaration");
    ignore.add("classDeclaration");
    ignore.add("classBodyDeclaration");
    ignore.add("memberDecl");
    ignore.add("formalParameterDecls");
    ignore.add("interfaceDeclaration");
    ignore.add("interfaceBodyDeclaration");
    ignore.add("classOrInterfaceType");
    ignore.add("primitiveType");
    ignore.add("variableDeclarator");
    ignore.add("enumConstants");
    ignore.add("enumBodyDeclarations");
    ignore.add("typeArguments");
    ignore.add("annotationTypeElementDeclaration");
    ignore.add("primary");
    ignore.add("literal");
    ignore.add("typeArgumentsOrDiamond");
    ignore.add("nonWildcardTypeArgumentsOrDiamond");


    ignore_recursive.add("block");
    //ignore_recursive.add("variableInitializer");
  }

  public IJavaASTNode addNode(String name) {
    if (ignore_recursive.contains(name)) {
      _ignoreCount++;
      return IGNORE_NODE;
    }
    if (ignore.contains(name) || name.contains("Expression")) {
      return null;
    }
    IJavaASTNode node = makeNode(name);
    if (_node != null) {
      _node.add(node);
    }
    return node;
  }

  private IJavaASTNode makeNode(String name) {
    if (name.equals("type")) {
      return new TypeASTNode();
    }
    return new JavaASTNode(name);
  }

  /**
   * Same as addNode(), except that a leaf should contain no child.
   */
  public IJavaASTNode addLeaf(String name, Token token) {
    if (_ignoreCount != 0
        ||
       (!isParentAnOpExpression()
          &&
         (token.getType() == JavaLexer.LBRACE ||
         token.getType() == JavaLexer.RBRACE ||

         // Parens need to be included in expressions
         token.getType() == JavaLexer.LPAREN ||
         token.getType() == JavaLexer.RPAREN ||

         token.getType() == JavaLexer.COMMA ||

         // Gt, Lt need to be included in expressions
         token.getType() == JavaLexer.GT ||
         token.getType() == JavaLexer.LT ||

         token.getType() == JavaLexer.SEMI ||

         // Amp needs to be included in expressions
         token.getType() == JavaLexer.AMP ||

         token.getType() == JavaLexer.PACKAGE ||
         token.getType() == JavaLexer.EQ ||
          token.getType() == JavaLexer.MONKEYS_AT ||
         token.getType() == JavaLexer.DEFAULT))
        ) {
      return null;
    }
    IJavaASTNode node = new LeafASTNode(token.getType(), StringCache.get(token.getText()));
    if (_node != null) {
      _node.add(node);
    }
    return node;
  }

  /**
   * Stores the current parent in the stack, this is usually called before setting a new parent.
   */
  public void pushTop() {
    stack.push(_node);
  }

  /**
   * Restores the previous parent.
   */
  public IJavaASTNode popTop() {
    IJavaASTNode ret = _node;
    if (_node == IGNORE_NODE) {
      _ignoreCount--;
    }
    _node = stack.pop();
    return ret;
  }

  public void setCurrentParent(IJavaASTNode currentParent) {
    if (currentParent != null) {
      _node = currentParent;
    }
  }

  public IJavaASTNode getTree() {
    return _node;
  }

  public boolean isParentAnOpExpression() {
    if( _node == null ) {
      return false;
    }
    String text = _node.getText();
    return text != null &&
           (text.endsWith( "Op" ) ||
            text.endsWith( "elementValue" ));
  }
}
