/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.xmlmatcher;

import gw.internal.xml.xsd.typeprovider.NotFoundException;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAny;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.xml.XmlException;

import javax.xml.namespace.QName;

public class XmlSchemaAnyMatchHandler extends XmlMatchHandler {

  private final XmlSchemaAny _any;
  private XmlSchemaElementMatchHandler _matchHandler;
  private boolean _done;

  public XmlSchemaAnyMatchHandler( XmlSchemaAny any ) {
    _any = any;
  }

  public void match( QName elementName, XmlSchemaCollection collection ) {
    if ( ! _done ) {
      _done = true;
      if ( _any != null && ! _any.accept( elementName ) ) {
        throw new NotFoundException( "This xsd:any does not allow " + elementName );
      }
      if ( _any != null && _any.getProcessContents() == XmlSchemaAny.ProcessContents.skip ) {
        throw new MatchFoundException( null );
      }
      XmlSchemaElement element;
      try {
        element = collection.getElementByQName( elementName );
      }
      catch ( NotFoundException ex ) {
        if ( _any == null || _any.getProcessContents() == XmlSchemaAny.ProcessContents.lax ) {
          throw new MatchFoundException( null );
        }
        throw new XmlException( "Unexpected element for xsd:any, processContents = strict: " + elementName, ex );
      }
      _matchHandler = new XmlSchemaElementMatchHandler( element );
      _matchHandler.match( elementName, collection );
      throw new MatchFoundException( XmlSchemaIndex.getGosuTypeBySchemaObject( element ) );
    }
    else if ( _matchHandler != null ) {
      _matchHandler.match( elementName, collection );
    }
  }

}
