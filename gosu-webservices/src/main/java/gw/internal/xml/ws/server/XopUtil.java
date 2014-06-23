/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import gw.internal.schema.gw.xsd.w3c.xop_include.Include;
import gw.internal.xml.ws.http.fragment.HttpFieldValue;
import gw.internal.xml.ws.http.fragment.HttpHeadersAndBody;
import gw.internal.xml.ws.http.fragment.HttpMultipartRelatedContent;
import gw.internal.xml.ws.http.fragment.HttpToken;
import gw.util.Pair;
import gw.xml.XmlElement;
import gw.xml.XmlSimpleValue;
import gw.xml.ws.WebServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class XopUtil {

  public static InputStream getInputStream( HttpMultipartRelatedContent content ) throws URISyntaxException {
    HttpHeadersAndBody rootPart = content.getRootPart();
    XmlElement xml = XmlElement.parse( rootPart.getBody() );
    substituteXopIncludes( xml, content );
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    xml.writeTo( baos );
    return new ByteArrayInputStream( baos.toByteArray() );
  }

  // recursively searches the specified XML for xop:include elements, and replaces them with the appropriate included content
  private static void substituteXopIncludes( XmlElement xml, HttpMultipartRelatedContent content ) throws URISyntaxException {
    List<XmlElement> children = new ArrayList<XmlElement>( xml.getChildren( Include.$QNAME ) ); // make safe copy of list since it gets modified during iteration
    for ( XmlElement xopInclude : children ) {
      URI contentIdUrl = new URI( xopInclude.getAttributeValue( "href" ) );
      if ( ! contentIdUrl.getScheme().equals( "cid" ) ) {
        throw new WebServiceException( "Unexpected URI scheme, expected 'cid', but was '" + contentIdUrl.getScheme() + "': " + contentIdUrl );
      }
      String contentId = contentIdUrl.getSchemeSpecificPart();
      HttpHeadersAndBody foundPart = null;
      for ( HttpHeadersAndBody part : content.getParts() ) {
        for ( Pair<HttpToken, HttpFieldValue> pair : part.getHttpHeaders() ) {
          if ( pair.getFirst().getText().toLowerCase().equals( "content-id" ) ) {
            if ( pair.getSecond().getText().equals( "<" + contentId + ">" ) ) {
              foundPart = part;
              break;
            }
          }
        }
      }
      if ( foundPart == null ) {
        throw new WebServiceException( "XOP Include not found for content id " + contentId );
      }
      xml.setSimpleValue( XmlSimpleValue.makeBase64BinaryInstance( foundPart.getBody() ) );
    }
    for ( XmlElement child : xml.getChildren() ) {
      substituteXopIncludes( child, content );
    }
  }


}
