/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

public enum XmlSchemaPropertyType {

  ELEMENT( "_Element" ),
  ATTRIBUTE( "_Attribute" );

  private final String _suffix;

  XmlSchemaPropertyType( String suffix ) {
    _suffix = suffix;
  }

  public String getSuffix() {
    return _suffix;
  }
}
