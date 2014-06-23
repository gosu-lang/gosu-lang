/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlSimpleValue;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class BigDecimalSimpleValueFactory extends XmlSimpleValueFactory {

  @Override
  public IType getGosuValueType() {
    return JavaTypes.BIG_DECIMAL();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (BigDecimal) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    return new Value( new BigDecimal( stringValue ) );
  }

  private class Value extends XmlSimpleValueBase {

    private final BigDecimal _value;

    public Value( BigDecimal value ) {
      _value = value;
    }

    @Override
    public IType getGosuValueType() {
      return JavaTypes.BIG_DECIMAL();
    }

    @Override
    public Object _getGosuValue() {
      return _value;
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      return _value.toPlainString();
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return _value.toPlainString();
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }

  }
}
