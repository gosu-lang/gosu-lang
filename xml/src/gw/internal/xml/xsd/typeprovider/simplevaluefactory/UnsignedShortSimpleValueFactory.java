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

public class UnsignedShortSimpleValueFactory extends XmlSimpleValueFactory {

  private static final int SHORT_MAX_VALUE = ( Short.MAX_VALUE * 2 ) + 1;

  @Override
  public IType getGosuValueType() {
    return JavaTypes.INTEGER();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (Integer) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    return new Value( Integer.parseInt( stringValue ) );
  }

  private void checkRange( int gosuValue ) {
    if ( gosuValue < 0 || gosuValue > SHORT_MAX_VALUE ) {
      throw new NumberFormatException( "Unsigned short value " + gosuValue + " is out of range" );
    }
  }

  private class Value extends XmlSimpleValueBase {

    private final int _gosuValue;

    public Value( int gosuValue ) {
      checkRange( gosuValue );
      _gosuValue = gosuValue;
    }

    @Override
    public IType getGosuValueType() {
      return UnsignedShortSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Integer _getGosuValue() {
      return _gosuValue;
    }

    public String _getStringValue( boolean isEnumCode ) {
      return String.valueOf( _gosuValue );
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return String.valueOf( _gosuValue );
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }

  }

}
