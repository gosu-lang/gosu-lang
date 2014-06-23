/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.xml.XmlElement;
import gw.xml.XmlSortException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * A sort handler that matches a single element. Any minOccurs/maxOccurs is handled by XmlSchemaObjectPluralityXmlSortHandler.
 */
public class XmlSchemaElementXmlSortHandler extends XmlSortHandler {
  
  private final XmlSchemaElement _element;
  private final Set<QName> _triedQNames = new HashSet<QName>();
  private QName _lastQName;
  private boolean _moreMatches = true;

  public XmlSchemaElementXmlSortHandler( XmlSchemaElement element ) {
    super( element );
    _element = element;
  }

  @Override
  public List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    boolean isRef = _element.getRefName() != null;
    QName elementQName = isRef ? _element.getRefName() : _element.getQName();
    ArrayList<XmlElement> ret = null;
    _moreMatches = false;
    for ( XmlElement child : remainingChildren ) {
      QName childQName = child.getQName();
      if ( _triedQNames.contains( childQName ) && ! childQName.equals( _lastQName ) ) {
        continue;
      }
      if ( ret == null ) {
        boolean match;
        if ( ! isRef ) {
          // if not a ref, cannot be a substitution group - so only check for an exact match
          match = childQName.equals( elementQName );
        }
        else {
          match = matchSubstitutionGroups( elementQName, childQName, requiredSchemas );
        }
        if ( match ) {
          _lastQName = childQName;
          ret = new ArrayList<XmlElement>( 1 );
          ret.add( child ); // can't use Collections.singletonList since this list gets modified later
        }
        else if ( preferredOnly ) {
          throw new XmlSortException();
        }
      }
      else if ( ! childQName.equals( _lastQName ) ) {
        // the substitution group matching logic can be cpu intensive, so let's make sure we're dealing with an element ref first
        if ( isRef && matchSubstitutionGroups( elementQName, childQName, requiredSchemas ) ) {
          _moreMatches = true;
          break;
        }
        else {
          _triedQNames.add( childQName );
        }
      }
    }
    if ( ret != null ) {
      return ret;
    }
    if ( _triedQNames.contains( elementQName ) ) {
      throw new XmlSortException();
    }
    else {
      throw new XmlSortException( Collections.singletonList( elementQName ) );
    }
  }

  private boolean matchSubstitutionGroups( QName elementQName, QName childQName, List<XmlSchemaIndex> requiredSchemas ) {
    // check substitution groups
    while ( childQName != null ) {
      if ( ! _triedQNames.contains( childQName ) && childQName.equals( elementQName ) ) {
        return true;
      }
      childQName = getSubstitutionGroup( childQName, requiredSchemas );
    }
    return false;
  }

  private QName getSubstitutionGroup( QName childQName, List<XmlSchemaIndex> requiredSchemas ) {
    for ( XmlSchemaIndex schemaIndex : requiredSchemas ) {
      XmlSchemaElement childElementSpec = schemaIndex.getXmlSchemaElementByQNameIfValid( childQName );
      if ( childElementSpec != null ) {
        return childElementSpec.getSubstitutionGroup();
      }
    }
    return null;
  }

  @Override
  protected boolean selectNextChoiceDirect() {
    if ( _lastQName != null ) {
      _triedQNames.add( _lastQName );
      _lastQName = null;
      return _moreMatches;
    }
    return false;
  }

  @Override
  public void _reset() {
    _lastQName = null;
    _triedQNames.clear();
    _moreMatches = true;
  }

  @Override
  protected void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas ) {
    boolean isRef = _element.getRefName() != null;
    QName elementQName = isRef ? _element.getRefName() : _element.getQName();
    for ( XmlElement child : remainingChildren ) {
      QName childQName = child.getQName();
      if ( _triedQNames.contains( childQName ) ) {
        continue;
      }
      boolean match;
      if ( ! isRef ) {
        // if not a ref, cannot be a substitution group - so only check for an exact match
        match = childQName.equals( elementQName );
      }
      else {
        match = matchSubstitutionGroups( elementQName, childQName, requiredSchemas );
      }
      if ( match ) {
        return;
      }
      _triedQNames.add( childQName ); // save bad match for sort algorithm
    }
    throw new XmlSortException( Collections.singletonList( elementQName ) );
  }

  @Override
  public void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas ) {
    Iterator<XmlElement> iter = children.iterator();
    while ( iter.hasNext() ) {
      XmlElement child = iter.next();
      boolean isRef = _element.getRefName() != null;
      QName elementQName = isRef ? _element.getRefName() : _element.getQName();
      QName childQName = child.getQName();
      boolean match = false;
      if ( ! isRef ) {
        // if not a ref, cannot be a substitution group - so only check for an exact match
        match = childQName.equals( elementQName );
      }
      else {
        // check substitution groups
        while ( childQName != null ) {
          if ( ! _triedQNames.contains( childQName ) && childQName.equals( elementQName ) ) {
            match = true;
            break;
          }
          childQName = getSubstitutionGroup( childQName, requiredSchemas );
        }
      }
      if ( match ) {
        iter.remove();
      }
    }
  }

}
