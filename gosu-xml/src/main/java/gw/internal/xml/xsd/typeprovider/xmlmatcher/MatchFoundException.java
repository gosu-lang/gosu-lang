/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.lang.reflect.IType;

public class MatchFoundException extends RuntimeException {

  private IType _type;

  public MatchFoundException( IType type ) {
    _type = type;
  }

  public IType getType() {
    return _type;
  }

}
