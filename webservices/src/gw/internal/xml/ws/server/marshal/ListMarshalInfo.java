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
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListMarshalInfo extends MarshalInfo {

  private final MarshalInfo _componentMarshalInfo;
  private final IType _componentType;
  private final boolean _isComponent;
  
  public ListMarshalInfo( IType componentType, MarshalInfo componentMarshalInfo, boolean isComponent ) {
    super( JavaTypes.LIST().getParameterizedType( componentType ) );
    _componentMarshalInfo = componentMarshalInfo;
    _componentType = componentType;
    _isComponent = isComponent;
  }

  @Override
  public void addType( LocalElement element, WsiServiceInfo createInfo ) throws Exception {
    if ( _isComponent ) {
      element.setNillable$( true );
    }
    else {
      element.setMinOccurs$( BigInteger.ZERO );
    }
    element.setAttributeValue( XmlSchemaParser.VIEWAS_ATTRIBUTE_NAME, XmlSchemaParser.VIEWAS_LIST );
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
  public Map<String,Set<XmlSchemaAccess>> getAllSchemas() {
    return _componentMarshalInfo.getAllSchemas();
  }

  public MarshalInfo getComponentMarshalInfo() {
    return _componentMarshalInfo;
  }

  public IType getComponentType() {
    return _componentType;
  }

  @Override
  public Object unmarshal(XmlElement componentElement, UnmarshalContext context) {
    ArrayList<Object> list = new ArrayList<Object>();
    for ( XmlElement child : componentElement.getChildren() ) {
      if ( child.isNil() ) {
        list.add( null );
      }
      else {
        list.add( XmlServices.unmarshal( _componentType, child, context) );
      }
    }
    return list;
  }

  @Override
  public void marshal(XmlElement parameterElement, IType type, Object obj, MarshalContext context) {
    @SuppressWarnings( {"unchecked"} )
    List<Object> list = (List<Object>) obj;
    for ( Object child : list ) {
      XmlElement childElement = new XmlElement( parameterElement.getNamespace().qualify( "Entry" ) );
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
