/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.ArrayList;
import java.util.List;

public final class XmlSchemaSequence extends XmlSchemaParticle<XmlSchemaSequence> {

  private final List<XmlSchemaParticle> _items;
  private boolean _resolvedGroups;

  public XmlSchemaSequence( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, List<XmlSchemaParticle> items, long minOccurs, long maxOccurs ) {
    super( schemaIndex, locationInfo, minOccurs, maxOccurs );
    _items = items;
  }

  public List<XmlSchemaParticle> getItems() {
    if ( ! _resolvedGroups ) {
      for ( int i = 0; i < _items.size(); i++ ) {
        _items.set( i, _items.get( i ).resolveGroups() );

      }
      _resolvedGroups = true;
    }
    return _items;
  }


  @Override
  public XmlSchemaSequence copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaSequence( schemaIndex, getLocationInfo(), copyList( schemaIndex, _items ), getMinOccurs(), getMaxOccurs() );
  }
}
