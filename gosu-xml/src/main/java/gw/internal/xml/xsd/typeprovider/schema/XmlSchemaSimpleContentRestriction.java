/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public final class XmlSchemaSimpleContentRestriction extends XmlSchemaContent {

  private final List<XmlSchemaAttributeOrAttributeGroup> _attributes;
  private final QName _baseTypeName;
  private final XmlSchemaType<?> _baseType;
  private final List<XmlSchemaFacet> _facets;
  private boolean _resolvedAttributeGroups;

  public XmlSchemaSimpleContentRestriction( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, List<XmlSchemaAttributeOrAttributeGroup> attributes, QName baseTypeName, XmlSchemaType<?> baseType, List<XmlSchemaFacet> facets ) {
    super( schemaIndex, locationInfo );
    _attributes = attributes;
    _baseTypeName = baseTypeName;
    _baseType = baseType;
    _facets = facets;
  }

  public List<XmlSchemaFacet> getFacets() {
    return _facets;
  }

  public QName getBaseTypeName() {
    return _baseTypeName;
  }

  public XmlSchemaType getBaseType() {
    return _baseType;
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

  @Override
  public XmlSchemaSimpleContentRestriction copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaSimpleContentRestriction( schemaIndex, getLocationInfo(), copyList( schemaIndex, _attributes ), _baseTypeName, _baseType == null ? null : _baseType.copy( schemaIndex ), copyList( schemaIndex, _facets ) );
  }
}
