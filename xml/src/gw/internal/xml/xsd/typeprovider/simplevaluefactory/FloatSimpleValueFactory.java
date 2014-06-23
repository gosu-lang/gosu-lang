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

public class FloatSimpleValueFactory extends XmlSimpleValueFactory {

  @Override
  public IType getGosuValueType() {
    return JavaTypes.FLOAT();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (Float) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    return new Value( stringValueToGosuValue( stringValue ) );
  }

  private static float stringValueToGosuValue( String stringValue ) {
    return parseFloat( stringValue );
  }

  public static float parseFloat( String stringValue ) {
    float gosuValue;
    if ( stringValue.equals( "INF" ) || stringValue.equals( "+INF" ) ) {
      gosuValue = Float.POSITIVE_INFINITY;
    }
    else if ( stringValue.equals( "-INF" ) ) {
      gosuValue = Float.NEGATIVE_INFINITY;
    }
    else {
      gosuValue = Float.parseFloat( stringValue );
      if ( gosuValue == Float.POSITIVE_INFINITY || gosuValue == Float.NEGATIVE_INFINITY ) {
        throw new NumberFormatException( stringValue );
      }
    }
    return gosuValue;
  }

  private static String gosuValueToStringValue( float gosuValue ) {
    String stringValue;
    if ( gosuValue == Float.POSITIVE_INFINITY ) {
      stringValue = "INF";
    }
    else if ( gosuValue == Float.NEGATIVE_INFINITY ) {
      stringValue = "-INF";
    }
    else {
      stringValue = String.valueOf( gosuValue );
    }
    return stringValue;
  }

  private class Value extends XmlSimpleValueBase {

    private final float _gosuValue;

    public Value( float gosuValue ) {
      _gosuValue = gosuValue;
    }

    @Override
    public IType getGosuValueType() {
      return FloatSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Float _getGosuValue() {
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
