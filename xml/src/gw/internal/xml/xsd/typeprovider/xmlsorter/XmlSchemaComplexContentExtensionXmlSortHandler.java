/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.xml.XmlElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Sort handler representing a complex content extension. If there is a base type, delegates to it first, followed
 * by any particle. Utilizes a XmlSchemaSequenceXmlSortHandler to handle the permutations of choices between the two children.
 */
public class XmlSchemaComplexContentExtensionXmlSortHandler extends XmlSortHandler {

  private final XmlSortHandler _child;

  public XmlSchemaComplexContentExtensionXmlSortHandler( XmlSchemaComplexContentExtension extension ) {
    super( extension );
    final XmlSchemaComplexType baseType = extension.getSchemaIndex().getXmlSchemaComplexTypeByQName( extension.getBaseTypeName() );
    XmlSortHandler baseTypeHandler = XmlSorter.createHandler( baseType );
    final XmlSchemaParticle particle = extension.getParticle();
    if ( particle != null ) {
      XmlSortHandler particleHandler = XmlSorter.createHandler( particle );
      _child = new XmlSchemaSequenceXmlSortHandler( extension, baseTypeHandler, particleHandler );
    }
    else {
      _child = baseTypeHandler;
    }
  }

  @Override
  public List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
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
