/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.internal.gosu.parser.java.JavaParser;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;

public class JavaSourceEnum extends JavaSourceType {

  /**
   * For top level.
   */
  public JavaSourceEnum(ISourceFileHandle fileHandle, IJavaASTNode node, IModule gosuModule) {
    super(fileHandle, node, JavaASTConstants.enumDeclaration, JavaParser.ENUM, JavaASTConstants.enumBody, gosuModule);
  }

  /**
   * For inner.
   */
  public JavaSourceEnum(IJavaASTNode node, JavaSourceType parent) {
    super(node, parent, JavaParser.ENUM, JavaASTConstants.enumBody);
  }

}
