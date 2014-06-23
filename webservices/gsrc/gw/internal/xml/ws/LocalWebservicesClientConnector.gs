/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws

uses java.io.OutputStream
uses java.io.InputStream
uses java.io.ByteArrayOutputStream
uses java.io.ByteArrayInputStream
uses java.lang.Thread
uses java.net.URI
uses java.lang.Integer
uses gw.xml.ws.WebServiceException
uses java.lang.Throwable
uses gw.xml.ws.HttpHeaders
uses gw.xml.XmlException
uses javax.servlet.http.HttpServletRequest
uses gw.internal.xml.ws.http.fragment.*
uses gw.internal.xml.ws.http.*
uses gw.lang.reflect.TypeSystem

class LocalWebservicesClientConnector extends WebservicesClientConnector {

  var _httpHeaders : HttpHeaders
  var _responseHttpHeaders = new HttpHeaders()
  var _servlet = gw.internal.xml.ws.server.WebservicesServletBase.getDefaultLocalWebservicesServlet()
  var _baos = new ByteArrayOutputStream()
  var _error : Integer
  var _doOutput = false
  var _response : WebservicesResponse
  
  construct( url : URI ) {
    super( url )
  }
  
  override function setHttpHeader( key : String, value : String ) {
    if ( _httpHeaders == null ) {
      _httpHeaders = new HttpHeaders()
    }
    _httpHeaders.setHeader( key, value )
  }

  override property get OutputStream() : OutputStream {
    _doOutput = true
    return _baos
  }

  class LocalRequest extends gw.internal.xml.ws.server.WebservicesRequest {

    var _in : InputStream

    construct( bytes : byte[] ) {
      _in = new ByteArrayInputStream( bytes )
    }

    override property get HttpServletRequest() : HttpServletRequest {
      return null;
   }

    override property get InputStream() : InputStream {
      return _in
    }

    override property get PathInfo() : String {
      return URI.Path
    }

    override property get QueryString() : String {
      return URI.Query
    }

    override property get RequestURL() : String {
      return URI.toString()
    }

    override property get HttpHeaders() : HttpHeaders {
      if ( _httpHeaders == null ) {
        _httpHeaders = new HttpHeaders()
      }
      return _httpHeaders
    }

    override function createSession() {
      // nothing to do for local
    }

  }
  
  class LocalResponse extends gw.internal.xml.ws.server.WebservicesResponseAdapter {

    var _out : OutputStream
    var _contentType : String = null;

    construct( out : OutputStream ) {
      _out = out
    }

    override property get OutputStream() : OutputStream {
      return _out
    }

    override function sendError( error : int, ex : XmlException ) {
      throw ex
    }

    override function setContentType( ct : String ) {
      _contentType = ct
      _responseHttpHeaders.setHeader( "Content-Type", ct )
    }

    override function setStatus( error : int ) {
      _error = error
    }
    
    property get ContentType() : String {
      return _contentType
    }

    override property get HttpHeaders() : HttpHeaders {
      if ( _responseHttpHeaders == null ) {
        _responseHttpHeaders = new HttpHeaders()
      }
      return _responseHttpHeaders
    }

    override function commitHttpHeaders() {
      // nothing to do
    }
    
  }

  override property get Response() : WebservicesResponse {
    if ( _response == null ) {
      var baos = new ByteArrayOutputStream()
      var resp = new LocalResponse( baos )
      var throwable : Throwable
      var moduleContext = TypeSystem.getCurrentModule()
      var t = new Thread( "Wsi-Local-Handler" ) {
        override function run() {
          TypeSystem.pushModule(moduleContext)
          try {
            if ( ! _doOutput ) {
                _servlet.doGet( new LocalRequest( {} ), resp )
            }
            else {
              _servlet.doPost( new LocalRequest( _baos.toByteArray() ), resp )
            }
          }
          catch ( t : Throwable ) {
            throwable = t
          } finally {
            TypeSystem.popModule(moduleContext)
          }
        }
      }
      t.start()
      t.join()
      if ( throwable != null ) {
        throw new WebServiceException( "The server encountered a problem processing the request", throwable )
      }
      if ( _error != null && _error != 200 && _error != 500) {
        if ( _error == 404 ) {
          throw new java.io.FileNotFoundException( URI.toString() )
        }
        throw new WebServiceException( "Received HTTP ${_error}" )
      }
      var bais = new ByteArrayInputStream( baos.toByteArray() )
      _response = new WebservicesResponse( URI ) {
        override function getContentType() : String {
          return resp.ContentType
        }

        override property get ResponseCode() : int {
          return _error
        }

        override function getResponseHeader( key : String ) : String {
          return _responseHttpHeaders.getHeader( key )
        }

        override function getResponseHeaders() : HttpHeaders {
          return _responseHttpHeaders
        }

        override property get InputStream() : InputStream {
          return bais
        }

      }
    }
    return _response
  }

}