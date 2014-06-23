/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public abstract class XmlSchemaAttributeOrAttributeGroup<T extends XmlSchemaAttributeOrAttributeGroup> extends XmlSchemaObject<T> {

  public XmlSchemaAttributeOrAttributeGroup( XmlSchemaIndex schemaIndex, LocationInfo locationInfo ) {
    super( schemaIndex, locationInfo );
  }

  public XmlSchemaAttributeOrAttributeGroup resolveAttributeGroups() {
    return this;
  }

}
