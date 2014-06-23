/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.List;

import javax.xml.namespace.QName;

public final class XmlSchemaSimpleTypeRestriction extends XmlSchemaSimpleTypeContent<XmlSchemaSimpleTypeRestriction> {

  private final XmlSchemaSimpleType _baseType;
  private final QName _baseTypeName;
  private final List<XmlSchemaFacet> _facets;

  public XmlSchemaSimpleTypeRestriction( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, XmlSchemaSimpleType baseType, QName baseTypeName, List<XmlSchemaFacet> facets ) {
    super( schemaIndex, locationInfo );
    _baseType = baseType;
    _baseTypeName = baseTypeName;
    _facets = facets;
  }

  public XmlSchemaSimpleType getBaseType() {
    return  _baseType;
  }

  public QName getBaseTypeName() {
    return _baseTypeName;
  }

  public List<XmlSchemaFacet> getFacets() {
    return _facets;
  }

   @Override
  public XmlSchemaSimpleTypeRestriction copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaSimpleTypeRestriction( schemaIndex, getLocationInfo(), _baseType == null ? null : _baseType.copy( schemaIndex ), _baseTypeName, copyList( schemaIndex, _facets) );
  }

}
