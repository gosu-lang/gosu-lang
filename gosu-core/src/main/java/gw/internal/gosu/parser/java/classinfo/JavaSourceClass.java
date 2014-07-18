/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.internal.gosu.parser.java.JavaParser;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;

public class JavaSourceClass extends JavaSourceType {

  /**
   * For top level classes.
   */
  public JavaSourceClass(ISourceFileHandle fileHandle, IJavaASTNode node, IModule gosuModule) {
    super(fileHandle, node, JavaASTConstants.normalClassDeclaration, JavaParser.CLASS, JavaASTConstants.classBody, gosuModule);
  }

  /**
   * For inner classes.
   */
  public JavaSourceClass(IJavaASTNode node, JavaSourceType parent) {
    super(node, parent, JavaParser.CLASS, JavaASTConstants.classBody);
  }
}
