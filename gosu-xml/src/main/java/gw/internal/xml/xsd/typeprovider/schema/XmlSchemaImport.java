/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.XMLConstants;
import java.net.URL;

public final class XmlSchemaImport extends XmlSchemaObject<XmlSchemaImport> {

  private final String _namespaceURI;
  private final String _schemaLocation;
  private final URL _baseUrl;

  public XmlSchemaImport( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String namespaceURI, String schemaLocation, URL baseUrl ) {
    super( schemaIndex, locationInfo );
    _namespaceURI = namespaceURI == null ? XMLConstants.NULL_NS_URI : namespaceURI;
    _schemaLocation = schemaLocation;
    _baseUrl = baseUrl;
  }

  public String getNamespaceURI() {
    return _namespaceURI;
  }

  public String getSchemaLocation() {
    return _schemaLocation;
  }

  @Override
  public String toString() {
    return _namespaceURI + " -> " + _schemaLocation;
  }

  public URL getBaseUrl() {
    return _baseUrl;
  }

  public XmlSchemaImport copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaImport( schemaIndex, getLocationInfo(), _namespaceURI, _schemaLocation, _baseUrl );
  }

}
