/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.internal.xml.xsd.typeprovider.IXmlSchemaEnumerationTypeData;
import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaFacet;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.QNameSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSchemaEnumSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSchemaListSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleTypeSimpleValueValidator;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleValueValidator;
import gw.lang.reflect.TypeSystem;
import gw.xml.BinaryData;
import gw.xml.IXmlSchemaEnumValue;
import gw.xml.XmlElement;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlSimpleValueException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSimpleValueInternals {

  private static final XmlSimpleValueInternals _instance = new XmlSimpleValueInternals();

  public static XmlSimpleValueInternals instance() {
    return _instance;
  }
  public XmlSimpleValue makeBooleanInstance( Boolean value ) {
    return value == null ? null : XmlSimpleValueFactory.BOOLEAN_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeBooleanInstance( String value ) {
    return value == null ? null : XmlSimpleValueFactory.BOOLEAN_INSTANCE.deserialize( value );
  }

  public XmlSimpleValue makeDecimalInstance( BigDecimal value ) {
    return value == null ? null : XmlSimpleValueFactory.DECIMAL_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeDoubleInstance( Double value ) {
    return value == null ? null : XmlSimpleValueFactory.DOUBLE_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeFloatInstance( Float value ) {
    return value == null ? null : XmlSimpleValueFactory.FLOAT_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeIntegerInstance( BigInteger value ) {
    return value == null ? null : XmlSimpleValueFactory.INTEGER_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeLongInstance( Long value ) {
    return value == null ? null : XmlSimpleValueFactory.LONG_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeIntInstance( Integer value ) {
    return value == null ? null : XmlSimpleValueFactory.INT_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeShortInstance( Short value ) {
    return value == null ? null : XmlSimpleValueFactory.SHORT_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeByteInstance( Byte value ) {
    return value == null ? null : XmlSimpleValueFactory.BYTE_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeUnsignedLongInstance( BigInteger value ) {
    return value == null ? null : XmlSimpleValueFactory.UNSIGNEDLONG_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeUnsignedIntInstance( Long value ) {
    return value == null ? null : XmlSimpleValueFactory.UNSIGNEDINT_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeUnsignedShortInstance( Integer value ) {
    return value == null ? null : XmlSimpleValueFactory.UNSIGNEDSHORT_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeUnsignedByteInstance( Short value ) {
    return value == null ? null : XmlSimpleValueFactory.UNSIGNEDBYTE_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeStringInstance( String value ) {
    return value == null ? null : XmlSimpleValueFactory.STRING_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeEncodedStringInstance( String value ) {
    return value == null ? null : XmlSimpleValueFactory.ENCODED_STRING_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeDateInstance( gw.xml.date.XmlDate value ) {
    return value == null ? null : XmlSimpleValueFactory.DATE_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeDateTimeInstance( gw.xml.date.XmlDateTime value ) {
    return value == null ? null : XmlSimpleValueFactory.DATETIME_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeTimeInstance( gw.xml.date.XmlTime value ) {
    return value == null ? null : XmlSimpleValueFactory.TIME_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeGYearMonthInstance( gw.xml.date.XmlYearMonth value ) {
    return value == null ? null : XmlSimpleValueFactory.GYEARMONTH_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeGYearInstance( gw.xml.date.XmlYear value ) {
    return value == null ? null : XmlSimpleValueFactory.GYEAR_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeGMonthDayInstance( gw.xml.date.XmlMonthDay value ) {
    return value == null ? null : XmlSimpleValueFactory.GMONTHDAY_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeGDayInstance( gw.xml.date.XmlDay value ) {
    return value == null ? null : XmlSimpleValueFactory.GDAY_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeGMonthInstance( gw.xml.date.XmlMonth value ) {
    return value == null ? null : XmlSimpleValueFactory.GMONTH_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeDurationInstance( gw.xml.date.XmlDuration value ) {
    return value == null ? null : XmlSimpleValueFactory.DURATION_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeBase64BinaryInstance( byte[] value ) {
    if ( value == null ) {
      return null;
    }
    BinaryData provider = new BinaryData();
    provider.setBytes( value );
    return makeBase64BinaryInstance( provider );
  }

  public XmlSimpleValue makeBase64BinaryInstance( BinaryData value ) {
    return value == null ? null : XmlSimpleValueFactory.BASE64BINARY_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeHexBinaryInstance( byte[] value ) {
    return value == null ? null : XmlSimpleValueFactory.HEXBINARY_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeAnyURIInstance( URI value ) {
    return value == null ? null : XmlSimpleValueFactory.ANYURI_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeQNameInstance( QName value, boolean validating ) {
    if ( validating ) {
      return value == null ? null : XmlSimpleValueFactory.QNAME_INSTANCE.gosuValueToStorageValue( value );
    }
    else {
      return value == null ? null : XmlSimpleValueFactory.NONVALIDATING_QNAME_INSTANCE.gosuValueToStorageValue( value );
    }
  }

  public XmlSimpleValue makeQNameInstance( String value, Map<String, String> namespaceContext, boolean validating ) {
    XmlDeserializationContext context = new XmlDeserializationContext( null );
    for ( Map.Entry<String, String> entry : namespaceContext.entrySet() ) {
      context.addNamespace( entry.getKey(), entry.getValue() );
    }
    if ( validating ) {
      return value == null ? null : XmlSimpleValueFactory.QNAME_INSTANCE.deserialize( context, value, false );
    }
    else {
      return value == null ? null : XmlSimpleValueFactory.NONVALIDATING_QNAME_INSTANCE.deserialize( context, value, false );
    }
  }

  public XmlSimpleValue makeIDInstance( String value ) {
    return value == null ? null : XmlSimpleValueFactory.ID_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeIDREFInstance( XmlElement value ) {
    return value == null ? null : XmlSimpleValueFactory.IDREF_INSTANCE.gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeEnumInstance( IXmlSchemaEnumValue value ) {
    return value == null ? null : new XmlSchemaEnumSimpleValueFactory( (IXmlSchemaEnumerationTypeData) TypeSystem.getFromObject( value ) ).gosuValueToStorageValue( value );
  }

  public XmlSimpleValue makeListOfQNameInstance( List<QName> value ) {
    return value == null ? null : new XmlSchemaListSimpleValueFactory( XmlSimpleValueFactory.QNAME_INSTANCE, new XmlSimpleTypeSimpleValueValidator( XmlSchemaPrimitiveType.QNAME, null, Collections.<XmlSchemaFacet>emptyList(), XmlSimpleValueFactory.QNAME_INSTANCE ) ).gosuValueToStorageValue( value );
  }

  public final void writeTo( XmlSimpleValue simpleValue, IXMLWriter writer, XmlSerializationContext context ) throws IOException {
    writer.addText( serialize( simpleValue, context ) );
  }

  public final String serialize( XmlSimpleValue simpleValue, XmlSerializationContext context ) {
    String value = ( (XmlSimpleValueBase) simpleValue )._serialize( context );
    if ( value == null ) {
      throw new RuntimeException( "Null string value" );
    }
    return value;
  }

  public void validate( XmlSimpleValue simpleValue, XmlSimpleValueValidator validator, XmlSimpleValueValidationContext context ) {
    // don't validate base64Binary values - they might be using XOP ( SOAP with attachments )
    if ( validator.getPrimitiveType() != XmlSchemaPrimitiveType.BASE64BINARY ) {
      String value = simpleValue.getStringValue();
      if ( value != null ) {
        try {
          validator.validate( value, context );
        }
        catch ( XmlSimpleValueException ex ) {
          throw new XmlSimpleValueException( "Value '" + value + "' violated one or more facet constraints of simple type definition: " + ex.getMessage(), ex );
        }
      }
    }
  }

  public String _getStringValue( XmlSimpleValue xmlSimpleValue, boolean isEnumCode ) {
    return ( (XmlSimpleValueBase) xmlSimpleValue )._getStringValue( isEnumCode );
  }

  public Object _getGosuValue( XmlSimpleValue xmlSimpleValue ) {
    return ( (XmlSimpleValueBase) xmlSimpleValue )._getGosuValue();
  }

  public List<QName> _getQNames( XmlSimpleValue xmlSimpleValue ) {
    return ( (XmlSimpleValueBase) xmlSimpleValue )._getQNames();
  }  

}
