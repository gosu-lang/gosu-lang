/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws

uses java.io.InputStream
uses gw.xml.ws.HttpHeaders
uses java.net.HttpURLConnection
uses gw.internal.xml.ws.http.fragment.*
uses gw.internal.xml.ws.http.*
uses java.net.URI

class HttpWebservicesResponse extends WebservicesResponse {

  var _conn : HttpURLConnection

  construct( conn : HttpURLConnection, url : URI ) {
    super( url )
    _conn = conn
  }

  override property get InputStream() : InputStream {
    if ( _conn.ResponseCode >= 400 ) {
      return _conn.ErrorStream
    }
    else {
      return _conn.InputStream
    }
  }
  
  override property get ResponseCode() : int {
    return _conn.ResponseCode
  }


  override function getResponseHeader(key : String) : String {
    return _conn.getHeaderField( key )  

  }

  override function getResponseHeaders() : HttpHeaders {
    var ret = new HttpHeaders()
    var i = 1
    while ( true ) {
      var key = _conn.getHeaderFieldKey( i )
      if ( key == null ) {
        break
      }
      var value = _conn.getHeaderField( i )
      ret.setHeader( key, value )
      i++
    }
    return ret
  }

  override function getContentType() : String {
    return _conn.ContentType
  }
  
}
