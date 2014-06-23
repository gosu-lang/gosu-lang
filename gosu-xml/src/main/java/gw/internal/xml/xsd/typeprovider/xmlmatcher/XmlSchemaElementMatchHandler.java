/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.NotFoundException;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;

import javax.xml.namespace.QName;

public class XmlSchemaElementMatchHandler extends XmlMatchHandler {

  private final XmlSchemaElement _element;
  private boolean _done;

  public XmlSchemaElementMatchHandler( XmlSchemaElement element ) {
    if ( element.getRefName() != null ) {
      element = element.getSchemaIndex().getXmlSchemaElementByQName( element.getRefName() );
    }
    _element = element;
  }

  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( ! _done ) {
      _done = true;
      QName origElementName = elementName;
      while ( elementName != null ) {
        if ( elementName.equals( _element.getQName() ) ) {
          XmlSchemaElement element;
          if ( _element.isTopLevel() ) {
            element = collection.getElementByQName( origElementName );
          }
          else {
            element = _element;
          }
          throw new MatchFoundException( XmlSchemaIndex.getGosuTypeBySchemaObject( element ) );
        }
        if ( ! _element.isTopLevel() ) {
          break; // can't be a substitution group if it's not a top-level element
        }
        // check for substitution group head
        XmlSchemaElement element = collection.getElementByQName( elementName );
        if ( element == null ) {
          break;
        }
        elementName = element.getSubstitutionGroup();
      }
      throw new NotFoundException( "Unexpected element: " + origElementName );
    }
  }

}
