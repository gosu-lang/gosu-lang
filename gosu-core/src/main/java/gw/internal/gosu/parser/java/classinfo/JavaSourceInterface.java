/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.util.SourcePositions;
import gw.lang.reflect.gs.ISourceFileHandle;

import java.util.List;

public class JavaSourceInterface extends JavaSourceType {

  /**
   * For top level.
   */
  public JavaSourceInterface( ISourceFileHandle fileHandle, CompilationUnitTree compilationUnitTree, ClassTree typeDecl, List<? extends ImportTree> imports, SourcePositions sourcePositions ) {
    super(fileHandle, compilationUnitTree, typeDecl, imports, sourcePositions );
  }

  /**
   * For inner.
   */
  public JavaSourceInterface(ClassTree typeDecl, JavaSourceType parent) {
    super(typeDecl, parent);
  }

  @Override
  public boolean isInterface()
  {
    return true;
  }
}
