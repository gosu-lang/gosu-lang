/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.util.SourcePositions;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

import java.util.List;

public class JavaSourceAnnotation extends JavaSourceType {

  /**
   * For top level.
   */
  public JavaSourceAnnotation( ISourceFileHandle fileHandle, CompilationUnitTree compilationUnitTree, ClassTree typeDecl, List<? extends ImportTree> imports, SourcePositions sourcePositions, IModule gosuModule ) {
    super(fileHandle, compilationUnitTree, typeDecl, imports, sourcePositions, gosuModule);
  }

  /**
   * For inner.
   */
  public JavaSourceAnnotation(ClassTree typeDecl, JavaSourceType parent) {
    super(typeDecl, parent);
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
