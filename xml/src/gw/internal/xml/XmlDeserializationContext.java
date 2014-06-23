/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.IDREFSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.xmlmatcher.XmlMatchHandler;
import gw.util.Pair;
import gw.util.ScopedMap;
import gw.xml.XmlElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;

/**
 * Context for parsing XML.
 */
public class XmlDeserializationContext {

  private XmlElement _currentElement;
  private final List<Pair<String,IDREFSimpleValueFactory.Value>> _idrefs;
  private final Map<String,XmlElement> _ids;
  private final StringBuilder _allText = new StringBuilder();
  private final XmlDeserializationContext _parent;
  private XmlSchemaTypeSchemaInfo _schemaInfo;
  private XmlMatchHandler _matchHandler;
  private ScopedMap<String, String> _namespaces;
  private boolean _mixed;

  public XmlDeserializationContext( XmlDeserializationContext parent ) {
    _parent = parent;
    if ( parent == null ) {
      _idrefs = new ArrayList<Pair<String, IDREFSimpleValueFactory.Value>>( );
      _ids = new HashMap<String, XmlElement>();
      _namespaces = new ScopedMap<String, String>();
      _namespaces.put( XMLConstants.DEFAULT_NS_PREFIX, XMLConstants.NULL_NS_URI );
      _namespaces.put( XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI );
      _namespaces.put( XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI );
    }
    else {
      _idrefs = parent._idrefs;
      _ids = parent._ids;
      _namespaces = parent._namespaces.pushScope();
      _mixed = parent._mixed;
    }
  }

  public void addNamespace( String prefix, String uri ) {
    _namespaces.put( prefix, uri );
  }

  public Map<String, String> getNamespaces() {
    return Collections.unmodifiableMap( _namespaces );
  }

  public void addIdref( String stringValue, IDREFSimpleValueFactory.Value value ) {
    _idrefs.add( new Pair<String, IDREFSimpleValueFactory.Value>( stringValue, value ) );
  }

  public XmlElement getCurrentElement() {
    return _currentElement;
  }

  public void setCurrentElement( XmlElement currentElement ) {
    _currentElement = currentElement;
  }

  public void addId( String id, XmlElement element ) {
    _ids.put( id, element );
  }

  public List<Pair<String, IDREFSimpleValueFactory.Value>> getIdrefs() {
    return _idrefs;
  }

  public Map<String, XmlElement> getIds() {
    return _ids;
  }

  public StringBuilder getAllText() {
    return _allText;
  }

  public XmlDeserializationContext getParent() {
    return _parent;
  }

  public void setSchemaInfo( XmlSchemaTypeSchemaInfo schemaInfo ) {
    _schemaInfo = schemaInfo;
  }

  public XmlSchemaTypeSchemaInfo getSchemaInfo() {
    return _schemaInfo;
  }

  public void setMatchHandler( XmlMatchHandler matchHandler ) {
    _matchHandler = matchHandler;
  }

  public XmlMatchHandler getMatchHandler() {
    return _matchHandler;
  }

  public void setMixed( boolean mixed ) {
    _mixed = mixed;
  }

  public boolean isMixed() {
    return _mixed;
  }

}
