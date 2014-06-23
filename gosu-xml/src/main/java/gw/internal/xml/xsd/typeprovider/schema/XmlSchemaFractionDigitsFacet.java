/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaFractionDigitsFacet extends XmlSchemaFacet<XmlSchemaFractionDigitsFacet> {

  public XmlSchemaFractionDigitsFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo, value );
  }

  @Override
  protected XmlSchemaFractionDigitsFacet copy( XmlSchemaIndex schemaIndex ) throws UnsupportedOperationException {
    return new XmlSchemaFractionDigitsFacet( schemaIndex, getLocationInfo(), getValue() );
  }
}
