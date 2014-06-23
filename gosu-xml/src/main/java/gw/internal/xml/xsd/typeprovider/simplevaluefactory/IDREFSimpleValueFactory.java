/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertySpec;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.xml.XmlElement;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlSimpleValueException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class IDREFSimpleValueFactory extends XmlSimpleValueFactory {

  @Override
  public IType getGosuValueType() {
    return TypeSystem.get( XmlElement.class );
  }

  @Override
  protected XmlSimpleValue _gosuValueToStorageValue( Object gosuValue ) {
    return new Value( (XmlElement) gosuValue );
  }

  @Override
  protected XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    final Value value = new Value( null );
    context.addIdref( stringValue, value );
    return value; // will get filled in with actual element later
  }

  public class Value extends XmlSimpleValueBase {

    private XmlElement _value;

    public Value( XmlElement value ) {
      setElement( value );
    }

    @Override
    public IType getGosuValueType() {
      return IDREFSimpleValueFactory.this.getGosuValueType();
    }

    @Override
    public XmlElement _getGosuValue() {
      if ( _value == null ) {
        throw new XmlSimpleValueException( "Unresolved IDREF. Attempt to deserialize IDREF outside of parse operation." );
      }
      return _value;
    }

    @Override
    public String _getStringValue( boolean isEnumCode ) {
      final List<IPropertyAccessor> accessors = getIdAccessors(_value);
      if (accessors == null) {
        XmlSimpleValue simpleValue = _value.getSimpleValue();
        if ( simpleValue instanceof IDSimpleValueFactory.Value ) {
          return ( (IDSimpleValueFactory.Value) simpleValue )._getGosuValue();
        }
        for ( QName attrName : _value.getAttributeNames() ) {
          XmlSimpleValue val = _value.getAttributeSimpleValue( attrName );
          if ( val instanceof IDSimpleValueFactory.Value ) {
            return ( (IDSimpleValueFactory.Value) val )._getGosuValue();
          }
        }
        throw new XmlSimpleValueException( "The schema does not define an ID member for element nor does element have ID " + _value.getQName() );
      }
      else {
        return (String) accessors.get( 0 ).getValue( _value.getTypeInstance() );
      }
    }

    @Override
    public String _serialize( XmlSerializationContext context ) {
      String idValue = _getStringValue( false );
      return context.makeUniqueId( _value, idValue );
    }

    @Override
    public List<QName> _getQNames() {
      return Collections.emptyList();
    }

    public void setElement( XmlElement element ) {
      if ( element != null ) {
        final List<IPropertyAccessor> accessors = getIdAccessors(element);
        if (accessors != null) {
          for ( IPropertyAccessor accessor : accessors) {
            if ( accessor.getValue( element.getTypeInstance() ) == null ) {
              accessor.setValue( element.getTypeInstance(), "ID" );
            }
          }
        }
      }
      _value = element;
    }

    private List<IPropertyAccessor> getIdAccessors( XmlElement element ) {
      List<IPropertyAccessor> accessors = new ArrayList<IPropertyAccessor>();
      final XmlSchemaTypeSchemaInfo schemaInfo = XmlSchemaIndex.getSchemaInfoByType( element.getTypeInstance().getIntrinsicType() );
      for ( XmlSchemaPropertySpec property : schemaInfo.getProperties() ) {
        if ( property.getSimpleValueFactory() instanceof IDSimpleValueFactory ) {
          String propertyName = property.getSimpleTypePropertyName();
          accessors.add( element.getTypeInstance().getIntrinsicType().getTypeInfo().getProperty( propertyName ).getAccessor() );
        }
      }
      return accessors.isEmpty() ? null : accessors;
    }

  }

}
