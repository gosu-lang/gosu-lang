/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaPatternFacet extends XmlSchemaFacet<XmlSchemaPatternFacet> {

  public XmlSchemaPatternFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo, value );
  }

  @Override
  protected XmlSchemaPatternFacet copy( XmlSchemaIndex schemaIndex ) throws UnsupportedOperationException {
    return new XmlSchemaPatternFacet( schemaIndex, getLocationInfo(), getValue() );
  }
}
