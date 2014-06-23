/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.GosuShop;
import gw.lang.parser.ISourceCodeTokenizer;

import java.util.ArrayList;
import java.util.List;

import static gw.lang.parser.ISourceCodeTokenizer.*;

public class GosuClassLightParser {
  private ISourceCodeTokenizer lexer;

  class ClassScope {
    String name;
    int braketLevel;
    List<ClassScope> children = new ArrayList<ClassScope>();
    final boolean isRoot;

    public ClassScope(String name) {
      this.name = name;
      isRoot = name.isEmpty();
      if (isRoot) {
        braketLevel = 1;
      }
    }

    private void parse() {
      advance();
      while (hasTokens()) {
        if (isRoot && isKeyword("package")) {
          advance();
          name = parsePackage();
        } else if (isChar('{')) {
          braketLevel++;
        } else if (isChar('}')) {
          braketLevel--;
          if (braketLevel == 0) {
            return;
          }
        } else if (braketLevel == 1 && (isKeyword("class") || isKeyword("interface") || isKeyword("structure") || isKeyword("enum"))) {
          String innerClassDeclaration = findInnerClassDeclaration(lexer);
          if (innerClassDeclaration != null) {
            ClassScope classScope = new ClassScope(innerClassDeclaration);
            classScope.parse();
            children.add(classScope);
          }
        }
        advance();
      }
    }

    private String parsePackage() {
      StringBuilder s = new StringBuilder();
      while (isWord() || isOperator(".")) {
        s.append(getTokenText());
        advance();
      }
      revert();
      return s.toString().trim();
    }

    @Override
    public String toString() {
      return name;
    }

    public void getClasses(String prefix, List<String> classes) {
      String qName = isRoot ? name : prefix + "." + name;
      if (!isRoot) classes.add(qName);
      for (ClassScope child : children) {
        child.getClasses(qName, classes);
      }
    }
  }

  private String getTokenText() {
    return lexer.getTokenAsString();
  }

  private int advance() {
    return lexer.nextToken();
  }

  public GosuClassLightParser(String text) {
    lexer = GosuShop.createSourceCodeTokenizer(text);
    lexer.setCommentsSignificant(false);
    lexer.setWhitespaceSignificant(false);
  }

  private String findInnerClassDeclaration(ISourceCodeTokenizer lexer) {
    advance();
    if (isWord()) {
      String name = lexer.getTokenAsString();
      consumeParametrization();
      if (isChar('{')) {
        revert();
        return name;
      }
    }
    return null;
  }

  private void revert() {
    lexer.restoreToMark(lexer.mark() - 1);
  }

  private void consumeParametrization() {
    int parametrizationLevel = 0;
    advance();
    if (!isOperator("<")) {
      return;
    }
    while (hasTokens()) {
      if (isOperator("<")) {
        parametrizationLevel++;
      } else if (isOperator(">")) {
        parametrizationLevel--;
        if (parametrizationLevel == 0) {
          advance();
          return;
        }
      }
      advance();
    }
  }

  private boolean isOperator(String text) {
    return lexer.getType() == TT_OPERATOR && getTokenText().equals("'" + text + "'");
  }

  private boolean hasTokens() {
    return lexer.getType() != TT_EOF;
  }


  private boolean isWord() {
    return lexer.getType() == TT_WORD;
  }

  private boolean isChar(char c) {
    return lexer.getType() == c;
  }

  private boolean isKeyword(String text) {
    return lexer.getType() == TT_KEYWORD && getTokenText().equals(text);
  }

  public List<String> getInnerClasses() {
    ClassScope root = new ClassScope("");
    root.parse();
    List<String> classes = new ArrayList<String>();
    root.getClasses("", classes);
    return classes;
  }
}
