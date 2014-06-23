/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.processors;

import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuEnhancement;

public class BeanMethodCallDependencyCollector implements IDependencyCollector<IBeanMethodCallExpression> {
  @Override
  public void collect(IBeanMethodCallExpression methodCall, DependencySink sink) {
    final IMethodInfo info = methodCall.getMethodDescriptor();
    if (info != null) {
      final IType type = info.getOwnersType();
      if (type instanceof IGosuEnhancement) {
        sink.addType(type);
      }
      // do not merge below code into opensource gosu
      if ("com.guidewire.pl.system.locale.DisplayKeyMethod".equals(info.getClass().getName())) {
        //special handling for displaykeys
        sink.addDisplayKey(methodCall.getRootExpression().toString() + "." + info.getName());
      }
    }
  }
}
