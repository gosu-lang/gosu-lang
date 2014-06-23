/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class XmlSchemaAttribute extends XmlSchemaAttributeOrAttributeGroup {
  private final XmlSchemaSimpleType _schemaType;
  private final QName _schemaTypeName;
  private final QName _refName;
  private final QName _qname;
  private final String _fixedValue;
  private final String _defaultValue;
  private final boolean _prohibited;

  public XmlSchemaAttribute( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, XmlSchemaSimpleType schemaType, QName schemaTypeName, QName refName, QName qname, String fixedValue, String defaultValue, boolean prohibited ) {
    super( schemaIndex, locationInfo );
    _schemaType = schemaType;
    _schemaTypeName = schemaTypeName;
    _refName = refName;
    _qname = qname;
    _fixedValue = fixedValue;
    _defaultValue = defaultValue;
    _prohibited = prohibited;
  }

  public XmlSchemaSimpleType getSchemaType() {
    return _schemaType;
  }

  public QName getSchemaTypeName() {
    return _schemaTypeName;
  }

  public QName getRefName() {
    return _refName;
  }

  public QName getQName() {
    return _qname;
  }

  public String getFixedValue() {
    return _fixedValue;
  }

  public String getDefaultValue() {
    return _defaultValue;
  }

  public boolean isProhibited() {
    return _prohibited;
  }

  @Override
  public XmlSchemaAttribute copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaAttribute( schemaIndex, getLocationInfo(), _schemaType == null ? null : _schemaType.copy( schemaIndex ), _schemaTypeName, _refName, _qname, _fixedValue, _defaultValue, _prohibited );
  }


}
