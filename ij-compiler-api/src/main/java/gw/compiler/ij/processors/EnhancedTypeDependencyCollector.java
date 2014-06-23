/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.processors;

import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;

public class EnhancedTypeDependencyCollector {
  public static void collect(IClassStatement classStatement, DependencySink sink) {
    final IGosuClass klass = classStatement.getGosuClass();
    if (klass instanceof IGosuEnhancement) {
      IType enhancedType = ((IGosuEnhancement) klass).getEnhancedType();
      enhancedType = TypeSystem.getPureGenericType(enhancedType);
      while (enhancedType.getEnclosingType() != null) {
        enhancedType = enhancedType.getEnclosingType();
      }
      sink.addType(enhancedType);
    }
  }
}

