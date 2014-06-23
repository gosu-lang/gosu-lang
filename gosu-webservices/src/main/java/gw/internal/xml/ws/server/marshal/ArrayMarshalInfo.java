/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.xmlschema.Sequence;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ExplicitGroup_Element;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalElement_ComplexType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.internal.xml.xsd.typeprovider.schemaparser.XmlSchemaParser;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

public class ArrayMarshalInfo extends MarshalInfo {

  private final MarshalInfo _componentMarshalInfo;
  private final IType _componentType;
  private final boolean _isComponent;

  public ArrayMarshalInfo( IType componentType, MarshalInfo componentMarshalInfo, boolean isComponent ) {
    super( componentType.getArrayType() );
    _componentType = componentType;
    _componentMarshalInfo = componentMarshalInfo;
    _isComponent = isComponent;
  }

  public MarshalInfo getComponentMarshalInfo() {
    return _componentMarshalInfo;
  }

  public IType getComponentType() {
    return _componentType;
  }

  @Override
  public Map<String,Set<XmlSchemaAccess>> getAllSchemas() {
    return _componentMarshalInfo.getAllSchemas();
  }

  @Override
  public void addType( LocalElement element, WsiServiceInfo createInfo ) throws Exception {
    if ( _isComponent ) {
      element.setNillable$( true );
    }
    else {
      element.setMinOccurs$( BigInteger.ZERO );
    }
    element.setAttributeValue( XmlSchemaParser.VIEWAS_ATTRIBUTE_NAME, XmlSchemaParser.VIEWAS_ARRAY );
    ExplicitGroup_Element entryElement = new ExplicitGroup_Element();
    entryElement.setMinOccurs$( BigInteger.ZERO );
    entryElement.setMaxOccurs$( "unbounded" );
    element.setComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement$( new LocalElement_ComplexType() );
    element.ComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement().setSequence$( new Sequence() );
    element.ComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement().Sequence().Element().add( entryElement );
    element.ComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement().Sequence().Element().get( 0 ).setName$( "Entry" );
    _componentMarshalInfo.addType( entryElement.getTypeInstance(), createInfo );
  }

  @Override
  public Object unmarshal(XmlElement componentElement, UnmarshalContext context) {
    int size = componentElement.getChildren().size();
    final IType arrayType = _componentType.getArrayType();
    Object list = _componentType.makeArrayInstance( size );
    for ( int i = 0; i < size; i++ ) {
      XmlElement child = componentElement.getChildren().get( i );
      if ( child.isNil() ) {
        arrayType.setArrayComponent( list, i, null );
      }
      else {
        final Object item = XmlServices.unmarshal( _componentType, child, context);
        _componentType.setArrayComponent( list, i, item );
      }
    }
    return list;
  }

  @Override
  public void marshal(XmlElement parameterElement, IType type, Object list, MarshalContext context) {
    int size = _componentType.getArrayLength( list );
    for ( int i = 0; i < size; i++ ) {
      Object child = _componentType.getArrayComponent( list, i );
      XmlElement childElement = new XmlElement( parameterElement.getNamespace().qualify( "Entry") );
      parameterElement.addChild( childElement );
      if ( child == null ) {
        childElement.setNil( true );
      }
      else {
        XmlServices.marshal( childElement, _componentType, child, context);
      }
    }
  }

}
