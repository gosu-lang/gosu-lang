/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.util.SourcePositions;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;

import java.util.List;

public class JavaSourceEnum extends JavaSourceType {

  /**
   * For top level.
   */
  public JavaSourceEnum( ISourceFileHandle fileHandle, CompilationUnitTree compilationUnitTree, ClassTree typeDecl, List<? extends ImportTree> imports, SourcePositions sourcePositions, IModule gosuModule ) {
    super(fileHandle, compilationUnitTree, typeDecl, imports, sourcePositions, gosuModule);
  }

  /**
   * For inner.
   */
  public JavaSourceEnum(ClassTree typeDecl, JavaSourceType parent) {
    super(typeDecl, parent);
  }
}
