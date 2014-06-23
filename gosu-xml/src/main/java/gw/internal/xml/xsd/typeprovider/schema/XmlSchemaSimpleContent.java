/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaSimpleContent extends XmlSchemaContentModel<XmlSchemaSimpleContent> {

  private final XmlSchemaContent<?> _content;

  public XmlSchemaSimpleContent( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, XmlSchemaContent<?> content ) {
    super( schemaIndex, locationInfo );
    _content = content;
  }

  public XmlSchemaContent getContent() {
    return _content;
  }

  @Override
  public XmlSchemaSimpleContent copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaSimpleContent( schemaIndex, getLocationInfo(), _content == null ? null : _content.copy( schemaIndex ) );
  }

}
