/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.XmlSimpleValue;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class HexBinarySimpleValueFactory extends XmlSimpleValueFactory {

  private static final LockingLazyVar<IType> TYPE = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.get( byte[].class );
    }
  };

  @Override
  public IType getGosuValueType() {
    return TYPE.get();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (byte[]) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    return new Value( decode( stringValue ) );
  }

  public static String encode( byte[] bytes ) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02X", b));
    }
    return sb.toString();
  }

  public static byte[] decode( String value ) {
    value = value.replaceAll("\\s", "").trim();
    if (value.length() % 2 != 0) {
      throw new IllegalArgumentException("Expecting even number of hex digits in xsd:hexBinary");
    }
    byte[] b = new byte[value.length() / 2];
    for (int i = 0; i < b.length; i++) {
      String s = value.substring(i * 2, (i * 2) + 2);
      b[i] = (byte) Integer.parseInt(s, 16);
    }
    return b;
  }

  private class Value extends XmlSimpleValueBase {

    private byte[] _value;

    public Value( byte[] value ) {
      _value = value;
    }

    @Override
    public IType getGosuValueType() {
      return TYPE.get();
    }

    @Override
    public Object _getGosuValue() {
      return _value;
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      return encode( _value );
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return getStringValue();
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }

  }

}
