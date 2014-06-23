/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.IType;
import gw.util.ScopedMap;
import gw.xml.XmlElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.XMLConstants;

public class XmlSerializationContext {

  private static final ScopedMap<String,String> _basePrefixToNamespaceUri = new ScopedMap<String, String>();
  private static final ScopedMap<String,TreeSet<String>> _baseNamespaceUriToPrefix = new ScopedMap<String, TreeSet<String>>();

  static {
    _basePrefixToNamespaceUri.put( XMLConstants.DEFAULT_NS_PREFIX, XMLConstants.NULL_NS_URI );
    _baseNamespaceUriToPrefix.put( XMLConstants.NULL_NS_URI, new TreeSet<String>( Collections.singletonList( XMLConstants.DEFAULT_NS_PREFIX ) ) );
    _basePrefixToNamespaceUri.put( XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI );
    _baseNamespaceUriToPrefix.put( XMLConstants.XML_NS_URI, new TreeSet<String>( Collections.singletonList( XMLConstants.XML_NS_PREFIX ) ) );
    _basePrefixToNamespaceUri.put( XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
    _baseNamespaceUriToPrefix.put( XMLConstants.XMLNS_ATTRIBUTE_NS_URI, new TreeSet<String>( Collections.singletonList( XMLConstants.XMLNS_ATTRIBUTE ) ) );

  }

  private ScopedMap<String,String> _prefixToNamespaceUri = _basePrefixToNamespaceUri.pushScope();
  private ScopedMap<String,TreeSet<String>> _namespaceUriToPrefix = _baseNamespaceUriToPrefix.pushScope();
  private Map<XmlElement,String> _idByElement = new HashMap<XmlElement, String>();
  private Set<String> _usedIds = new HashSet<String>();
  private int _nextAutomaticId = 0;
  private XmlElement _currentElement;
  private SortedMap<String, XmlSchemaIndex> _requiredSchemas = new TreeMap<String, XmlSchemaIndex>();

  public XmlSerializationContext() {
  }

  public ScopedMap<String, String> getPrefixToNamespaceUriMap() {
    return _prefixToNamespaceUri;
  }

  public ScopedMap<String, TreeSet<String>> getNamespaceUriToPrefixMap() {
    return _namespaceUriToPrefix;
  }

  public void pushScope() {
    _prefixToNamespaceUri = _prefixToNamespaceUri.pushScope();
    _namespaceUriToPrefix = _namespaceUriToPrefix.pushScope();
  }

  public void popScope() {
    _prefixToNamespaceUri = _prefixToNamespaceUri.popScope();
    _namespaceUriToPrefix = _namespaceUriToPrefix.popScope();
  }

  public void setCurrentElement( XmlElement currentElement ) {
    _currentElement = currentElement;
  }

  public XmlElement getCurrentElement() {
    return _currentElement;
  }

  public String makeUniqueId( XmlElement element, String suggestedValue ) {
    String id = _idByElement.get( element );
    if ( id == null ) {
      id = suggestedValue == null ? "ID" : suggestedValue;
      while ( ! _usedIds.add( id ) ) {
        id = "ID" + _nextAutomaticId++;
      }
      _idByElement.put( element, id );
    }
    return id;
  }

  // Add a schema that is required for parsing this XML document. For example:
  // Element A exists in schema A. There is a substitution group member B in schema B.
  // When creating an instance, you actually substitute a B for an A. When serializing the XML,
  // you must realize that since element B ( from schema B ) is included in the instance,
  // the actual schema for element B must be included in the list of schemas to be validated
  // against, or the instance document will not be considered valid. This goes for xsi:types as
  // well, since the xsi:type qname will only resolve if the schema containing the type referenced
  // by the xsi:type is included in the list of schemas at validation time.
  public void addRequiredSchema( IType type ) {
    if ( type != null ) {
      // schemas can refer to some of the simple types in the schema schema without referencing that schema in any way.
      // Referencing anything else in the schema schema requires an explicit import.
      if ( ! XmlSchemaIndex.isBuiltInDatatype( type ) ) {
        XmlSchemaIndex<?> schemaIndex = XmlSchemaIndex.getSchemaIndexByType( type );
        if ( schemaIndex != null ) {
          _requiredSchemas.put( schemaIndex.getPackageName(), schemaIndex );
        }
      }
    }
  }

  public List<XmlSchemaIndex> getRequiredSchemas() {
    return new ArrayList<XmlSchemaIndex>( _requiredSchemas.values() );
  }

}
