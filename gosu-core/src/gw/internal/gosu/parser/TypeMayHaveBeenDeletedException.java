/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.RefreshKind;

public class TypeMayHaveBeenDeletedException extends TypeResolveException {
  private AbstractTypeRef _reference;

  public TypeMayHaveBeenDeletedException(String msg, AbstractTypeRef reference) {
    super(msg);
    _reference = reference;
  }

  public void deleteTheType() {
    _reference._setStale(RefreshKind.DELETION);
  }
}
