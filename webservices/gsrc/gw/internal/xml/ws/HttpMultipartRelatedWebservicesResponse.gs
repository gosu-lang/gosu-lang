/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws

uses java.io.InputStream
uses java.net.HttpURLConnection
uses java.net.URI
uses gw.internal.xml.ws.server.XopUtil
uses gw.internal.xml.ws.WebservicesResponse
uses gw.internal.xml.ws.http.fragment.*
uses gw.internal.xml.ws.http.*
uses gw.util.StreamUtil
uses gw.xml.ws.HttpHeaders

class HttpMultipartRelatedWebservicesResponse extends WebservicesResponse {

  var _conn : HttpURLConnection
  var _multipartContentType : HttpMediaType
  var _http : HttpMultipartRelatedContent

  construct( conn : HttpURLConnection, contentType : HttpMediaType, url : URI ) {
    super( url )
    _conn = conn
    _multipartContentType = contentType
    var content = StreamUtil.getContent( conn.InputStream )
    //var type = contentType.Parameters.firstWhere( \ h -> h.Attribute.Text == "type" ).Value.Text
    _http = new HttpMultipartRelatedContent( new HttpParseContext( content ), contentType )
  }

  override property get InputStream() : InputStream {
    return XopUtil.getInputStream( _http )
  }

  override property get ResponseCode() : int {
    return _conn.ResponseCode
  }


  override function getResponseHeader( key : String ) : String {
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
    return _multipartContentType.Parameters.firstWhere( \ h -> h.Attribute.Text.toLowerCase() == "start-info" ).Value.Text
  }

}
