/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.AnyType;
import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlElementInternals;
import gw.lang.PublishInGosu;
import gw.lang.PublishedType;
import gw.lang.PublishedTypes;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Pair;
import gw.util.StreamUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.*;

import javax.xml.namespace.QName;
/**
 * Represents an XML element. Unlike DOM, it does not directly contain any children. It has a backing XML type
 * instance (XmlTypeInstance), however, which contains it's children. Call getTypeInstance() to access the backing
 * type instance. All of the methods and properties of the backing type instance have been re-exposed on the
 * XmlElement, however, so accessing the backing type instance directly should rarely be necessary.
 */
@PublishInGosu
@PublishedTypes({
    @PublishedType(fromType = "gw.xml.XmlTypeInstance", toType = "gw.xsd.w3c.xmlschema.types.complex.AnyType")
})
public class XmlElement extends XmlBase implements IXmlMixedContent {

  static final QName XOP_INCLUDE_QNAME = new QName( "http://www.w3.org/2004/08/xop/include", "Include" );

  final QName _qname;
  final IType _type;
  final IType _schemaDefinedTypeInstanceType;
  // This must be a list rather than a map, since it can have duplicate keys that are resolved for conflicts at serialization time
  List<Pair<String, URI>> _declaredNamespaceBindings = new ArrayList<Pair<String, URI>>( 0 );
  String _comment;
  private boolean _nil;

  protected XmlElement( QName qname, IType type, IType schemaDefinedTypeInstanceType, XmlTypeInstance typeInstance ) {
    _type = type;
    XmlElementInternals.instance().validateQName( qname );
    _schemaDefinedTypeInstanceType = XmlElementInternals.instance().isAnyType( schemaDefinedTypeInstanceType ) ? null : schemaDefinedTypeInstanceType;
    if ( typeInstance == null ) {
      if ( _schemaDefinedTypeInstanceType == null ) {
        typeInstance = new AnyType();
      }
      else {
        typeInstance = (XmlTypeInstance) _schemaDefinedTypeInstanceType.getTypeInfo().getConstructor().getConstructor().newInstance();
      }
    }
    _qname = qname;
    setTypeInstance( typeInstance );
  }

  /**
   * Creates a new element with the specified QName, and the specified backing type instance.
   * @param qname The QName for the element
   * @param typeInstance The backing type instance
   */
  public XmlElement( QName qname, XmlTypeInstance typeInstance ) {
    this( qname, null, null, typeInstance );
  }

  /**
   * Creates a new element with the specified name in the null namespace, and the specified backing type instance.
   * @param localPart the local name for the element in the null namespace
   * @param typeInstance The backing type instance
   */
  public XmlElement( String localPart, XmlTypeInstance typeInstance ) {
    this( new QName( localPart ), null, null, typeInstance );
  }

  /**
   * Creates a new element with the specified QName,
   * and an empty gw.xsd.w3c.xmlschema.types.complex.AnyType backing type instance.
   * @param qname The QName for the element
   */
  public XmlElement( QName qname ) {
    this( qname, null );
  }

  /**
   * Creates a new element with the specified local name in the null namespace,
   * and an empty gw.xsd.w3c.xmlschema.types.complex.AnyType backing type instance.
   * @param name the name of the element in the null namespace
   */
  public XmlElement( String name ) {
    this( name, null );
  }

  /**
   * Returns the QName of this element.
   * @return the QName of this element
   */
  public QName getQName() {
    return _qname;
  }

  /**
   * Returns the namespace of this element.
   * @return the namespace of this element
   */
  public XmlNamespace getNamespace() {
    return new XmlNamespace( _qname.getNamespaceURI(), _qname.getPrefix() );
  }

  /**
   * Serializes this element to the console. Equivalent to System.out.println( asUTFString() )
   */
  public void print() {
    try {
      writeTo( System.out );
    }
    finally {
      System.out.println();
    }
  }

  /**
   * Serializes this element to the console. Equivalent to System.out.println( asUTFString() )
   * @param options the options to control serialization
   */
  public void print( XmlSerializationOptions options) {
    writeTo( System.out, options );
    System.out.println();
  }

  /**
   * Serializes this element to a string. The returned string is in a format suitable for byte conversion
   * to either UTF-8 or UTF-16 (with a leading byte order mark to indicate endianness). The use of
   * getBytes() is highly recommended over the use of this method.
   * @return a string containing the serialized XML
   */
  public String asUTFString() {
    return StreamUtil.toString( bytes(new XmlSerializationOptions()) );
  }

  /**
   * Serializes this element to a string. The returned string is in a format suitable for byte conversion
   * to either UTF-8 or UTF-16 (with a leading byte order mark to indicate endianness). The use of
   * getBytes() is highly recommended over the use of this method.
   * @param options the options to control serialization
   * @return a string containing the serialized XML
   */
  public String asUTFString( XmlSerializationOptions options) {
    return StreamUtil.toString( bytes(options) );
  }

  /**
   * Serializes this element to a byte array using UTF-8 encoding.
   * @return a byte array containing the serialized XML
   */
  public byte[] bytes() {
    return bytes(new XmlSerializationOptions());
  }

  /**
   * Serializes this element to a byte array using UTF-8 encoding.
   * @param options the options to control serialization
   * @return a byte array containing the serialized XML
   */
  public byte[] bytes( XmlSerializationOptions options) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    writeTo( baos, options );
    return baos.toByteArray();
  }

  /**
   * Serializes this element to the specified output stream using UTF-8 encoding. The stream will not be closed.
   * @param out the output stream where the data should be written
   */
  public void writeTo( OutputStream out ) {
    writeTo( out, new XmlSerializationOptions() );
  }

   /**
   * Serializes this element to the specified output stream using UTF-8 encoding. The stream will not be closed.
   * @param out the output stream where the data should be written
   * @param options the options to control serialization
   */
  public void writeTo( OutputStream out, XmlSerializationOptions options ) {
     XmlElementInternals.instance().writeTo( this, out, options );
  }

  /**
   * Explicitly declares an XML namespace and a suggested prefix at this level.
   * The prefix that actually ends up being bound to the namespace may not be the same as the suggested prefix.
   * @param nsuri the namespace URI to which the prefix should be bound
   * @param suggestedPrefix the suggested prefix to use
   */
  public void declareNamespace(URI nsuri, String suggestedPrefix) {
    if (nsuri == null) {
      throw new IllegalArgumentException("The value for the nsuri parameter must not be null");
    }
    XmlElementInternals.instance().doDeclareNamespace( this, nsuri.toString(), suggestedPrefix, null );
  }

  /**
   * Explicitly declares an XML namespace and a suggested prefix at this level.
   * The prefix that actually ends up being bound to the namespace may not be the same as the suggested prefix.
   * @param qname the qname that contains the namespace URI to be declared and the suggested prefix to which the namespace should be bound
   */
  public void declareNamespace(QName qname) {
    if (qname == null) {
        throw new IllegalArgumentException("The value for the qname parameter must not be null");
    }
    XmlElementInternals.instance().doDeclareNamespace( this, qname.getNamespaceURI(), qname.getPrefix(), null );
  }

  /**
   * Explicitly declares an XML namespace and a suggested prefix at this level.
   * The prefix that actually ends up being bound to the namespace may not be the same as the suggested prefix.
   * @param ns the XmlNamespace that contains the namespace URI to be declared and the suggested prefix to which the namespace should be bound
   */
  public void declareNamespace(XmlNamespace ns) {
    if (ns == null) {
      throw new IllegalArgumentException("The value for the ns parameter must not be null");
    }
    XmlElementInternals.instance().doDeclareNamespace( this, ns.getNamespaceURI(), ns.getPrefix(), null );
  }

  /**
   * Explicitly declares an XML namespace at this level with the specified namespace URI.
   * @param nsuri the namespace URI to be declared
   */
  public void declareNamespace(URI nsuri) {
    if (nsuri == null) {
      throw new IllegalArgumentException("The value for the nsuri parameter must not be null");
    }
    XmlElementInternals.instance().doDeclareNamespace( this, nsuri.toString(), "", null );
  }

  /**
   * Parses XML from the specified input stream. The input stream will be closed by this method.
   * @param stream The stream containing the XML
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( InputStream stream ) {
    return XmlElementInternals.instance().parse( stream );
  }

  /**
   * Parses XML from the specified input stream. The input stream will be closed by this method.
   * @param stream The stream containing the XML
   * @param options parsing options
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( InputStream stream, XmlParseOptions options ) {
    return XmlElementInternals.instance().parse( stream, options );
  }

  /**
   * Parses XML from the specified byte array.
   * @param bytes The byte array containing the XML
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( byte[] bytes ) {
    return XmlElementInternals.instance().parse( bytes );
  }

  /**
   * Parses XML from the specified byte array.
   * @param bytes The byte array containing the XML
   * @param options parsing options
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( byte[] bytes, XmlParseOptions options ) {
    return XmlElementInternals.instance().parse( bytes, options );
  }

  /**
   * Parses XML from the specified file.
   * @param file The file containing the XML
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( File file ) {
    return XmlElementInternals.instance().parse( file );
  }

  /**
   * Parses XML from the specified file.
   * @param file The file containing the XML
   * @param options parsing options
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( File file, XmlParseOptions options ) {
    return XmlElementInternals.instance().parse( file, options );
  }

  /**
   * Parses XML from the specified string.
   * @param string The string containing the XML
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( String string ) {
    return XmlElementInternals.instance().parse( string );
  }

  /**
   * Parses XML from the specified string.
   * @param string The string containing the XML
   * @param options parsing options
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( String string, XmlParseOptions options ) {
    return XmlElementInternals.instance().parse( string, options );
  }

  /**
   * Parses XML from the specified reader. The reader will be closed by this method.
   * @param reader The reader containing the XML
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( Reader reader ) {
    return XmlElementInternals.instance().parse( reader );
  }

  /**
   * Parses XML from the specified reader. The reader will be closed by this method.
   * @param reader The reader containing the XML
   * @param options parsing options
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( Reader reader, XmlParseOptions options ) {
    return XmlElementInternals.instance().parse( reader, options );
  }

  /**
   * Parses XML from the specified URL.
   * @param url The URL containing the XML
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( URL url ) {
    return XmlElementInternals.instance().parse( url );
  }

  /**
   * Parses XML from the specified URL.
   * @param url The URL containing the XML
   * @param options parsing options
   * @return An XmlElement representing the XML content
   */
  public static XmlElement parse( URL url, XmlParseOptions options ) {
    return XmlElementInternals.instance().parse( url, options );
  }

  /**
   * Returns the backing type instance of this element.
   * @return the backing type instance of this element
   */
  public XmlTypeInstance getTypeInstance() {
    return super.getTypeInstance();
  }

  /**
   * Sets the backing type instance of this element.
   * @param xmlTypeInstance the new backing type instance for this element
   */
  public void setTypeInstance( XmlTypeInstance xmlTypeInstance ) {
    XmlElementInternals.instance().checkSetTypeInstance( this, xmlTypeInstance );
    super.setTypeInstance( xmlTypeInstance );
  }

  @Override
  public IType getIntrinsicType() {
    return _type == null ? JavaTypes.getSystemType( getClass() ) : _type;
  }

  /**
   * Returns a human-readable description of this element.
   * @return a human-readable description of this element
   */
  public String toString() {
    return _type == null ? ( "Element " + getQName() )  : super.toString();
  }

  /**
   * Returns the namespace context of this element.
   * @return the namespace context of this element
   */
  public Map<String,String> getNamespaceContext() {
    Map<String,String> ret = new HashMap<String, String>();
    ret.putAll( new XmlDeserializationContext( null ).getNamespaces() );
    for ( Pair<String, URI> declaredNamespaceBinding : _declaredNamespaceBindings ) {
      ret.put( declaredNamespaceBinding.getFirst(), declaredNamespaceBinding.getSecond().toString() );
    }
    return ret;
  }

  /**
   * Returns a list of explicitly declared namespaces at this element's level.
   * @return a list of explicitly declared namespaces at this element's level
   */
  public List<Pair<String,URI>> getDeclaredNamespaces() {
    return Collections.unmodifiableList( _declaredNamespaceBindings );
  }

  /**
   * Attaches a comment to this element.
   * @param comment the comment to be set
   */
  public void setComment( String comment ) {
    _comment = comment;
  }

  /**
   * Returns the comment previously set on this element, if applicable.
   * @return the comment previously set on this element, if applicable
   */
  public String getComment() {
    return _comment;
  }

  /**
   * Returns true if this element is nil.
   * @return true if this element is nil
   */
  public boolean isNil() {
    return _nil;
  }

  /**
   * Sets the nillness of this element.
   * @param nil the nillness of this element
   */
  public void setNil( boolean nil ) {
    _nil = nil;
  }

}
