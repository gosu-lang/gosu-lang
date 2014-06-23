/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;

public interface IXmlSchemaTypeData<T> extends IXmlTypeData {

  XmlSchemaObject getSchemaObject();

  boolean isAnonymous();

  T getContext();

  void maybeInit();

  XmlSchemaIndex<T> getSchemaIndex();

}
