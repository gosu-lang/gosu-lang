/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws

uses java.net.URI
uses java.io.OutputStream

abstract class WebservicesClientConnector {

  var _uri : URI as URI

  static function forURI( uri: URI ) : WebservicesClientConnector {
    if ( uri.Scheme == "local" ) {
      return new LocalWebservicesClientConnector( uri )
    }
    else if ( uri.Scheme == "file" ) {
      return new FileWebservicesClientConnector( uri )
    }
    else {
      return new HttpWebservicesClientConnector( uri )
    }
  }
  
  construct(url : URI) {
    URI = url;
  }
  
  abstract function setHttpHeader( key : String, value : String )
  
  abstract property get OutputStream() : OutputStream
  
  abstract property get Response() : WebservicesResponse

}
