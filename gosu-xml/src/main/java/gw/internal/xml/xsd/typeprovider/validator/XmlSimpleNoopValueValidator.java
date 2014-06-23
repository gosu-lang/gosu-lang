/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.validator;

import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.xsd.typeprovider.XmlWhitespaceHandling;
import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.xml.XmlSimpleValueException;

public class XmlSimpleNoopValueValidator extends XmlSimpleValueValidator {

  @Override
  public void validate(String value, XmlSimpleValueValidationContext validationContext ) throws XmlSimpleValueException {
  }

  @Override
  public XmlSchemaPrimitiveType getPrimitiveType() {
    return XmlSchemaPrimitiveType.STRING;
  }

  @Override
  protected XmlWhitespaceHandling getWhitespaceHandling( String value ) {
    return XmlWhitespaceHandling.preserve;
  }

}
