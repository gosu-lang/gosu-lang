/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws

uses java.io.InputStream
uses java.io.FileNotFoundException
uses gw.xml.ws.WebServiceException
uses gw.util.StreamUtil
uses gw.xml.ws.HttpHeaders
uses java.net.URI
uses gw.internal.xml.ws.http.*
uses gw.internal.xml.ws.http.fragment.*
uses java.lang.System
uses java.lang.UnsupportedOperationException

abstract class WebservicesResponse {

  var _uri : URI as URI

  construct( url : URI ) {    
    _uri = url
  }

  abstract property get InputStream() : InputStream
  
  abstract property get ResponseCode() : int
  
  abstract function getResponseHeader( key : String ) : String
  
  abstract function getContentType() : String

  abstract function getResponseHeaders() : HttpHeaders

  protected function checkResponse( is : InputStream ) {
    if ( is == null ) {
      throw new WebServiceException( "The server at ${URI} returned HTTP Response Code ${ResponseCode}" )
    }
    var contentTypeString = getContentType()
    if ( contentTypeString == null ) {
      throw new WebServiceException( 'SOAP response envelope from ${URI} had no content type' )
    }
    var contentType = new HttpMediaType( new HttpParseContext( contentTypeString.getBytes( "US-ASCII" ) ) )
    switch ( contentType.MediaType ) {
      case "text/xml":
      case "application/soap+xml":
      case "application/xml":
        break // good
      default:
        //  TODO dlank - use character encoding for parsing XML instead of usual XML parsing rules? Check specification
        var content = StreamUtil.toString( StreamUtil.getContent( is ) ) // TODO - fix character encoding
        if ( getResponseCode() == 404 ) {
          throw new FileNotFoundException( URI + ": " + (content == null || content.size == 0 ?  "<no content>" : content) )
        }
        throw new WebServiceException( 'SOAP response envelope from ${URI} had wrong content type, "${contentType.MediaType}".\n\nContent:\n${content}\n' )
    }
  }

}
