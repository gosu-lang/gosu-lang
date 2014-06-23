/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaGroup;
import gw.xml.XmlElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Sort handler which handles a group. Contains a single child representing the group's particle.
 */
public class XmlSchemaGroupXmlSortHandler extends XmlSortHandler {

  private final XmlSortHandler _child;

  public XmlSchemaGroupXmlSortHandler( XmlSchemaGroup group ) {
    super( group );
    _child = XmlSorter.createHandler( group.getParticle() );
  }

  @Override
  protected List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    return _child.sort( remainingChildren, preferredOnly, requiredSchemas, mustMatch );
  }

  @Override
  protected boolean selectNextChoiceDirect() {
    return _child.selectNextChoice();
  }

  @Override
  public void _reset() {
    _child.reset();
  }

  @Override
  protected void checkMissingRequiredElements( LinkedList<XmlElement> remainingChildren, List<XmlSchemaIndex> requiredSchemas ) {
    _child.checkMissingRequiredElements( remainingChildren, requiredSchemas );
  }

  @Override
  public void match( Set<XmlElement> children, List<XmlSchemaIndex> requiredSchemas ) {
    _child.match( children, requiredSchemas );
  }

}
