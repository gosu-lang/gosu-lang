/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.xmlschema.Enumeration;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Restriction;
import gw.internal.schema.gw.xsd.w3c.xmlschema.SimpleType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.Marshaller;
import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.XmlElement;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RemotableMarshalInfo extends ClassBasedMarshalInfo {

  static final LockingLazyVar<IType> _gwRemotableType = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.pl.util.webservices.GWRemotable" );
    }
  };
  static final LockingLazyVar<IType> _gwRemotableEnumType = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.pl.util.webservices.GWEnumeration" );
    }
  };

  public RemotableMarshalInfo(IType type, boolean isComponent) {
    super( type, isComponent);
  }

  static boolean isExportable(IType type) {
    return _gwRemotableType.get().isAssignableFrom(type);
  }

  @Override
  public void addType(LocalElement element, WsiServiceInfo createInfo) throws Exception {
    if (!_gwRemotableEnumType.get().isAssignableFrom(getType())) {
      super.addType(element, createInfo);
      return;
    }
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
      if (_type instanceof IJavaType) { // java enums
        for (Field field : getFields((IJavaType) _type)) {
          Enumeration enumEL = new Enumeration();
          enumEL.setValue$(field.getName());
          enumerations.add(enumEL);
        }
      }
      restriction.setEnumeration$(enumerations);
      simpleType.setRestriction$(restriction);
    }
    element.setType$( createInfo.getQName(_type) );
  }

  @Override
  public Object unmarshal(XmlElement componentElement, UnmarshalContext context) {
    if (!_gwRemotableEnumType.get().isAssignableFrom(getType())) {
      return super.unmarshal(componentElement, context);
    }
    final String name = componentElement.getText();
    try {
      final Class clazz = ((IJavaType) getType()).getBackingClass();
      final Field field = clazz.getField(name);
      final int modifiers = field.getModifiers();
      if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && clazz.equals(field.getType())) {
        return field.get(null);
      }
      XmlServices.getLogger(XmlServices.Category.XmlUnMarshal).error("Unable to unmarshal " + _type + ": " + name);
    } catch (Throwable e) {
      XmlServices.getLogger(XmlServices.Category.XmlUnMarshal).error("Unable to unmarshal " + _type + ": " + name, e);
    }
    return null;
  }

  @Override
  public void marshal(XmlElement returnEl, IType type, Object obj, MarshalContext context) {
    if (!_gwRemotableEnumType.get().isAssignableFrom(getType())) {
      super.marshal(returnEl, type, obj, context);
      return;
    }
    for (Field field : getFields((IJavaType) getType())) {
      try {
        if (field.get(null).equals(obj)) {
          returnEl.setText(field.getName());
          return;
        }
      } catch (IllegalAccessException e) {
        XmlServices.getLogger(XmlServices.Category.XmlMarshal).error("Exception on " + _type + ": " + obj, e);
      }
    }
    XmlServices.getLogger(XmlServices.Category.XmlMarshal).error("Unable to marshal " + _type + ": " + obj);
  }

  @Override
  public void checkType(Marshaller marshaller, IParsedElement parsedElement, String label, IType type, Map<String, Object> seenNamespaces) {
    if (!_gwRemotableEnumType.get().isAssignableFrom(getType())) {
      super.checkType(marshaller, parsedElement, label, type, seenNamespaces);
      return;
    }
    if (!(type instanceof IJavaType)) { // java enums
      parsedElement.addParseException(Res.WS_ERR_Can_Not_Marshal, type.getDisplayName(), label);
    }
    else if (getFields((IJavaType) type).size() == 0) {
      parsedElement.addParseException(Res.WS_ERR_Can_Not_Marshal, type.getDisplayName(), label);
    }
  }

  protected Map<String,IPropertyInfo> getProperties(IType type) {
    Map<String, IPropertyInfo> rtn = new TreeMap<String,IPropertyInfo>();
    ITypeInfo typeInfo = type.getTypeInfo();
    if (typeInfo instanceof IRelativeTypeInfo) {
      IRelativeTypeInfo pogoTI =  ((IRelativeTypeInfo) typeInfo);
      for (IPropertyInfo property : pogoTI.getProperties(_gwRemotableType.get())) {
        if (property.isPublic() && property.isWritable() && property.isReadable()) {
          rtn.put(property.getName(), property);
        }
      }
    }
    return rtn;
  }

  private List<Field> getFields(IJavaType type) {
    List<Field> fields = new ArrayList<Field>();
    Class clazz = type.getBackingClass();
    for (Field field : clazz.getDeclaredFields()) {
      final int modifiers = field.getModifiers();
      if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && clazz.equals(field.getType())) {
        fields.add(field);
      }
    }
    return fields;
  }


}
