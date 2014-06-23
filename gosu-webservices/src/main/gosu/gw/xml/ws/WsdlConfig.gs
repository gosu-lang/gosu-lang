/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

uses java.net.URI
uses gw.xsd.guidewire.soapheaders_ref.Guidewire
uses java.lang.Long
uses gw.xml.XmlElement
uses java.util.ArrayList
uses java.lang.IllegalArgumentException
uses java.io.InputStream
uses gw.xml.XmlSerializationOptions
uses gw.xml.XmlParseOptions

final class WsdlConfig implements gw.internal.xml.ws.IWsdlConfig {
  
  final var _requestSoapHeaders = new ArrayList<XmlElement>()
  var _serverOverrideUrl : URI as ServerOverrideUrl
  var _callTimeout : Long
  var _requestTransform( is : InputStream ) : InputStream as RequestTransform
  var _responseTransform( is : InputStream ) : InputStream as ResponseTransform
  var _http : Http as Http = new Http() 
  var _guidewire : Guidewire as Guidewire = new Guidewire()
  var _xmlSerializationOptions : XmlSerializationOptions as XmlSerializationOptions = new XmlSerializationOptions()
  var _xmlParseOptions : XmlParseOptions as XmlParseOptions = new XmlParseOptions()

  override property get RequestSoapHeaders() : List<XmlElement> {
    return _requestSoapHeaders
  }

  override property get CallTimeout() : Long {
    return _callTimeout
  }

  override property set CallTimeout( timeout : Long ) {
    if ( timeout != null && timeout < 1 ) {
      throw new IllegalArgumentException( "Call timeout must either be null or greater than 0." )
    }
    _callTimeout = timeout
  }
  
  static class Http {
    
    @Autocreate
    var _authentication : Http_Authentication as Authentication
    var _headers : HttpHeaders as RequestHeaders = new HttpHeaders()

  }
  
  static class Http_Authentication {
    
    @Autocreate
    var _basic : Http_Authentication_Basic as Basic
    @Autocreate
    var _digest : Http_Authentication_Digest as Digest
    
  }
    
  static class Http_Authentication_Basic {
    
    var _username : String as Username
    var _password : String as Password
    
  }
    
  static class Http_Authentication_Digest {
    
    var _username : String as Username
    var _password : String as Password
    
  }
    
}
