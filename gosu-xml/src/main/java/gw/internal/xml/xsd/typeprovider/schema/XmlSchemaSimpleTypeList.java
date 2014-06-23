/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class XmlSchemaSimpleTypeList extends XmlSchemaSimpleTypeContent<XmlSchemaSimpleTypeList> {

  private final QName _itemTypeName;
  private final XmlSchemaSimpleType _itemType;

  public XmlSchemaSimpleTypeList( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, QName itemTypeName, XmlSchemaSimpleType itemType ) {
    super( schemaIndex, locationInfo );
    _itemTypeName = itemTypeName;
    _itemType = itemType;
  }

  public QName getItemTypeName() {
    return _itemTypeName;
  }

  public XmlSchemaSimpleType getItemType() {
    return _itemType;
  }

  @Override
  public XmlSchemaSimpleTypeList copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaSimpleTypeList( schemaIndex, getLocationInfo(), _itemTypeName, _itemType == null ? null : _itemType.copy( schemaIndex ) );
  }
  
}
