/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.internal.xml.XmlSimpleValueInternals;
import gw.lang.PublishInGosu;
import gw.lang.reflect.IType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * Represents an XML simple value, such as an xsd:int. Instances of this class know how to translate between
 * Gosu representation (42), String representation ("42"), and know how to serialize themselves to the output
 * XML, which may differ from the value returned by getStringValue(). For example, in the case of a QName,
 * the Gosu representation would be a javax.xml.namespace.QName, the String representation would be the
 * QName's namespace prefix and it's local part joined by a colon, yet when serialized to XML, the actual
 * namespaces at the time of serialization would be taken into account, and a different prefix might be chosen.
 */
@PublishInGosu
public abstract class XmlSimpleValue {

  /**
   * Returns the Gosu value type.
   * @return the Gosu value type
   */
  public abstract IType getGosuValueType();

  /**
   * Creates a simple value representing an xsd:boolean.
   * @param value The boolean value
   * @return a simple value representing an xsd:boolean
   */
  public static XmlSimpleValue makeBooleanInstance( Boolean value ) {
    return XmlSimpleValueInternals.instance().makeBooleanInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:decimal.
   * @param value The decimal value
   * @return a simple value representing an xsd:decimal
   */
  public static XmlSimpleValue makeDecimalInstance( BigDecimal value ) {
    return XmlSimpleValueInternals.instance().makeDecimalInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:double.
   * @param value The double value
   * @return a simple value representing an xsd:double
   */
  public static XmlSimpleValue makeDoubleInstance( Double value ) {
    return XmlSimpleValueInternals.instance().makeDoubleInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:float.
   * @param value The float value
   * @return a simple value representing an xsd:float
   */
  public static XmlSimpleValue makeFloatInstance( Float value ) {
    return XmlSimpleValueInternals.instance().makeFloatInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:integer.
   * @param value The integer value
   * @return a simple value representing an xsd:integer
   */
  public static XmlSimpleValue makeIntegerInstance( BigInteger value ) {
    return XmlSimpleValueInternals.instance().makeIntegerInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:long.
   * @param value The long value
   * @return a simple value representing an xsd:long
   */
  public static XmlSimpleValue makeLongInstance( Long value ) {
    return XmlSimpleValueInternals.instance().makeLongInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:int.
   * @param value The int value
   * @return a simple value representing an xsd:int
   */
  public static XmlSimpleValue makeIntInstance( Integer value ) {
    return XmlSimpleValueInternals.instance().makeIntInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:short.
   * @param value The short value
   * @return a simple value representing an xsd:short
   */
  public static XmlSimpleValue makeShortInstance( Short value ) {
    return XmlSimpleValueInternals.instance().makeShortInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:byte.
   * @param value The byte value
   * @return a simple value representing an xsd:byte
   */
  public static XmlSimpleValue makeByteInstance( Byte value ) {
    return XmlSimpleValueInternals.instance().makeByteInstance( value );
  }


  /**
   * Creates a simple value representing an xsd:unsignedLong.
   * @param value The unsignedLong value
   * @return a simple value representing an xsd:unsignedLong
   */
  public static XmlSimpleValue makeUnsignedLongInstance( BigInteger value ) {
    return XmlSimpleValueInternals.instance().makeUnsignedLongInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:unsignedInt.
   * @param value The unsignedInt value
   * @return a simple value representing an xsd:unsignedInt
   */
  public static XmlSimpleValue makeUnsignedIntInstance( Long value ) {
    return XmlSimpleValueInternals.instance().makeUnsignedIntInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:unsignedShort.
   * @param value The unsignedShort value
   * @return a simple value representing an xsd:unsignedShort
   */
  public static XmlSimpleValue makeUnsignedShortInstance( Integer value ) {
    return XmlSimpleValueInternals.instance().makeUnsignedShortInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:unsignedByte.
   * @param value The unsignedByte value
   * @return a simple value representing an xsd:unsignedByte
   */
  public static XmlSimpleValue makeUnsignedByteInstance( Short value ) {
    return XmlSimpleValueInternals.instance().makeUnsignedByteInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:string.
   * @param value The string value
   * @return a simple value representing an xsd:string
   */
  public static XmlSimpleValue makeEncodedStringInstance( String value ) {
    return XmlSimpleValueInternals.instance().makeEncodedStringInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:string.
   * @param value The string value
   * @return a simple value representing an xsd:string
   */
  public static XmlSimpleValue makeStringInstance( String value ) {
    return XmlSimpleValueInternals.instance().makeStringInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:date.
   * @param value The date value
   * @return a simple value representing an xsd:date
   */
  public static XmlSimpleValue makeDateInstance( gw.xml.date.XmlDate value ) {
    return XmlSimpleValueInternals.instance().makeDateInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:datetime.
   * @param value The datetime value
   * @return a simple value representing an xsd:datetime
   */
  public static XmlSimpleValue makeDateTimeInstance( gw.xml.date.XmlDateTime value ) {
    return XmlSimpleValueInternals.instance().makeDateTimeInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:time.
   * @param value The time value
   * @return a simple value representing an xsd:time
   */
  public static XmlSimpleValue makeTimeInstance( gw.xml.date.XmlTime value ) {
    return XmlSimpleValueInternals.instance().makeTimeInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:gYearMonth.
   * @param value The gYearMonth value
   * @return a simple value representing an xsd:gYearMonth
   */
  public static XmlSimpleValue makeGYearMonthInstance( gw.xml.date.XmlYearMonth value ) {
    return XmlSimpleValueInternals.instance().makeGYearMonthInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:gYear.
   * @param value The gYear value
   * @return a simple value representing an xsd:gYear
   */
  public static XmlSimpleValue makeGYearInstance( gw.xml.date.XmlYear value ) {
    return XmlSimpleValueInternals.instance().makeGYearInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:gMonthDay.
   * @param value The gMonthDay value
   * @return a simple value representing an xsd:gMonthDay
   */
  public static XmlSimpleValue makeGMonthDayInstance( gw.xml.date.XmlMonthDay value ) {
    return XmlSimpleValueInternals.instance().makeGMonthDayInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:gDay.
   * @param value The gDay value
   * @return a simple value representing an xsd:gDay
   */
  public static XmlSimpleValue makeGDayInstance( gw.xml.date.XmlDay value ) {
    return XmlSimpleValueInternals.instance().makeGDayInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:gMonth.
   * @param value The gMonth value
   * @return a simple value representing an xsd:gMonth
   */
  public static XmlSimpleValue makeGMonthInstance( gw.xml.date.XmlMonth value ) {
    return XmlSimpleValueInternals.instance().makeGMonthInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:duration.
   * @param value The duration value
   * @return a simple value representing an xsd:duration
   */
  public static XmlSimpleValue makeDurationInstance( gw.xml.date.XmlDuration value ) {
    return XmlSimpleValueInternals.instance().makeDurationInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:base64Binary.
   * @param value The base64Binary value
   * @return a simple value representing an xsd:base64Binary
   */
  public static XmlSimpleValue makeBase64BinaryInstance( byte[] value ) {
    return XmlSimpleValueInternals.instance().makeBase64BinaryInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:base64Binary.
   * @param value The base64Binary value
   * @return a simple value representing an xsd:base64Binary
   */
  public static XmlSimpleValue makeBase64BinaryInstance( BinaryData value ) {
    return XmlSimpleValueInternals.instance().makeBase64BinaryInstance( value );
  }


  /**
   * Creates a simple value representing an xsd:hexBinary.
   * @param value The hexBinary value
   * @return a simple value representing an xsd:hexBinary
   */
  public static XmlSimpleValue makeHexBinaryInstance( byte[] value ) {
    return XmlSimpleValueInternals.instance().makeHexBinaryInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:anyURI.
   * @param value The anyURI value
   * @return a simple value representing an xsd:anyURI
   */
  public static XmlSimpleValue makeAnyURIInstance( URI value ) {
    return XmlSimpleValueInternals.instance().makeAnyURIInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:QName.
   * @param value The QName value
   * @return a simple value representing an xsd:QName
   */
  public static XmlSimpleValue makeQNameInstance( QName value ) {
    return XmlSimpleValueInternals.instance().makeQNameInstance( value, true );
  }

  /**
   * Creates a simple value representing an xsd:QName, without validating the QName for valid characters.
   * @param value The QName value
   * @return a simple value representing an xsd:QName
   */
  public static XmlSimpleValue makeNonValidatingQNameInstance( QName value ) {
    return XmlSimpleValueInternals.instance().makeQNameInstance( value, false );
  }

  /**
   * Creates a simple value representing an xsd:QName.
   * @param value The colonized value
   * @param namespaceContext The namespace context
   * @return a simple value representing an xsd:QName
   */
  public static XmlSimpleValue makeQNameInstance( String value, Map<String,String> namespaceContext ) {
    return XmlSimpleValueInternals.instance().makeQNameInstance( value, namespaceContext, true );
  }

  /**
   * Creates a simple value representing an xsd:QName, without validating the QName for valid characters.
   * @param value The colonized value
   * @param namespaceContext The namespace context
   * @return a simple value representing an xsd:QName
   */
  public static XmlSimpleValue makeNonValidatingQNameInstance( String value, Map<String,String> namespaceContext ) {
    return XmlSimpleValueInternals.instance().makeQNameInstance( value, namespaceContext, false );
  }

  /**
   * Creates a simple value representing an xsd:ID.
   * @param value The ID value
   * @return a simple value representing an xsd:ID
   */
  public static XmlSimpleValue makeIDInstance( String value ) {
    return XmlSimpleValueInternals.instance().makeIDInstance( value );
  }

  /**
   * Creates a simple value representing an xsd:IDREF.
   * @param value The IDREF value
   * @return a simple value representing an xsd:IDREF
   */
  public static XmlSimpleValue makeIDREFInstance( XmlElement value ) {
    return XmlSimpleValueInternals.instance().makeIDREFInstance( value );
  }

  /**
   * Creates a simple value representing an enum.
   * @param value The enum value
   * @return a simple value representing an enum
   */
  public static XmlSimpleValue makeEnumInstance( IXmlSchemaEnumValue value ) {
    return XmlSimpleValueInternals.instance().makeEnumInstance( value );
  }

  /**
   * Creates a simple value representing a list of QName.
   * @param value The list of QName value
   * @return a simple value representing a list of QName
   */
  public static XmlSimpleValue makeListOfQNameInstance( List<QName> value ) {
    return XmlSimpleValueInternals.instance().makeListOfQNameInstance( value );
  }

  /**
   * Returns a string representation of this simple value. This may differ from the actual value that
   * will be serialized to XML.
   * @return a string representation of this simple value
   */
  public abstract String getStringValue();

  /**
   * Returns the Gosu value of this simple value.
   * @return the Gosu value of this simple value
   */
  public final Object getGosuValue() {
    Object value = XmlSimpleValueInternals.instance()._getGosuValue( this );
    if ( value == null ) {
      throw new RuntimeException( "Null value" );
    }
    return value;
  }

  /**
   * Returns a list of QNames associated with this simple value ( such as in the case of xs:QName or an xs:list of
   * the same )
   * @return the list of qnames
   */
  public final List<QName> getQNames() {
    return XmlSimpleValueInternals.instance()._getQNames( this );
  }

  /**
   * Returns a string representation for debugging.
   * @return a string representation for debugging
   */
  public String toString() {
    return "Simple value: " + getStringValue();
  }

}
