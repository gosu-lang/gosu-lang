/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaMaxInclusiveFacet extends XmlSchemaFacet<XmlSchemaMaxInclusiveFacet> {

  public XmlSchemaMaxInclusiveFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo, value );
  }

  @Override
  protected XmlSchemaMaxInclusiveFacet copy( XmlSchemaIndex schemaIndex ) throws UnsupportedOperationException {
    return new XmlSchemaMaxInclusiveFacet( schemaIndex, getLocationInfo(), getValue() );
  }
}
