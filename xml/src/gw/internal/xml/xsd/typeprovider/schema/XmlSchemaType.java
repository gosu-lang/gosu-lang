/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public abstract class XmlSchemaType<T extends XmlSchemaType> extends XmlSchemaObject<T> {

  private String _name;
  private QName _qname;

  public XmlSchemaType( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String name, QName qname ) {
    super( schemaIndex, locationInfo );
    _name = name;
    _qname = qname;
  }

  public String getName() {
    return _name;
  }

  public QName getQName() {
    return _qname;
  }

  public String toString() {
    return _qname.toString();
  }

  public void setName( String name ) {
    _name = name;
  }

  public void setQName( QName qname ) {
    _qname = qname;
  }

  public XmlSchemaAnyAttribute getAnyAttributeRecursiveIncludingSupertypes() {
    return null;
  }

  public XmlSchemaAny getAnyRecursiveIncludingSupertypes() {
    return null;
  }

}
