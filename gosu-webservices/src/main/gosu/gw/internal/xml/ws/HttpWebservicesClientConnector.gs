/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws

uses java.net.URI
uses java.net.HttpURLConnection
uses java.io.OutputStream
uses gw.internal.xml.ws.http.HttpParseContext
uses gw.internal.xml.ws.http.fragment.HttpMediaType
uses gw.internal.xml.ws.HttpMultipartRelatedWebservicesResponse

class HttpWebservicesClientConnector extends WebservicesClientConnector {

  var _conn : HttpURLConnection
  var _response : WebservicesResponse

  construct( url : URI ) {
    super(url)
    _conn = url.toURL().openConnection() as HttpURLConnection
    _conn.setDoOutput( true )
  }
  
  override function setHttpHeader( key : String, value : String ) {
    _conn.setRequestProperty( key, value )
  }

  override property get OutputStream() : OutputStream {
    try {
      return _conn.OutputStream
    }
    catch ( ex : java.net.ConnectException ) {
      throw new java.net.ConnectException( "Unable to connect to URL ${URI}: ${ex.Message}" ) 
    }
  }
  
  override property get Response() : WebservicesResponse {
    if ( _response == null ) {
      var contentTypeString = _conn.ContentType
      var contentType = contentTypeString == null ? null : new HttpMediaType( new HttpParseContext( contentTypeString.getBytes( "US-ASCII" ) ) )
      if ( contentType != null and contentType.MediaType == "multipart/related" ) {
        _response = new HttpMultipartRelatedWebservicesResponse( _conn, contentType, URI )
      }
      else {
        _response = new HttpWebservicesResponse( _conn, URI )
      }
    }
    return _response
  }

}
