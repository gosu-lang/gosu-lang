/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.internal.xml.xsd.typeprovider.IXmlSchemaTypeData;
import gw.internal.xml.xsd.typeprovider.IXmlType;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;
import gw.xml.XmlTypeInstance;

import java.math.BigInteger;
import java.util.*;

import javax.xml.namespace.QName;

public class XmlTypeInstanceMarshalInfo extends MarshalInfo {

  private boolean _isComponent;

  public XmlTypeInstanceMarshalInfo( IType type, boolean isComponent ) {
    super( type );
    _isComponent = isComponent;
  }

  @Override
  public Map<String, Set<XmlSchemaAccess>> getAllSchemas() {
    IXmlType xmlType = (IXmlType) getType();
    IXmlSchemaTypeData typedata = (IXmlSchemaTypeData) xmlType.getTypeData();
    final XmlSchemaObject xmlSchemaObject = typedata.getSchemaObject();
    final XmlSchemaIndex<?> schemaIndex = xmlSchemaObject.getSchemaIndex();
    Map<String,Set<XmlSchemaAccess>> rtn = new LinkedHashMap<String, Set<XmlSchemaAccess>>();
    for ( Map.Entry<String, LinkedHashSet<String>> entry : schemaIndex.getGosuNamespacesByXmlNamespace().entrySet() ) {
      Set<String> gosuNamespaces = entry.getValue();
      for ( String gosuNamespace : gosuNamespaces ) {
        XmlSchemaIndex targetSchema = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace(schemaIndex.getTypeLoader().getModule(), gosuNamespace);
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

  @Override
  public void addType( LocalElement element, WsiServiceInfo createInfo ) throws Exception {
    if ( _isComponent ) {
      element.setNillable$( true );
    }
    else {
      element.setMinOccurs$( BigInteger.ZERO );
    }
    IXmlType xmlType = (IXmlType) getType();
    IXmlSchemaTypeData typedata = (IXmlSchemaTypeData) xmlType.getTypeData();
    XmlSchemaType schemaType = (XmlSchemaType) typedata.getSchemaObject();
    String namespace = schemaType.getSchemaIndex().getTargetNamespaceAsDeclaredInSchema();
    QName qname = schemaType.getQName();
    XmlMarshaller.findOrImportSchema( schemaType.getSchemaIndex(), createInfo, qname.getNamespaceURI() );
    element.setType$( qname );
  }

  @Override
  public Object unmarshal(XmlElement componentElement, UnmarshalContext context) {
    return componentElement.getTypeInstance();
  }

  @Override
  public void marshal(XmlElement parameterElement, IType type, Object obj, MarshalContext context) {
    parameterElement.setTypeInstance( (XmlTypeInstance) obj );
  }

}
