/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class XmlSchemaGroup extends XmlSchemaParticle<XmlSchemaGroup> {

  private XmlSchemaParticle<?> _particle;
  private QName _name;
  private final QName _refName;
  private boolean _resolvedGroups;

  public XmlSchemaGroup( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, XmlSchemaParticle<?> particle, QName name, QName refName, long minOccurs, long maxOccurs ) {
    super( schemaIndex, locationInfo, minOccurs, maxOccurs );
    _particle = particle;
    _name = name;
    _refName = refName;
  }

  public XmlSchemaParticle<?> getParticle() {
    if ( ! _resolvedGroups ) {
      _particle = _particle == null ? null : _particle.resolveGroups();
      _resolvedGroups = true;
    }
    return _particle;
  }

  public QName getName() {
    return _name;
  }

  public void setName( QName name ) {
    _name = name;
  }

  @Override
  public XmlSchemaGroup copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaGroup( schemaIndex, getLocationInfo(), getParticle() == null ? null : getParticle().copy( schemaIndex ), _name, _refName, getMinOccurs(), getMaxOccurs() );
  }

  @Override
  public XmlSchemaGroup resolveGroups() {
    if ( _refName == null ) {
      return this;
    }
    else {
      XmlSchemaGroup group = getSchemaIndex().getXmlSchemaGroupByQName( _refName ).copy();
      group.setMinOccurs( getMinOccurs() );
      group.setMaxOccurs( getMaxOccurs() );
      return group;
    }
  }

}
