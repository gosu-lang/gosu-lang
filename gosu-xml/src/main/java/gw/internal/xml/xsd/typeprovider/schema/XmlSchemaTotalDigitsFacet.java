/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaTotalDigitsFacet extends XmlSchemaFacet<XmlSchemaTotalDigitsFacet> {

  public XmlSchemaTotalDigitsFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo, value );
  }

  @Override
  protected XmlSchemaTotalDigitsFacet copy( XmlSchemaIndex schemaIndex ) throws UnsupportedOperationException {
    return new XmlSchemaTotalDigitsFacet( schemaIndex, getLocationInfo(), getValue() );
  }
}
