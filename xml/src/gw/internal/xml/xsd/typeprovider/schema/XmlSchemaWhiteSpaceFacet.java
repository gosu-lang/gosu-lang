/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class XmlSchemaWhiteSpaceFacet extends XmlSchemaFacet<XmlSchemaWhiteSpaceFacet> {

  public XmlSchemaWhiteSpaceFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo, value );
  }

  @Override
  protected XmlSchemaWhiteSpaceFacet copy(XmlSchemaIndex schemaIndex) throws UnsupportedOperationException {
    return new XmlSchemaWhiteSpaceFacet( schemaIndex, getLocationInfo(), getValue() );
  }

}
