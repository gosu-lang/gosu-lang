/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.xml.XmlElement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Sort handler representing a complex content restriction. Since restriction cannot add element children, this
 * handler simply contains one child representing the particle which by definition must reproduce all of the
 * children from the base type.
 */
public class XmlSchemaComplexContentRestrictionXmlSortHandler extends XmlSortHandler {
  
  private final XmlSortHandler _child;

  public XmlSchemaComplexContentRestrictionXmlSortHandler( XmlSchemaComplexContentRestriction restriction ) {
    super( restriction );
    final XmlSchemaParticle particle = restriction.getParticle();
    if ( particle != null ) {
      _child = XmlSorter.createHandler( particle );
    }
    else {
      _child = null;
    }
  }

  @Override
  public List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    return _child == null ? Collections.<XmlElement>emptyList() : _child.sort( remainingChildren, preferredOnly, requiredSchemas, mustMatch );
  }

  @Override
  protected boolean selectNextChoiceDirect() {
    //noinspection SimplifiableConditionalExpression
    return _child == null ? false : _child.selectNextChoice();
  }

  @Override
  public void _reset() {
    if ( _child != null ) {
      _child.reset();
    }
  }

  @Override
  protected void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas ) {
    if ( _child != null ) {
      _child.checkMissingRequiredElements( remainingChildren, requiredSchemas );
    }
  }

  @Override
  public void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas ) {
    if ( _child != null ) {
      _child.match( children, requiredSchemas );
    }
  }

}
