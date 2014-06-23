/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.ArrayList;
import java.util.List;

public final class XmlSchemaAll extends XmlSchemaParticle<XmlSchemaAll> {

  private final List<XmlSchemaElement> _items;

  public XmlSchemaAll( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, List<XmlSchemaElement> items, long minOccurs, long maxOccurs ) {
    super( schemaIndex, locationInfo, minOccurs, maxOccurs );
    _items = items;
  }

  public List<XmlSchemaElement> getItems() {
    return _items;
  }


  @Override
  public XmlSchemaAll copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaAll( schemaIndex, getLocationInfo(), copyList( schemaIndex, _items ), getMinOccurs(), getMaxOccurs() );
  }
}
