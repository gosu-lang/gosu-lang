/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAll;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.xml.XmlElement;
import gw.xml.XmlSortException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * Sort handler that represents an xsd:all.
 */
public class XmlSchemaAllXmlSortHandler extends XmlSortHandler {

  private final XmlSortHandler[] _handlers;

  public XmlSchemaAllXmlSortHandler( XmlSchemaAll all ) {
    super( all );
    _handlers = convertChildrenToHandlers( all );
  }

  private static XmlSortHandler[] convertChildrenToHandlers( XmlSchemaAll all ) {
    List<XmlSchemaElement> items = all.getItems();
    Iterator<XmlSchemaElement> iter = items.iterator();
    XmlSortHandler[] children = new XmlSortHandler[ items.size() ];
    int idx = 0;
    while ( iter.hasNext() ) {
      children[ idx++ ] = XmlSorter.createHandler( iter.next() );
    }
    return children;
  }

  // It appears that an xs:all must appear in isolation. It can contain only elements with minOccurs=0 or 1, and
  // can't have any sibling particles whatsoever. But I didn't want to trust that interpretation until I understand
  // the specification better. So the algorithm is to try to match each element in the remainingChildren list
  // against some handler in the handler list. Each match occurs only once, and we remove that handler when we
  // match it. A handler can match even if the element doesn't exist in the remainingChildren, since minOccurs="0"
  // would always match. Once we're done, if there are any handlers that are left unmatched, we throw an
  // XmlSortException. Tests are in XmlSchemaAllTest.
  @Override
  protected List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    List<XmlElement> allMatches = new ArrayList<XmlElement>();
    LinkedHashSet<XmlSortHandler> remainingHandlers = new LinkedHashSet<XmlSortHandler>( Arrays.asList( _handlers ) );
    for ( XmlElement remainingChild : remainingChildren ) {
      XmlElement matchedElement = null;
      XmlSortHandler matchedHandler = null;
      for ( XmlSortHandler remainingHandler : remainingHandlers ) {
        remainingHandler.reset();
        try {
          List<XmlElement> matchedElements = remainingHandler.sort( new LinkedList<XmlElement>( Collections.singletonList( remainingChild ) ), true, requiredSchemas, Collections.<XmlElement>emptySet() );
          matchedHandler = remainingHandler;
          if ( matchedElements.size() == 1 ) {
            matchedElement = matchedElements.get( 0 );
            break; // found best match
          }
        }
        catch ( XmlSortException ex ) {
          // continue
        }
      }
      if ( matchedElement != null ) {
        allMatches.add( matchedElement );
      }
      remainingHandlers.remove( matchedHandler );
    }
    if ( ! remainingHandlers.isEmpty() ) {
      // remove any remaining handlers that are minOccurs="0"
      Iterator<XmlSortHandler> iter = remainingHandlers.iterator();
      while ( iter.hasNext() ) {
        XmlSortHandler remainingHandler = iter.next();
        try {
          remainingHandler.sort( new LinkedList<XmlElement>(), true, requiredSchemas, Collections.<XmlElement>emptySet() );
          iter.remove();
        }
        catch ( XmlSortException ex ) {
          // ignore
        }
      }
      if ( ! remainingHandlers.isEmpty() ) {
        QName[] qnames = new QName[remainingHandlers.size()];
        int idx = 0;
        for ( XmlSortHandler remainingHandler : remainingHandlers ) {
          XmlSchemaElement element = (XmlSchemaElement) remainingHandler.getSchemaObject();
          QName qname = element.getQName();
          if ( qname == null ) {
            qname = element.getRefName();
          }
          qnames[ idx++ ] = qname;
        }
        throw new XmlSortException( Arrays.asList( qnames ) );
      }
    }
    return allMatches;
  }

  @Override
  protected boolean selectNextChoiceDirect() {
    return false;
  }

  @Override
  protected void _reset() {
  }

  @Override
  protected void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas ) {
    for ( XmlSortHandler handler : _handlers ) {
      handler.checkMissingRequiredElements( remainingChildren, requiredSchemas );
    }

  }

  @Override
  public void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas ) {
    for ( XmlSortHandler handler : _handlers ) {
      handler.match( children, requiredSchemas );
    }
  }
}
