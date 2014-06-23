/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public abstract class XmlSchemaParticle<T extends XmlSchemaParticle> extends XmlSchemaObject<T> {

  private long _minOccurs;
  private long _maxOccurs;

  public XmlSchemaParticle( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, long minOccurs, long maxOccurs ) {
    super( schemaIndex, locationInfo );
    _minOccurs = minOccurs;
    _maxOccurs = maxOccurs;
  }

  public long getMinOccurs() {
    return _minOccurs;
  }

  public long getMaxOccurs() {
    return _maxOccurs;
  }

  public void setMinOccurs( long minOccurs ) {
    _minOccurs = minOccurs;
  }

  public void setMaxOccurs( long maxOccurs ) {
    _maxOccurs = maxOccurs;
  }

  public XmlSchemaParticle resolveGroups() {
    return this;
  }
}
