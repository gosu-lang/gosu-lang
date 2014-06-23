/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.processors;

import gw.lang.parser.IConstructorFunctionSymbol;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuEnhancement;

public class MethodCallDependencyCollector implements IDependencyCollector<IMethodCallExpression> {
  private boolean isBlock(IFunctionSymbol function) {
    return function.getType() instanceof IBlockType;
  }

  private boolean isPrintFunction(IFunctionSymbol function) {
    return function.getName().equals("print");
  }

  private boolean isConstructor(IFunctionSymbol function) {
    return function instanceof IConstructorFunctionSymbol;
  }

  @Override
  public void collect(IMethodCallExpression methodCall, DependencySink sink) {
    final IFunctionSymbol function = methodCall.getFunctionSymbol();
    if (function != null &&
        !isBlock(function) &&
        !isPrintFunction(function) &&
        !isConstructor(function)) {
      final IFunctionType type = methodCall.getFunctionType();
      if (type != null) {
        final IMethodInfo info = type.getMethodInfo();
        if (info != null) {
          final IType ownerType = info.getOwnersType();
          if (ownerType instanceof IGosuEnhancement) {
            sink.addType(ownerType);
          }
        }
      }
    }
  }
}
