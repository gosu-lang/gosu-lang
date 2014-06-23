/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public abstract class XmlSchemaFacet<T extends XmlSchemaFacet> extends XmlSchemaObject<T> {

  private final String _value;

  public XmlSchemaFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo );
    _value = value;
  }

  public final String getValue() {
    return _value;
  }

}
