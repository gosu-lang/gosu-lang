/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlsorter;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.xml.XmlElement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Sort handler representing a complex type. The complex type either has a particle ( i.e. xsd:sequence ) or a
 * content model ( xsd:simpleContent or xsd:complexContent ). In the case of simpleContent, this is a noop, since
 * simple contents cannot contain any children. Otherwise, contains a single child representing the particle or
 * content model.
 */
public class XmlSchemaComplexTypeXmlSortHandler extends XmlSortHandler {

  private final XmlSortHandler _child;

  public XmlSchemaComplexTypeXmlSortHandler( XmlSchemaComplexType complexType ) {
    super( complexType );
    if ( complexType.getContentModel() instanceof XmlSchemaComplexContent ) {
      _child = XmlSorter.createHandler( complexType.getContentModel() );
    }
    else {
      _child = null;
    }
  }

  @Override
  public List<XmlElement> sortDirect( LinkedList<XmlElement> remainingChildren, boolean preferredOnly, List<XmlSchemaIndex> requiredSchemas, Set<XmlElement> mustMatch ) {
    List<XmlElement> ret;
    if ( _child != null ) {
      ret = _child.sort( remainingChildren, preferredOnly, requiredSchemas, mustMatch );
    }
    else {
      ret = Collections.emptyList();
    }
    return ret;
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
