/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public abstract class XmlSchemaContentModel<T extends XmlSchemaContentModel> extends XmlSchemaObject<T> {

  public XmlSchemaContentModel( XmlSchemaIndex schemaIndex, LocationInfo locationInfo ) {
    super( schemaIndex, locationInfo );
  }

  public XmlSchemaContent getContent() {
    throw new UnsupportedOperationException();
  }

}
