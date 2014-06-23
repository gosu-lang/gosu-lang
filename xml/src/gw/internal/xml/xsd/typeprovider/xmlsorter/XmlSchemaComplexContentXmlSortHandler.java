/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaContent;
import gw.xml.XmlElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Sort handler representing a complex content. Since a complexContent simply contains an extension or restriction,
 * this handler contains a single child representing that extension or restriction.
 */
public class XmlSchemaComplexContentXmlSortHandler extends XmlSortHandler {

  private final XmlSortHandler _child;

  public XmlSchemaComplexContentXmlSortHandler( XmlSchemaComplexContent content ) {
    super( content );
    final XmlSchemaContent restrictionOrExtension = content.getContent();
    _child = XmlSorter.createHandler( restrictionOrExtension );
  }

  @Override
  public List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    return _child.sort( remainingChildren, preferredOnly, requiredSchemas, mustMatch );
  }

  @Override
  protected boolean selectNextChoiceDirect() {
    //noinspection SimplifiableConditionalExpression
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
