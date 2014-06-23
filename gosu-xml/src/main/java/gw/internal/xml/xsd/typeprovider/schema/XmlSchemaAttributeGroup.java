/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public final class XmlSchemaAttributeGroup extends XmlSchemaAttributeOrAttributeGroup<XmlSchemaAttributeGroup> {

  private final List<XmlSchemaAttributeOrAttributeGroup> _attributes;
  private final XmlSchemaAnyAttribute _anyAttribute;
  private QName _name;
  private final QName _refName;
  private boolean _resolvedAttributeGroups;

  public XmlSchemaAttributeGroup( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, List<XmlSchemaAttributeOrAttributeGroup> attributes, QName name, QName refName, XmlSchemaAnyAttribute anyAttribute ) {
    super( schemaIndex, locationInfo );
    _attributes = attributes;
    _name = name;
    _refName = refName;
    _anyAttribute = anyAttribute;
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

  public QName getName() {
    return _name;
  }

  public void setName( QName name ) {
    _name = name;
  }

  public XmlSchemaAnyAttribute getAnyAttribute() {
    return _anyAttribute;
  }

  @Override
  public XmlSchemaAttributeGroup resolveAttributeGroups() {
    if ( _refName == null ) {
      return this;
    }
    else {
      return getSchemaIndex().getXmlSchemaAttributeGroupByQName( _refName ).copy();
    }
  }

  @Override
  public XmlSchemaAttributeGroup copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaAttributeGroup( schemaIndex, getLocationInfo(), copyList( schemaIndex, getAttributes() ), _name, _refName, _anyAttribute == null? null : _anyAttribute.copy( schemaIndex ) );
  }

}
