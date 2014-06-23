/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;

import java.math.BigInteger;
import java.util.Map;

import javax.xml.namespace.QName;
import java.util.Set;

public class SimpleValueMarshalInfo extends MarshalInfo {

  private final QName _qname;
  private final XmlSimpleValueFactory _valueFactory;
  private final String _gwType;
  private boolean _isComponent;

  public SimpleValueMarshalInfo( IType type, QName qname, XmlSimpleValueFactory valueFactory, String gwType, boolean isComponent ) {
    super( type );
    _qname = qname;
    _valueFactory = valueFactory;
    _gwType = gwType;
    _isComponent = isComponent;
  }

  @Override
  public Map<String,Set<XmlSchemaAccess>> getAllSchemas() {
    return null;
  }

  @Override
  public void addType( LocalElement element, WsiServiceInfo createInfo ) throws Exception {
    element.setType$( _qname );
    if ( _gwType != null ) {
        element.setAttributeValue(GW_TYPE_QNAME, _gwType );
    }
    if ( ! getType().isPrimitive() ) {
      if ( _isComponent ) {
        element.setNillable$( true );
      }
      else {
        element.setMinOccurs$( BigInteger.ZERO );
      }
    }
  }

  @Override
  public Object unmarshal(XmlElement componentElement, UnmarshalContext context) {
    final XmlDeserializationContext dc = new XmlDeserializationContext( null );
    for ( Map.Entry<String, String> entry : componentElement.getNamespaceContext().entrySet() ) {
      dc.addNamespace( entry.getKey(), entry.getValue() );
    }
    return _valueFactory.deserialize( dc, componentElement.getText(), false ).getGosuValue();
  }

  @Override
  public void marshal(XmlElement parameterElement, IType type, Object obj, MarshalContext context) {
    parameterElement.setSimpleValue( _valueFactory.gosuValueToStorageValue( obj ) );
  }
}
