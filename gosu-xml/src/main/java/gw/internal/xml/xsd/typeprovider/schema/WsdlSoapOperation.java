/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public final class WsdlSoapOperation extends XmlSchemaObject<WsdlSoapOperation> {

  private final String _soapAction;

  public WsdlSoapOperation( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String soapAction ) {
    super( schemaIndex, locationInfo );
    _soapAction = soapAction;
  }

  public String getSoapAction() {
    return _soapAction;
  }

}
