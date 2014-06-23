/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaComplexContent extends XmlSchemaContentModel {

  private final XmlSchemaContent<?> _content;

  public XmlSchemaComplexContent( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, XmlSchemaContent<?> content ) {
    super( schemaIndex, locationInfo );
    _content = content;
  }

  public XmlSchemaContent getContent() {
    return _content;
  }

  @Override
  public XmlSchemaComplexContent copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaComplexContent( schemaIndex, getLocationInfo(), _content == null ? null : _content.copy( schemaIndex ) );
  }


}
