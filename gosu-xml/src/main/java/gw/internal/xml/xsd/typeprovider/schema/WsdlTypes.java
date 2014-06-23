/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.List;

public final class WsdlTypes extends XmlSchemaObject<WsdlTypes> {

  private List<XmlSchema> _schemas = null;

  public WsdlTypes(XmlSchemaIndex index, LocationInfo locationInfo, List<XmlSchema> schemas){
    super( index, locationInfo );
    _schemas = schemas;
  }

  public List<XmlSchema> getSchemas() {
    return _schemas;
  }

  public void setSchemas(List<XmlSchema> schemas) {
    _schemas = schemas;
  }

}
