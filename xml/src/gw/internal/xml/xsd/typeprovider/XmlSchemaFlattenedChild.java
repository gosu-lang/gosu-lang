/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAny;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;

public class XmlSchemaFlattenedChild {

  private final XmlSchemaObject _xmlSchemaObject;
  private final boolean _plural;

  public XmlSchemaFlattenedChild( XmlSchemaObject xmlSchemaObject, boolean plural ) {
    if ( ! ( xmlSchemaObject instanceof XmlSchemaElement || xmlSchemaObject instanceof XmlSchemaAttribute || xmlSchemaObject instanceof XmlSchemaAny ) ) {
      throw new IllegalArgumentException( "INTERNAL ERROR: Unsupported xml schema object: " + xmlSchemaObject );
    }
    _xmlSchemaObject = xmlSchemaObject;
    _plural = plural;
  }

  public XmlSchemaObject getXmlSchemaObject() {
    return _xmlSchemaObject;
  }

  public boolean isPlural() {
    return _plural;
  }
}
