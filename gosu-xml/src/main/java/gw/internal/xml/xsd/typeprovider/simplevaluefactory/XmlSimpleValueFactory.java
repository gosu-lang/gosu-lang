/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.simplevaluefactory;

import gw.internal.xml.XmlDeserializationContext;
import gw.lang.reflect.IType;
import gw.xml.XmlSimpleValue;

import java.math.BigInteger;

public abstract class XmlSimpleValueFactory {

  /******************************************************************************/
  /* IF YOU UPDATE THIS LIST, PLEASE ADD A "MAKE" METHOD IN XMLSIMPLEVALUE.JAVA */
  /* AND ADD A TEST IN XMLSIMPLEVALUETEST.GS                                    */
  /******************************************************************************/
  public static final BooleanSimpleValueFactory BOOLEAN_INSTANCE = new BooleanSimpleValueFactory();
  public static final SimpleSimpleValueFactory BYTE_INSTANCE = new SimpleSimpleValueFactory( Byte.class );
  public static final BigDecimalSimpleValueFactory DECIMAL_INSTANCE = new BigDecimalSimpleValueFactory();
  public static final DoubleSimpleValueFactory DOUBLE_INSTANCE = new DoubleSimpleValueFactory();
  public static final FloatSimpleValueFactory FLOAT_INSTANCE = new FloatSimpleValueFactory();
  public static final SimpleSimpleValueFactory INT_INSTANCE = new SimpleSimpleValueFactory( Integer.class );
  public static final SimpleSimpleValueFactory INTEGER_INSTANCE = new SimpleSimpleValueFactory( BigInteger.class );
  public static final SimpleSimpleValueFactory LONG_INSTANCE = new SimpleSimpleValueFactory( Long.class );
  public static final SimpleSimpleValueFactory SHORT_INSTANCE = new SimpleSimpleValueFactory( Short.class );
  public static final UnsignedLongSimpleValueFactory UNSIGNEDLONG_INSTANCE = new UnsignedLongSimpleValueFactory();
  public static final UnsignedIntSimpleValueFactory UNSIGNEDINT_INSTANCE = new UnsignedIntSimpleValueFactory();
  public static final UnsignedShortSimpleValueFactory UNSIGNEDSHORT_INSTANCE = new UnsignedShortSimpleValueFactory();
  public static final UnsignedByteSimpleValueFactory UNSIGNEDBYTE_INSTANCE = new UnsignedByteSimpleValueFactory();
  public static final StringSimpleValueFactory STRING_INSTANCE = new StringSimpleValueFactory();
  public static final EncodedStringSimpleValueFactory ENCODED_STRING_INSTANCE = new EncodedStringSimpleValueFactory();
  public static final Base64BinarySimpleValueFactory BASE64BINARY_INSTANCE = new Base64BinarySimpleValueFactory();
  public static final HexBinarySimpleValueFactory HEXBINARY_INSTANCE = new HexBinarySimpleValueFactory();
  public static final QNameSimpleValueFactory QNAME_INSTANCE = new QNameSimpleValueFactory( true );
  public static final QNameSimpleValueFactory NONVALIDATING_QNAME_INSTANCE = new QNameSimpleValueFactory( false );
  public static final IDSimpleValueFactory ID_INSTANCE = new IDSimpleValueFactory();
  public static final IDREFSimpleValueFactory IDREF_INSTANCE = new IDREFSimpleValueFactory();
  public static final JavaClassSimpleSimpleValueFactory ANYURI_INSTANCE = new JavaClassSimpleSimpleValueFactory( java.net.URI.class );
  public static final JavaClassSimpleSimpleValueFactory DATE_INSTANCE = new JavaClassSimpleSimpleValueFactory( gw.xml.date.XmlDate.class );
  public static final JavaClassSimpleSimpleValueFactory DATETIME_INSTANCE = new JavaClassSimpleSimpleValueFactory( gw.xml.date.XmlDateTime.class );
  public static final JavaClassSimpleSimpleValueFactory TIME_INSTANCE = new JavaClassSimpleSimpleValueFactory( gw.xml.date.XmlTime.class );
  public static final JavaClassSimpleSimpleValueFactory GYEARMONTH_INSTANCE = new JavaClassSimpleSimpleValueFactory( gw.xml.date.XmlYearMonth.class );
  public static final JavaClassSimpleSimpleValueFactory GYEAR_INSTANCE = new JavaClassSimpleSimpleValueFactory( gw.xml.date.XmlYear.class );
  public static final JavaClassSimpleSimpleValueFactory GMONTHDAY_INSTANCE = new JavaClassSimpleSimpleValueFactory( gw.xml.date.XmlMonthDay.class );
  public static final JavaClassSimpleSimpleValueFactory GDAY_INSTANCE = new JavaClassSimpleSimpleValueFactory( gw.xml.date.XmlDay.class );
  public static final JavaClassSimpleSimpleValueFactory GMONTH_INSTANCE = new JavaClassSimpleSimpleValueFactory( gw.xml.date.XmlMonth.class );
  public static final JavaClassSimpleSimpleValueFactory DURATION_INSTANCE = new JavaClassSimpleSimpleValueFactory( gw.xml.date.XmlDuration.class );
  /******************************************************************************/
  /* IF YOU UPDATE THIS LIST, PLEASE ADD A "MAKE" METHOD IN XMLSIMPLEVALUE.JAVA */
  /******************************************************************************/

  /** This will return the type for this factory
   *
   * @return the type
   */
  public abstract IType getGosuValueType();

  public final XmlSimpleValue gosuValueToStorageValue( Object gosuValue ) {
    if ( gosuValue == null ) {
      return null;
    }
    XmlSimpleValue storageValue = _gosuValueToStorageValue( gosuValue );
    if ( ! storageValue.getGosuValueType().equals( getGosuValueType() ) ) {
      throw new ClassCastException( "Simple type storage has the wrong type on write. Expected: " + getGosuValueType() + ", Actual: " + storageValue.getGosuValueType() );
    }
    return storageValue;
  }

  public final XmlSimpleValue deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault ) {
    if ( stringValue == null ) {
      return null;
    }
    XmlSimpleValue simpleValue = _deserialize( context, stringValue, isDefault );
    if ( simpleValue == null ) {
      throw new RuntimeException( this + " converted '" + stringValue + "' to null" );
    }
    return simpleValue;
  }

  /**
   * Does not take into account any deserialization context. Therefore this method should
   * not be used for deserializing QNames or IDREFs. The use of deserialize using a deserialization
   * context is preferred over the use of this method.
   * @param stringValue the value to deserialize
   * @return the deserialized value
   */
  public final XmlSimpleValue deserialize( String stringValue ) {
    return deserialize( new XmlDeserializationContext( null ), stringValue, false );
  }

  protected abstract XmlSimpleValue _gosuValueToStorageValue( Object gosuValue );

  protected abstract XmlSimpleValue _deserialize( XmlDeserializationContext context, String stringValue, boolean isDefault );

}
