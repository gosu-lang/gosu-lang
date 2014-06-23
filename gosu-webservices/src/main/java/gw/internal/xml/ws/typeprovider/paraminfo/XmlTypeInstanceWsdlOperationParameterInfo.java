/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider.paraminfo;

import gw.internal.xml.xsd.typeprovider.IXmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.IXmlSchemaTypeInstanceTypeData;
import gw.internal.xml.xsd.typeprovider.IXmlType;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertySpec;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlTypeInstance;

public class XmlTypeInstanceWsdlOperationParameterInfo extends WsdlOperationParameterInfo {

  public XmlTypeInstanceWsdlOperationParameterInfo( XmlSchemaPropertySpec parameterPropertySpec ) {
    super( parameterPropertySpec.getElementPropertyGosuType(), parameterPropertySpec.getQName() );
  }

  @Override
  public IType getType() {
    IXmlType xmlType = (IXmlType) getParameterElementType();
    IXmlSchemaElementTypeData elementGosuType = (IXmlSchemaElementTypeData) xmlType.getTypeData();
    XmlSchemaTypeSchemaInfo schemaInfo = elementGosuType.getSchemaInfo();
    IXmlSchemaTypeInstanceTypeData typeData = schemaInfo.getTypeData();
    return typeData.getType();
  }

  @Override
  public Object unwrap( XmlElement node ) {
    return node.getTypeInstance();
  }

  @Override
  public String getName() {
    return "request";
  }

  @Override
  public XmlElement wrap( Object obj, XmlElement componentElement ) {
    componentElement.setTypeInstance( (XmlTypeInstance) obj );
    return componentElement;
  }

}