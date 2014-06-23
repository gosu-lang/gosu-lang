/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.Base64Util;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.BinaryData;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlSimpleValueException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class Base64BinarySimpleValueFactory extends XmlSimpleValueFactory {

  private static final LockingLazyVar<IType> TYPE = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.get( BinaryData.class );
    }
  };

  @Override
  public IType getGosuValueType() {
    return TYPE.get();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (BinaryData) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    BinaryData provider = new BinaryData();
    provider.setBytes( Base64Util.decode( stringValue ) );
    return new Value( provider );
  }

  private class Value extends XmlSimpleValueBase {

    private BinaryData _value;

    public Value( BinaryData value ) {
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
      try {
        return Base64Util.encode( _value.getBytes() );
      }
      catch ( IOException e ) {
        throw new XmlSimpleValueException( "Unable to serialize Base64 value", e );
      }
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
