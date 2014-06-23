/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaMaxExclusiveFacet extends XmlSchemaFacet<XmlSchemaMaxExclusiveFacet> {

  public XmlSchemaMaxExclusiveFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo, value );
  }

  @Override
  protected XmlSchemaMaxExclusiveFacet copy( XmlSchemaIndex schemaIndex ) throws UnsupportedOperationException {
    return new XmlSchemaMaxExclusiveFacet( schemaIndex, getLocationInfo(), getValue() );
  }
}
