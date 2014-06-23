/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class WsdlSoapAddress extends XmlSchemaObject<WsdlSoapAddress> {

  private final String _location;

  public WsdlSoapAddress( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String location ) {
    super( schemaIndex, locationInfo );
    _location = location;
  }

  public String getLocation() {
    return _location;
  }

}
