/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public abstract class XmlSchemaSimpleTypeContent<T extends XmlSchemaSimpleTypeContent> extends XmlSchemaObject<T> {

  public XmlSchemaSimpleTypeContent( XmlSchemaIndex schemaIndex, LocationInfo locationInfo ) {
    super( schemaIndex, locationInfo );
  }

}
