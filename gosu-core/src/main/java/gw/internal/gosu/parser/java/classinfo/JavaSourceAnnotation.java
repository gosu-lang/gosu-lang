/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.internal.gosu.parser.java.JavaParser;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

public class JavaSourceAnnotation extends JavaSourceType {

  /**
   * For top level.
   */
  public JavaSourceAnnotation(ISourceFileHandle fileHandle, IJavaASTNode node, IModule gosuModule) {
    super(fileHandle, node, JavaASTConstants.annotationTypeDeclaration, JavaParser.INTERFACE, JavaASTConstants.annotationTypeBody, gosuModule);
  }

  /**
   * For inner.
   */
  public JavaSourceAnnotation(IJavaASTNode node, JavaSourceType parent) {
    super(node, parent, JavaParser.INTERFACE, JavaASTConstants.annotationTypeBody);
  }

  @Override
  public IJavaClassInfo[] getInterfaces() {
    if (_interfaces == null) {
      _interfaces = new IJavaClassInfo[] {JavaTypes.ANNOTATION().getBackingClassInfo()};
    }
    return _interfaces;
  }

  @Override
  public IJavaClassType[] getGenericInterfaces() {
    if (_genericInterfaces == null) {
      _genericInterfaces = new IJavaClassInfo[] {JavaTypes.ANNOTATION().getBackingClassInfo()};
    }
    return _genericInterfaces;
  }

}
