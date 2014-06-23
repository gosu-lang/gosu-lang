/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public final class WsdlMessage extends XmlSchemaObject<WsdlMessage> {
  private final Map<String, WsdlPart> _partsByName;
  private final QName _qname;
  private final List<WsdlPart> _parts;

  public WsdlMessage( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, QName qname, Map<String, WsdlPart> partsByName, List<WsdlPart> parts ) {
    super( schemaIndex, locationInfo );
    _qname = qname;
    _partsByName = partsByName;
    _parts = parts;
  }

  public WsdlPart getPartByName( String partName ) {
    return _partsByName.get( partName );
  }

  public QName getQName() {
    return _qname;
  }

  public List<WsdlPart> getParts() {
    return _parts;
  }
  
}
