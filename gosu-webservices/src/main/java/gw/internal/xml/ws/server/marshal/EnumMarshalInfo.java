/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.xmlschema.Enumeration;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Restriction;
import gw.internal.schema.gw.xsd.w3c.xmlschema.SimpleType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.IXmlLoggerFactory;
import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.lang.reflect.IEnumConstant;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class EnumMarshalInfo extends MarshalInfo {
  private final IEnumType _type;
  private boolean _isComponent;

  public EnumMarshalInfo(IEnumType type, boolean isComponent ) {
    super( type );
    _type = type;
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
    SimpleType simpleType = createInfo.getSimpleTypeIfNeededFor(_type);
    if (simpleType != null) {
      Restriction restriction = new Restriction();
      restriction.setBase$(XS_STRING_QNAME);
      ArrayList<Enumeration> enumerations = new ArrayList<Enumeration>();
      for (IEnumValue value : _type.getEnumValues()) {
        Enumeration enumEL = new Enumeration();
        enumEL.setValue$(value.getCode());
        enumerations.add(enumEL);
      }
      restriction.setEnumeration$(enumerations);
      simpleType.setRestriction$(restriction);
    }
    element.setType$( createInfo.getQName(_type) );
  }

  @Override
  public Object unmarshal(XmlElement componentElement, UnmarshalContext context) {
    if (_type instanceof IJavaType) { // java enums
      try {
        IJavaType jtype = (IJavaType) _type;
        Class clazz = jtype.getBackingClass();
        for (Field field : clazz.getDeclaredFields()) {
          if (field.getName().equalsIgnoreCase(componentElement.getText())) {
            return field.get(null);
          }
        }
        throw new RuntimeException("invalid " + _type + " '" + componentElement.getText() + "");
      } catch (Throwable e) {
        XmlServices.getLogger(IXmlLoggerFactory.Category.XmlUnMarshal).error("Exception on " + _type + " '" + componentElement.getText() + "", e);
      }
    }
    IEnumValue value = _type.getEnumValue(componentElement.getText());
    if (value == null) {
      throw new RuntimeException("invalid " + _type + " '" + componentElement.getText() + "");
    }
    return value;
  }

  @Override
  public void marshal(XmlElement returnEl, IType type, Object obj, MarshalContext context) {
    if (_type instanceof IJavaType) { // java enums
      try {
        IJavaType jtype = (IJavaType) _type;
        Class clazz = jtype.getBackingClass();
        for (Field field : clazz.getDeclaredFields()) {
          if (field.get(null).equals(obj)) {
            returnEl.setText(field.getName());
            return;
          }
        }
        XmlServices.getLogger(IXmlLoggerFactory.Category.XmlMarshal).error("Couldn't find " + _type + ": " + obj);
      } catch (Throwable e) {
        XmlServices.getLogger(IXmlLoggerFactory.Category.XmlUnMarshal).error("Exception on " + _type + ": " + obj, e);
      }
    }
    else { // typelists, gosu enums
      returnEl.setText(((IEnumConstant)obj).getCode());
    }
  }


}
