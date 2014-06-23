/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;

import java.util.Map;

import javax.xml.namespace.QName;
import java.util.Set;

public abstract class MarshalInfo {

  private final IType _type;

  public MarshalInfo( IType type ) {
    _type = type;
  }

  public static final QName GW_TYPE_QNAME = new QName("http://guidewire.com/xsd", "type", "gw");
  public static final QName XS_STRING_QNAME = gw.internal.schema.gw.xsd.w3c.xmlschema.types.simple.String.$QNAME;

  public abstract Map<String,Set<XmlSchemaAccess>> getAllSchemas();

  public abstract void addType( LocalElement element, WsiServiceInfo serviceInfo ) throws Exception;

  public abstract Object unmarshal(XmlElement componentElement, UnmarshalContext context);

  public abstract void marshal(XmlElement parameterElement, IType type, Object obj, MarshalContext context);

  public final IType getType() {
    return _type;
  }
}
