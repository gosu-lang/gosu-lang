/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schemaparser.SoapVersion;
import gw.lang.reflect.LocationInfo;

public final class WsdlSoapBinding extends XmlSchemaObject<WsdlSoapBinding> {

  private final String _style;
  private final String _transport;
  private final SoapVersion _soapVersion;

  public WsdlSoapBinding( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String style, String transport, SoapVersion soapVersion ) {
    super( schemaIndex, locationInfo );
    _style = style;
    _transport = transport;
    _soapVersion = soapVersion;
  }

  public String getStyle() {
    return _style;
  }

  public String getTransport() {
    return _transport;
  }

  public SoapVersion getSoapVersion() {
    return _soapVersion;
  }
}
