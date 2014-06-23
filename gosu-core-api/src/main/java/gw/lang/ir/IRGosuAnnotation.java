/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.internal.gosu.parser.IGosuAnnotation;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRGosuAnnotation {

  private IRType _descriptor;
  private boolean _include;
  private IGosuAnnotation _gosuAnnotation;

  public IRGosuAnnotation(IRType descriptor, boolean include, IGosuAnnotation gosuAnnotation) {
    _descriptor = descriptor;
    _include = include;
    _gosuAnnotation = gosuAnnotation;
//    CompileTimeAnnotationHandler.evalGosuAnnotation( rawAnnotation ) )
  }

  public IRType getDescriptor() {
    return _descriptor;
  }

  public boolean isInclude() {
    return _include;
  }

  public Object getValue()
  {
    return _gosuAnnotation;
  }
}
