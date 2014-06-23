/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.List;

public final class WsdlBindingOutput extends XmlSchemaObject<WsdlBindingOutput> {

  private final WsdlSoapBody _soapBody;
  private final List<WsdlSoapHeader> _soapHeaders;

  public WsdlBindingOutput( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, WsdlSoapBody soapBody, List<WsdlSoapHeader> soapHeaders ) {
    super( schemaIndex, locationInfo );
    _soapBody = soapBody;
    _soapHeaders = soapHeaders;
  }

  public WsdlSoapBody getSoapBody() {
    return _soapBody;
  }

  public List<WsdlSoapHeader> getSoapHeaders() {
    return _soapHeaders;
  }

}