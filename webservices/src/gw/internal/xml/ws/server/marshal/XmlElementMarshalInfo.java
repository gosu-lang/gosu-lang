/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.xmlschema.Any;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Sequence;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ExplicitGroup_Element;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalElement_ComplexType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.enums.Wildcard_ProcessContents;
import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.internal.xml.xsd.typeprovider.IXmlSchemaTypeData;
import gw.internal.xml.xsd.typeprovider.IXmlType;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.xml.namespace.QName;
import java.util.Set;

public class XmlElementMarshalInfo extends MarshalInfo {

  private boolean _isComponent;

  public XmlElementMarshalInfo( IType type, boolean isComponent ) {
    super( type );
    _isComponent = isComponent;
  }

  @Override
  public Map<String,Set<XmlSchemaAccess>> getAllSchemas() {
    if ( getType() instanceof IXmlType ) {
      IXmlType xmlType = (IXmlType) getType();
      if ( xmlType.getTypeData() instanceof IXmlSchemaTypeData ) {
        IXmlSchemaTypeData typedata = (IXmlSchemaTypeData) xmlType.getTypeData();
        final XmlSchemaObject xmlSchemaObject = typedata.getSchemaObject();
        final XmlSchemaIndex<?> schemaIndex = xmlSchemaObject.getSchemaIndex();
        Map<String,Set<XmlSchemaAccess>> rtn = new HashMap<String, Set<XmlSchemaAccess>>();
        for ( Map.Entry<String, LinkedHashSet<String>> entry : schemaIndex.getGosuNamespacesByXmlNamespace().entrySet() ) {
          Set<String> gosuNamespaces = entry.getValue();
          for ( String gosuNamespace : gosuNamespaces ) {
            XmlSchemaIndex<?> targetSchema = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace(schemaIndex.getTypeLoader().getModule(), gosuNamespace);
            Set<XmlSchemaAccess> xmlSchemaAccesses = rtn.get( entry.getKey() );
            if ( xmlSchemaAccesses == null ) {
              xmlSchemaAccesses = new LinkedHashSet<XmlSchemaAccess>();
              rtn.put( entry.getKey(), xmlSchemaAccesses );
            }
            xmlSchemaAccesses.add( targetSchema.getXmlSchemaAccess() );
          }
        }
        return rtn;
      }
    }
    return null;
  }

  @Override
  public void addType( LocalElement element, WsiServiceInfo createInfo ) throws Exception {
    if ( _isComponent ) {
      element.setNillable$( true );
    }
    else {
      element.setMinOccurs$( BigInteger.ZERO );
    }
    element.setComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement$( new LocalElement_ComplexType() );
    element.ComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement().setSequence$( new Sequence() );
    if ( getType() instanceof IXmlType ) { // is a schema backed element so create a complex type which refers to the schema
      element.ComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement().Sequence().Element().add( new ExplicitGroup_Element() );
      IXmlType xmlType = (IXmlType) getType();
      IXmlSchemaTypeData typedata = (IXmlSchemaTypeData) xmlType.getTypeData();
      XmlSchemaElement schemaElement = (XmlSchemaElement) typedata.getSchemaObject();
      QName qname = schemaElement.getQName();
      XmlMarshaller.findOrImportSchema( schemaElement.getSchemaIndex(), createInfo, qname.getNamespaceURI() );
      element.ComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement().Sequence().Element().get( 0 ).setRef$( qname );
    }
    else { // is not schema backed xml element so use Any
      element.ComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement().Sequence().Any().add( new Any() );
      element.ComplexType$$gw_xsd_w3c_xmlschema_types_complex_LocalElement().Sequence().Any().get( 0 ).setProcessContents$( Wildcard_ProcessContents.Skip ); // no validation
    }
  }

  @Override
  public Object unmarshal(XmlElement componentElement, UnmarshalContext context) {
    return componentElement.getChildren().get( 0 );
  }

  @Override
  public void marshal(XmlElement parameterElement, IType type, Object obj, MarshalContext context) {
    parameterElement.addChild( (XmlElement) obj );
  }

}
