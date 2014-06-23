/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * Represents an XML namespace with an associated prefix. This class can be used to repetitively qualify local names
 * without having to create QName instances manually.
 */
public final class XmlNamespace {

  public static final XmlNamespace NULL_NAMESPACE = new XmlNamespace( XMLConstants.NULL_NS_URI, XMLConstants.DEFAULT_NS_PREFIX );

  private final String _namespaceURI;
  private final String _prefix;

  /**
   * Creates a new XML Namespace with the specified namespace URI and prefix.
   * @param namespaceURI The namespace URI
   * @param prefix The desired prefix
   */
  public XmlNamespace( String namespaceURI, String prefix ) {
    _namespaceURI = namespaceURI;
    _prefix = prefix;
  }

  /**
   * Creates a new XML Namespace with the specified namespace URI and the default (empty) prefix.
   * @param namespaceURI The namespace URI
   * @see javax.xml.XMLConstants#DEFAULT_NS_PREFIX
   */
  public XmlNamespace( String namespaceURI ) {
    this( namespaceURI, XMLConstants.DEFAULT_NS_PREFIX );
  }

  /**
   * Creates a new QName in this XML Namespace using the supplied local name.
   * @param localPart The local name
   * @return a new QName in this XML Namespace using the supplied local name
   */
  public QName qualify( String localPart ) {
    return new QName( _namespaceURI, localPart, _prefix );
  }

  /**
   * Returns the namespace URI of this namespace.
   * @return the namespace URI of this namespace
   */
  public String getNamespaceURI() {
    return _namespaceURI;
  }

  /**
   * Returns the prefix of this namespace.
   * @return the prefix of this namespace
   */
  public String getPrefix() {
    return _prefix;
  }

  /**
   * Returns the namespace for the specified QName.
   * @param qname The QName from which to get the namespace
   * @return the namespace for the specified QName
   */
  public static XmlNamespace forQName( QName qname ) {
    return new XmlNamespace( qname.getNamespaceURI(), qname.getPrefix() );
  }

  /**
   * Returns the namespace uri of this namespace as a java.net.URI.
   * @return the namespace uri of this namespace as a java.net.URI
   * @throws URISyntaxException if the namespace uri could not be parsed as a uri reference
   */
  public URI toURI() throws URISyntaxException {
    return new URI( _namespaceURI );
  }

  /**
   * Returns a string representation of this XmlNamespace object.
   * @return a string representation of this XmlNamespace object 
   */
  @Override
  public String toString() {
    return "{" + _namespaceURI + "}";
  }

  @Override
  public int hashCode() {
    return _namespaceURI.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof XmlNamespace && _namespaceURI.equals(((XmlNamespace)obj)._namespaceURI);
  }
}
