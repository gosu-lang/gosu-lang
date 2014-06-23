/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class WsdlPart extends XmlSchemaObject<WsdlPart> {

  private final QName _elementName;
  private final String _name;
  private final QName _typeName;

  public WsdlPart( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, QName elementName, String name, QName typeName ) {
    super( schemaIndex, locationInfo );
    _elementName = elementName;
    _name = name;
    _typeName = typeName;
  }

  public QName getElementName() {
    return _elementName;
  }

  public String getName() {
    return _name;
  }

  public XmlSchemaElement getElement() {
    if ( _elementName == null ) {
      return null;
    }
    return getSchemaIndex().getXmlSchemaElementByQName( _elementName );
  }

  public XmlSchemaType getType() {
    if ( _typeName == null ) {
      return null;
    }
    return getSchemaIndex().getXmlSchemaTypeByQName( _typeName );
  }

  @Override
  public String toString() {
    return _name;
  }

}
