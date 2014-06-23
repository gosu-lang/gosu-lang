/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.xml.XmlElement;
import gw.xml.XmlSortException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This sort handler handles minOccurs and maxOccurs boundings on behalf of other schema particles. This sort handler
 * acts similarly to an xsd:choice handler. Lets say we have a single element with minOccurs=0 and maxOccurs=unbounded.
 * And let's say there are actually 5 copies of this element at this level. Then this sort handler presents 6 choices.
 * The first choice is the max number of elements available, 5. The other choices are 4, 3, 2, 1, and 0. When told
 * to reset itself, this sort handler will recalculate the max number of children on the next call to sort(), which
 * could be greater or less than the previous time it was reset due to previous handlers in the chain adjusting.
 */
public class XmlSchemaObjectPluralityXmlSortHandler extends XmlSortHandler {

  private final XmlSchemaObject _schemaObject;
  private List<XmlSortHandler> _children;
  private final long _minOccurs;
  private final long _maxOccurs;
  private final Constructor<? extends XmlSortHandler> _handlerConstructor;

  public XmlSchemaObjectPluralityXmlSortHandler( long minOccurs, long maxOccurs, XmlSchemaObject schemaObject, Class<? extends XmlSortHandler> handlerClass ) {
    super( schemaObject );
    try {
      _handlerConstructor = handlerClass.getConstructor( schemaObject.getClass() );
    }
    catch ( NoSuchMethodException ex ) {
      throw new RuntimeException( ex );
    }
    _schemaObject = schemaObject;
    _minOccurs = minOccurs;
    _maxOccurs = maxOccurs;
    _children = null;
  }

  private XmlSortHandler makeChild() {
    try {
      return _handlerConstructor.newInstance( _schemaObject );
    }
    catch ( Exception ex) {
      throw new RuntimeException( ex );
    }
  }

  @Override
  public List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    XmlSortException exception = null;
    LinkedList<XmlElement> localRemainingChildren = remainingChildren;
    List<XmlElement> sortedChildren = null;
    if ( _children == null ) {
      // match as many children as possible
      _children = new ArrayList<XmlSortHandler>();
      try {
        for ( int i = 0; i < _maxOccurs; i++ ) {
          final XmlSortHandler child = makeChild();
          List<XmlElement> localSortedChildren = child.sort( localRemainingChildren, preferredOnly, requiredSchemas, Collections.<XmlElement>emptySet() );
          while ( localSortedChildren.isEmpty() ) {
            // imagine trying to find how many possible legal matches can be made on the choice:
            // <choice minOccurs="0" maxOccurs="unbounded">
            //   <xsd:element name="Child" minOccurs="0" maxOccurs="unbounded"/>
            // </choice>
            // therefore, if the child hasn't actually matched any elements, we stop trying to match
            if ( ! child.selectNextChoice() ) {
              throw new XmlSortException();
            }
            localSortedChildren = child.sort( localRemainingChildren, preferredOnly, requiredSchemas, mustMatch );
          }
          if ( ! localSortedChildren.isEmpty() ) {
            if ( sortedChildren == null || sortedChildren.isEmpty() ) {
              sortedChildren = localSortedChildren;
            }
            else {
              sortedChildren.addAll( localSortedChildren );
            }
            //noinspection ObjectEquality
            if ( localRemainingChildren == remainingChildren ) {
              localRemainingChildren = new LinkedList<XmlElement>( remainingChildren );
            }
            // this is NOT equivalent to removeAll() when multiples of the same child exist in the content list
            for ( XmlElement localSortedChild : localSortedChildren ) {
              localRemainingChildren.remove( localSortedChild );
            }
          }
          _children.add( child );
        }
      }
      catch ( XmlSortException ex ) {
        // ok - minOccurs will be checked below
        exception = ex;
      }
    }
    else {
      for ( XmlSortHandler child : _children ) {
        final List<XmlElement> localSortedChildren = child.sort( localRemainingChildren, preferredOnly, requiredSchemas, mustMatch );
        if ( ! localSortedChildren.isEmpty() ) {
          if ( sortedChildren == null ) {
            sortedChildren = new ArrayList<XmlElement>();
          }
          sortedChildren.addAll( localSortedChildren );
          //noinspection ObjectEquality
          if ( localRemainingChildren == remainingChildren ) {
            localRemainingChildren = new LinkedList<XmlElement>( remainingChildren );
          }
          // this is NOT equivalent to removeAll() when multiples of the same child exist in the content list
          for ( XmlElement localSortedChild : localSortedChildren ) {
            localRemainingChildren.remove( localSortedChild );
          }
        }
      }
    }
    if ( _children.size() < _minOccurs ) {
      throw new XmlSortException( "minOccurs not satisfied; expected at least " + _minOccurs + " but found " + _children.size() );
    }
    return sortedChildren == null ? Collections.<XmlElement>emptyList() : sortedChildren;
  }

  @Override
  protected boolean selectNextChoiceDirect() {
    if ( selectNextChoice( 0 ) ) {
      return true;
    }
    else { // no children changed state
      if ( _children.size() > _minOccurs ) {
        _children.remove( _children.size() - 1 );
        for ( XmlSortHandler child : _children ) {
          child.reset();
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public void _reset() {
    _children = null;
  }

  @Override
  protected void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas ) {
    if ( _minOccurs > 0 ) {
      makeChild().checkMissingRequiredElements( remainingChildren, requiredSchemas );
    }
  }

  @Override
  public void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas ) {
    makeChild().match( children, requiredSchemas );
  }

  private boolean selectNextChoice( int idx ) {
    if ( idx < _children.size() ) {
      final XmlSortHandler child = _children.get( idx );
      if ( child.selectNextChoice() ) {
        return true;
      }
      else {
        // try next child
        if ( selectNextChoice( idx + 1 ) ) {
          child.reset();
          return true;
        }
      }
    }
    return false;
  }

}
