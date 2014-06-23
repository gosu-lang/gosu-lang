/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public final class XmlSchemaSimpleTypeUnion extends XmlSchemaSimpleTypeContent<XmlSchemaSimpleTypeUnion> {

  private final List<QName> _memberTypesQNames;
  private final List<XmlSchemaSimpleType> _baseTypes;

  public XmlSchemaSimpleTypeUnion( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, List<QName> memberTypesQNames, List<XmlSchemaSimpleType> baseTypes ) {
    super( schemaIndex, locationInfo );
    _memberTypesQNames = memberTypesQNames;
    _baseTypes = baseTypes;
  }

  public List<QName> getMemberTypesQNames() {
    return _memberTypesQNames;
  }

  public List<XmlSchemaSimpleType> getBaseTypes() {
    return _baseTypes;
  }

  @Override
  public XmlSchemaSimpleTypeUnion copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaSimpleTypeUnion( schemaIndex, getLocationInfo(), _memberTypesQNames, copyList( schemaIndex, _baseTypes ) );
  }

}
