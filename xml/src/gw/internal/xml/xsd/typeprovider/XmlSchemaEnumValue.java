/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuObject;
import gw.xml.IXmlSchemaEnumValue;
import gw.xml.XmlSimpleValue;

public class XmlSchemaEnumValue implements IXmlSchemaEnumValue, IEnumValue, IGosuObject {

  private final XmlSchemaEnumerationTypeData _type;
  private final XmlSimpleValue _serializedValue;
  private final String _code;
  private final int _ordinal;

  public XmlSchemaEnumValue( XmlSchemaEnumerationTypeData type, XmlSimpleValue serializedValue, String code, int ordinal ) {
    _type = type;
    _serializedValue = serializedValue;
    _code = code;
    _ordinal = ordinal;
  }

  @Override
  public Object getValue() {
    return this;
  }

  @Override
  public String getCode() {
    return _code;
  }

  @Override
  public int getOrdinal() {
    return _ordinal;
  }

  @Override
  public String getDisplayName() {
    return _serializedValue.getStringValue();
  }

  @Override
  public IType getIntrinsicType() {
    return _type.getType();
  }

  @Override
  public String toString() {
    return _serializedValue.getStringValue();
  }

  public XmlSimpleValue getSerializedValue() {
    return _serializedValue;
  }

}
