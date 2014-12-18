/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.rt;

import gw.config.CommonServices;
import gw.fs.IFile;
import gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope;
import gw.internal.xml.IXmlLoggerFactory;
import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.AsyncResponseInternal;
import gw.internal.xml.ws.IWsdlConfig;
import gw.internal.xml.ws.WsdlSoapHeaders;
import gw.internal.xml.ws.typeprovider.Wsdl;
import gw.internal.xml.ws.typeprovider.WsdlOperationInputInfo;
import gw.internal.xml.ws.typeprovider.paraminfo.WsdlOperationParameterInfo;
import gw.internal.xml.xsd.typeprovider.IWsdlPortTypeData;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPort;
import gw.internal.xml.xsd.typeprovider.schemaparser.SoapVersion;
import gw.lang.PublishedType;
import gw.lang.PublishedTypes;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.JavaTypes;
import gw.util.ILogger;
import gw.util.Pair;
import gw.util.concurrent.LocklessLazyVar;
import gw.xml.XmlElement;
import gw.xml.XmlSchemaAccess;
import gw.xml.ws.WebServiceException;
import gw.xml.ws.WsdlFault;
import gw.xml.ws.IWsdlPort;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;


/**
 * This is the runtime implementation of a web service port, note that if there is a singleton
 * port then this will also be a wsdl service.  A web service port has the operations on it and
 * is what most people think of as a web service.  And that is true if there is a singleton port or
 * a singleton soap1.2 port.
 *
 * This class is not directly exposed, it is meant to be the backing object for the WsdlPortType objects.
 *
 * @author dandrews
 */
@PublishedTypes(
        @PublishedType(fromType = "gw.internal.xml.ws.IWsdlConfig", toType = "gw.xml.ws.WsdlConfig")
)
public class WsdlPortImpl implements IGosuObject, IWsdlPort {

  final private IType _ownersType;
  final private QName _portQName;

  private ILogger _log;
  final private IWsdlConfig _config;
  final private String _portTypePackageName;
  private final WsdlPort _wsdlPort;
  private final IType _type;
  private final IWsdlPortTypeData _typeData;
  final private QName _serviceQName;
  final protected Wsdl _wsdl;
  final private IFile _resourceFile;

  private static LocklessLazyVar<IType> ASYNC_RESPONSE_IMPL_TYPE = new LocklessLazyVar<IType>() {
    protected IType init() {
      return TypeSystem.getByFullName( "gw.internal.xml.ws.AsyncResponseImpl" );
    }
  };
  private static LocklessLazyVar<IConstructorHandler> ASYNC_RESPONSE_IMPL_GENERIC_CONSTRUCTOR = new LocklessLazyVar<IConstructorHandler>() {
    protected IConstructorHandler init() {
      return ASYNC_RESPONSE_IMPL_TYPE.get().getTypeInfo().getConstructors().get( 0 ).getConstructor();
    }
  };
  private static LocklessLazyVar<IType> ASYNC_RESPONSE_TYPE = new LocklessLazyVar<IType>() {
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.AsyncResponse" );
    }
  };

  /**
   * Constructs a port.
   *
   * @param ownersType  the owner's itype
   * @param resourceFile the file with the wsdl
   * @param serviceQName the service qname
   * @param wsdl the gw wsdl object for this port and service
   * @param portQName the port's qname
   * @param config the config
   * @param wsdlPort the default port
   */
  public WsdlPortImpl( IType ownersType, IFile resourceFile, QName serviceQName, Wsdl wsdl, QName portQName, IWsdlConfig config, WsdlPort wsdlPort ) {
    _type = ownersType;
    _typeData = (IWsdlPortTypeData) ownersType;
    _resourceFile = resourceFile;
    _serviceQName = serviceQName;
    _wsdl = wsdl;
    _wsdlPort = wsdlPort;
    _portTypePackageName = wsdlPort.getBinding().getPortType().getSchemaIndex().getPackageName();
    _log = XmlServices.getLogger( IXmlLoggerFactory.Category.Runtime);
    _ownersType = ownersType;
    _portQName = portQName;
    _config = config;
  }

  /**
   * The type of the owner for this port.
   *
   * @return the itype
   */
  public IType getOwnersType() {
    return _ownersType;
  }

  /**
   * The qname for this port.
   *
   * @return the qname
   */
  public QName getPortQName() {
    return _portQName;
  }


  /**
   * This will get the log for this port
   *
   * @return a WSFLogging object
   */
  public ILogger getLogger() {
    return _log;
  }

  /** This will set the logging feature for this port
   *
   * @param log a WSFLogging feature (or subclass like WSFJavaLogging or WSFl4jLogging)
   */
  public void setLogger(ILogger log) {
    if (log == null) {
      throw new IllegalArgumentException("Log required");
    }
    _log = log;
  }

  /** This will invoke an operation
   *
   *
   * @param opTypeData data about the operation, its name, its inputType, its outputType, its unwrapped outputtype
   * @param args the arguements for the operation  @return the returned object if any
   * @return the object returned from the call
   */
  public Object invoke(final WsdlOperationInfo opTypeData, Object... args) {
    final long timeBefore = System.currentTimeMillis(); //Calculate the round trip
    try {
      AsyncResponseInternal response = invokeAsync( opTypeData, args );
      Long callTimeout = _config.getCallTimeout();
      if ( callTimeout == null || callTimeout <= 0 ) {
        response.run();
        return response.get();
      }
      else {
        response.start();
        return response.get( callTimeout, TimeUnit.MILLISECONDS );
      }
    }
    catch ( WsdlFault ex ) {
      throw ex;
    }
    catch ( Exception ex ) {
      throw new WebServiceException( ex );
    }
    finally {
      if (CommonServices.getGosuProfilingService() != null) {
        CommonServices.getGosuProfilingService().completed(timeBefore, System.currentTimeMillis(),
                                                              _serviceQName == null ? "" : _serviceQName.toString(),
                                                              opTypeData == null ? "" : opTypeData.getName(), 1, 0);
      }
    }
  }

  /** This will invoke an operation asynchronously
   *
   *
   * @param opTypeData data about the operation, its name, its inputType, its outputType, its unwrapped outputtype
   * @param args the arguements for the operation
   * @return a response object that can be polled for the data
   */
  public AsyncResponseInternal invokeAsync( final WsdlOperationInfo opTypeData, Object... args ) {
    Pair<XmlElement,List<XmlElement>> pair = createRequestXmlElement(opTypeData, args);
    List<XmlElement> requestHeaders = pair.getSecond();
    List<XmlElement> allHeaders = new ArrayList<XmlElement>();
    allHeaders.addAll( _config.getRequestSoapHeaders() );
    allHeaders.addAll( requestHeaders );
    allHeaders.addAll( _config.getGuidewire().getChildren() );
    //noinspection unchecked
    XmlElement requestXML = pair.getFirst();
    XmlElement envelope;
    SoapVersion soapVersion = _wsdlPort.getBinding().getSoapBinding().getSoapVersion();
    if ( soapVersion == SoapVersion.SOAP_12 ) {
      gw.internal.schema.gw.xsd.w3c.soap12_envelope.Body body = new gw.internal.schema.gw.xsd.w3c.soap12_envelope.Body();
      gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope requestEnvelope = new gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope();
      gw.internal.schema.gw.xsd.w3c.soap12_envelope.Header header = null;
      for ( XmlElement element : allHeaders ) {
        if ( element == null ) {
          continue;
        }
        if ( header == null ) {
          header = new gw.internal.schema.gw.xsd.w3c.soap12_envelope.Header();
          requestEnvelope.setHeader$( header );
        }
        header.addChild( element );
      }
      requestEnvelope.setBody$( body );
      if ( requestXML != null ) {
        body.addChild( requestXML );
      }
      envelope = requestEnvelope;
    }
    else {
      gw.internal.schema.gw.xsd.w3c.soap11_envelope.Body body = new gw.internal.schema.gw.xsd.w3c.soap11_envelope.Body();
      gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope requestEnvelope = new gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope();
      gw.internal.schema.gw.xsd.w3c.soap11_envelope.Header header = null;
      for ( XmlElement element : allHeaders ) {
        if ( element == null ) {
          continue;
        }
        if ( header == null ) {
          header = new gw.internal.schema.gw.xsd.w3c.soap11_envelope.Header();
          requestEnvelope.setHeader$( header );
        }
        header.addChild( element );
      }
      requestEnvelope.setBody$( body );
      if ( requestXML != null ) {
        body.addChild( requestXML );
      }
      envelope = requestEnvelope;
    }

    // dlank: trying to parameterize the type before invoking the constructor causes a race condition at multiple customer sites, so we simply construct the generic type
    return (AsyncResponseInternal) ASYNC_RESPONSE_IMPL_GENERIC_CONSTRUCTOR.get().newInstance( opTypeData, this, envelope, getSchemaAccess(), getSoapVersion(), _portTypePackageName );
  }

  public static IType getParameterizedAsyncResponseType( WsdlOperationInfo opTypeData, SoapVersion soapVersion, boolean impl ) {
    IType parameterType;
    if ( opTypeData == null ) {
      parameterType = JavaTypes.getJreType( XmlElement.class );
    }
    else if ( opTypeData.getOutputInfo() == null ) {
      parameterType = JavaTypes.pVOID();
    }
    else {
      parameterType = opTypeData.getOutputInfo().getReturnType();
    }
    IType envelopeType = soapVersion == SoapVersion.SOAP_12 ? Envelope.TYPE.get() : gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope.TYPE.get();
    IType responseType = impl ? ASYNC_RESPONSE_IMPL_TYPE.get() : ASYNC_RESPONSE_TYPE.get();
    return responseType.getParameterizedType(parameterType, envelopeType);
  }

  /** This will take the arguements and wrap them into an xml fragment that can be
   * included in the request.
   *
   * @param inputInfo the input info
   * @param args the arguements
   * @return the resulting xml fragment
   */
  Pair<XmlElement,Integer> wrapRequest( WsdlOperationInputInfo inputInfo, Object... args ) {
    int idx = 0;
    XmlElement node;
    if ( ! inputInfo.isUnwrapped() ) {
      // non-wrapped
      node = (XmlElement) args[ idx++];
      if ( node == null ) {
        throw new IllegalArgumentException( "Request cannot be null" );
      }
    }
    else {
      Iterator<Pair<IType, QName>> iter = inputInfo.getUnwrapLevels().iterator();
      if ( iter.hasNext() ) {
        Pair<IType, QName> pair = iter.next();
        while ( true ) {
          node = (XmlElement) pair.getFirst().getTypeInfo().getConstructor().getConstructor().newInstance();
          if ( ! iter.hasNext() ) {
            break;
          }
          pair = iter.next();
        }
      }
      else {
        node = (XmlElement) inputInfo.getRequestElementType().getTypeInfo().getConstructor().getConstructor().newInstance();
      }
      for ( Pair<WsdlOperationParameterInfo, Boolean> paramInfo : inputInfo.getParameterInfos() ) {
        Object arg = args[idx++];
        if ( arg != null ) {
          if ( paramInfo.getSecond() ) {
            @SuppressWarnings( {"unchecked"} )
            List<Object> list = (List<Object>) arg;
            for ( Object item : list ) {
              wrapParameter( node, paramInfo, item );
            }
          }
          else {
            wrapParameter( node, paramInfo, arg );
          }
        }
      }
    }
    return new Pair<XmlElement,Integer>( node, idx );
  }

  private void wrapParameter( XmlElement node, Pair<WsdlOperationParameterInfo, Boolean> paramInfo, Object arg ) {
    XmlElement parameterElement = (XmlElement) paramInfo.getFirst().getParameterElementType().getTypeInfo().getConstructor().getConstructor().newInstance();
    parameterElement = paramInfo.getFirst().wrap( arg, parameterElement );
    node.addChild( parameterElement );
  }

  private Pair<XmlElement,List<XmlElement>> createRequestXmlElement( WsdlOperationInfo opTypeData, Object... args) {
    if ( opTypeData == null ) {
      return new Pair<XmlElement, List<XmlElement>>( (XmlElement) args[0], Collections.<XmlElement>emptyList() );
    }
    else {
      Pair<XmlElement,Integer> pair = wrapRequest( opTypeData.getInputInfo(), args );
      XmlElement root = pair.getFirst();
      int idx = pair.getSecond();
      List<XmlElement> headerElements = new ArrayList<XmlElement>();
      if ( idx < args.length ) {
        WsdlSoapHeaders headers = (WsdlSoapHeaders) args[ idx ];
        if ( headers != null ) {
          for ( XmlElement xmlElement : headers.getAllHeaders().values() ) {
            headerElements.add( xmlElement );
          }
        }
      }
      return new Pair<XmlElement,List<XmlElement>>( root, headerElements );
    }
  }

  public IWsdlConfig getConfig() {
    return _config;
  }

  public WsdlPort getWsdlPort() {
    return _wsdlPort;
  }

  @Override
  public IType getIntrinsicType() {
    return _type;
  }

  @Override
  public String toString() {
    return _type.getRelativeName() + " Port ( " + _type.getName() + " )";
  }

  /**
   * This will get the QName of the service
   *
   * @return the qname
   */
  public QName getServiceQName() {
    return _serviceQName;
  }

  /** This will get the name space for this service
   *
   * @return the name space
   */
  public String getNamespace() {
    return _wsdl.getPackageName();
  }

  public IFile getResourceFile() {
    return _resourceFile;
  }

  public Wsdl getWsdl() {
    return _wsdl;
  }

  public SoapVersion getSoapVersion() {
    return getWsdlPort().getBinding().getSoapBinding().getSoapVersion();
  }

  public XmlSchemaAccess getSchemaAccess() {
    return getWsdl().getXmlSchemaAccess();
  }

  public URI getAddress() {
    URI url = _config.getServerOverrideUrl();
    if ( url == null ) {
      url = _typeData.getAddress();
    }
    return url;
  }

}


