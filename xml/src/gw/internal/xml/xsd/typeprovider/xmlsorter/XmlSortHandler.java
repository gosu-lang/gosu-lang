/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.xml.XmlElement;
import gw.xml.XmlSortException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * A handler representing a portion of an XML schema, used for sorting the direct children of an XML element in order
 * to make the ordering of those children match the schema.
 */
public abstract class XmlSortHandler {

  @SuppressWarnings( { "UnusedDeclaration", "FieldCanBeLocal" } )
  private final XmlSchemaObject _schemaObject;
  private boolean _sortCalledSinceLastSelect;
  private boolean _matchesEmpty;

  public XmlSortHandler( XmlSchemaObject schemaObject ) {
    _schemaObject = schemaObject;
  }

  protected abstract List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch );

  protected abstract boolean selectNextChoiceDirect();

  public final void reset() {
    _reset();
  }

  protected abstract void _reset();

  public final List<XmlElement> sort( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    if ( _matchesEmpty && remainingChildren.isEmpty() ) {
      return Collections.emptyList();
    }
    HashSet<XmlElement> localMustMatch = new HashSet<XmlElement>( mustMatch );
    match( localMustMatch, requiredSchemas );
    if ( ! localMustMatch.isEmpty() ) {
      // try sorting 1 time to get a real exception
      _sortCalledSinceLastSelect = true;
      sortDirect( remainingChildren, false, requiredSchemas, mustMatch );
      // no exception from sort routine, so let's throw one
      List<QName> unmatchedQNames = new ArrayList<QName>();
      for ( XmlElement unmatchedElement : localMustMatch ) {
        unmatchedQNames.add( unmatchedElement.getQName() );
      }
      if ( unmatchedQNames.size() == 1 ) {
        throw new XmlSortException( "Unexpected child element: " + unmatchedQNames.get( 0 ) );
      }
      else {
        throw new XmlSortException( "Unexpected child elements: " + unmatchedQNames );
      }
    }
    List<QName> qnames = null;
    String exceptionMessage = null;
    while ( true ) {
      try {
        _sortCalledSinceLastSelect = true;
        List<XmlElement> sortedChildren = sortDirect( remainingChildren, preferredOnly, requiredSchemas, mustMatch );
        if ( sortedChildren.isEmpty() ) {
          _matchesEmpty = true;
          if ( remainingChildren.isEmpty() ) {
            return Collections.emptyList();
          }
          // optimization - try to match children before matching a lack of children
          throw new XmlSortException( "Matched empty" );
        }
        return sortedChildren;
      }
      catch ( XmlSortException ex ) {
        if ( qnames == null ) {
          qnames = new ArrayList<QName>();
        }
        if ( exceptionMessage == null && ex.getMessage() != null ) {
          exceptionMessage = ex.getMessage();
        }
        qnames.addAll( ex.getQNames() );
        if ( ! selectNextChoice() ) {
          if ( _matchesEmpty ) {
            return Collections.emptyList();
          }
          if ( qnames.isEmpty() ) {
            throw new XmlSortException( exceptionMessage );
          }
          else {
            throw new XmlSortException( qnames );
          }
        }
      }
    }
  }

  protected abstract void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas );

  public final boolean selectNextChoice() {
    // if sort was not called since the last selectNextChoice() call, you could not have been part of the failure,
    // and are not obligated to change your state
    if ( _sortCalledSinceLastSelect ) {
      _sortCalledSinceLastSelect = false;
      return selectNextChoiceDirect();
    }
    else {
      return false;
    }
  }

  public abstract void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas );

  public XmlSchemaObject getSchemaObject() {
    return _schemaObject;
  }
  
}
