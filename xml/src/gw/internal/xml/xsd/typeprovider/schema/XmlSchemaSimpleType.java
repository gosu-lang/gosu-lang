/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class XmlSchemaSimpleType extends XmlSchemaType<XmlSchemaSimpleType> {

  private final XmlSchemaSimpleTypeContent<?> _content;
  private final String _gwTypeName;

  public XmlSchemaSimpleType( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String name, QName qname, XmlSchemaSimpleTypeContent<?> content, String gwTypeName ) {
    super( schemaIndex, locationInfo, name, qname );
    _content = content;
    _gwTypeName = gwTypeName;
  }

  public XmlSchemaSimpleTypeContent getContent() {
    return _content;
  }

  public String getGwTypeName() {
    return _gwTypeName;
  }

  @Override
  public XmlSchemaSimpleType copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaSimpleType( schemaIndex, getLocationInfo(), getName(), getQName(), _content == null ? null : _content.copy( schemaIndex ), _gwTypeName );
  }
}
