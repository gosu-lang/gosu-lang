/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public final class XmlSchemaSimpleContentExtension extends XmlSchemaContent<XmlSchemaSimpleContentExtension> {

  private final List<XmlSchemaAttributeOrAttributeGroup> _attributes;
  private final QName _baseTypeName;
  private boolean _resolvedAttributeGroups;

  public XmlSchemaSimpleContentExtension( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, List<XmlSchemaAttributeOrAttributeGroup> attributes, QName baseTypeName ) {
    super( schemaIndex, locationInfo );
    _attributes = attributes;
    _baseTypeName = baseTypeName;
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

  @Override
  public XmlSchemaSimpleContentExtension copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaSimpleContentExtension( schemaIndex, getLocationInfo(), copyList( schemaIndex, _attributes ), _baseTypeName );
  }
}
