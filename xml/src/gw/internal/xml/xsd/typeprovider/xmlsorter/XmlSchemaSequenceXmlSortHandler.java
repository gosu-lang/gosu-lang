/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSequence;
import gw.xml.XmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Sort handler that represents an xsd:sequence.
 */
public class XmlSchemaSequenceXmlSortHandler extends XmlSortHandler {
  
  private final XmlSortHandler[] _children;

  public XmlSchemaSequenceXmlSortHandler( XmlSchemaSequence sequence ) {
    this( sequence, convertChildrenToHandlers( sequence ) );
  }

  public XmlSchemaSequenceXmlSortHandler( XmlSchemaObject schemaObject, XmlSortHandler... children ) {
    super( schemaObject );
    _children = children;
  }

  private static XmlSortHandler[] convertChildrenToHandlers( XmlSchemaSequence sequence ) {
    List<XmlSchemaParticle> items = sequence.getItems();
    Iterator<XmlSchemaParticle> iter = items.iterator();
    XmlSortHandler[] children = new XmlSortHandler[ items.size() ];
    int idx = 0;
    while ( iter.hasNext() ) {
      children[ idx++ ] = XmlSorter.createHandler( iter.next() );
    }
    return children;
  }

  @Override
  public List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    Map<XmlElement, Set<Integer>> matches = new HashMap<XmlElement, Set<Integer>>();
    for ( int i = 0; i < _children.length; i++ ) {
      Set<XmlElement> matchResults = new HashSet<XmlElement>( mustMatch );
      Set<XmlElement> matchResults2 = new HashSet<XmlElement>( mustMatch );
      _children[i].match( matchResults, requiredSchemas );
      // this is NOT equivalent to removeAll() when multiples of the same child exist in the content list
      for ( XmlElement match : matchResults ) {
        matchResults2.remove( match );
      }
      // each item in matchResults2 was matched by this child
      for ( XmlElement match : matchResults2 ) {
        addMatch( matches, i, match );
      }
    }
    List<XmlElement> sortedChildren = new ArrayList<XmlElement>();
    LinkedList<XmlElement> localRemainingChildren = new LinkedList<XmlElement>( remainingChildren );
    for ( int i = 0; i < _children.length; i++ ) {
      Set<XmlElement> localMustMatch = new HashSet<XmlElement>();
      for ( XmlElement remainingChild : localRemainingChildren ) {
        Set<Integer> indexes = matches.get( remainingChild );
        if ( indexes != null && indexes.size() == 1 && indexes.iterator().next() == i ) {
          // we must match this child
          localMustMatch.add( remainingChild );
        }
      }
      List<XmlElement> localSortedChildren = _children[i].sort( localRemainingChildren, preferredOnly, requiredSchemas, localMustMatch );
      sortedChildren.addAll( localSortedChildren );
      // this is NOT equivalent to removeAll() when multiples of the same child exist in the content list
      for ( XmlElement localSortedChild : localSortedChildren ) {
        localRemainingChildren.remove( localSortedChild );
      }
    }
    return sortedChildren;
  }

  private void addMatch( Map<XmlElement, Set<Integer>> matches, int i, XmlElement match ) {
    Set<Integer> set = matches.get( match );
    if ( set == null ) {
      set = new HashSet<Integer>();
      matches.put( match, set );
    }
    set.add( i );
  }

  @Override
  protected boolean selectNextChoiceDirect() {
    return selectNextChoice( _children.length - 1 );
  }

  @Override
  public void _reset() {
    for ( XmlSortHandler child : _children ) {
      child.reset();
    }
  }

  @Override
  protected void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas ) {
    for ( XmlSortHandler child : _children ) {
      child.checkMissingRequiredElements( remainingChildren, requiredSchemas );
    }

  }

  @Override
  public void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas ) {
    for ( XmlSortHandler child : _children ) {
      child.match( children, requiredSchemas );
    }
  }

  private boolean selectNextChoice( int idx ) {
    if ( idx >= 0 ) {
      final XmlSortHandler child = _children[ idx ];
      if ( child.selectNextChoice() ) {
        return true;
      }
      else {
        // try next child
        if ( selectNextChoice( idx - 1 ) ) {
          child.reset();
          return true;
        }
      }
    }
    return false;
  }
}
