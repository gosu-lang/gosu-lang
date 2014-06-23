/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public final class XmlSchemaComplexContentRestriction extends XmlSchemaContent<XmlSchemaComplexContentRestriction> {

  private XmlSchemaParticle<?> _particle;
  private final List<XmlSchemaAttributeOrAttributeGroup> _attributes;
  private final QName _baseTypeName;
  private final XmlSchemaAnyAttribute _anyAttribute;
  private boolean _resolvedGroups;
  private boolean _resolvedAttributeGroups;

  public XmlSchemaComplexContentRestriction( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, XmlSchemaParticle<?> particle, List<XmlSchemaAttributeOrAttributeGroup> attributes, QName baseTypeName, XmlSchemaAnyAttribute anyAttribute ) {
    super( schemaIndex, locationInfo );
    _particle = particle;
    _attributes = attributes;
    _baseTypeName = baseTypeName;
    _anyAttribute = anyAttribute;
  }

  public XmlSchemaParticle getParticle() {
    if ( ! _resolvedGroups ) {
      _particle = _particle == null ? null : _particle.resolveGroups();
      _resolvedGroups = true;
    }
    return _particle;
  }

  public List<XmlSchemaAttributeOrAttributeGroup> getAttributes() {
    if ( ! _resolvedAttributeGroups ) {
      for ( int i = 0; i < _attributes.size(); i++ ) {
        _attributes.set( i, _attributes.get( i ).resolveAttributeGroups() );
      }
      _resolvedAttributeGroups = true;
    }
    return _attributes;
  }

  public QName getBaseTypeName() {
    return _baseTypeName;
  }

  public XmlSchemaAnyAttribute getAnyAttribute() {
    return _anyAttribute;
  }

  @Override
  public XmlSchemaComplexContentRestriction copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaComplexContentRestriction( schemaIndex, getLocationInfo(), _particle == null ? null : _particle.copy( schemaIndex ), copyList(schemaIndex, _attributes), _baseTypeName, _anyAttribute == null ? null : _anyAttribute.copy( schemaIndex ) );
  }
}
