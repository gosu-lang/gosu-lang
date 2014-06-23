/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.IType;
import gw.xml.BinaryData;
import gw.xml.XmlElement;
import gw.xml.XmlParseOptions;
import gw.xml.XmlSerializationOptions;
import gw.xml.XmlTypeInstance;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;

public abstract class XmlElementInternals {

  private static final XmlElementInternals _instance;

  static {
    try {
      Constructor<?> constructor = Class.forName( "gw.xml.XmlElementInternalsImpl" ).getDeclaredConstructor();
      constructor.setAccessible( true );
      _instance = (XmlElementInternals) constructor.newInstance();
    }
    catch ( Exception ex ) {
      throw new RuntimeException( ex );
    }
  }

  public static XmlElementInternals instance() {
    return _instance;
  }

  public abstract void writeTo( XmlElement element, OutputStream out, XmlSerializationOptions options );

  public abstract void doDeclareNamespace( XmlElement element, String nsuri, String suggestedPrefix, Map<String, URI> _uriCache );

  public abstract void checkSetTypeInstance( XmlElement element, XmlTypeInstance xmlTypeInstance );

  public abstract boolean isAnyType( IType type );

  public abstract XmlElement parse( InputStream stream );

  public abstract XmlElement parse( byte[] bytes );

  public abstract XmlElement parse( File file );

  public abstract XmlElement parse( URL url );

  public abstract XmlElement parse( String string );

  public abstract XmlElement parse( Reader reader );

  public abstract XmlElement parse( InputStream stream, XmlParseOptions options );

  public abstract XmlElement parse( InputStream inputStream, XmlParseOptions options, XmlTypeResolver typeResolver );  

  public abstract XmlElement parse( Reader reader, XmlParseOptions options, XmlTypeResolver typeResolver );

  public abstract XmlElement parse( byte[] bytes, XmlParseOptions options );

  public abstract XmlElement parse( File file, XmlParseOptions options );

  public abstract XmlElement parse( URL url, XmlParseOptions options );

  public abstract XmlElement parse( String string, XmlParseOptions options );

  public abstract XmlElement parse( Reader reader, XmlParseOptions options );

  public abstract XmlElement parse( InputStream inputStream, URL schemaEF, XmlSchemaLocalResourceResolver resolver, XmlParserCallback callback );

  public abstract XmlElement parse( IType type, Reader stream, String description, boolean validating, HashMap<String, BinaryData> attachments, XmlParseOptions options );

  public abstract XmlElement parse( IType type, InputStream stream, String description, boolean validating, HashMap<String, BinaryData> attachments, XmlParseOptions options, String systemId );

  public abstract void addNamespacesToElementFromParse( XmlElement element, Map<String, String> allNamespaces, Map<String, URI> _uriCache );

  public abstract void validateQName( QName qname );

  public abstract XmlElement create( QName qName, IType type, IType xmlTypeInstanceType, XmlTypeInstance xmlTypeInstance );

  public abstract IType getType( XmlElement element );

  public abstract void resolveXsiTypeAndNil( XmlElement element, List<XmlSchemaIndex> schemaIndexes, IType typeInstanceType, Attributes attributes, XmlDeserializationContext context );

}
