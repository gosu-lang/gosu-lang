/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.validator;

import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.xsd.typeprovider.XmlWhitespaceHandling;
import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.xml.XmlSimpleValueException;

public abstract class XmlSimpleValueValidator {

  public abstract void validate( String value, XmlSimpleValueValidationContext validationContext ) throws XmlSimpleValueException;

  public abstract XmlSchemaPrimitiveType getPrimitiveType();

  protected abstract XmlWhitespaceHandling getWhitespaceHandling( String value );

  public final String collapseWhitespace( String value, XmlSimpleValueValidationContext validationContext ) {
    XmlWhitespaceHandling whitespaceHandling;
    switch ( getPrimitiveType() ) {
      case STRING:
      case UNION: {
        whitespaceHandling = getWhitespaceHandling( value );
        break;
      }
      default: {
        whitespaceHandling = XmlWhitespaceHandling.collapse;
      }
    }
    switch ( whitespaceHandling ) {
      case collapse: value = validationContext.doCollapse( value ); break;
      case replace: value = validationContext.doReplace( value ); break;
    }
    return value;
  }

}
