/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.lang.reflect.IEnumValue;
import gw.xml.XmlSimpleValue;

import java.util.Map;

public interface IXmlSchemaEnumerationTypeData<T> extends IXmlSchemaTypeData<T> {

  IEnumValue deserialize( XmlSimpleValue value );

  XmlSimpleValueFactory getSimpleValueFactory();

  XmlSimpleValue getEnumSimpleValue( IEnumValue value );

  Map<IEnumValue, XmlSimpleValue> getEnumSimpleValues();

}
