/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlSimpleValue;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class BooleanSimpleValueFactory extends XmlSimpleValueFactory {

  @Override
  public IType getGosuValueType() {
    return JavaTypes.BOOLEAN();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (Boolean) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    return new Value( stringValueToGosuValue( stringValue ) );
  }

  private static boolean stringValueToGosuValue( String stringValue ) {
    return parseBoolean( stringValue );
  }

  public static boolean parseBoolean( String stringValue ) {
    boolean gosuValue;
    if ( stringValue.equals( "0" ) || stringValue.equals( "false" ) ) {
      gosuValue = false;
    }
    else if ( stringValue.equals( "1" ) || stringValue.equals( "true" ) ) {
      gosuValue = true;
    }
    else {
      throw new RuntimeException( "Invalid xsd:boolean value, expected 1, 0, true, or false" );
    }
    return gosuValue;
  }

  private static String gosuValueToStringValue( boolean gosuValue ) {
    return String.valueOf( gosuValue );
  }

  private class Value extends XmlSimpleValueBase {

    private final boolean _gosuValue;

    public Value( boolean gosuValue ) {
      _gosuValue = gosuValue;
    }

    @Override
    public IType getGosuValueType() {
      return BooleanSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Boolean _getGosuValue() {
      return _gosuValue;
    }

    public String _getStringValue( boolean isEnumCode ) {
      return gosuValueToStringValue( _gosuValue );
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return gosuValueToStringValue( _gosuValue );
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }

  }

}
