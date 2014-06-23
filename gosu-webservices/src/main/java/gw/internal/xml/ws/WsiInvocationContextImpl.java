/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws;

import gw.internal.xml.XmlTypeResolver;
import gw.internal.xml.ws.server.WebservicesRequest;
import gw.internal.xml.ws.server.WebservicesServletBase;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.internal.xml.xsd.typeprovider.schemaparser.SoapVersion;
import gw.lang.function.Function1;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;
import gw.xml.XmlParseOptions;
import gw.xml.XmlSerializationOptions;
import gw.xml.ws.DefaultWsiInvocationHandler;
import gw.xml.ws.HttpHeaders;
import gw.xml.ws.WebServiceException;
import gw.xml.ws.WsiAuthenticationException;
import gw.xml.ws.WsiInvocationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class WsiInvocationContextImpl extends WsiInvocationContext {

  private XmlElement _requestEnvelope;
  private HttpHeaders _requestHttpHeaders;
  private XmlElement _requestSoapHeaders;

  private HttpHeaders _responseHttpHeaders;
  private List<XmlElement> _responseSoapHeaders;
  private HttpServletRequest _request;
  private XmlSerializationOptions _xmlSerializationOptions;
  private WebService _webService;
  private WebservicesServletBase _webservicesServletBase;
  private IType _webserviceType;
  private SoapVersion _soapVersion;
  private WebservicesRequest _webservicesRequest;
  private List<Callable> _finallyList;
  private boolean _mtomEnabled;

  @Override
  public HttpServletRequest getHttpServletRequest() {
    return _request;
  }

  @Override
  public HttpHeaders getRequestHttpHeaders() {
    return _requestHttpHeaders;
  }

  @Override
  public XmlElement getRequestEnvelope() {
    return _requestEnvelope;
  }

  @Override
  public XmlElement getRequestSoapHeaders() {
    return _requestSoapHeaders;
  }

  @Override
  public HttpHeaders getResponseHttpHeaders() {
    if ( _responseHttpHeaders == null ) {
      _responseHttpHeaders = new HttpHeaders();
    }
    return _responseHttpHeaders;
  }

  @Override
  public List<XmlElement> getResponseSoapHeaders() {
    if ( _responseSoapHeaders == null ) {
      _responseSoapHeaders = new ArrayList<XmlElement>();
    }
    return _responseSoapHeaders;
  }

  @Override
  public XmlSerializationOptions getXmlSerializationOptions() {
    if ( _xmlSerializationOptions == null ) {
      _xmlSerializationOptions = _webService._serializationOptions;
      if ( _xmlSerializationOptions == null ) {
        _xmlSerializationOptions = new XmlSerializationOptions();
      }
      else {
        _xmlSerializationOptions = _xmlSerializationOptions.copy();
      }
    }
    return _xmlSerializationOptions;
  }

  @Override
  public void setXmlSerializationOptions( XmlSerializationOptions xmlSerializationOptions ) {
    _xmlSerializationOptions = xmlSerializationOptions;
  }

  @Override
  public void setMtomEnabled( boolean mtomEnabled ) {
    _mtomEnabled = mtomEnabled;
  }

  @Override
  public boolean isMtomEnabled() {
    return _mtomEnabled;
  }

  public List<XmlElement> getResponseSoapHeadersDirect() {
    return _responseSoapHeaders;
  }

  public HttpHeaders getResponseHttpHeadersDirect() {
    return _responseHttpHeaders;
  }

  public WebService getWebService() {
    return _webService;
  }

  public void setWebService( WebService webservice ) {
    _webService = webservice;
  }

  public void setWebservicesServletBase( WebservicesServletBase webservicesServletBase ) {
    _webservicesServletBase = webservicesServletBase;
  }

  public WebservicesServletBase getWebservicesServletBase() {
    return _webservicesServletBase;
  }

  public void setWebserviceType( IType type ) {
    _webserviceType = type;
  }

  public IType getWebserviceType() {
    return _webserviceType;
  }

  public SoapVersion getSoapVersion() {
    return _soapVersion;
  }

  public void setSoapVersion( SoapVersion soapVersion ) {
    _soapVersion = soapVersion;
  }

  public void setWebservicesRequest( WebservicesRequest request ) {
    _webservicesRequest = request;
  }

  public WebservicesRequest getWebservicesRequest() {
    return _webservicesRequest;
  }

  public void setFinallyList( List<Callable> finallyList ) {
    _finallyList = finallyList;
  }

  public List<Callable> getFinallyList() {
    return _finallyList;
  }

  public void setRequestHttpHeaders( HttpHeaders httpHeaders ) {
    _requestHttpHeaders = httpHeaders;
  }

  public void setHttpServletRequest( HttpServletRequest httpServletRequest ) {
    _request = httpServletRequest;
  }

  public void setRequestEnvelope( XmlElement envelope ) {
    _requestEnvelope = envelope;
  }

  public void setRequestSoapHeaders( XmlElement headers ) {
    _requestSoapHeaders = headers;
  }

  public static class WebService {
    public XmlParseOptions _parseOptions;
    public XmlSerializationOptions _serializationOptions;
    public XmlTypeResolver _typeResolver;
    public Class<?> _backingClass;
    public WsiServiceInfo _serviceInfo;
    public Object _worker;
    public Function1 _requestTransform;
    public Function1 _responseTransform;
    public Function1 _requestXmlTransform;
    public Function1 _responseXmlTransform;
    public DefaultWsiInvocationHandler _invocationHandler;
  }

  @Override
  public void preExecute(XmlElement requestElement, IMethodInfo method) throws WebServiceException, WsiAuthenticationException, IOException {
    getWebservicesServletBase().preUnmarshal( getWebserviceType(),
        getRequestSoapHeaders(),
        requestElement, method,
        getFinallyList(),
        getWebservicesRequest(),
        requestElement.getQName() );
  }
}
