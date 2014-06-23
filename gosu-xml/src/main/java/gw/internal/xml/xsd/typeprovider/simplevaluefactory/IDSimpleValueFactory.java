/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.internal.xml.XmlUtil;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlSimpleValue;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class IDSimpleValueFactory extends XmlSimpleValueFactory {

  @Override
  public IType getGosuValueType() {
    return JavaTypes.STRING();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (String) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    final Value value = new Value( stringValue );
    context.addId( stringValue, context.getCurrentElement() );
    return value; // will get filled in with actual element later
  }

  public class Value extends XmlSimpleValueBase {

    private String _value;

    public Value( String value ) {
      XmlUtil.validateNCName(value);
      _value = value;
    }

    @Override
    public IType getGosuValueType() {
      return IDSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public String _getGosuValue() {
      return _value;
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      return _value;
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return context.makeUniqueId( context.getCurrentElement(), _value );
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }

  }

}
