/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.Map;

public final class XmlSchemaEnumerationFacet extends XmlSchemaFacet<XmlSchemaEnumerationFacet> {

  private final Map<String, String> _namespaceContext;

  public XmlSchemaEnumerationFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value, Map<String,String> namespaceContext ) {
    super( schemaIndex, locationInfo, value );
    _namespaceContext = namespaceContext;
  }

  public Map<String,String> getNamespaceContext() {
    return _namespaceContext;
  }

  @Override
  protected XmlSchemaEnumerationFacet copy( XmlSchemaIndex schemaIndex ) throws UnsupportedOperationException {
    return new XmlSchemaEnumerationFacet( schemaIndex, getLocationInfo(), getValue(), _namespaceContext );
  }

}
