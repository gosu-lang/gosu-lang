/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaInclude extends XmlSchemaObject<XmlSchemaInclude> {

  private final String _schemaLocation;

  public XmlSchemaInclude( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String schemaLocation ) {
    super( schemaIndex, locationInfo );
    _schemaLocation = schemaLocation;
  }

  public String getSchemaLocation() {
    return _schemaLocation;
  }

}
