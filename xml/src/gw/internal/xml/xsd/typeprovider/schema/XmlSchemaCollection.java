/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.NotFoundException;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.xml.namespace.QName;

public final class XmlSchemaCollection {

  private LinkedHashSet<XmlSchema> _schemas = new LinkedHashSet<XmlSchema>();

  public LinkedHashSet<XmlSchema> getXmlSchemas() {
    return _schemas;
  }

  public XmlSchemaElement getElementByQName( QName name ) {
    XmlSchemaElement element = getElementByQNameIfValid( name );
    if ( element == null ) {
      throw new NotFoundException( "Element not found for QName: " + name );
    }
    return element;
  }

  public XmlSchemaElement getElementByQNameIfValid( QName name ) {
    XmlSchemaElement element = null;
    for ( XmlSchema schema : _schemas ) {
      element = schema.getElements().get( name );
      if ( element != null ) {
        break;
      }
    }
    return element;
  }

  public XmlSchemaType getTypeByQName( QName name ) {
    XmlSchemaType type = getTypeByQNameIfValid( name );
    if ( type == null ) {
      throw new NotFoundException( "Type not found for QName: " + name );
    }
    return type;
  }

  public XmlSchemaType getTypeByQNameIfValid( QName name ) {
    XmlSchemaType type = null;
    for ( XmlSchema schema : _schemas ) {
      type = schema.getTypes().get( name );
      if ( type != null ) {
        break;
      }
    }
    return type;
  }

  public XmlSchemaAttribute getAttributeByQName( QName name ) {
    XmlSchemaAttribute attribute = getAttributeByQNameIfValid( name );
    if ( attribute == null ) {
      throw new NotFoundException( "Attribute not found for QName: " + name );
    }
    return attribute;
  }

  public XmlSchemaAttribute getAttributeByQNameIfValid( QName name ) {
    XmlSchemaAttribute attribute = null;
    for ( XmlSchema schema : _schemas ) {
      attribute = schema.getAttributes().get( name );
      if ( attribute != null ) {
        break;
      }
    }
    return attribute;
  }

  public boolean addSchemas( Collection<XmlSchema> schemas ) {
    return _schemas.addAll( schemas );
  }

}
