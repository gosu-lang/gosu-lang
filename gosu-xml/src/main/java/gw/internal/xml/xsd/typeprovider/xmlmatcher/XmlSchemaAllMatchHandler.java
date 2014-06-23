/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.IXmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAll;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.lang.reflect.IType;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

public class XmlSchemaAllMatchHandler extends XmlMatchHandler {

  private XmlSchemaAll _all;
  private Map<QName, XmlMatchHandler> _matchHandlersByQName;
  private Map<IType, XmlMatchHandler> _matchHandlersByIType;

  public XmlSchemaAllMatchHandler( XmlSchemaAll all ) {
    _all = all;
  }

  @Override
  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( _matchHandlersByQName == null ) {
      _matchHandlersByQName = new HashMap<QName, XmlMatchHandler>();
      _matchHandlersByIType = new HashMap<IType, XmlMatchHandler>();
      for ( XmlSchemaElement element : _all.getItems() ) {
        XmlMatchHandler matchHandler = XmlMatchHandler.getMatchHandler( element );
        if ( element.getRefName() != null ) {
          element = XmlSchemaIndex.getActualElement( element );
          IType elementType = XmlSchemaIndex.getGosuTypeBySchemaObject( element );
          _matchHandlersByIType.put( elementType, matchHandler );
        }
        else {
          _matchHandlersByQName.put( element.getQName(), matchHandler );
        }
      }
    }
    // First try to find an anonymous matching element with the correct name
    XmlMatchHandler matchHandler = _matchHandlersByQName.get( elementName );
    if ( matchHandler == null ) {
      // Then look for element references, including substitution groups
      XmlSchemaElement element = collection.getElementByQName( elementName );
      IType elementType = XmlSchemaIndex.getGosuTypeBySchemaObject( element );
      while ( matchHandler == null && elementType instanceof IXmlSchemaElementTypeData ) {
        matchHandler = _matchHandlersByIType.get( elementType );
        elementType = elementType.getSupertype();
      }
    }
    if ( matchHandler != null ) {
      matchHandler.match( elementName, collection );
    }
  }

}
