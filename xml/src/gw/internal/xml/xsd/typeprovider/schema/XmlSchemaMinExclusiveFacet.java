/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaMinExclusiveFacet extends XmlSchemaFacet<XmlSchemaMinExclusiveFacet> {

  public XmlSchemaMinExclusiveFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo, value );
  }

  @Override
  protected XmlSchemaMinExclusiveFacet copy( XmlSchemaIndex schemaIndex ) throws UnsupportedOperationException {
    return new XmlSchemaMinExclusiveFacet( schemaIndex, getLocationInfo(), getValue() );
  }
}
