/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.processors;

import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuEnhancement;

public class FieldAccessDependencyCollector implements IDependencyCollector<IFieldAccessExpression> {
  @Override
  public void collect(IFieldAccessExpression memberAccess, DependencySink sink) {
    try {
      IPropertyInfo info = memberAccess.getPropertyInfo();
      if (info != null) {
        final IType ownersType = info.getOwnersType();
        if (ownersType instanceof IGosuEnhancement) {
          sink.addType(ownersType);
        }

        // do not merge below code into opensource gosu
        if ("com.guidewire.pl.system.locale.DisplayKeyProperty".equals(info.getClass().getName())
                && "java.lang.String".equals(memberAccess.getType().toString())) {
          //special handling for displaykeys
          sink.addDisplayKey(memberAccess.toString());
        }
      }
    } catch (RuntimeException e) {
      /* unfortunately, the same code is reused to parse member access
      * and package statements like "com.abc.def",
      * and getPropertyInfo will throw RuntimeException for packages names
      * we have to ignore this for now.
      */
      return;
    }
  }
}
