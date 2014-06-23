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

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class UnsignedLongSimpleValueFactory extends XmlSimpleValueFactory {

  private static final BigInteger LONG_MAX_VALUE = new BigInteger( String.valueOf( Long.MAX_VALUE ) ).multiply( new BigInteger( "2" ) ).add( BigInteger.ONE );

  @Override
  public IType getGosuValueType() {
    return JavaTypes.BIG_INTEGER();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (BigInteger) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    return new Value( new BigInteger( stringValue ) );
  }

  private void checkRange( BigInteger gosuValue ) {
    if ( gosuValue.compareTo( BigInteger.ZERO ) < 0 || gosuValue.compareTo( LONG_MAX_VALUE ) > 0 ) {
      throw new NumberFormatException( "Unsigned long value " + gosuValue + " is out of range" );
    }
  }

  private class Value extends XmlSimpleValueBase {

    private final BigInteger _gosuValue;

    public Value( BigInteger gosuValue ) {
      checkRange( gosuValue );
      _gosuValue = gosuValue;
    }

    @Override
    public IType getGosuValueType() {
      return UnsignedLongSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public BigInteger _getGosuValue() {
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
