/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws

uses java.net.URI
uses java.io.OutputStream
uses java.io.InputStream
uses gw.xml.ws.HttpHeaders
uses java.net.URLConnection
uses java.io.ByteArrayOutputStream
uses gw.internal.xml.ws.http.fragment.*
uses gw.internal.xml.ws.http.*

class FileWebservicesClientConnector extends WebservicesClientConnector {

  var _conn : URLConnection

  construct( url : URI ) {
    super(url)
    _conn = url.toURL().openConnection()
  }
  
  override function setHttpHeader( key : String, value : String ) {
  }

  override property get OutputStream() : OutputStream {
    return new ByteArrayOutputStream()
  }

  override property get Response() : WebservicesResponse {
    return new WebservicesResponse( URI ) {
      
      override property get InputStream() : InputStream {
        return _conn.InputStream
      }
  

      override property get ResponseCode() : int {
        return 200
      }


      override function getResponseHeader(key : String) : String {
        return null
      }

      override function getResponseHeaders() : HttpHeaders {
        return new HttpHeaders()
      }

      override function getContentType() : String {
        return _conn.ContentType
      }
      
    }
  }

}
