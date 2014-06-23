/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws

uses gw.internal.xml.ws.rt.WsdlPortImpl
uses gw.internal.xml.ws.rt.WsdlOperationInfo
uses java.lang.*
uses gw.internal.xml.xsd.typeprovider.schemaparser.SoapVersion
uses gw.util.StreamUtil
uses gw.util.Base64Util
uses gw.util.GosuExceptionUtil
uses gw.xml.XmlElement
uses gw.xml.XmlParseOptions
uses gw.xml.ws.AsyncResponse
uses gw.xml.ws.WsdlFault
uses gw.xml.ws.WebServiceException
uses java.util.concurrent.TimeUnit
uses java.util.concurrent.TimeoutException
uses gw.xsd.w3c.xml.attributes.Lang
uses gw.internal.xml.ws.AsyncResponseInternal
uses java.io.ByteArrayInputStream
uses java.io.InputStream
uses gw.xml.XmlSerializationOptions
uses gw.xml.XmlSchemaAccess
uses java.util.HashMap
uses javax.xml.namespace.QName
uses java.util.Map
uses java.util.Collections
uses gw.internal.xml.ws.rt.DigestAuthentication
uses gw.xml.ws.HttpHeaders
uses gw.internal.xml.ws.server.WebservicesServletBase

final class AsyncResponseImpl<T, E extends XmlElement> extends AsyncResponse<T, E> implements AsyncResponseInternal<T, E> {

  var _port : WsdlPortImpl
  var _opTypeData : WsdlOperationInfo
  var _thread : Thread
  var _requestEnvelope : E
  var _responseEnvelope : E
  var _throwable : Throwable
  var _done : boolean
  var _soapVersion : SoapVersion
  var _started : boolean
  var _requestTransform( is : InputStream ) : InputStream
  var _responseTransform( is : InputStream ) : InputStream
  var _responseHttpHeaders : HttpHeaders
  var _schemaAccess : XmlSchemaAccess
  var _portTypePackageName : String
  
  /**
   * This should only be created by WsdlPort when an async request
   * has been launched.
   *
   * @param opTypeData the operation information (used to unmarshal the response)
   * @param port the port that this operation was executed on
   * @param requestEnvelope
   */
  construct( opTypeData : WsdlOperationInfo, port : WsdlPortImpl, reqEnv : E, schemaAccess : XmlSchemaAccess, soapVersion : SoapVersion, portTypePackageName : String ) {
    var encoding = port.Config.XmlSerializationOptions.Encoding
    var charset = encoding == null ? "utf-8" : encoding.name().toLowerCase()
    _schemaAccess = schemaAccess
    _soapVersion = soapVersion
    _requestTransform = port.Config.RequestTransform
    _responseTransform = port.Config.ResponseTransform
    _portTypePackageName = portTypePackageName
    _opTypeData = opTypeData
    _port = port
    _requestEnvelope = reqEnv
    _thread = new Thread( "WSI-AsyncResponse" ) {
      override function run()
      {
        var is : InputStream
        var challenge : String
        try {
          var httpHeaders = port.Config.Http.RequestHeaders
          while ( true ) {
            var conn = gw.internal.xml.ws.WebservicesClientConnector.forURI( port.Address )
            if ( _soapVersion == SOAP_12 ) {
              var contentType = "application/soap+xml;charset=${charset}"
              if ( opTypeData != null && opTypeData.SoapAction != null ) {
                contentType += '; action="${opTypeData.SoapAction}"'
              }
              if ( httpHeaders.getHeader( "Content-Type" ) == null ) {
                conn.setHttpHeader( "Content-Type", contentType )
              }
            }
            else {
              if ( httpHeaders.getHeader( "Content-Type" ) == null ) {
                conn.setHttpHeader( "Content-Type", "text/xml;charset=${charset}" )
              }
              if ( httpHeaders.getHeader( "SOAPAction" ) == null ) {
                // quoted SOAPAction is required by BP 1.0, even if not specified in WSDL
                var soapAction = '"' + ( (opTypeData == null || opTypeData.SoapAction == null) ? "" : opTypeData.SoapAction ) + '"'
                conn.setHttpHeader( "SOAPAction", soapAction )
              }
            }
            if ( challenge != null ) {
              // we already checked that digest authentication was specified before setting the 'challenge' variable to non-null
              var username = port.Config.Http.Authentication.Digest.Username
              var password = port.Config.Http.Authentication.Digest.Password
              var header = DigestAuthentication.generateDigestAuthorizationHeader( challenge, username, password, port.Address.toString() )
              conn.setHttpHeader( "Authorization", header )
            }
            // the triple null check is needed due to Jira PL-14416
            else if ( port.Config != null && port.Config.Http != null && port.Config.Http.Authentication != null && port.Config.Http.Authentication.Basic != null ) {
              var username = port.Config.Http.Authentication.Basic.Username
              var password = port.Config.Http.Authentication.Basic.Password
              if ( username == null ) {
                throw new IllegalArgumentException( "HTTP Basic Authentication: Username is null" )
              }
              if ( password == null ) {
                throw new IllegalArgumentException( "HTTP Basic Authentication: Password is null" )
              }
              var base64UsernamePassword = Base64Util.encode( StreamUtil.toBytes( username + ':' + password ) )
              if ( httpHeaders.getHeader( "Authorization" ) == null ) {
                conn.setHttpHeader( "Authorization", "Basic " + base64UsernamePassword )
              }
            }
            for ( headerName in httpHeaders.HeaderNames ) {
              conn.setHttpHeader( headerName, httpHeaders.getHeader( headerName ) )
            }
            var out = conn.OutputStream
            if ( RequestTransform != null ) {
              var ris = RequestTransform( new ByteArrayInputStream( _requestEnvelope.bytes( port.Config.XmlSerializationOptions ) ) )
              if ( ris == null ) {
                ris = new ByteArrayInputStream( {} )
              }
              StreamUtil.copy( ris, out )
            }
            else {
              _requestEnvelope.writeTo( out, port.Config.XmlSerializationOptions )
            }
            out.close()
            // opTypeData == null means document_literal() request
            // opTypeData.OutputInfo == null means one-way request
            if ( opTypeData == null or opTypeData.OutputInfo != null ) {
              is = conn.Response.InputStream
              if ( conn.Response.ResponseCode == 401 && challenge == null ) {
                // check for digest authentication challenge
                if ( port.Config != null and port.Config.Http != null and port.Config.Http.Authentication != null and port.Config.Http.Authentication.Digest != null ) {
                  var tmp = conn.Response.getResponseHeader( "WWW-Authenticate" )
                  if ( tmp != null && tmp.startsWith( "Digest " ) ) {
                    challenge = tmp
                    if ( is != null ) {
                      is.close()
                    }
                    continue
                  }
                }
              }
              _responseHttpHeaders = conn.Response.getResponseHeaders()
              conn.Response.checkResponse( is )
              if ( ResponseTransform != null ) {
                is = ResponseTransform( is )
                if ( is == null ) {
                  is = new ByteArrayInputStream( {} )
                }
              }
              if (_responseEnvelope == null) {
                var xml = XmlElement.parse( is, getXmlParseOptions( port.Config.XmlParseOptions ) )
                if ( xml typeis E ) {
                  _responseEnvelope = xml
                }
                else {
                  var x = E.Type.Name
                  throw new WebServiceException( "Expected ${ x } but received ${ typeof xml }:\n${ xml.asUTFString( XmlSerializationOptions.debug() ) }" )
                }
              }
            }
            break
          }
        } 
        catch (t : Throwable) {
          _throwable = t
        } 
        finally {
          using ( _thread as IMonitorLock ) {
            _done = true
            _thread.notifyAll()
          }
          if ( is != null ) {
            is.close()
          }
        }
      }
    }
  }
  
  private function getXmlParseOptions( userRequestedParseOptions : XmlParseOptions ) : XmlParseOptions {
    var options = userRequestedParseOptions.copy()
    options.AdditionalSchemas.addAll( {
          _schemaAccess, 
          _soapVersion == SoapVersion.SOAP_12
                ? gw.xsd.w3c.soap12_envelope.util.SchemaAccess 
                : gw.xsd.w3c.soap11_envelope.util.SchemaAccess 
    } )
    return options
  }

  /**
   * Returns the unwrapped response object.  Note that this is
   * a blocking call.
   *
   * @return the object or null if no return object
   */
  override function get() : T
  {
    return get( getTimeoutDuration(), TimeUnit.MILLISECONDS )
  }

  /**
   * Returns the request envelope.
   *
   * @return the request envelope
   */
  override property get RequestEnvelope() : E
  {
    return _requestEnvelope
  }

  override property set RequestEnvelope( envelope : E )
  {
    if (envelope == null) {
      throw new IllegalArgumentException( "Envelope cannot be null" )
    }
    _requestEnvelope = envelope
  }

  override property set ResponseEnvelope( envelope : E )
  {
    if (envelope == null) {
      throw new IllegalArgumentException( "Envelope cannot be null" )
    }
    _responseEnvelope = envelope
  }

  /**
   * Returns the response envelope.
   *
   * @return the response envelope
   */
  override property get ResponseEnvelope() : E
  {
    return getResponseEnvelope( getTimeoutDuration(), TimeUnit.MILLISECONDS )
  }

  /**
   * Returns the response envelope.
   *
   * @return the response envelope
   */
  override property get ResponseHttpHeaders() : HttpHeaders 
  {
    try {
      getResponseEnvelope( getTimeoutDuration(), TimeUnit.MILLISECONDS )
    }
    catch ( ex : Exception ) {
      // avoid compiler warning
      ex.toString()  
    }
    
    return _responseHttpHeaders
  }

  /**
   * Returns the response envelope. This call will block until the request completes or
   * the timeout occurs.
   *
   * @param timeout
   * @param unit
   * @return the response envelope  @param timeout
   */
  override function getResponseEnvelope( timeout : long, unit : TimeUnit ) : E
  {
    if (_responseEnvelope != null) {
      return _responseEnvelope
    }
    start()
    var endTime = System.currentTimeMillis() + unit.toMillis( timeout )
    using ( _thread as IMonitorLock ) {
      if (timeout == 0) {
        if (!_done) {
          throw new TimeoutException()
        }
      } else if (timeout > 0) {
        while (!_done) {
          var remaining = endTime - System.currentTimeMillis()
          if (remaining <= 0) {
            throw new TimeoutException()
          }
          _thread.wait( remaining )
        }
      } else {
        while (!_done) {
          _thread.wait()
        }
      }
      if (_throwable != null) {
        throw GosuExceptionUtil.forceThrow( _throwable )
      }
      return _responseEnvelope
    }
  }

  /**
   * Waits if necessary for at most the given time for the computation
   * to complete, and then retrieves its result, if available.
   *
   * @param timeout the maximum time to wait
   * @param unit the time unit of the timeout argument
   * @return the computed result
   * @throws InterruptedException if the current thread was interrupted
   * while waiting
   * @throws TimeoutException if the wait timed out
   */
  override function get( timeout : long, unit : TimeUnit ) : T
  {
    var respEnv = getResponseEnvelope( timeout, unit )
    var bodyChild : XmlElement
    if ( _soapVersion == SoapVersion.SOAP_12 ) {
      var envelope = respEnv as gw.xsd.w3c.soap12_envelope.Envelope
      bodyChild = envelope.Body.$Children[0]
      if ( bodyChild typeis gw.xsd.w3c.soap12_envelope.Fault ) {
        throwSoap12Fault( bodyChild )
      }
    } else {
      var envelope = respEnv as gw.xsd.w3c.soap11_envelope.Envelope
      bodyChild = envelope.Body.$Children[0]
      if ( bodyChild typeis gw.xsd.w3c.soap11_envelope.Fault ) {
        throwSoap11Fault( bodyChild )
      }
    }
    // _opTypeData == null means a document_literal() request
    if ( _opTypeData == null ) {
      return bodyChild as T
    }
    else {
      return unwrapResponse( bodyChild, _opTypeData.OutputInfo ) as T
    }
  }

  override function start()
  {
    if ( ! _started ) {
      _started = true
      if ( _responseEnvelope == null ) {
        _thread.start()
      }
    }
  }
  
  override function run()
  {
    if ( _started ) {
      throw new IllegalStateException( "Thread is already started" )
    }
    _started = true
    if ( _responseEnvelope == null ) {
      _thread.run()
    }
  }

  override property get RequestTransform() : block( is : InputStream ) : InputStream {
    return _requestTransform
  }
  
  override property set RequestTransform( bl( is : InputStream ) : InputStream ) {
    _requestTransform = bl
  }
  
  override property get ResponseTransform() : block( is : InputStream ) : InputStream {
    return _responseTransform
  }
  
  override property set ResponseTransform( bl( is : InputStream ) : InputStream ) {
    _responseTransform = bl
  }
  
  // SOAP 1.2-specific fault handling
  private function throwSoap12Fault( faultElement : gw.xsd.w3c.soap12_envelope.Fault ) {
    var wsdl = _port.Wsdl
    var detail = faultElement.Detail
    var exception : XmlElement = null
    var qname : QName = null
    if ( detail != null && detail.$Children.Count > 0 ) {
      exception = detail.$Children[0]
      qname = exception.QName
    }
    var wsdlFaultType = wsdl.getWsdlFaultTypeByElementQName(qname)
    var faultCode : WsdlFault.FaultCode
    switch(faultElement.Code.Value) {
      case DataEncodingUnknown:
        faultCode = WsdlFault.FaultCode.DataEncodingUnknown
        break
      case MustUnderstand:
        faultCode = WsdlFault.FaultCode.MustUnderstand
        break
      case Receiver:
        faultCode = WsdlFault.FaultCode.Receiver
        break
      case Sender:
        faultCode = WsdlFault.FaultCode.Sender
        break
      case VersionMismatch:
        faultCode = WsdlFault.FaultCode.VersionMismatch
        break
      default:
        throw new RuntimeException( "Unrecognized fault code: " + faultElement.Code.Value )
    }
    var faultCodeQName = faultElement.Code.Value.GosuValue
    var reason : String = null
    for ( textElement in faultElement.Reason.Text_elem ) {
      if (reason == null || "".equals( textElement.getAttributeValue( Lang.$QNAME ) ) ) {
        reason = textElement.$Value
      }
    }
    var actorURI = faultElement.Role
    var wsdlFault : WsdlFault
    if (wsdlFaultType != null) {
      wsdlFault = wsdlFaultType.TypeInfo.getConstructor( { String } ).Constructor.newInstance( { reason } ) as WsdlFault
    } else {
      wsdlFault = new WsdlFault( reason )
    }
    if ( exception != null ) {
      wsdlFault.Detail = exception
    }
    wsdlFault.Code = faultCode
    wsdlFault.CodeQName = faultCodeQName
    wsdlFault.ActorRole = actorURI
    throw wsdlFault
  }
  
  // SOAP 1.1-specific fault handling
  private function throwSoap11Fault( faultElement : gw.xsd.w3c.soap11_envelope.Fault ) {
    var faultCodeQName = faultElement.Faultcode
    var wsdl = _port.Wsdl
    var detail = faultElement.Detail
    var exception = detail.$Children.Count > 0 ? detail.$Children[0] : null
    var qname = exception.QName
    var wsdlFaultType = wsdl.getWsdlFaultTypeByElementQName(qname)
    var reason = faultElement.Faultstring
    var faultCode = WebservicesServletBase.SOAP11_FAULT_TO_GENERIC_FAULT.get( faultCodeQName )
    if (faultCode == null) {
      faultCode = WsdlFault.FaultCode.Other
    }
    var actorURI = faultElement.Faultactor
    var wsdlFault : WsdlFault
    if (wsdlFaultType != null) {
      wsdlFault = wsdlFaultType.TypeInfo.getConstructor( { String } ).Constructor.newInstance( { reason } ) as WsdlFault
    } else {
      wsdlFault = new WsdlFault( reason )
    }
    if (exception != null) {
      wsdlFault.Detail = exception
    }
    wsdlFault.Code = faultCode
    wsdlFault.CodeQName = faultCodeQName
    wsdlFault.ActorRole = actorURI
    throw wsdlFault
  }

  private function unwrapResponse( node : XmlElement, outputInfo : gw.internal.xml.ws.typeprovider.WsdlOperationOutputInfo ) : Object {
    if (node == null) {
      return null
    }
    var iter = outputInfo.getUnwrapLevels().iterator()
    if ( iter.hasNext() ) {
      var pair = iter.next()
      while ( true ) {
        if ( typeof node != pair.First ) {
          throw new RuntimeException( "Types do not match, expected " + pair.First.Name + " but found " + node )
        }
        if ( not iter.hasNext() ) {
          break
        }
        pair = iter.next()
        node = node.getChild( pair.getSecond() )
      }
    }
    var paramInfo = outputInfo.getReturnParameterInfo()
    if ( paramInfo == null ) {
      return null // void return type
    }
    else if ( outputInfo.getUseParentElement() ) {
      return paramInfo.unwrap( node )
    }
    else {
      var parameterElement = node.getChild( paramInfo.getParameterElementName() )
      if ( parameterElement == null ) {
        return null
      }
      else {
        return paramInfo.unwrap( parameterElement )
      }
    }
  }
  
  function getTimeoutDuration() : long {
    return _port.Config.CallTimeout == null ? -1 : _port.Config.CallTimeout
  }
  
}
