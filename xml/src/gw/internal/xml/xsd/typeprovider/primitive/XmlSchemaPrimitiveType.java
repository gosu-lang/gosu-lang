/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.primitive;

import gw.internal.xml.xsd.typeprovider.simplevaluefactory.DoubleSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.FloatSimpleValueFactory;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.xml.XmlSimpleValueException;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

public enum XmlSchemaPrimitiveType {

  // todo dlank - double check all "ordered" primitives in xml schema schema, and add comparators for them
  STRING( "string", null ), // not ordered
  BOOLEAN( "boolean", null ), // not ordered
  FLOAT( "float", null ), // totally ordered
  DOUBLE( "double", null ), // totally ordered
  DECIMAL( "decimal", new XmlSchemaDecimalComparator() ),
  DURATION( "duration", null ), // partially ordered
  DATETIME( "dateTime", null ), // partially ordered
  TIME( "time", null ), // partially ordered
  DATE( "date", null ), // partially ordered
  GYEARMONTH( "gYearMonth", null ), // partially ordered
  GYEAR( "gYear", null ), // partially ordered
  GMONTHDAY( "gMonthDay", null ), // partially ordered
  GDAY( "gDay", null ), // partially ordered
  GMONTH( "gMonth", null ), // partially ordered
  HEXBINARY( "hexBinary", null ), // not ordered
  BASE64BINARY( "base64Binary", null ), // not ordered
  ANYURI( "anyURI", null ), // not ordered
  QNAME( "QName", null ), // not ordered
  NOTATION( "NOTATION", null ), // not ordered

  LIST( null, null ), // not ordered
  UNION( null, null ), // not ordered

  ANYSIMPLETYPE( "anySimpleType", null ); // Does not exist in schema schema

  private static final Map<QName, XmlSchemaPrimitiveType> _typesByQName = new HashMap<QName, XmlSchemaPrimitiveType>();
  private final String _localPart;
  private final Comparator<String> _valueComparator;

  static {
    for ( XmlSchemaPrimitiveType type : values() ) {
      if ( type._localPart != null ) {
        _typesByQName.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, type._localPart ), type );
      }
    }
  }

  XmlSchemaPrimitiveType( String localPart, Comparator<String> valueComparator ) {
    _localPart = localPart;
    _valueComparator = valueComparator;
  }

  public static XmlSchemaPrimitiveType get( QName qName ) {
    return _typesByQName.get( qName );
  }

  public QName getQName() {
    return new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, _localPart );
  }

  public Comparator<String> getValueComparator() {
    return _valueComparator;
  }

  public void validate( String value ) {
    switch ( this ) {
      case ANYSIMPLETYPE:
        // nothing to do
        break;
      case ANYURI:
        try {
          new URI( value );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:anyURI", ex );
        }
        break;
      case BASE64BINARY:
        // not going to bother here - could affect performance on large byte arrays
        break;
      case BOOLEAN:
        try {
          Boolean.valueOf( value );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:boolean", ex );
        }
        break;
      case DATE:
        try {
          create( value, "gw.xml.date.XmlDate" );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:date", ex );
        }
        break;
      case DATETIME:
        try {
          create( value, "gw.xml.date.XmlDateTime" );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:dateTime", ex );
        }
        break;
      case DECIMAL:
        try {
          new BigDecimal( value );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:decimal", ex );
        }
        break;
      case DOUBLE:
        try {
          DoubleSimpleValueFactory.parseDouble( value );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:double", ex );
        }
        break;
      case DURATION:
        try {
          create( value, "gw.xml.date.XmlDuration" );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:duration", ex );
        }
        break;
      case FLOAT:
        try {
          FloatSimpleValueFactory.parseFloat( value );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:float", ex );
        }
        break;
      case GDAY:
        try {
          create( value, "gw.xml.date.XmlDay" );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:gDay", ex );
        }
        break;
      case GMONTH:
        try {
          create( value, "gw.xml.date.XmlMonth" );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:gMonth", ex );
        }
        break;
      case GMONTHDAY:
        try {
          create( value, "gw.xml.date.XmlMonthDay" );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:gMonthDay", ex );
        }
        break;
      case GYEAR:
        try {
          create( value, "gw.xml.date.XmlYear" );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:gYear", ex );
        }
        break;
      case GYEARMONTH:
        try {
          create( value, "gw.xml.date.XmlYearMonth" );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:gYearMonth", ex );
        }
        break;
      case HEXBINARY:
        // not going to bother here - could affect performance on large byte arrays
        break;
      case LIST:
        // will be handled by item types
        break;
      case NOTATION:
        // From spec: For some datatypes, notably QName and NOTATION, the mapping from lexical representations to values is context-dependent; for these types, no canonical mapping is defined.
        break;
      case QNAME:
        // From spec: For some datatypes, notably QName and NOTATION, the mapping from lexical representations to values is context-dependent; for these types, no canonical mapping is defined.
        break;
      case STRING:
        // nothing to do
        break;
      case TIME:
        try {
          create( value, "gw.xml.date.XmlTime" );
        }
        catch ( Exception ex ) {
          throw new XmlSimpleValueException( "Invalid xsd:time", ex );
        }
        break;
      case UNION:
        // will be handled by member types
        break;
    }
  }

  private void create( String value, String typeName ) {
    TypeSystem.getByFullName( typeName ).getTypeInfo().getConstructor( JavaTypes.STRING() ).getConstructor().newInstance( value );
  }

}
