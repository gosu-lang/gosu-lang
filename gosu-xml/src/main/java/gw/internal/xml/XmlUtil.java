/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.config.CommonServices;
import gw.fs.IFile;
import gw.util.GosuExceptionUtil;
import gw.xml.XmlElement;
import gw.xml.XmlException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Utility methods for XML.
 */
public class XmlUtil {

  private static final boolean[] NAME_START_CHARS = new boolean[ 256 ];

  static {
	  // ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] |
    // [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] |
    // [#x10000-#xEFFFF]
    NAME_START_CHARS[ ':' ] = true;
    for ( char ch = 'A'; ch <= 'Z'; ch++ ) {
      NAME_START_CHARS[ ch ] = true;
    }
    NAME_START_CHARS[ '_' ] = true;
    for ( char ch = 'a'; ch <= 'z'; ch++ ) {
      NAME_START_CHARS[ ch ] = true;
    }
    for ( char ch = 0xC0; ch <= 0xD6; ch++ ) {
      NAME_START_CHARS[ ch ] = true;
    }
    for ( char ch = 0xD8; ch <= 0xF6; ch++ ) {
      NAME_START_CHARS[ ch ] = true;
    }
    for ( char ch = 0xF8; ch <= 0xFF; ch++ ) {
      NAME_START_CHARS[ ch ] = true;
    }
  }

  public static String[] splitQName(String qualifiedName) {
    if ( qualifiedName == null ) {
      throw new IllegalArgumentException( "Null QName" );
    }
    int idx = qualifiedName.indexOf(':');
    String[] parts = { null, null };
    if (idx >= 0) {
      parts[0] = qualifiedName.substring(0, idx);
      parts[1] = qualifiedName.substring(idx + 1);
    }
    else {
      parts[0] = XMLConstants.DEFAULT_NS_PREFIX;
      parts[1] = qualifiedName;
    }
    return parts;
  }

  /**
   * Discards the QName's namespace URI, and returns it's colonized name using the QName's embedded
   * prefix string.
   * @param qName the qualified name
   * @return qName prefix - colon - local part
   */
  public static String qnameToString(QName qName) {
    if ( qName == null ) {
      throw new IllegalArgumentException( "Null QName" );
    }
    validateQName( qName );
    return qnameToString( qName.getPrefix(), qName.getLocalPart() );
  }

  public static QName stringToQName(String qualifiedName, Map<String,String> namespaceMap) {
    QName qname;
    String[] parts = XmlUtil.splitQName(qualifiedName);
    String nsURI = namespaceMap.get(parts[0]);
    if (nsURI == null) {
      throw new XmlException("Namespace not found for prefix '" + parts[0] + "'");
    }
    qname = new QName(nsURI, parts[1], parts[0]);
    validateQName( qname );
    return qname;
  }

  public static String qnameToString( String prefix, String localPart ) {
    if ( prefix == null ) {
      throw new IllegalArgumentException( "Null prefix" );
    }
    if ( localPart == null ) {
      throw new IllegalArgumentException( "Null localPart" );
    }
    if ( prefix.equals( XMLConstants.DEFAULT_NS_PREFIX ) ) {
      return localPart;
    }
    else {
      return prefix + ':' + localPart;
    }
  }

  public static void validateQName( QName qname ) {
    if ( qname == null ) {
      throw new IllegalArgumentException( "Null QName" );
    }
    try {
      validateNCName( qname.getLocalPart() );
    }
    catch ( XmlException ex ) {
      throw new XmlException( "Invalid QName: " + qname, ex );
    }
  }

  public static void validateNCName( String localPart ) {
    if ( localPart.indexOf( ':' ) >= 0 ) {
      throw new XmlException( "Invalid NCName: " + localPart + " ( contains a colon )" );
    }
    if ( localPart.length() == 0 ) {
      throw new XmlException( "NCName cannot be empty" );
    }
    int idx = 0;
    if ( ! isNameStartChar( localPart.codePointAt( idx ) ) ) {
      throw new XmlException( "Invalid NCName: " + localPart + " ( invalid start character )" );
    }
    if ( Character.isHighSurrogate( localPart.charAt( idx ) ) ) {
      idx += 2;
    }
    else {
      idx++;
    }
    while ( idx < localPart.length() ) {
      if ( ! isNameChar( localPart.codePointAt( idx ) ) ) {
        throw new XmlException( "Invalid NCName: " + localPart + " ( invalid character at position " + idx + " )" );
      }
      if ( Character.isHighSurrogate( localPart.charAt( idx ) ) ) {
        idx += 2;
      }
      else {
        idx++;
      }
    }
  }

  // 	NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
  @SuppressWarnings( { "RedundantIfStatement" } )
  public static boolean isNameChar( int ch ) {
    if ( isNameStartChar( ch ) ) {
      return true;
    }
    if ( ch == '-' || ch == '.' ) {
      return true;
    }
    if ( ch >= '0' && ch <= '9' ) {
      return true;
    }
    if ( ch == 0xB7 ) {
      return true;
    }
    if ( ch >= 0x300 && ch <= 0x36F ) {
      return true;
    }
    if ( ch >= 0x203F && ch <= 0x2040 ) {
      return true;
    }
    return false;
  }

  // ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] |
  // [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] |
  // [#x10000-#xEFFFF]
  @SuppressWarnings( { "RedundantIfStatement" } )
  public static boolean isNameStartChar( int ch ) {
    if ( ch < 256 ) {
      return NAME_START_CHARS[ ch ];
    }
    else if ( ch <= 0x2FF ) {
      return true;
    }
    else if ( ch < 0x370 ) {
      return false;
    }
    else if ( ch <= 0x37D ) {
      return true;
    }
    else if ( ch < 0x37F ) {
      return false;
    }
    else if ( ch <= 0x1FFF ) {
      return true;
    }
    else if ( ch < 0x200C ) {
      return false;
    }
    else if ( ch <= 0x200D ) {
      return true;
    }
    else if ( ch < 0x2070 ) {
      return false;
    }
    else if ( ch <= 0x218F ) {
      return true;
    }
    else if ( ch < 0x2C00 ) {
      return false;
    }
    else if ( ch <= 0x2FEF ) {
      return true;
    }
    else if ( ch < 0x3001 ) {
      return false;
    }
    else if ( ch <= 0xD7FF ) {
      return true;
    }
    else if ( ch < 0xF900 ) {
      return false;
    }
    else if ( ch <= 0xFDCF ) {
      return true;
    }
    else if ( ch < 0xFDF0 ) {
      return false;
    }
    else if ( ch <= 0xFFFD ) {
      return true;
    }
    else if ( ch < 0x10000 ) {
      return false;
    }
    else if ( ch <= 0xEFFFF ) {
      return true;
    }
    else {
      return false;
    }
  }

  public static void main( String... args ) {
    testValidateNCNameChars();
  }

  // this is called from XmlUtilTest. It's too slow to perform in pure Gosu until bytecode is turned on.
  static void testValidateNCNameChars() {
    // 	NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]

    for ( int ch = 0; ch < 0xfffff; ch++ ) {
      try {
        if (
           // name part chars:
           ( ch == '-' ) ||
           ( ch == '.' ) ||
           ( ch >= '0' && ch <= '9' ) ||
           ( ch == 0xb7 ) ||
           ( ch >= 0x300 && ch <= 0x36f ) ||
           ( ch >= 0x203f && ch <= 0x2040 ) ||
           // name start chars:
           ( ch >= 'A' && ch <= 'Z' ) ||
           ( ch == '_' ) ||
           ( ch >= 'a' && ch <= 'z' ) ||
           ( ch >= 0xc0 && ch <= 0xd6 ) ||
           ( ch >= 0xd8 && ch <= 0xf6 ) ||
           ( ch >= 0xf8 && ch <= 0x2ff ) ||
           ( ch >= 0x370 && ch <= 0x37d ) ||
           ( ch >= 0x37f && ch <= 0x1fff ) ||
           ( ch >= 0x200c && ch <= 0x200d ) ||
           ( ch >= 0x2070 && ch <= 0x218f ) ||
           ( ch >= 0x2c00 && ch <= 0x2fef ) ||
           ( ch >= 0x3001 && ch <= 0xd7ff ) ||
           ( ch >= 0xf900 && ch <= 0xfdcf ) ||
           ( ch >= 0xfdf0 && ch <= 0xfffd ) ||
           ( ch >= 0x10000 && ch <= 0xeffff ) ) {
          validateNCName( "x" + String.valueOf( Character.toChars( ch ) ) );
        }
        else {
          try {
            validateNCName( "x" + String.valueOf( Character.toChars( ch ) ) );
            throw new RuntimeException( "Expected XmlException" );
          }
          catch ( XmlException ex ) {
            // good
          }
        }
      }
      catch ( Throwable t ) {
        throw new RuntimeException( "Exception while evaluating character 0x" + Integer.toHexString( ch ), t );
      }
    }
  }

  // this is called from XmlUtilTest. It's too slow to perform in pure Gosu until bytecode is turned on.
  static void testValidateNCNameStartChars() {
    // ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] |
    // [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] |
    // [#x10000-#xEFFFF]

    for ( int ch = 0; ch < 0xfffff; ch++ ) {
      try {
        if (
           ( ch >= 'A' && ch <= 'Z' ) ||
           ch == '_' ||
           ( ch >= 'a' && ch <= 'z' ) ||
           ( ch >= 0xc0 && ch <= 0xd6 ) ||
           ( ch >= 0xd8 && ch <= 0xf6 ) ||
           ( ch >= 0xf8 && ch <= 0x2ff ) ||
           ( ch >= 0x370 && ch <= 0x37d ) ||
           ( ch >= 0x37f && ch <= 0x1fff ) ||
           ( ch >= 0x200c && ch <= 0x200d ) ||
           ( ch >= 0x2070 && ch <= 0x218f ) ||
           ( ch >= 0x2c00 && ch <= 0x2fef ) ||
           ( ch >= 0x3001 && ch <= 0xd7ff ) ||
           ( ch >= 0xf900 && ch <= 0xfdcf ) ||
           ( ch >= 0xfdf0 && ch <= 0xfffd ) ||
           ( ch >= 0x10000 && ch <= 0xeffff ) ) {
          validateNCName( String.valueOf( Character.toChars( ch ) ) );
        }
        else {
          try {
            validateNCName( String.valueOf( Character.toChars( ch ) ) );
            throw new RuntimeException( "Expected XmlException" );
          }
          catch ( XmlException ex ) {
            // good
          }
        }
      }
      catch ( Throwable t ) {
        throw new RuntimeException( "Exception while evaluating character 0x" + Integer.toHexString( ch ), t );
      }
    }
  }

  /** This will convert an dom element into an XmlElement
   *
   * @param el the dom element
   * @return the XmlElement
   */
  public static XmlElement convert( Element el ) {
    return XmlElement.parse( serializeDOM( el ) );
  }

  public static byte[] serializeDOM( Element el ) {
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer;

      transformer = transformerFactory.newTransformer();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      transformer.transform( new DOMSource( el ), new StreamResult( baos ) );
      return baos.toByteArray();
    }
    catch ( Exception ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
  }

  public static Document parseDOM( byte[] bytes ) throws ParserConfigurationException, IOException, SAXException {
    return parseDOM( new ByteArrayInputStream( bytes ) );
  }

  public static Document parseDOM( InputStream inputStream ) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware( true );
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse( inputStream );
  }

}
