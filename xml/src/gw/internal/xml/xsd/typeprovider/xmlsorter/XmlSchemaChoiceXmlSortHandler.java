/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaChoice;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.xml.XmlElement;
import gw.xml.XmlSortException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Sort handler representing an xsd:choice. Iterates through its children when told to change state.
 */
public class XmlSchemaChoiceXmlSortHandler extends XmlSortHandler {

  private final List<XmlSortHandler> _children = new ArrayList<XmlSortHandler>();
  private int _idx = 0;

  public XmlSchemaChoiceXmlSortHandler( XmlSchemaChoice choice ) {
    super( choice );
    for ( XmlSchemaParticle particle : choice.getItems() ) {
      _children.add( XmlSorter.createHandler( particle ) );
    }
  }

  @Override
  public List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    if ( _children.isEmpty() ) {
      // a choice with no choices is legal - but unsatisfiable
      throw new XmlSortException( "Empty xsd:choice" );
    }
    return _children.get( _idx ).sort( remainingChildren, preferredOnly, requiredSchemas, mustMatch );
  }

  @Override
  protected boolean selectNextChoiceDirect() {
    if ( ! _children.isEmpty() ) { // a choice with no choices is legal
      boolean foundChoice = _children.get( _idx ).selectNextChoice();
      if ( foundChoice ) {
        return true;
      }
      if ( _idx < _children.size() - 1 ) {
        _idx++;
        _children.get( _idx ).reset();
        return true;
      }
    }
    return false;
  }

  @Override
  public void _reset() {
    _idx = 0;
    if ( ! _children.isEmpty() ) {
      _children.get( _idx ).reset();
    }
  }

  @Override
  protected void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas ) {
    // TODO - maybe see if all choices throw, and throw
  }

  @Override
  public void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas ) {
    for ( XmlSortHandler child : _children ) {
      child.match( children, requiredSchemas );
    }

  }

}
