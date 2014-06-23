/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaAnyAttribute extends XmlSchemaObject<XmlSchemaAnyAttribute> {

  private final ProcessContents _processContents;

  public enum ProcessContents { skip, lax, strict }

  public XmlSchemaAnyAttribute( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, ProcessContents processContents ) {
    super( schemaIndex, locationInfo );
    _processContents = processContents;
  }

  @Override
  public XmlSchemaAnyAttribute copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaAnyAttribute( schemaIndex, getLocationInfo(), _processContents );
  }

  public ProcessContents getProcessContents() {
    return _processContents;
  }

}