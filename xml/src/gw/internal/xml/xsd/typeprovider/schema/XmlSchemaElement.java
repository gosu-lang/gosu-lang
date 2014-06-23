/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class XmlSchemaElement extends XmlSchemaParticle<XmlSchemaElement> {
  private final QName _schemaTypeName;
  private final XmlSchemaType<?> _schemaType;
  private final QName _substitutionGroup;
  private final QName _refName;
  private final QName _qname;
  private final String _name;
  private final boolean _nillable;
  private final String _gwViewAs;
  private final boolean _topLevel;


  public XmlSchemaElement( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, QName schemaTypeName, XmlSchemaType<?> schemaType, QName substitutionGroup, QName refName, QName qname, String name, boolean nillable, long minOccurs, long maxOccurs, String gwViewAs, boolean topLevel ) {
    super( schemaIndex, locationInfo, minOccurs, maxOccurs );
    _schemaTypeName = schemaTypeName;
    _schemaType = schemaType;
    _substitutionGroup = substitutionGroup;
    _refName = refName;
    _qname = qname;
    _name = name;
    _nillable = nillable;
    _gwViewAs = gwViewAs;
    _topLevel = topLevel;
  }

  public QName getSchemaTypeName() {
    return _schemaTypeName;
  }

  public XmlSchemaType getSchemaType() {
    return _schemaType;
  }

  public QName getSubstitutionGroup() {
    return _substitutionGroup;
  }

  public QName getRefName() {
    return _refName;
  }

  public QName getQName() {
    return _qname;
  }

  public String getName() {
    return _name;
  }

  public boolean isNillable() {
    return _nillable;
  }

  public String getGwViewAs() {
    return _gwViewAs;
  }

  @Override
  public String toString() {
    QName qname = getQName();
    if ( qname == null ) {
      qname = getRefName();
    }
    return qname.toString();
  }

  public boolean isTopLevel() {
    return _topLevel;
  }

  @Override
  public XmlSchemaElement copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaElement( schemaIndex, getLocationInfo(), _schemaTypeName, _schemaType == null ? null : _schemaType.copy( schemaIndex ), _substitutionGroup, _refName, _qname, _name, _nillable, getMinOccurs(), getMaxOccurs(), _gwViewAs, _topLevel );
  }
  
}
