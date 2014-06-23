/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.Set;

public final class WsdlSoapBody extends XmlSchemaObject<WsdlSoapBody> {

  private final String _use;
  private final Set<String> _parts;

  public WsdlSoapBody( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String use, Set<String> parts ) {
    super( schemaIndex, locationInfo );
    _use = use;
    _parts = parts;
  }

  public String getUse() {
    return _use;
  }

  public Set<String> getParts() {
    return _parts;
  }

}
