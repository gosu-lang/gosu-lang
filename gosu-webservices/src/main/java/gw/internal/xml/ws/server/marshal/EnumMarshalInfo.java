/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.xmlschema.Enumeration;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Restriction;
import gw.internal.schema.gw.xsd.w3c.xmlschema.SimpleType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.lang.reflect.IEnumConstant;
import gw.lang.reflect.IEnumData;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class EnumMarshalInfo extends MarshalInfo {
  protected boolean _isComponent;

  public EnumMarshalInfo(IType type, boolean isComponent) {
    super(type);
    _isComponent = isComponent;
  }

  @Override
  public Map<String,Set<XmlSchemaAccess>> getAllSchemas() {
    return Collections.emptyMap();
  }

  @Override
  public void addType( LocalElement element, WsiServiceInfo createInfo ) throws Exception {
    if ( _isComponent ) {
      element.setNillable$( true );
    }
    else {
      element.setMinOccurs$( BigInteger.ZERO );
    }
    SimpleType simpleType = createInfo.getSimpleTypeIfNeededFor(getType());
    if (simpleType != null) {
      Restriction restriction = new Restriction();
      restriction.setBase$(XS_STRING_QNAME);
      ArrayList<Enumeration> enumerations = new ArrayList<Enumeration>();
      for (IEnumValue value : getEnumData().getEnumValues()) {
        Enumeration enumEL = new Enumeration();
        enumEL.setValue$(value.getCode());
        enumerations.add(enumEL);
      }
      restriction.setEnumeration$(enumerations);
      simpleType.setRestriction$(restriction);
    }
    element.setType$( createInfo.getQName(getType()) );
  }

  @Override
  public Object unmarshal(XmlElement componentElement, UnmarshalContext context) {
    IEnumValue value = getEnumData().getEnumValue(componentElement.getText());
    if (value == null) {
      throw new RuntimeException("invalid " + getType() + " '" + componentElement.getText() + "");
    }
    return value;
  }

  @Override
  public void marshal(XmlElement returnEl, IType type, Object obj, MarshalContext context) {
    returnEl.setText(((IEnumConstant)obj).getCode());
  }

  protected abstract IEnumData getEnumData();
}
