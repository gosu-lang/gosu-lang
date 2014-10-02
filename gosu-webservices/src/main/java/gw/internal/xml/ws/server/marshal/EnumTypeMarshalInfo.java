/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.xml.IXmlLoggerFactory;
import gw.internal.xml.config.XmlServices;
import gw.lang.reflect.IEnumData;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.xml.XmlElement;

import java.lang.reflect.Field;

public class EnumTypeMarshalInfo extends EnumMarshalInfo {

  public EnumTypeMarshalInfo(IEnumType type, boolean isComponent) {
    super( type, isComponent);
  }

  @Override
  protected IEnumData getEnumData() {
    return (IEnumType) getType();
  }

  @Override
  public Object unmarshal(XmlElement componentElement, UnmarshalContext context) {
    if (getType() instanceof IJavaType) { // java enums
      try {
        IJavaType jtype = (IJavaType) getType();
        Class clazz = jtype.getBackingClass();
        for (Field field : clazz.getDeclaredFields()) {
          if (field.getName().equalsIgnoreCase(componentElement.getText())) {
            return field.get(null);
          }
        }
        throw new RuntimeException("invalid " + getType() + " '" + componentElement.getText() + "");
      } catch (Throwable e) {
        XmlServices.getLogger(IXmlLoggerFactory.Category.XmlUnMarshal).error("Exception on " + getType() + " '" + componentElement.getText() + "", e);
      }
    }
    return super.unmarshal(componentElement, context);
  }

  @Override
  public void marshal(XmlElement returnEl, IType type, Object obj, MarshalContext context) {
    if (getType() instanceof IJavaType) { // java enums
      try {
        IJavaType jtype = (IJavaType) getType();
        Class clazz = jtype.getBackingClass();
        for (Field field : clazz.getDeclaredFields()) {
          if (field.get(null).equals(obj)) {
            returnEl.setText(field.getName());
            return;
          }
        }
        XmlServices.getLogger(IXmlLoggerFactory.Category.XmlMarshal).error("Couldn't find " + getType() + ": " + obj);
      } catch (Throwable e) {
        XmlServices.getLogger(IXmlLoggerFactory.Category.XmlUnMarshal).error("Exception on " + getType() + ": " + obj, e);
      }
    }
    else { // typelists, gosu enums
      super.marshal(returnEl, type, obj, context);
    }
  }
}
