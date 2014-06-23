/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuObject;
import gw.xml.XmlElement;
import gw.xml.ws.WsdlFault;

public class StaticallyTypedWsdlFaultImpl extends WsdlFault implements IGosuObject {

  private final IType _type;

  public StaticallyTypedWsdlFaultImpl( String message, Throwable cause, IType type ) {
    super( message, cause );
    _type = type;
  }

  public IType getIntrinsicType() {
    return _type;
  }

  public String toString() {
    return _type.getName() + ": " + getMessage();
  }

}
