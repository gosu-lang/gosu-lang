/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.processors;

import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

public class UsesStatementsDependencyCollector {
  public static void collect(IUsesStatement usesStatement, DependencySink sink) {
    final String typeName = usesStatement.getTypeName();
    if (typeName.endsWith("*")) {
      processMultipleImport(typeName, sink);
    } else {
      processSingleImport(typeName, sink);
    }
  }

  private static void processMultipleImport(String typeName, DependencySink sink) {
    sink.addNamespace(typeName.substring(0, typeName.lastIndexOf('.')));
  }

  private static void processSingleImport(String typeName, DependencySink sink) {
    final IType type = TypeSystem.getByFullNameIfValid(typeName);
    if (type != null && !(type instanceof INamespaceType)) {
      sink.addType(type);
      processPackageSegment(typeName, sink);
    }
  }

  private static void processPackageSegment(String packageStatement, DependencySink sink) {
    int i = packageStatement.lastIndexOf('.');
    while (i >= 0) {
      packageStatement = packageStatement.substring(0, i);
      IType type = TypeSystem.getByFullNameIfValid(packageStatement);
      if (type != null && !(type instanceof INamespaceType)) {
        sink.addType(type);
      }
      i = packageStatement.lastIndexOf('.');
    }
  }
}
