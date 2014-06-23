/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

public abstract class XmlSchemaTypeData<T> extends XmlTypeData implements IXmlSchemaTypeData<T> {

  private final XmlSchemaIndex<T> _schemaIndex;

  public XmlSchemaTypeData( XmlSchemaIndex<T> schemaIndex ) {
    _schemaIndex = schemaIndex;
  }

  @Override
  public XmlSchemaIndex<T> getSchemaIndex() {
    return _schemaIndex;
  }

}
