/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.simplevaluefactory.GosuEnumSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.additional.DateXmlSimpleValueFactory;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.Pair;
import gw.xml.XmlException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

public class XmlSchemaTypeToGosuTypeMappings {

  private static Map<QName, XmlSimpleValueFactory> _schemaToGosu;
  private static Map<IType, Pair<QName, XmlSimpleValueFactory>> _gosuToSchema;
  private static boolean _initialized = false;

  public static void maybeInit() {
    if ( ! _initialized ) {
      _initialized = true;
      _schemaToGosu = new HashMap<QName, XmlSimpleValueFactory>();
      _gosuToSchema = new HashMap<IType, Pair<QName, XmlSimpleValueFactory>>();
      map( "boolean", XmlSimpleValueFactory.BOOLEAN_INSTANCE, true, true );
      map( "byte", XmlSimpleValueFactory.BYTE_INSTANCE, true, true );
      map( "decimal", XmlSimpleValueFactory.DECIMAL_INSTANCE, true, true );
      map( "double", XmlSimpleValueFactory.DOUBLE_INSTANCE, true, true );
      map( "float", XmlSimpleValueFactory.FLOAT_INSTANCE, true, true );
      map( "int", XmlSimpleValueFactory.INT_INSTANCE, true, true );                                
      map( "integer", XmlSimpleValueFactory.INTEGER_INSTANCE, true, true );
      map( "long", XmlSimpleValueFactory.LONG_INSTANCE, true, true );
      map( "short", XmlSimpleValueFactory.SHORT_INSTANCE, true, true );
      map( "unsignedLong", XmlSimpleValueFactory.UNSIGNEDLONG_INSTANCE, true, false );
      map( "unsignedInt", XmlSimpleValueFactory.UNSIGNEDINT_INSTANCE, true, false );
      map( "unsignedShort", XmlSimpleValueFactory.UNSIGNEDSHORT_INSTANCE, true, false );
      map( "unsignedByte", XmlSimpleValueFactory.UNSIGNEDBYTE_INSTANCE, true, false );
      map( "date", XmlSimpleValueFactory.DATE_INSTANCE, true, true );
      map( "dateTime", XmlSimpleValueFactory.DATETIME_INSTANCE, true, true );
      map( "time", XmlSimpleValueFactory.TIME_INSTANCE, true, true );
      map( "gYearMonth", XmlSimpleValueFactory.GYEARMONTH_INSTANCE, true, true );
      map( "gYear", XmlSimpleValueFactory.GYEAR_INSTANCE, true, true );
      map( "gMonthDay", XmlSimpleValueFactory.GMONTHDAY_INSTANCE, true, true );
      map( "gDay", XmlSimpleValueFactory.GDAY_INSTANCE, true, true );
      map( "gMonth", XmlSimpleValueFactory.GMONTH_INSTANCE, true, true );
      map( "duration", XmlSimpleValueFactory.DURATION_INSTANCE, true, true );
      map( "base64Binary", XmlSimpleValueFactory.BASE64BINARY_INSTANCE, true, true );
      map( "hexBinary", XmlSimpleValueFactory.HEXBINARY_INSTANCE, true, true );
      map( "anyURI", XmlSimpleValueFactory.ANYURI_INSTANCE, true, true );
      map( "QName", XmlSimpleValueFactory.QNAME_INSTANCE, true, true );
      map( "ID", XmlSimpleValueFactory.ID_INSTANCE, true, false );
      map( "IDREF", XmlSimpleValueFactory.IDREF_INSTANCE, true, false );
      map( "string", XmlSimpleValueFactory.STRING_INSTANCE, true, true );
      map( "anySimpleType", XmlSimpleValueFactory.STRING_INSTANCE, true, false );
      // Though the following is technically correct, javax.xml.namespace.QName will never be used in practice,
      // since NOTATION MUST be restricted by an enumeration per spec, so the user will see an enumeration instead.
      map( "NOTATION", XmlSimpleValueFactory.QNAME_INSTANCE, true, false );

      // additional mappings for supporting GX and webservices
      map( "dateTime", new DateXmlSimpleValueFactory(), false, true );

      // additional mappings for supporting configuration files
      map(gw.internal.schema.gw.xsd.gw.gw_schema_additions.types.simple.EncodedString.$QNAME, XmlSimpleValueFactory.ENCODED_STRING_INSTANCE, true, false );
    }
  }

  public static void map( String localPart, XmlSimpleValueFactory valueFactory, boolean mapSchemaType, boolean mapGosuType ) {
    map(new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, localPart), valueFactory, mapSchemaType, mapGosuType);
  }

  public static void map(QName qName, XmlSimpleValueFactory valueFactory, boolean mapSchemaType, boolean mapGosuType) {
    maybeInit();
    if ( mapSchemaType ) {
      XmlSimpleValueFactory oldSchemaType = _schemaToGosu.put(qName, valueFactory );
      if ( oldSchemaType != null ) {
        throw new IllegalArgumentException( "Schema type already mapped: " + qName);
      }
    }
    if ( mapGosuType ) {
      IType gosuType = valueFactory.getGosuValueType();
      Pair<QName, XmlSimpleValueFactory> oldGosuType = _gosuToSchema.put( gosuType, new Pair<QName, XmlSimpleValueFactory>(qName, valueFactory ) );
      if ( oldGosuType != null ) {
        throw new IllegalArgumentException( "Gosu type already mapped: " + gosuType );
      }
    }
  }

  public static XmlSimpleValueFactory schemaToGosu( QName qname ) {
    maybeInit();
    return _schemaToGosu.get( qname );
  }

  public static Pair<QName, XmlSimpleValueFactory> gosuToSchemaIfValid( IType gosuType ) {
    if (gosuType == null) {
      return null;
    }
    maybeInit();
    if ( gosuType.isPrimitive() ) {
      gosuType = TypeSystem.getBoxType( gosuType );
    }
    else if ( gosuType.isEnum() ) {
      return new Pair<QName, XmlSimpleValueFactory>( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "string" ), new GosuEnumSimpleValueFactory( (IEnumType) gosuType ) );
    }
    return _gosuToSchema.get( gosuType );
  }

  public static Pair<QName, XmlSimpleValueFactory> gosuToSchema( IType gosuType ) {
    Pair<QName, XmlSimpleValueFactory> schemaTypePair = gosuToSchemaIfValid( gosuType );
    if ( schemaTypePair == null ) {
      throw new XmlException( "Unable to map " + gosuType + " to a schema type" );
    }
    return schemaTypePair;
  }

  public static Set<IType> getSupportedGosuTypes() {
    return Collections.unmodifiableSet( _gosuToSchema.keySet() );
  }

}
