/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.internal.xml.XmlSimpleValueInternals;
import gw.internal.xml.xsd.typeprovider.IXmlSchemaEnumerationTypeData;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IType;
import gw.xml.XmlSimpleValue;

import java.util.List;

import javax.xml.namespace.QName;
import java.util.Map;

public class XmlSchemaEnumSimpleValueFactory extends XmlSimpleValueFactory {

  private final IXmlSchemaEnumerationTypeData<?> _enumType;

  public XmlSchemaEnumSimpleValueFactory( IXmlSchemaEnumerationTypeData enumType ) {
    _enumType = enumType;
  }

  @Override
  public IType getGosuValueType() {
    return _enumType.getType();
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (IEnumValue) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    XmlSimpleValue simpleValue = _enumType.getSimpleValueFactory().deserialize( context, stringValue, isDefault );
    return new Value( _enumType.deserialize( simpleValue ) );
  }

  private class Value extends XmlSimpleValueBase {

    private final IEnumValue _value;

    public Value( IEnumValue value ) {
      _value = value;
    }

    @Override
    public IType getGosuValueType() {
      return XmlSchemaEnumSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public Object _getGosuValue() {
      return _value.getValue();
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      return _value.getValue().toString();
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      return XmlSimpleValueInternals.instance().serialize( _enumType.getEnumSimpleValue( _value ), context );
    }

    @Override
    public List<QName> _getQNames() {
      XmlSimpleValue simpleValue = _enumType.getEnumSimpleValue( _value );
      if ( simpleValue == null ) {
        // PL-17888 - the following code is here to assist in debugging this issue
        StringBuilder sb = new StringBuilder();
        sb.append( "(Guidewire Jira PL-17888) Unable to find simple value " );
        sb.append( _value );
        sb.append( " (" );
        sb.append( System.identityHashCode( _value ) );
        sb.append( ") for enum type " );
        sb.append( _enumType.getName() );
        sb.append( ", values:\n" );
        for ( Map.Entry<IEnumValue,XmlSimpleValue> entry : _enumType.getEnumSimpleValues().entrySet() ) {
          IEnumValue value = entry.getKey();
          sb.append( "  " );
          sb.append( value );
          sb.append( " (" );
          sb.append( System.identityHashCode( value ) );
          sb.append( ") = " );
          sb.append( entry.getValue() );
          sb.append( "\n" );
        }

        throw new IllegalStateException( sb.toString() );
      }
      return simpleValue.getQNames();
    }
  }
}
