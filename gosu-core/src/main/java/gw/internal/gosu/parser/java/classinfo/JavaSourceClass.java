/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ImportTree;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;

import java.util.List;

public class JavaSourceClass extends JavaSourceType {

  /**
   * For top level classes.
   */
  public JavaSourceClass(ISourceFileHandle fileHandle, ClassTree typeDecl, List<? extends ImportTree> imports, IModule gosuModule) {
    super(fileHandle, typeDecl, imports, gosuModule);
  }

  /**
   * For inner classes.
   */
  public JavaSourceClass(ClassTree typeDecl, JavaSourceType parent) {
    super(typeDecl, parent);
  }
}
