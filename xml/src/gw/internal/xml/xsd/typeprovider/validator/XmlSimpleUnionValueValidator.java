/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.validator;

import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.xsd.typeprovider.XmlWhitespaceHandling;
import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.xml.XmlSimpleValueException;

import java.util.ArrayList;
import java.util.List;

public class XmlSimpleUnionValueValidator extends XmlSimpleValueValidator {

  private final List<XmlSimpleValueValidator> _validators;

  public XmlSimpleUnionValueValidator(List<XmlSimpleValueValidator> validators) {
    _validators = validators;
  }

  @Override
  public void validate( String value, XmlSimpleValueValidationContext validationContext ) throws XmlSimpleValueException {
    getMatchingValidator( value, validationContext );
  }

  private XmlSimpleValueValidator getMatchingValidator( String value, XmlSimpleValueValidationContext validationContext ) {
    List<XmlSimpleValueException> exceptions = new ArrayList<XmlSimpleValueException>( 0 );
    for ( XmlSimpleValueValidator validator : _validators ) {
      try {
        validator.validate( value, validationContext );
        return validator;
      }
      catch ( XmlSimpleValueException ex ) {
        exceptions.add( ex );
      }
    }
    StringBuilder sb = new StringBuilder( "value does not match any of xsd:union members " );
    boolean gotOne = false;
    for ( XmlSimpleValueException ex : exceptions ) {
      if ( gotOne ) {
        sb.append( ", " );
      }
      sb.append( "[ " );
      sb.append( ex.getMessage() );
      sb.append( " ]" );
      gotOne = true;
    }
    throw new XmlSimpleValueException( sb.toString(), exceptions );
  }

  @Override
  public XmlSchemaPrimitiveType getPrimitiveType() {
    return XmlSchemaPrimitiveType.UNION;
  }

  @Override
  protected XmlWhitespaceHandling getWhitespaceHandling( String value ) {
    // have to figure out which union component matches in order to determine whitespace facet
    XmlSimpleValueValidator validator = getMatchingValidator( value, new XmlSimpleValueValidationContext() );
    return validator.getWhitespaceHandling( value );
  }

}
