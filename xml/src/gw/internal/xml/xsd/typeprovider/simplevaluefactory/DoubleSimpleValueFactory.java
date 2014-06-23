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

public class DoubleSimpleValueFactory extends XmlSimpleValueFactory {

  @Override
  public IType getGosuValueType() {
    return JavaTypes.DOUBLE();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (Double) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    return new Value( stringValueToGosuValue( stringValue ) );
  }

  private static double stringValueToGosuValue( String stringValue ) {
    return parseDouble( stringValue );
  }

  public static double parseDouble( String stringValue ) {
    double gosuValue;
    if ( stringValue.equals( "INF" ) || stringValue.equals( "+INF" ) ) {
      gosuValue = Double.POSITIVE_INFINITY;
    }
    else if ( stringValue.equals( "-INF" ) ) {
      gosuValue = Double.NEGATIVE_INFINITY;
    }
    else {
      gosuValue = Double.parseDouble( stringValue );
      if ( gosuValue == Double.POSITIVE_INFINITY || gosuValue == Double.NEGATIVE_INFINITY ) {
        throw new NumberFormatException( stringValue );
      }
    }
    return gosuValue;
  }

  private static String gosuValueToStringValue( double gosuValue ) {
    String stringValue;
    if ( gosuValue == Double.POSITIVE_INFINITY ) {
      stringValue = "INF";
    }
    else if ( gosuValue == Double.NEGATIVE_INFINITY ) {
      stringValue = "-INF";
    }
    else {
      stringValue = String.valueOf( gosuValue );
    }
    return stringValue;
  }

  private class Value extends XmlSimpleValueBase {

    private final double _gosuValue;

    public Value( double gosuValue ) {
      _gosuValue = gosuValue;
    }

    @Override
    public IType getGosuValueType() {
      return DoubleSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Double _getGosuValue() {
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
