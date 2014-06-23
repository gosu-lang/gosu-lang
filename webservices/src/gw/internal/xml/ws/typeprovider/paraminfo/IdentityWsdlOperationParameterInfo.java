/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider.paraminfo;

import gw.internal.xml.xsd.typeprovider.IXmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.IXmlType;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;

import javax.xml.namespace.QName;

public class IdentityWsdlOperationParameterInfo extends WsdlOperationParameterInfo {

  public IdentityWsdlOperationParameterInfo( IType parameterElementType, QName parameterElementName, boolean throwIfAnonymous ) throws AnonymousElementException {
    super( parameterElementType, parameterElementName );
    if ( parameterElementType instanceof IXmlType ) {
      IXmlType xmlType = (IXmlType) parameterElementType;
      IXmlSchemaElementTypeData typeData = (IXmlSchemaElementTypeData) xmlType.getTypeData();
      if ( throwIfAnonymous && typeData.isAnonymous() ) {
        throw new AnonymousElementException(); // we don't want to expose any anonymous types
      }
    }
  }

  @Override
  public IType getType() {
    return getParameterElementType();
  }

  @Override
  public Object unwrap( XmlElement node ) {
    return node;
  }

  @Override
  public String getName() {
    return "request";
  }

  @Override
  public XmlElement wrap( Object obj, XmlElement componentElement ) {
    return (XmlElement) obj;
  }

}
