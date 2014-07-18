/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.internal.gosu.parser.java.JavaParser;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;

public class JavaSourceInterface extends JavaSourceType {

  /**
   * For top level.
   */
  public JavaSourceInterface(ISourceFileHandle fileHandle, IJavaASTNode node, IModule gosuModule) {
    super(fileHandle, node, JavaASTConstants.normalInterfaceDeclaration, JavaParser.INTERFACE, JavaASTConstants.interfaceBody, gosuModule);
  }

  /**
   * For inner.
   */
  public JavaSourceInterface(IJavaASTNode node, JavaSourceType parent) {
    super(node, parent, JavaParser.INTERFACE, JavaASTConstants.interfaceBody);
  }

}
