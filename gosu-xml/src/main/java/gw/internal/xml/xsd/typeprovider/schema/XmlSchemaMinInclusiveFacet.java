/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaMinInclusiveFacet extends XmlSchemaFacet<XmlSchemaMinInclusiveFacet> {

  public XmlSchemaMinInclusiveFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo, value );
  }

  @Override
  protected XmlSchemaMinInclusiveFacet copy(XmlSchemaIndex schemaIndex) throws UnsupportedOperationException {
    return new XmlSchemaMinInclusiveFacet( schemaIndex, getLocationInfo(), getValue() );
  }

}
