/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.validator;

import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.xsd.typeprovider.XmlWhitespaceHandling;
import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.xml.XmlSimpleValueException;

public class XmlSimpleListValueValidator extends XmlSimpleValueValidator {

  private XmlSimpleValueValidator _itemValidator;

  public XmlSimpleListValueValidator( XmlSimpleValueValidator itemValidator ) {
    _itemValidator = itemValidator;
  }

  @Override
  public void validate( String value, XmlSimpleValueValidationContext validationContext ) throws XmlSimpleValueException {
    String[] parts = value.split( " " );
    for ( String part : parts ) {
      if ( part.length() == 0 ) {
        continue;
      }
      _itemValidator.validate( part, new XmlSimpleValueValidationContext() );
    }
  }

  @Override
  public XmlSchemaPrimitiveType getPrimitiveType() {
    return XmlSchemaPrimitiveType.LIST;
  }

  @Override
  protected XmlWhitespaceHandling getWhitespaceHandling( String value ) {
    return XmlWhitespaceHandling.collapse;
  }

}
