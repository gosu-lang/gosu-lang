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

public class UnsignedIntSimpleValueFactory extends XmlSimpleValueFactory {

  private static final long INT_MAX_VALUE = ( ( (long) Integer.MAX_VALUE ) * 2L ) + 1L;

  @Override
  public IType getGosuValueType() {
    return JavaTypes.LONG();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (Long) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    return new Value( Long.parseLong( stringValue ) );
  }

  private void checkRange( long gosuValue ) {
    if ( gosuValue < 0 || gosuValue > INT_MAX_VALUE ) {
      throw new NumberFormatException( "Unsigned int value " + gosuValue + " is out of range" );
    }
  }

  private class Value extends XmlSimpleValueBase {

    private final long _gosuValue;

    public Value( long gosuValue ) {
      checkRange( gosuValue );
      _gosuValue = gosuValue;
    }

    @Override
    public IType getGosuValueType() {
      return UnsignedIntSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Long _getGosuValue() {
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
