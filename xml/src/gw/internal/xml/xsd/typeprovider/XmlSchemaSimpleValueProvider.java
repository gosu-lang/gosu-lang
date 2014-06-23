/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleValueValidator;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;

public abstract class XmlSchemaSimpleValueProvider {

  public abstract XmlSimpleValueValidator getValidator();

  public abstract XmlSimpleValueFactory getSimpleValueFactory();

  public abstract boolean hasSimpleContent();

  public IType getSimpleTypePropertyGosuType() {
    IType propType = null;
    if ( hasSimpleContent() ) {
      propType = getSimpleValueFactory().getGosuValueType();
    }
    return propType;
  }

  public IType getSimpleTypePropertyGosuType( boolean plural ) {
    IType propType = getSimpleTypePropertyGosuType();
    if ( propType != null && plural ) {
      propType = JavaTypes.LIST().getParameterizedType( propType );
    }
    return propType;
  }

}
