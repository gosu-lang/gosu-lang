/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IType;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlSimpleValueException;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class GosuEnumSimpleValueFactory extends XmlSimpleValueFactory {

  private final IEnumType _enumType;

  public GosuEnumSimpleValueFactory( IEnumType enumType ) {
    _enumType = enumType;
  }

  @Override
  public IType getGosuValueType() {
    return _enumType;
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    for ( IEnumValue enumValue : _enumType.getEnumValues() ) {
      if ( enumValue.getValue().equals(gosuValue) ) {
        return new Value( enumValue );
      }
    }
    throw new IllegalStateException( "Enum value not found for type " + _enumType + ": " + gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    IEnumValue value = _enumType.getEnumValue( stringValue );
    if ( value == null ) {
      throw new XmlSimpleValueException( "Enum constant not found with name '" + stringValue + "'" );
    }
    return new Value(value);
  }

  private class Value extends XmlSimpleValueBase {

    private final IEnumValue _value;

    public Value( IEnumValue value ) {
      _value = value;
    }

    @Override
    public IType getGosuValueType() {
      return GosuEnumSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Object _getGosuValue() {
      return _value.getValue();
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      return _value.getCode();
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return _value.getCode();
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }
  }
}
