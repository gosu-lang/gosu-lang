/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import java.lang.reflect.Constructor;

public interface IXmlSchemaTypeInstanceTypeData<T> extends IXmlSchemaTypeData<T> {
  
  XmlSchemaTypeSchemaInfo getSchemaInfo();

  Constructor<?> getConstructorInternal();

}