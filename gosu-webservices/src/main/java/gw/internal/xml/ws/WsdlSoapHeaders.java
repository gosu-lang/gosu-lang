/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuObject;
import gw.xml.XmlElement;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WsdlSoapHeaders implements IGosuObject {

  private final IType _type;
  private TreeMap<String, XmlElement> _headers = new TreeMap<String, XmlElement>(); // TODO dlank - maintain declaration order?

  public WsdlSoapHeaders( IType type ) {
    _type = type;
  }

  @Override
  public IType getIntrinsicType() {
    return _type;
  }

  public XmlElement get( String propertyName ) {
    return _headers.get( propertyName );
  }

  public void set( String propertyName, XmlElement value ) {
    _headers.put( propertyName, value );
  }

  public TreeMap<String, XmlElement> getAllHeaders() {
    return _headers;
  }

}
