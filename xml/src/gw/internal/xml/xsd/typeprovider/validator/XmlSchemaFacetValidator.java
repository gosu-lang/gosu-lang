/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.validator;

import gw.internal.ext.org.apache.xerces.impl.xpath.regex.RegularExpression;
import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaFractionDigitsFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaLengthFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMaxExclusiveFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMaxInclusiveFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMaxLengthFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMinExclusiveFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMinInclusiveFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMinLengthFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaPatternFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaTotalDigitsFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaWhiteSpaceFacet;
import gw.xml.XmlSimpleValueException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class XmlSchemaFacetValidator<T extends XmlSchemaFacet> {

  private static final Map<Class<? extends XmlSchemaFacet>, XmlSchemaFacetValidator> _validators = new HashMap<Class<? extends XmlSchemaFacet>, XmlSchemaFacetValidator>();

  static {
//    _validators.put( XmlSchemaEnumerationFacet.class, new XmlSchemaFacetValidator<XmlSchemaEnumerationFacet>() {
//      @Override
//      protected String validateInternal( XmlSchemaEnumerationFacet facet, String value ) {
//        // Handled elsewhere, so this facet should never be passed to this method
//      }
//    } );
    _validators.put( XmlSchemaFractionDigitsFacet.class, new XmlSchemaFacetValidator<XmlSchemaFractionDigitsFacet>() {
      @Override
      protected void validateInternal( XmlSchemaFractionDigitsFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        if ( primitiveType != XmlSchemaPrimitiveType.DECIMAL ) {
          throwDueToWrongPrimitiveType( "fractionDigits", primitiveType );
        }
        int idx = value.lastIndexOf( '.' );
        int fractionDigits = 0;
        if ( idx >= 0 ) {
          fractionDigits = value.length() - idx - 1;
        }
        int maxDigits = Integer.parseInt( facet.getValue() );
        if ( fractionDigits > maxDigits ) {
          throw new XmlSimpleValueException( "fractionDigits " + fractionDigits + " > " + maxDigits );
        }
      }
    } );
    _validators.put( XmlSchemaLengthFacet.class, new XmlSchemaFacetValidator<XmlSchemaLengthFacet>() {
      @Override
      protected void validateInternal( XmlSchemaLengthFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        int length = Integer.parseInt( facet.getValue() );
        Integer actualLength = getActualLength( "length", value, validationContext, primitiveType );
        if ( actualLength != null && actualLength != length ) {
          throw new XmlSimpleValueException( "length " + actualLength + " != " + length );
        }
      }
    } );
    _validators.put( XmlSchemaMaxExclusiveFacet.class, new XmlSchemaFacetValidator<XmlSchemaMaxExclusiveFacet>() {
      @Override
      protected void validateInternal( XmlSchemaMaxExclusiveFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        Comparator<String> comparator = primitiveType.getValueComparator();
        if ( comparator != null ) {
          int result = comparator.compare( value, facet.getValue() );
          if ( result >= 0 ) {
            throw new XmlSimpleValueException( "value must be less than " + facet.getValue() );
          }
        }
      }
    } );
    _validators.put( XmlSchemaMaxInclusiveFacet.class, new XmlSchemaFacetValidator<XmlSchemaMaxInclusiveFacet>() {
      @Override
      protected void validateInternal(XmlSchemaMaxInclusiveFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        Comparator<String> comparator = primitiveType.getValueComparator();
        if ( comparator != null ) {
          int result = comparator.compare( value, facet.getValue() );
          if ( result > 0 ) {
            throw new XmlSimpleValueException( "value must be no greater than " + facet.getValue() );
          }
        }
      }
    } );
    _validators.put( XmlSchemaMaxLengthFacet.class, new XmlSchemaFacetValidator<XmlSchemaMaxLengthFacet>() {
      @Override
      protected void validateInternal(XmlSchemaMaxLengthFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        int length = Integer.parseInt( facet.getValue() );
        Integer actualLength = getActualLength( "maxLength", value, validationContext, primitiveType );
        if ( actualLength != null && actualLength > length ) {
          throw new XmlSimpleValueException( "length " + actualLength + " > " + length );
        }
      }
    } );
    _validators.put( XmlSchemaMinLengthFacet.class, new XmlSchemaFacetValidator<XmlSchemaMinLengthFacet>() {
      @Override
      protected void validateInternal(XmlSchemaMinLengthFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        int length = Integer.parseInt( facet.getValue() );
        Integer actualLength = getActualLength( "minLength", value, validationContext, primitiveType );
        if ( actualLength != null && actualLength < length ) {
          throw new XmlSimpleValueException( "length " + actualLength + " < " + length );
        }
      }
    } );
    _validators.put( XmlSchemaMinExclusiveFacet.class, new XmlSchemaFacetValidator<XmlSchemaMinExclusiveFacet>() {
      @Override
      protected void validateInternal(XmlSchemaMinExclusiveFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        Comparator<String> comparator = primitiveType.getValueComparator();
        if ( comparator != null ) {
          int result = comparator.compare( value, facet.getValue() );
          if ( result <= 0 ) {
            throw new XmlSimpleValueException( "value must be greater than " + facet.getValue() );
          }
        }
      }
    } );
    _validators.put( XmlSchemaMinInclusiveFacet.class, new XmlSchemaFacetValidator<XmlSchemaMinInclusiveFacet>() {
      @Override
      protected void validateInternal(XmlSchemaMinInclusiveFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        Comparator<String> comparator = primitiveType.getValueComparator();
        if ( comparator != null ) {
          int result = comparator.compare( value, facet.getValue() );
          if ( result < 0 ) {
            throw new XmlSimpleValueException( "value must be no less than " + facet.getValue() );
          }
        }
      }
    } );
    _validators.put( XmlSchemaTotalDigitsFacet.class, new XmlSchemaFacetValidator<XmlSchemaTotalDigitsFacet>() {
      @Override
      protected void validateInternal(XmlSchemaTotalDigitsFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        // TODO dlank - disabling this impl since it's wrong. The spec doesn't restrict total digits in the output string,
        // rather it restricts total *significant* digits. In the case of a precisionDecimal, trailing zeroes are significant,
        // in a regular decimal they are not. totalDigits also does not restrict NaN or +/-INF values. precisionDecimal is new
        // to the spec and might be removed I believe, so we might not need to support that case. In any case, the whole point
        // of these validators is to give the user early feedback, rather than to strictly validate anything. For example, we
        // don't restrict lengths of xsd:list values at all, since it would prevent people from incrementally filling lists
        // in Gosu, so it might be ok to leave this disabled permanently.
        //        if ( primitiveType != XmlSchemaPrimitiveType.DECIMAL ) {
        //          throwDueToWrongPrimitiveType( "totalDigits", primitiveType );
        //        }
        //        int digits = Integer.parseInt( value );
        //        int actualDigits = 0;
        //        for ( int i = 0; i < value.length(); i++ ) {
        //          if ( Character.isDigit( value.charAt( i ) ) ) {
        //            actualDigits++;
        //          }
        //        }
        //        if ( actualDigits > digits ) {
        //          throw new XmlSimpleValueException( "number of digits " + actualDigits + " > " + digits );
        //        }
      }
    } );

    _validators.put( XmlSchemaWhiteSpaceFacet.class, new XmlSchemaFacetValidator<XmlSchemaWhiteSpaceFacet>() {
      @Override
      protected void validateInternal( XmlSchemaWhiteSpaceFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
        // handled elsewhere
      }
    } );
  }

  private static Integer getActualLength( String facetName, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType ) {
    Integer actualLength;
    switch ( primitiveType ) {
      case STRING:
      case ANYURI: {
        actualLength = value.length();
        break;
      }
      case HEXBINARY: {
        byte[] bytes = validationContext.getByteArray( primitiveType, value );
        actualLength = bytes.length;
        break;
      }
      case LIST: {
        // don't validate length, so users can incrementally add new members without issues
        actualLength = null;
        break;
      }
      default: {
        actualLength = 0;
        throwDueToWrongPrimitiveType( facetName, primitiveType );
        break;
      }
    }
    return actualLength;
  }

  private static void throwDueToWrongPrimitiveType( String facetName, XmlSchemaPrimitiveType primitiveType ) throws XmlSimpleValueException {
    throw new XmlSimpleValueException( "Found " + facetName + " constraint on simple type extending primitive datatype " + primitiveType.getQName() );
  }

  public static void validate(XmlSchemaFacet facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException {
    //noinspection unchecked
    getValidator( facet.getClass() ).validateInternal( facet, value, validationContext, primitiveType );
  }

  protected abstract void validateInternal(T facet, String value, XmlSimpleValueValidationContext validationContext, XmlSchemaPrimitiveType primitiveType) throws XmlSimpleValueException;

  private static XmlSchemaFacetValidator getValidator( Class clazz ) {
    return _validators.get( clazz );
  }

}
