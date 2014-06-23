/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAny;
import gw.xml.XmlElement;
import gw.xml.XmlSortException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Sort handler representing an xsd:any. This handler matches a single element, but there are loose restrictions
 * on what type of element it matches, as defined by the XML Schema specification. minOccurs/maxOccurs is handled
 * by the XmlSchemaObjectPluralityXmlSortHandler.
 */
public class XmlSchemaAnyXmlSortHandler extends XmlSortHandler {

  private XmlElement _currentElement = null;
  private Set<XmlElement> _triedElements = new HashSet<XmlElement>();
  private XmlSchemaAny _any;

  // This constructor is called reflectively
  public XmlSchemaAnyXmlSortHandler( XmlSchemaAny any ) {
    super( any );
    _any = any;
  }

  @Override
  protected List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    if ( _currentElement == null ) {
      for ( XmlElement element : remainingChildren ) {
        if ( ! _triedElements.contains( element ) ) {
          _triedElements.add( element );
          if ( _any.accept( element.getQName() ) ) {
            _currentElement = element;
            break;
          }
        }
        if ( preferredOnly ) {
          throwNoMatches();
        }
      }
    }
    if ( _currentElement == null ) {
      return throwNoMatches();
    }
    final ArrayList<XmlElement> sortedChildren = new ArrayList<XmlElement>( 1 ); // this list will be modified
    sortedChildren.add( _currentElement );
    return sortedChildren;
  }

  private List<XmlElement> throwNoMatches() {
    throw new XmlSortException( "No matches for xsd:any, namespace = \"" + _any.getNamespaceSpec() + "\", targetNamespace = [" + _any.getTargetNamespace() + "]" );
  }

  @Override
  protected boolean selectNextChoiceDirect() {
    if ( _currentElement == null ) {
      return false;
    }
    else {
      _currentElement = null;
      return true;
    }
  }

  @Override
  public void _reset() {
    _triedElements.clear();
    _currentElement = null;
  }

  @Override
  protected void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas ) {
    // TODO?
  }

  @Override
  public void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas ) {
    Iterator<XmlElement> iter = children.iterator();
    while ( iter.hasNext() ) {
      XmlElement child = iter.next();
      if ( _any.accept( child.getQName() ) ) {
        iter.remove();
      }
    }
  }

}
