/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.validator;

import gw.internal.ext.org.apache.xerces.impl.xpath.regex.RegularExpression;
import gw.internal.xml.xsd.typeprovider.schema.*;
import gw.xml.XmlSimpleValueException;
import gw.xml.XmlSimpleValue;
import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.xsd.typeprovider.XmlWhitespaceHandling;
import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.internal.xml.xsd.typeprovider.XmlWhitespaceHandling;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlSimpleValueException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XmlSimpleTypeSimpleValueValidator extends XmlSimpleValueValidator {

  private final XmlSimpleValueValidator _parent;
  private final XmlSchemaPrimitiveType _primitiveType;
  private final XmlSimpleValueFactory _valueFactory;
  private final List<ValidationStep> _validationSteps;
  private final XmlWhitespaceHandling _whitespace;

  public XmlSimpleTypeSimpleValueValidator( XmlSchemaPrimitiveType primitiveType, XmlSimpleValueValidator parent, List<XmlSchemaFacet> facets, XmlSimpleValueFactory valueFactory ) {
    _primitiveType = primitiveType;
    _parent = parent;
    _valueFactory = valueFactory;
    List<ValidationStep> validationSteps = new ArrayList<ValidationStep>();
    StringBuilder patterns = null;
    Set<String> enumerationOptions = null;
    XmlWhitespaceHandling whitespace = null;
    for ( XmlSchemaFacet facet : facets ) {
      if ( facet instanceof XmlSchemaEnumerationFacet ) {
        if ( enumerationOptions == null ) {
          enumerationOptions = new HashSet<String>(); // this simple type uses enumerations, so initialize with a mutable set
        }
        XmlSchemaEnumerationFacet enumFacet = (XmlSchemaEnumerationFacet) facet;
        XmlDeserializationContext context = new XmlDeserializationContext( null );
        for ( Map.Entry<String, String> entry : enumFacet.getNamespaceContext().entrySet() ) {
          context.addNamespace( entry.getKey(), entry.getValue() );
        }
        XmlSimpleValue simpleValue = _valueFactory.deserialize( context, facet.getValue(), false );
        enumerationOptions.add( simpleValue.getStringValue() );
      }
      else if ( facet instanceof XmlSchemaPatternFacet ) {
        if ( patterns == null ) {
          patterns = new StringBuilder();
        }
        else {
          patterns.append( '|' );
        }
        patterns.append( facet.getValue() );
      }
      else if ( facet instanceof XmlSchemaWhiteSpaceFacet ) {
        whitespace = XmlWhitespaceHandling.valueOf( facet.getValue() );
      }
      else {
        validationSteps.add( new FacetValidationStep( facet ) );
      }
    }
    if ( enumerationOptions != null ) {
      // validate that value matches enumeration
      final Set<String> fEnumerationOptions = enumerationOptions;
      validationSteps.add( new ValidationStep() {
        @Override
        public void validate( String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType ) {
          if ( ! fEnumerationOptions.contains( value ) ) {
            StringBuilder sb = new StringBuilder();
            for ( String option : fEnumerationOptions ) {
              if ( sb.length() > 0 ) {
                sb.append( ", " );
              }
              sb.append( option );
            }
            throw new XmlSimpleValueException( "value does not match enumeration [ " + sb + " ]" );
          }
        }
      } );
    }
    if ( patterns != null ) {
      final String regexString = patterns.toString();
      final RegularExpression regex = new RegularExpression( regexString, "X" );
      validationSteps.add( new ValidationStep() {
        @Override
        public void validate( String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType ) {
          if ( ! regex.matches( value ) ) {
            throw new XmlSimpleValueException( "value does not match pattern " + regexString );
          }
        }
      } );
    }
    _whitespace = whitespace;
    // reduce size of validation steps by reconstructing list
    _validationSteps = validationSteps.isEmpty() ? Collections.<ValidationStep>emptyList() : new ArrayList<ValidationStep>( validationSteps );
  }

  public void validate( String value, XmlSimpleValueValidationContext validationContext ) throws XmlSimpleValueException {
    final XmlSchemaPrimitiveType primitiveType = getPrimitiveType();
    String collapsedValue = collapseWhitespace( value, validationContext );
    primitiveType.validate( collapsedValue );
    for ( ValidationStep validationStep : _validationSteps ) {
      validationStep.validate( collapsedValue, validationContext, primitiveType );
    }
    if ( _parent != null ) {
      _parent.validate( value, validationContext );
    }
  }

  public XmlSchemaPrimitiveType getPrimitiveType() {
    return _primitiveType == null ? _parent.getPrimitiveType() : _primitiveType;
  }

  @Override
  protected XmlWhitespaceHandling getWhitespaceHandling( String value ) {
    if ( _whitespace != null ) {
      return _whitespace;
    }
    return _parent == null ? XmlWhitespaceHandling.preserve : _parent.getWhitespaceHandling( value );
  }

  private interface ValidationStep {
    void validate( String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType );
  }

  private class FacetValidationStep implements ValidationStep {

    private final XmlSchemaFacet _facet;

    public FacetValidationStep( XmlSchemaFacet facet ) {
      _facet = facet;
    }

    @Override
    public void validate( String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType ) {
      XmlSchemaFacetValidator.validate( _facet, collapseWhitespace( value, validationContext ), validationContext, primitiveType );
    }

  }
}
