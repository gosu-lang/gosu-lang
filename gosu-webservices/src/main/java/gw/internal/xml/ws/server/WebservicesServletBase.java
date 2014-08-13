/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import gw.config.CommonServices;
import gw.fs.IFile;
import gw.internal.schema.gw.xsd.w3c.soap12_envelope.anonymous.elements.Fault_Code;
import gw.internal.schema.gw.xsd.w3c.soap12_envelope.anonymous.elements.Fault_Reason;
import gw.internal.schema.gw.xsd.w3c.soap12_envelope.enums.FaultcodeEnum;
import gw.internal.xml.XmlElementInternals;
import gw.internal.xml.XmlSchemaAccessImpl;
import gw.internal.xml.XmlTypeResolver;
import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.GosuWebservicesServlet;
import gw.internal.xml.ws.WsiInvocationContextImpl;
import gw.internal.xml.ws.http.HttpParseContext;
import gw.internal.xml.ws.http.fragment.HttpMediaType;
import gw.internal.xml.ws.http.fragment.HttpMultipartRelatedContent;
import gw.internal.xml.ws.server.marshal.ArrayMarshalInfo;
import gw.internal.xml.ws.server.marshal.ListMarshalInfo;
import gw.internal.xml.ws.server.marshal.MarshalInfo;
import gw.internal.xml.ws.server.marshal.XmlElementMarshalInfo;
import gw.internal.xml.ws.server.marshal.XmlTypeInstanceMarshalInfo;
import gw.internal.xml.xsd.typeprovider.NotFoundException;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schemaparser.SoapVersion;
import gw.lang.function.Function1;
import gw.lang.parser.ISource;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoaderListener;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassRepository;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;
import gw.util.GosuClassUtil;
import gw.util.GosuExceptionUtil;
import gw.util.ILogger;
import gw.util.Pair;
import gw.util.StreamUtil;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.BinaryData;
import gw.xml.XmlElement;
import gw.xml.XmlNamespace;
import gw.xml.XmlParseOptions;
import gw.xml.XmlSchemaAccess;
import gw.xml.XmlSerializationOptions;
import gw.xml.ws.DefaultWsiInvocationHandler;
import gw.xml.ws.WebServiceException;
import gw.xml.ws.WsdlFault;
import gw.xml.ws.WsiRequestLocal;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;

/**
 * This class is the base class for exposing web services via an app server
 */
public abstract class WebservicesServletBase extends HttpServlet implements ITypeLoaderListener {

  public static final String CONFIG_PARAM_AVAILABLE_SERVICES = "AvailableServices";
  public static final String CONFIG_PARAM_HIDE_SERVICE_LIST_PAGE = "HideListPage";

  private Set<String> _availableServices = new HashSet<String>();
  private boolean _hideServiceListPage;
  private boolean _initializedFromConfig;

  private static final String XSD_SUFFIX = ".xsd";
  private static final String WSDL_SUFFIX = ".wsdl";
  private static final String GX_SUFFIX = ".gx";
  private static final LockingLazyVar<IType> _wsiWebServiceAnnotationType = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiWebService" );
    }
  };
  private static final LockingLazyVar<IType> _doNotVerifyResourceAnnotationType = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullNameIfValid("gw.testharness.DoNotVerifyResource");
    }
  };
  private static ILogger _logger;
  private static ILogger _requestLogger;
  private Map<String, ILogger> _loggers = new HashMap<String, ILogger>();
  private static final Map<String, Pair<String, String>> RESOURCES = new HashMap<String, Pair<String, String>>();
  private static final Map<IGosuClass, WsiInvocationContextImpl.WebService> _webservices = new HashMap<IGosuClass, WsiInvocationContextImpl.WebService>();
  private static LockingLazyVar<WebservicesServletBase> _defaultLocalWebservicesServlet = new LockingLazyVar<WebservicesServletBase>() {
    @Override
    protected WebservicesServletBase init() {
      try {
        return _defaultLocalWebservicesServletClass.getConstructor( boolean.class ).newInstance( true );
      } catch ( Exception ex ) {
        throw GosuExceptionUtil.forceThrow( ex );
      }
    }
  };
  private static Class<? extends WebservicesServletBase> _defaultLocalWebservicesServletClass = GosuWebservicesServlet.class;
  private final boolean _wsiLocal;
  private boolean _initialized;
  private IType _requestTransformType;
  private IType _responseTransformType;
  private IType _requestXmlTransformType;
  private IType _responseXmlTransformType;
  private IType _wsiInvocationHandlerType;
  private IType _wsiParseOptionsAnnotationType;
  private IType _wsiSerializationOptionsAnnotationType;
  private static final ThreadLocal<WeakHashMap<WsiRequestLocal, ?>> _requestLocals = new ThreadLocal<WeakHashMap<WsiRequestLocal, ?>>();
  private static Set<WebservicesServletBaseListenerForTesting> _listenersForTesting = new HashSet<WebservicesServletBaseListenerForTesting>();

  public static final Map<QName, WsdlFault.FaultCode> SOAP11_FAULT_TO_GENERIC_FAULT = new HashMap<QName, WsdlFault.FaultCode>();
  public static final Map<WsdlFault.FaultCode, QName> GENERIC_FAULT_TO_SOAP11_FAULT = new HashMap<WsdlFault.FaultCode, QName>();

  private static void mapSoap11Fault( String soap11FaultName, WsdlFault.FaultCode genericFaultCode ) {
    String nsUri = gw.internal.schema.gw.xsd.w3c.soap11_envelope.Fault.$QNAME.getNamespaceURI();
    QName soap11FaultQName = new QName( nsUri, soap11FaultName );
    SOAP11_FAULT_TO_GENERIC_FAULT.put( soap11FaultQName, genericFaultCode );
    GENERIC_FAULT_TO_SOAP11_FAULT.put( genericFaultCode, soap11FaultQName );
  }

  static {
    mapSoap11Fault( "VersionMismatch", WsdlFault.FaultCode.VersionMismatch );
    mapSoap11Fault( "MustUnderstand", WsdlFault.FaultCode.MustUnderstand );
    mapSoap11Fault( "Client", WsdlFault.FaultCode.Sender );
    mapSoap11Fault( "Server", WsdlFault.FaultCode.Receiver );
    // specifically list out resources to avoid possible security holes
    addResource( "/resources.dftree/dftree.css", "dftree/dftree.css", "text/css" );
    addResource( "/resources.dftree/dftree.js", "dftree/dftree.js", "application/x-javascript" );
    addResource( "/resources.dftree/img/foldertree_folder.gif", "dftree/img/foldertree_folder.gif", "image/gif" );
    addResource( "/resources.dftree/img/foldertree_folderopen.gif", "dftree/img/foldertree_folderopen.gif", "image/gif" );
    addResource( "/resources.dftree/img/foldertree_minusbottom.gif", "dftree/img/foldertree_minusbottom.gif", "image/gif" );
    addResource( "/resources.dftree/img/foldertree_plusbottom.gif", "dftree/img/foldertree_plusbottom.gif", "image/gif" );
    addResource( "/resources.dftree/img/foldertree_plus.gif", "dftree/img/foldertree_plus.gif", "image/gif" );
    addResource( "/resources.dftree/img/foldertree_minus.gif", "dftree/img/foldertree_minus.gif", "image/gif" );
    addResource( "/resources.dftree/img/foldertree_joinbottom.gif", "dftree/img/foldertree_joinbottom.gif", "image/gif" );
    addResource( "/resources.dftree/img/foldertree_join.gif", "dftree/img/foldertree_join.gif", "image/gif" );
    addResource( "/resources.dftree/img/foldertree_page.gif", "dftree/img/foldertree_page.gif", "image/gif" );
    addResource( "/resources.dftree/img/foldertree_line.gif", "dftree/img/foldertree_line.gif", "image/gif" );
  }

  public WebservicesServletBase() {
    this( false );
  }

  public WebservicesServletBase( boolean wsiLocal ) {
    _wsiLocal = wsiLocal;
  }

  private static void addResource( String publicName, String resourceName, String contentType ) {
    RESOURCES.put( publicName, new Pair<String, String>( resourceName, contentType ) );
  }

  /**
   * This will return a logger for use by the customer's logger.
   *
   * @return the ILogger
   */
  public static ILogger getILogger() {
    if ( _logger == null ) {
      _logger = XmlServices.getLogger( XmlServices.Category.Runtime );
    }
    return _logger;
  }

  /**
   * This will return a logger for use by the customer's logger.
   *
   * @return the ILogger
   */
  public static ILogger getRequestILogger() {
    if ( _requestLogger == null ) {
      _requestLogger = XmlServices.getLogger( XmlServices.Category.Request );
    }
    return _requestLogger;
  }

  public static Set<String> getAllWebserviceTypesStatic() {
    Set<String> allTypeNames = new HashSet<String>();
    IGosuClassRepository repository = GosuClassTypeLoader.getDefaultClassLoader().getRepository();
    for ( String typeName : repository.getAllTypeNames( ".gs" ) ) {
      // dvl: added temporary variables to help debug NPE (PL-18521):
      final ISourceFileHandle clazz = repository.findClass( typeName, new String[]{ GosuClassTypeLoader.GOSU_CLASS_FILE_EXT } );
      if ( clazz == null ) {
        continue; // avoid NPE - probably not the right thing to do, but this issue is nearly impossible to reproduce to figure out what else should be done
      }
      final ISource parserSource = clazz.getSource();
      final String sourceString = parserSource.getSource();
      final String lowercaseSourceString = sourceString.toLowerCase();
      int idx = lowercaseSourceString.indexOf( "wsiwebservice" ); // optimization
      if ( idx < 0 ) {
        continue;
      }
      if ( testTypeName( typeName ) ) {
        allTypeNames.add( typeName );
      }
    }
    return allTypeNames;
//    return TypeSystem.getAllTypeNames(); // http://jira/jira/browse/PL-11741
  }

  protected void initAvailable() {
  }

  protected boolean initAvailable( String path ) {
    final URL resource = getClass().getClassLoader().getResource( path );
    if ( resource != null ) {
      try {
        //      try {
//        return CommonServices.getFileSystem().getIFile(new File(url.toURI()));
//      } catch (URISyntaxException e) {
//        throw new RuntimeException(e);
//      }
        InputStream stream = CommonServices.getFileSystem().getIFile(resource).openInputStream();
        readAvailable( stream );
        return true;
      } catch ( Throwable e ) {
        throw new RuntimeException( "initAvailable from resource " + path, e );
      }
    }
    File file = new File( path );
    if ( file.exists() ) {
      try {
        readAvailable( new FileInputStream( file ) );
      } catch ( Throwable e ) {
        throw new RuntimeException( "initAvailable from file system " + path, e );
      }
    }
    return false;
  }

  protected String getStringValue( ServletConfig config, final String name, final String defaultValue ) {
    if ( config == null ) {
      return defaultValue;
    }
    final String value = config.getInitParameter( name );
    return value == null || value.length() == 0 ? defaultValue : value;
  }

  protected boolean getBooleanValue( ServletConfig config, final String name, final boolean defaultValue ) {
    if ( config == null ) {
      return defaultValue;
    }
    final String value = config.getInitParameter( name );
    return value == null || value.length() == 0 ? defaultValue : Boolean.parseBoolean( value );
  }

  protected void onRefreshTypeSystem() {
    TypeSystem.lock();
    try {
      if ( !_initializedFromConfig ) {
        _availableServices.clear();
        for ( String typeName : getAllWebserviceTypesStatic() ) {
          _availableServices.add( typeName );
        }
      }
    } finally {
      TypeSystem.unlock();
    }
  }

  protected void addWebService( String typeName ) {
    TypeSystem.lock();
    try {
      if ( testTypeName( typeName ) ) {
        _availableServices.add( typeName );
      }
    } finally {
      TypeSystem.unlock();
    }
  }

  protected void removeWebService( String typeName ) {
    TypeSystem.lock();
    try {
      _availableServices.remove( typeName );
    } finally {
      TypeSystem.unlock();
    }
  }

  private static boolean testTypeName( String typeName ) {
    try {
      IGosuClass clazz = (IGosuClass) TypeSystem.getByFullNameIfValid( typeName );
      if ( clazz == null ) {
        if ( getILogger().isDebugEnabled() ) {
          getILogger().debug( "Could not find typeName " + typeName );
        }
        return false;
      }
      if ( ! clazz.getTypeInfo().hasAnnotation( _wsiWebServiceAnnotationType.get() ) ) {
        return false;
      }
      IType annotationType = _doNotVerifyResourceAnnotationType.get();
      if ( annotationType != null && clazz.getTypeInfo().hasAnnotation( annotationType ) ) {
        return false;
      }
    } catch ( Throwable ex ) {
      if ( getILogger().isDebugEnabled() ) {
        getILogger().debug( "On typeName " + typeName, ex );
      }
      return false;
    }
    return true;
  }

  protected void readAvailable( InputStream is ) throws IOException {
    try {
      final BufferedReader reader = new BufferedReader( StreamUtil.getInputStreamReader( is ) );
      while ( true ) {
        String line = reader.readLine();
        if ( line == null ) {
          break;
        }
        addWebService( line.trim() );
      }
    } finally {
      is.close();
    }
  }

  /**
   * This will be called before the invoke method is called, it is used to ensure the server is in the correct
   * state.
   */
  protected void beforeInvoke( WebservicesRequest request, WebservicesResponseAdapter responseAdapter ) {
    _requestLocals.set(new WeakHashMap<WsiRequestLocal, Object>());
  }

  /**
   * This will be called after the invoke method is called, it is used to ensure the server gets back into the
   * correct state.  It is called in a finally block.
   */
  protected void afterInvoke( WebservicesRequest request, WebservicesResponseAdapter responseAdapter ) {
    _requestLocals.remove();
  }

  /**
   * This will be called after the requestElement is determined and before it is executed.
   *
   * @param request        the httpRequest for this request.
   * @param requestElement the requestElement (QName is effectively the operation name)
   */
  protected void logRequest( WebservicesRequest request, XmlElement requestElement ) {
    if ( getRequestILogger().isDebugEnabled() ) {
      String userName = null;
      String userAddr = null;
      if ( request != null ) {
        HttpServletRequest httpRequest = request.getHttpServletRequest();
        if ( httpRequest != null ) {
          userName = httpRequest.getRemoteUser();
          userAddr = httpRequest.getRemoteAddr();
        }
      }
      getRequestILogger().debug( "Do " + ( userName == null ? "<unknown>" : userName ) + "@" + ( userAddr == null ? "<unknown>" : userAddr ) + ": " + requestElement.getQName() );
    }
  }

  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException {
    doPost( new ServletWebservicesRequest( request ), new ServletWebservicesResponseAdapter( response, getILogger() ) );
  }

  public void doPost( WebservicesRequest request, WebservicesResponseAdapter responseAdapter ) throws ServletException {
    Map<String, String> requestPairs = parseRequestPairs( request.getQueryString() );
    boolean createSession = false;
    for (String key : requestPairs.keySet()) {
      if ("stateful".equalsIgnoreCase( key )) {
        createSession = true;
        break;
      }
    }
    if ( createSession ) {
      request.createSession();
    }
    beforeInvoke( request, responseAdapter );
    final WsiInvocationContextImpl context = new WsiInvocationContextImpl();
    try {
      maybeInit();
      Pair<IType, SoapVersion> pair = getWebServiceInfo(request);
      if ( pair == null ) {
        getILogger().info( "Webservices servlet: 404 Not Found (web service type): " + request.getPathInfo() );
        send404NotFound( responseAdapter );
        return;
      }
      IType type = pair.getFirst();
      SoapVersion soapVersion = pair.getSecond();

      List<Callable> finallyList = new ArrayList<Callable>();

      WsiInvocationContextImpl.WebService webservice = getWebServiceForType( type );
      context.setWebserviceType( type );
      context.setWebService( webservice );
      context.setWebservicesServletBase( this );
      context.setSoapVersion( soapVersion );
      context.setWebservicesRequest( request );
      context.setFinallyList( finallyList );

      context.setRequestHttpHeaders( request.getHttpHeaders() );
      context.setHttpServletRequest( request.getHttpServletRequest() );

      OutputStream os;
      if ( webservice._responseTransform != null ) {
        os = new ByteArrayOutputStream();
      } else {
        os = responseAdapter.getOutputStream();
      }

      // Once we have the soap version, we can now return a fault for anything erroneous, so we start the try/catch here
      try {
        XmlElement envelope;
        InputStream is = request.getInputStream();

        // get character set
        String contentTypeString = request.getHttpHeaders().getHeader( "Content-Type" );
        String charset = null;
        if ( contentTypeString != null ) {
          HttpMediaType httpMediaType = new HttpMediaType( new HttpParseContext( contentTypeString.getBytes( "US-ASCII" ) ) );
          if ( httpMediaType.getMediaType().equals( "multipart/related" ) ) {
            is = XopUtil.getInputStream( new HttpMultipartRelatedContent( new HttpParseContext( StreamUtil.getContent( is ) ), httpMediaType ) );
          }
          charset = httpMediaType.getFirstParameter( "charset" );
        }

       // log request if desired
        ILogger wsLogger = getLogger(webservice._serviceInfo.getWebserviceType().getName());
        if (wsLogger.isDebugEnabled()) {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          StreamUtil.copy(is,baos);
          wsLogger.debug(">>>\n" + (charset == null ? baos.toString("UTF-8") : baos.toString(charset)));
          is = new ByteArrayInputStream(baos.toByteArray());
        }

        if ( webservice._requestTransform != null ) {
          is = (InputStream) webservice._requestTransform.invoke( is );
        }
        // if charset is specified in content-type http header, use that, otherwise use charset embedded in XML
        if ( charset == null ) {
          envelope = XmlElementInternals.instance().parse( is, webservice._parseOptions, webservice._typeResolver );
        }
        else {
          byte[] content = StreamUtil.getContent( is );
          String xmlString = new String( content, charset );
          envelope = XmlElementInternals.instance().parse( new StringReader( xmlString ), webservice._parseOptions, webservice._typeResolver );
        }
        context.setRequestEnvelope( envelope );
        context.setRequestSoapHeaders( getHeadersFromEnvelope( envelope, soapVersion ) );

        if ( webservice._requestXmlTransform != null ) {
          webservice._requestXmlTransform.invoke( envelope );
        }
        XmlElement body = getBodyFromEnv( envelope, soapVersion );
        List<XmlElement> bodyChildren = body.getChildren();
        if ( bodyChildren.size() != 1 ) {
          throw new WebServiceException( "Expected SOAP body to contain 1 child, but found " + bodyChildren.size(), true );
        }
        XmlElement requestElement = bodyChildren.get( 0 );
        logRequest( request, requestElement );
        XmlElement resp = webservice._invocationHandler.invoke( requestElement, context );

        XmlElement responseEnvelopeXml;
        if ( soapVersion == SoapVersion.SOAP_12 ) {
          gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope responseEnvelope = new gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope();
          responseEnvelope.setBody$( new gw.internal.schema.gw.xsd.w3c.soap12_envelope.Body() );
          responseEnvelope.Body().addChild( resp );
          if ( context != null && context.getResponseSoapHeadersDirect() != null && !context.getResponseSoapHeadersDirect().isEmpty() ) {
            responseEnvelope.setHeader$( new gw.internal.schema.gw.xsd.w3c.soap12_envelope.Header() );
            responseEnvelope.Header().getChildren().addAll( context.getResponseSoapHeadersDirect() );
          }
          responseEnvelopeXml = responseEnvelope;
        } else {
          gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope responseEnvelope = new gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope();
          responseEnvelope.setBody$( new gw.internal.schema.gw.xsd.w3c.soap11_envelope.Body() );
          responseEnvelope.Body().addChild( resp );
          if ( context != null && context.getResponseSoapHeadersDirect() != null && !context.getResponseSoapHeadersDirect().isEmpty() ) {
            responseEnvelope.setHeader$( new gw.internal.schema.gw.xsd.w3c.soap11_envelope.Header() );
            responseEnvelope.Header().getChildren().addAll( context.getResponseSoapHeadersDirect() );
          }
          responseEnvelopeXml = responseEnvelope;
        }
        if ( context != null && context.getResponseHttpHeadersDirect() != null ) {
          for ( String headerName : context.getResponseHttpHeadersDirect().getHeaderNames() ) {
            String headerValue = context.getResponseHttpHeadersDirect().getHeader( headerName );
            responseAdapter.getHttpHeaders().setHeader( headerName, headerValue );
          }
        }
        if ( webservice._responseXmlTransform != null ) {
          webservice._responseXmlTransform.invoke( responseEnvelopeXml );
        }
        if (wsLogger.isDebugEnabled()) {
          wsLogger.debug("<<<\n" + responseEnvelopeXml.asUTFString());
        }
        writeResponseEnvelope( responseEnvelopeXml, os, context, webservice, responseAdapter, soapVersion );

      } catch ( Throwable throwable ) {
        responseAdapter.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        final WsdlFault wsdlFault = getWsdlFault( throwable, webservice );
        handleWsdlFault( wsdlFault, soapVersion, webservice, os, context, responseAdapter );
        logWsdlFault(wsdlFault.getDetail() != null, throwable);
      } finally {
        try {
          if ( webservice._responseTransform != null ) {
            ByteArrayOutputStream baos = (ByteArrayOutputStream) os;
            byte[] bytes = baos.toByteArray();
            InputStream in = (InputStream) webservice._responseTransform.invoke( new ByteArrayInputStream( bytes ) );
            StreamUtil.copy( in, responseAdapter.getOutputStream() );
          }
        } finally {
          for ( Callable callable : finallyList ) {
            try {
              callable.call();
            } catch ( Throwable t ) {
              t.printStackTrace();
            }
          }
        }
      }
    } catch ( IOException ex ) {
      throw new ServletException( ex );
    } finally {
      afterInvoke( request, responseAdapter );
    }
  }

  private ILogger getLogger(String name) {
    ILogger logger = _loggers.get(name);
    if (logger == null) {
      synchronized (_loggers) {
        logger = _loggers.get(name);
        if (logger == null) {
          logger = XmlServices.getLogger(name);
          _loggers.put(name, logger);
        }
      }
    }
    return logger;
  }

  private static void writeString( OutputStream os, String s ) throws IOException {
    os.write( s.getBytes( "UTF-8" ) );
  }

  private void writeResponseEnvelope( XmlElement responseEnvelopeXml, OutputStream os, WsiInvocationContextImpl context, WsiInvocationContextImpl.WebService webservice, WebservicesResponseAdapter responseAdapter, SoapVersion soapVersion ) throws IOException {
    boolean mtomEnabled = context != null && context.isMtomEnabled();
    XmlSerializationOptions options = context == null ? webservice._serializationOptions : context.getXmlSerializationOptions();
    HashMap<XmlElement, Pair<String, BinaryData>> xopIncludes = new HashMap<XmlElement, Pair<String, BinaryData>>();
    if ( mtomEnabled ) {
      // find base64Binarys in XML tree, remove them for use as attachments
      removeBinaryData( responseEnvelopeXml, xopIncludes );
    }
    // optimization - if sorting or validating, pre-serialize the XML to catch any exceptions
    if ( options == null || options.getSort() || options.getValidate() ) {
      responseEnvelopeXml.writeTo( new DevNullOutputStream(), options );
      if ( options == null ) {
        options = new XmlSerializationOptions();
      } else {
        options = options.copy();
      }
      options.setValidate( false ); // no need to validate a second time
    }
    String contentType = getContentType( soapVersion );
    String multipartBoundary = null;
    if ( mtomEnabled ) {
      responseEnvelopeXml.declareNamespace( new XmlNamespace( gw.internal.schema.gw.xsd.w3c.xop_include.Include.$QNAME.getNamespaceURI(), "xop" ) );
      // place xop includes
      for ( Map.Entry<XmlElement, Pair<String, BinaryData>> entry : xopIncludes.entrySet() ) {
        XmlElement xml = entry.getKey();
        Pair<String, BinaryData> pair = entry.getValue();
        String uuid = pair.getFirst();
        BinaryData binaryData = pair.getSecond();
        gw.internal.schema.gw.xsd.w3c.xop_include.Include include = new gw.internal.schema.gw.xsd.w3c.xop_include.Include();
        try {
          include.setHref$( new URI( "cid:" + uuid ) );
        }
        catch ( URISyntaxException ex ) {
          throw new RuntimeException( ex ); // shouldn't happen unless we did something wrong
        }
        xml.addChild( include );
      }
      multipartBoundary = "uuid:" + UUID.randomUUID().toString();
      String multipartStart = "<root.message@ws.guidewire.com>";
      responseAdapter.setContentType( "multipart/related; type=\"" + contentType + "\"; boundary=\"" + multipartBoundary + "\"; start=\"" + multipartStart + "\"; start-info=\"text/xml\"" );
      writeString( os, "\r\n" );
      writeString( os, "--" );
      writeString( os, multipartBoundary );
      writeString( os, "\r\n" );
      writeString( os, "Content-Type: " );
      writeString( os, contentType );
      writeString( os, "\r\n" );
      writeString( os, "Content-Transfer-Encoding: binary\r\n" );
      writeString( os, "Content-ID: " );
      writeString( os, multipartStart );
      writeString( os, "\r\n" );
      writeString( os, "\r\n" );
    }
    else {
      responseAdapter.setContentType( contentType );
    }
    responseAdapter.commitHttpHeaders();
    responseEnvelopeXml.writeTo( os, options );
    if ( mtomEnabled ) {
      for ( Pair<String, BinaryData> pair : xopIncludes.values() ) {
        String uuid = pair.getFirst();
        BinaryData binaryData = pair.getSecond();
        writeString( os, "\r\n" );
        writeString( os, "--" );
        writeString( os, multipartBoundary );
        writeString( os, "\r\n" );
        writeString( os, "Content-Type: application/octet-stream\r\n" ); // TODO dlank - what should this be set to?
        writeString( os, "Content-Transfer-Encoding: binary\r\n" );
        writeString( os, "Content-ID: <" + uuid + ">\r\n" );
        writeString( os, "\r\n" );
        StreamUtil.copy( binaryData.getInputStream(), os );
      }

      writeString( os, "\r\n" );
      writeString( os, "--" );
      writeString( os, multipartBoundary );
      writeString( os, "--" );
      writeString( os, "\r\n" );
    }

  }

  private void removeBinaryData( XmlElement xml, Map<XmlElement, Pair<String, BinaryData>> xopIncludes ) {
    if ( xml.getSimpleValue() != null && xml.getSimpleValue().getGosuValueType().equals( TypeSystem.get( BinaryData.class ) ) ) {
      String uuid = UUID.randomUUID() + "@ws.guidewire.com";
      xopIncludes.put( xml, new Pair<String, BinaryData>( uuid, (BinaryData) xml.getSimpleValue().getGosuValue() ) );
      xml.setSimpleValue( null );
    }
    for ( XmlElement child : xml.getChildren() ) {
      removeBinaryData( child, xopIncludes );
    }
  }

  private static Map<String, String> parseRequestPairs( String queryString ) {
    if ( queryString == null || queryString.length() == 0 ) {
      return Collections.emptyMap();
    }
    Map<String, String> ret = new HashMap<String, String>();
    while ( true ) {
      int idx = queryString.indexOf( '&' );
      String keyValue;
      if ( idx < 0 ) {
        keyValue = queryString;
        queryString = "";
      } else {
        keyValue = queryString.substring( 0, idx );
        queryString = queryString.substring( idx + 1 );
      }
      idx = keyValue.indexOf( '=' );
      String key, value;
      if ( idx < 0 ) {
        key = keyValue;
        value = "";
      } else {
        key = keyValue.substring( 0, idx );
        value = keyValue.substring( idx + 1 );
      }
      ret.put( key, value );
      if ( queryString.length() == 0 ) {
        break;
      }
    }
    return ret;
  }

  private WsdlFault getWsdlFault( Throwable throwable, WsiInvocationContextImpl.WebService webservice ) {
    WsdlFault wsdlFault;

    if ( throwable instanceof WsdlFault ) {
      wsdlFault = (WsdlFault) throwable;
    } else {
      WsdlFault.FaultCode faultCode = null;
      IType declaredFaultType = getExceptionTypeExpected( webservice._serviceInfo.getThrownExceptions(), TypeSystem.getTypeFromObject( throwable ) );

      // set fault code
      if ( declaredFaultType != null || ( throwable instanceof WebServiceException && ( (WebServiceException) throwable ).isSenderFault() ) ) {
        faultCode = WsdlFault.FaultCode.Sender;
      }

      // set message
      String message;
      if ( declaredFaultType != null || throwable instanceof WebServiceException ) {
        message = throwable.getMessage() == null ? "" : throwable.getMessage();
      }
      else {
        message = throwable.toString();
        throwable.printStackTrace();
      }

      // set detail element
      XmlElement detailElement = null;
      if ( declaredFaultType != null ) {
        detailElement = new XmlElement( new QName( webservice._serviceInfo.getSchema().TargetNamespace().toString(), declaredFaultType.getRelativeName() ) );
      }

      wsdlFault = new WsdlFault( message, throwable );
      wsdlFault.setCode( faultCode );
      wsdlFault.setDetail( detailElement );
    }

    return wsdlFault;
  }

  private IType getExceptionTypeExpected( Set<IType> types, IType exceptionType ) {
    IType testingType = exceptionType;
    while ( testingType != null && !types.contains( testingType ) ) {
      testingType = testingType.getSupertype();
    }
    return testingType;
  }

  private void handleWsdlFault( final WsdlFault wsdlFault, final SoapVersion soapVersion, final WsiInvocationContextImpl.WebService webservice, final OutputStream os, final WsiInvocationContextImpl context, WebservicesResponseAdapter responseAdapter ) throws IOException {
    final String message = wsdlFault.getMessage();
    final XmlElement detailElement = wsdlFault.getDetail();
    if ( soapVersion == SoapVersion.SOAP_12 ) {
      gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope responseEnvelope = new gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope();
      responseEnvelope.setBody$( new gw.internal.schema.gw.xsd.w3c.soap12_envelope.Body() );
      gw.internal.schema.gw.xsd.w3c.soap12_envelope.Fault soapFaultElem = new gw.internal.schema.gw.xsd.w3c.soap12_envelope.Fault();
      Fault_Code faultCode = new Fault_Code();
      FaultcodeEnum faultCodeValue = FaultcodeEnum.Receiver;
      if ( wsdlFault.getCode() != null ) {
        switch ( wsdlFault.getCode() ) {
          case DataEncodingUnknown:
            faultCodeValue = FaultcodeEnum.DataEncodingUnknown;
            break;
          case MustUnderstand:
            faultCodeValue = FaultcodeEnum.MustUnderstand;
            break;
          case Sender:
            faultCodeValue = FaultcodeEnum.Sender;
            break;
          case VersionMismatch:
            faultCodeValue = FaultcodeEnum.VersionMismatch;
            break;
        }
      }
      faultCode.setValue$( faultCodeValue );
      soapFaultElem.setCode$( faultCode );
      soapFaultElem.setReason$( new Fault_Reason() );
      soapFaultElem.Reason().setText$( Collections.singletonList( message ) );
      soapFaultElem.Reason().Text_elem().get( 0 ).setLang$( "" );
      responseEnvelope.Body().addChild( soapFaultElem );
      if ( detailElement != null ) {
        soapFaultElem.setDetail$( new gw.internal.schema.gw.xsd.w3c.soap12_envelope.anonymous.elements.Fault_Detail() );
        soapFaultElem.Detail().addChild( detailElement );
      }
      if ( webservice._responseXmlTransform != null ) {
        webservice._responseXmlTransform.invoke( responseEnvelope );
      }
      writeResponseEnvelope( responseEnvelope, os, context, webservice, responseAdapter, soapVersion );
    } else {
      gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope responseEnvelope = new gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope();
      responseEnvelope.setBody$( new gw.internal.schema.gw.xsd.w3c.soap11_envelope.Body() );
      gw.internal.schema.gw.xsd.w3c.soap11_envelope.Fault soapFaultElem = new gw.internal.schema.gw.xsd.w3c.soap11_envelope.Fault();
      QName soap11FaultCodeQName = GENERIC_FAULT_TO_SOAP11_FAULT.get( wsdlFault.getCode() );
      if ( soap11FaultCodeQName == null ) {
        soap11FaultCodeQName = GENERIC_FAULT_TO_SOAP11_FAULT.get( WsdlFault.FaultCode.Receiver );
      }
      soapFaultElem.setFaultcode$( soap11FaultCodeQName );
      soapFaultElem.setFaultstring$( message == null ? "" : message );
      soapFaultElem.setFaultactor$( wsdlFault.getActorRole() );
      responseEnvelope.Body().addChild( soapFaultElem );
      if ( detailElement != null ) {
        soapFaultElem.setDetail$( new gw.internal.schema.gw.xsd.w3c.soap11_envelope.anonymous.elements.Fault_Detail() );
        soapFaultElem.Detail().addChild( detailElement );
      }
      if ( webservice._responseXmlTransform != null ) {
        webservice._responseXmlTransform.invoke( responseEnvelope );
      }
      writeResponseEnvelope( responseEnvelope, os, context, webservice, responseAdapter, soapVersion );
    }
  }

  private void logWsdlFault( final boolean expectedFault, final Throwable throwable ) {
    if ( expectedFault ) {
      if ( getILogger().isDebugEnabled() ) {
        getILogger().debug( "DEBUG: EXPECTED FAULT RECEIVED", throwable );
      }
      getILogger().info( TypeSystem.getTypeFromObject( throwable ).getName() + ": " + throwable.getLocalizedMessage() );
    } else {
      getILogger().info( "Unexpected exception", throwable );
    }
  }

  private static String getContentType( SoapVersion soapVersion ) {
    if ( soapVersion == SoapVersion.SOAP_12 ) {
      return "application/soap+xml;charset=utf-8";
    } else {
      return "text/xml;charset=utf-8";
    }
  }

  private XmlElement getBodyFromEnv( XmlElement envelope, SoapVersion soapVersion ) {
    XmlElement body;
    if ( soapVersion == SoapVersion.SOAP_12 ) {
      gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope env = (gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope) envelope;
      body = env.Body();
    } else {
      gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope env = (gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope) envelope;
      body = env.Body();
    }
    return body;
  }

  private WsiInvocationContextImpl.WebService getWebServiceForType( IType type ) {
    IGosuClass gosuClass = (IGosuClass) type;
    WsiInvocationContextImpl.WebService webservice;
    TypeSystem.lock();
    try {
      webservice = _webservices.get( gosuClass );
      // check if webservice code has changed
      Class<?> backingClass = gosuClass.getBackingClass();
      //noinspection ObjectEquality
      if ( webservice != null && webservice._backingClass != backingClass ) {
        _webservices.remove( gosuClass );
        webservice = null;
      }
      if ( webservice == null ) {
        webservice = new WsiInvocationContextImpl.WebService();
        webservice._backingClass = backingClass;
        webservice._worker = type.getTypeInfo().getConstructor().getConstructor().newInstance();
        resolveRequestTransformAnnotation( type, webservice );
        resolveResponseTransformAnnotation( type, webservice );
        resolveRequestXmlTransformAnnotation( type, webservice );
        resolveResponseXmlTransformAnnotation( type, webservice );
        resolveInvocationHandlerAnnotation( type, webservice );
        XmlParseOptions parseOptions;
        XmlSerializationOptions serializationOptions;
        IAnnotationInfo parseOptionsAnnotation = type.getTypeInfo().getAnnotation( _wsiParseOptionsAnnotationType );
        if ( parseOptionsAnnotation != null ) {
          Object instance = parseOptionsAnnotation.getInstance();
          parseOptions = (XmlParseOptions) _wsiParseOptionsAnnotationType.getTypeInfo().getProperty( "ParseOptions" ).getAccessor().getValue( instance );
          if ( parseOptions == null ) {
            throw new IllegalArgumentException( "null argument to " + _wsiParseOptionsAnnotationType.getName() + " annotation on class " + type.getName() );
          }
          parseOptions = parseOptions.copy();
        } else {
          parseOptions = new XmlParseOptions();
        }

        final List<XmlSchemaIndex> additionalSchemaIndexes = new ArrayList<XmlSchemaIndex>();

        for ( XmlSchemaAccess xmlSchemaAccess : parseOptions.getAdditionalSchemas() ) {
          additionalSchemaIndexes.add( ( (XmlSchemaAccessImpl) xmlSchemaAccess ).getSchemaIndex() );
        }

        IModule defaultModule = TypeSystem.getGlobalModule();
        additionalSchemaIndexes.add( XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( defaultModule, "gw.xsd.w3c.soap12_envelope" ) );
        additionalSchemaIndexes.add( XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( defaultModule, "gw.xsd.w3c.soap11_envelope" ) );
        additionalSchemaIndexes.add( XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( defaultModule, "gw.xsd.guidewire.soapheaders" ) );

        IAnnotationInfo serializationOptionsAnnotation = type.getTypeInfo().getAnnotation( _wsiSerializationOptionsAnnotationType );
        if ( serializationOptionsAnnotation != null ) {
          Object instance = serializationOptionsAnnotation.getInstance();
          serializationOptions = (XmlSerializationOptions) _wsiSerializationOptionsAnnotationType.getTypeInfo().getProperty( "SerializationOptions" ).getAccessor().getValue( instance );
          if ( serializationOptions == null ) {
            throw new IllegalArgumentException( "null argument to " + _wsiSerializationOptionsAnnotationType.getName() + " annotation on class " + type.getName() );
          }
          serializationOptions = serializationOptions.copy();
        } else {
          serializationOptions = null;
        }

        StringBuilder xsdRootURL = new StringBuilder();
        String[] parts = type.getName().split( "\\." );
        for ( int i = 0; i < parts.length - 1; i++ ) {
          if ( xsdRootURL.length() > 0 ) {
            xsdRootURL.append( '/' );
          }
          xsdRootURL.append( ".." );
        }
        final WsiServiceInfo serviceInfo = WsiUtilities.generateWsdl( type, "urn:ignored", xsdRootURL.toString(), this );

        for ( XmlSchemaIndex schema : serviceInfo.getSchemas() ) {
          additionalSchemaIndexes.add( schema );
        }

        List<XmlSchemaAccess> additionalSchemaAccesses = new ArrayList<XmlSchemaAccess>();
        for ( XmlSchemaIndex schemaIndex : additionalSchemaIndexes ) {
          additionalSchemaAccesses.add( schemaIndex.getXmlSchemaAccess() );
        }
        parseOptions.setAdditionalSchemas( additionalSchemaAccesses );

        final Schema schemaForValidation;
        try {
          // this is necessary since there is no XmlSchemaAccess or XmlSchemaIndex for the wsdl representing this service
          schemaForValidation = XmlSchemaIndex.parseSchemasButDoNotCache( additionalSchemaIndexes, serviceInfo.getWsdl(), serviceInfo.getWebserviceType() );
        }
        catch ( Exception ex ) {
          throw GosuExceptionUtil.forceThrow( ex );
        }

        XmlTypeResolver typeResolver = new XmlTypeResolver() {
          @Override
          public Pair<IType, IType> resolveTypes( List<QName> elementStack ) {
            // Resolve elements/typeinstances at a depth that they are actually based on a schema on the server side,
            // since the elements containing them are imaginary as far as the XSD typeloader is concerned.
            int elementStackDepth = elementStack.size();
            if ( elementStackDepth > 2 && elementStack.get( 1 ).getLocalPart().equals( "Body" ) ) {
              if ( elementStackDepth >= 4 ) {
                QName opName = elementStack.get( 2 );
                QName paramName = elementStack.get( 3 );
                Map<QName, MarshalInfo> map = serviceInfo.getMarshalInfoMap().get( opName );
                if ( map != null ) {
                  MarshalInfo marshalInfo = map.get( paramName );
                  if ( marshalInfo != null ) {
                    int arrayLevels = 0;
                    MarshalInfo componentMarshalInfo = marshalInfo;
                    while ( true ) {
                      if ( componentMarshalInfo instanceof ArrayMarshalInfo ) {
                        arrayLevels++;
                        componentMarshalInfo = ( (ArrayMarshalInfo) componentMarshalInfo ).getComponentMarshalInfo();
                      } else if ( componentMarshalInfo instanceof ListMarshalInfo ) {
                        arrayLevels++;
                        componentMarshalInfo = ( (ListMarshalInfo) componentMarshalInfo ).getComponentMarshalInfo();
                      } else {
                        break;
                      }
                    }
                    if ( elementStackDepth == 4 + arrayLevels && componentMarshalInfo instanceof XmlTypeInstanceMarshalInfo ) {
                      return new Pair<IType, IType>( null, componentMarshalInfo.getType() );
                    } else if ( elementStackDepth == 5 + arrayLevels && componentMarshalInfo instanceof XmlElementMarshalInfo ) {
                      // If typed as XmlElement, processContents is skip, so skip typing
                      if ( !TypeSystem.get( XmlElement.class ).equals( componentMarshalInfo.getType() ) ) {
                        for ( XmlSchemaIndex schema : serviceInfo.getSchemas() ) {
                          QName elementName = elementStack.get( 4 + arrayLevels );
                          try {
                            XmlSchemaElement element = schema.getXmlSchemaElementByQName( elementName );
                            IType elementType = XmlSchemaIndex.getGosuTypeBySchemaObject( element );
                            if ( elementType == null ) {
                              throw new IllegalStateException( "Element " + elementName + " not found in schema " + schema );
                            }
                            return new Pair<IType, IType>( elementType, null );
                          } catch ( NotFoundException ex ) {
                            // continue
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            return new Pair<IType, IType>( null, null );
          }

          @Override
          public Schema getSchemaForValidation() {
            return schemaForValidation;
          }

          @Override
          public String getValidationSchemasDescription() {
            StringBuilder sb = new StringBuilder();
            sb.append( "@WsiWebService " );
            sb.append( serviceInfo.getWebserviceType().getName() );
            if ( ! additionalSchemaIndexes.isEmpty() ) {
              TreeSet<String> sortedSet = new TreeSet<String>();
              for ( XmlSchemaIndex schemaIndex : additionalSchemaIndexes ) {
                sortedSet.add( schemaIndex.getPackageName() );
              }
              sb.append( " and schemas [" );
              boolean first = true;
              for ( String schemaPackageName : sortedSet ) {
                if ( ! first ) {
                  sb.append( ", " );
                }
                first = false;
                sb.append( schemaPackageName );
              }
              sb.append( "]" );
            }
            return sb.toString();
          }
        };
        webservice._parseOptions = parseOptions;
        webservice._serializationOptions = serializationOptions;
        webservice._typeResolver = typeResolver;
        webservice._serviceInfo = serviceInfo;
        _webservices.put( gosuClass, webservice );
      }
    } finally {
      TypeSystem.unlock();
    }
    return webservice;
  }

  private void resolveRequestTransformAnnotation( IType type, WsiInvocationContextImpl.WebService webservice ) {
    IAnnotationInfo requestTransformAnnotation = type.getTypeInfo().getAnnotation( _requestTransformType );
    if ( requestTransformAnnotation != null ) {
      Object instance = requestTransformAnnotation.getInstance();
      webservice._requestTransform = (Function1) _requestTransformType.getTypeInfo().getProperty( "Transform" ).getAccessor().getValue( instance );
    }
  }

  private void resolveResponseTransformAnnotation( IType type, WsiInvocationContextImpl.WebService webservice ) {
    IAnnotationInfo responseTransformAnnotation = type.getTypeInfo().getAnnotation( _responseTransformType );
    if ( responseTransformAnnotation != null ) {
      Object instance = responseTransformAnnotation.getInstance();
      webservice._responseTransform = (Function1) _responseTransformType.getTypeInfo().getProperty( "Transform" ).getAccessor().getValue( instance );
    }
  }

  private void resolveRequestXmlTransformAnnotation( IType type, WsiInvocationContextImpl.WebService webservice ) {
    IAnnotationInfo xmlTransformAnnotation = type.getTypeInfo().getAnnotation( _requestXmlTransformType );
    if ( xmlTransformAnnotation != null ) {
      Object instance = xmlTransformAnnotation.getInstance();
      webservice._requestXmlTransform = (Function1) _requestXmlTransformType.getTypeInfo().getProperty( "Transform" ).getAccessor().getValue( instance );
    }
  }

  private void resolveResponseXmlTransformAnnotation( IType type, WsiInvocationContextImpl.WebService webservice ) {
    IAnnotationInfo xmlTransformAnnotation = type.getTypeInfo().getAnnotation( _responseXmlTransformType );
    if ( xmlTransformAnnotation != null ) {
      Object instance = xmlTransformAnnotation.getInstance();
      webservice._responseXmlTransform = (Function1) _responseXmlTransformType.getTypeInfo().getProperty( "Transform" ).getAccessor().getValue( instance );
    }
  }

  private void resolveInvocationHandlerAnnotation( IType type, WsiInvocationContextImpl.WebService webservice ) {
    IAnnotationInfo invocationHandlerAnnotationInfo = type.getTypeInfo().getAnnotation( _wsiInvocationHandlerType );
    if ( invocationHandlerAnnotationInfo != null ) {
      Object instance = invocationHandlerAnnotationInfo.getInstance();
      webservice._invocationHandler = (DefaultWsiInvocationHandler) _wsiInvocationHandlerType.getTypeInfo().getProperty( "InvocationHandler" ).getAccessor().getValue( instance );
    } else {
      webservice._invocationHandler = new DefaultWsiInvocationHandler();
    }
  }

  public void preUnmarshal( IType type, XmlElement headersFromEnvelope, XmlElement body, IMethodInfo method, List<Callable> finallyList, WebservicesRequest request, QName opName ) throws IOException {
  }

  @Override
  protected void service( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
    Throwable throwable = null;
    try {
      super.service( req, resp );
    }
    catch ( Throwable t ) {
      throwable = t;
      throw GosuExceptionUtil.forceThrow( t );
    }
    finally {
      for (WebservicesServletBaseListenerForTesting listener : _listenersForTesting ) {
        listener.requestComplete( req, throwable );
      }
    }
  }

  public static void addListenerForTesting( WebservicesServletBaseListenerForTesting listener ) {
    _listenersForTesting.add( listener );
  }

  public static void removeListenerForTesting( WebservicesServletBaseListenerForTesting listener ) {
    _listenersForTesting.remove( listener );
  }

  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException {
    doGet( new ServletWebservicesRequest( request ), new ServletWebservicesResponseAdapter( response, getILogger() ) );
  }

  public void doGet( WebservicesRequest request, WebservicesResponseAdapter responseAdapter ) throws ServletException {
    beforeInvoke( request, responseAdapter );
    try {
      maybeInit();
      String path = request.getPathInfo();
      String queryStr = request.getQueryString();
      if ( path == null ) {
        path = "";
      }
      String fullRequest = request.getRequestURL();
      String prefix;
      if ( path.length() == 0 ) {
        int idx = fullRequest.lastIndexOf( '/' );
        prefix = fullRequest.substring( idx + 1 ) + "/";
        path = "/";
      } else {
        prefix = "";
      }
      if ( path.equals( "/" ) ) {
        if ( !_hideServiceListPage ) {
          doGetIndex( responseAdapter, prefix );
        }
      } else if ( path.startsWith( "/resources." ) ) {
        if ( RESOURCES.containsKey( path ) ) {
          try {
            Pair<String, String> pair = RESOURCES.get( path );
            String relativePath = pair.getFirst();
            String contentType = pair.getSecond();
            responseAdapter.setContentType( contentType );
            IModule module = TypeSystem.getCurrentModule();
            IFile resourceFile = module.getFileRepository().findFirstFile( relativePath );
            StreamUtil.copy( resourceFile.openInputStream(), responseAdapter.getOutputStream() );
          } catch ( IOException ex ) {
            throw new ServletException( ex );
          }
        } else {
          getILogger().info( "Webservices servlet: 404 Not Found: " + path );
          send404NotFound( responseAdapter );
        }
      } else if ( path.endsWith( XSD_SUFFIX ) ) {
        doGetXSD( responseAdapter, path.substring( 1 ) );
      } else if ( path.endsWith( WSDL_SUFFIX ) ) {
        doGetXSD( responseAdapter, path.substring( 1 ) );
      } else if ( path.endsWith( GX_SUFFIX ) ) {
        doGetXSD( responseAdapter, path.substring( 1 ) );
      } else {
        if ( queryStr != null && queryStr.equalsIgnoreCase( "wsdl" ) ) {
          Pair<IType, SoapVersion> pair = getWebServiceInfo(request);
          if ( pair != null ) {
            doGetWSDL( responseAdapter, pair.getFirst(), request.getRequestURL() );
          } else {
            getILogger().info( "Webservices servlet: 404 Not Found (WSDL): " + path );
            send404NotFound( responseAdapter );
          }
        } else {
          getILogger().info( "Webservices servlet: 404 Not Found (other): " + path );
          send404NotFound( responseAdapter );
        }
      }
    } finally {
      afterInvoke( request, responseAdapter );
    }
  }

  private void maybeInit() {
    if ( _initialized ) {
      return;
    }
    TypeSystem.lock();
    try {
      if ( _initialized ) {
        return;
      }
      _requestTransformType = TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiRequestTransform" );
      _responseTransformType = TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiResponseTransform" );
      _requestXmlTransformType = TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiRequestXmlTransform" );
      _responseXmlTransformType = TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiResponseXmlTransform" );
      _wsiInvocationHandlerType = TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiInvocationHandler" );
      _wsiParseOptionsAnnotationType = TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiParseOptions" );
      _wsiSerializationOptionsAnnotationType = TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiSerializationOptions" );
      _hideServiceListPage = getBooleanValue( getServletConfig(), CONFIG_PARAM_HIDE_SERVICE_LIST_PAGE, false );

      final String path = getStringValue( getServletConfig(), CONFIG_PARAM_AVAILABLE_SERVICES, null );
      if ( path != null && path.length() > 0 ) {
        _initializedFromConfig = true;
        initAvailable( path );
      }
      if ( !_initializedFromConfig ) {
        initAvailable();
      }
      _initialized = true;
      TypeSystem.addTypeLoaderListenerAsWeakRef(this);
    } finally {
      TypeSystem.unlock();
    }
  }

  private Pair<IType, SoapVersion> getWebServiceInfo(WebservicesRequest request) {
    String soapVersionString = null;
    String fqName = request.getPathInfo().substring( 1 ).replace( "/", "." ); // /foo/bar -> foo.bar (path to namespace)
    IType type = TypeSystem.getByFullNameIfValid( fqName );
    if ( type == null ) {
      soapVersionString = GosuClassUtil.getNameNoPackage( fqName );
      fqName = GosuClassUtil.getPackage( fqName );
      type = TypeSystem.getByFullNameIfValid( fqName );
    }
    if ( type instanceof IGosuClass && checkWebServiceForErrors( type ) == null && isWebserviceAvailable( type ) ) {
      SoapVersion soapVersion;
      if ( soapVersionString == null || soapVersionString.equals( "soap12" ) ) {
        soapVersion = SoapVersion.SOAP_12;
      } else if ( soapVersionString.equals( "soap11" ) ) {
        soapVersion = SoapVersion.SOAP_11;
      } else {
        return null;
      }
      return new Pair<IType, SoapVersion>( type, soapVersion );
    }
    return null;
  }

  private void send404NotFound( WebservicesResponseAdapter responseAdapter ) {
    try {
      responseAdapter.setStatus( HttpServletResponse.SC_NOT_FOUND );
      responseAdapter.setContentType( "text/plain;charset=utf-8" );
      responseAdapter.getOutputStream().write( StreamUtil.toBytes( "404 Not Found" ) );
    } catch ( IOException ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
  }

  protected void printIndexBody( DftreeNode node, String reqPath ) throws IOException {
    printIndexWebServices( node, reqPath );
    Set<String> xsds = new TreeSet<String>();
    Set<String> wsdls = new TreeSet<String>();
    extractXmlResourcesForIndex( xsds, wsdls );
    printIndexSchemas( node, reqPath, xsds );
    printIndexWSDLs( node, reqPath, wsdls );
  }

  protected void printIndexWebServices( DftreeNode node, String reqPath ) throws IOException {
    TreeMap<String, String> path2TypeName = new TreeMap<String, String>();
    for ( CharSequence typeNameCS : getAllWebserviceTypes() ) {
      String typeName = typeNameCS.toString();
      IType type = TypeSystem.getByFullNameIfValid( typeName );
      if ( type != null ) {
        if ( checkWebServiceForErrors( type ) == null && isWebserviceAvailable( type ) ) {
          String nsPath = typeName.replace( ".", "/" );
          path2TypeName.put( reqPath + nsPath, typeName );
        }
      }
    }
    if ( !path2TypeName.isEmpty() ) {
      DftreeNode category = new DftreeNode( "Document/Literal Web Services" );
      node.addChild( category );
      for ( Map.Entry<String, String> entry : path2TypeName.entrySet() ) {
        addDftreeChild( category, entry.getValue().replace( '.', '/' ), entry.getKey() + "?WSDL" );
      }
    }
  }

  protected void printIndexWSDLs( DftreeNode node, String reqPath, Set<String> wsdls ) throws IOException {
    DftreeNode category = new DftreeNode( "Supporting WSDLs" );
    node.addChild( category );
    for ( String path : wsdls ) {
      addDftreeChild( category, path, reqPath + path );
    }
  }

  protected void printIndexSchemas( DftreeNode node, String reqPath, Set<String> xsds ) throws IOException {
    DftreeNode category = new DftreeNode( "Supporting Schemas" );
    node.addChild( category );
    for ( String path : xsds ) {
      String url = reqPath + path;
      addDftreeChild( category, path, url );
    }
  }

  protected void addDftreeChild( DftreeNode parent, String name, String url ) {
    int idx = name.indexOf( '/' );
    if ( idx < 0 ) {
      DftreeNode child = new DftreeNode( name );
      parent.addChild( child );
      child.setTargetPath( url );
    } else {
      String start = name.substring( 0, idx );
      String rest = name.substring( idx + 1 );
      DftreeNode child = null;
      if ( parent.getChildren() != null ) {
        // find existing child if available
        for ( DftreeNode dftreeNode : parent.getChildren() ) {
          if ( dftreeNode.getName().equals( start ) ) {
            child = dftreeNode;
            break;
          }
        }
      }
      if ( child == null ) {
        child = new DftreeNode( start );
        parent.addChild( child );
      }
      addDftreeChild( child, rest, url );
    }
  }

  protected void extractXmlResourcesForIndex(Set<String> xsds, Set<String> wsdls) {
    for ( XmlSchemaResourceTypeLoaderBase<?> typeLoader : TypeSystem.getGlobalModule().getTypeLoaders( XmlSchemaResourceTypeLoaderBase.class ) ) {
      String tpClassName = typeLoader.getClass().getName();
      if ( tpClassName.equals( "gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoader" ) ) {
        for ( String ns : typeLoader.getAllSchemaNamespaces() ) {
          final XmlSchemaIndex<?> schemaForNamespace = typeLoader.getSchemaForNamespace( ns );
          final String path = schemaForNamespace.getXSDSourcePath();
          xsds.add( path );
        }
      } else if ( tpClassName.equals( "com.guidewire.commons.system.gx.GXTypeLoader" ) ) {
        for ( String ns : typeLoader.getAllSchemaNamespaces() ) {
          final XmlSchemaIndex<?> schemaForNamespace = typeLoader.getSchemaForNamespace( ns );
          final String path = schemaForNamespace.getXSDSourcePath();
          xsds.add( path );
        }
      } else if ( tpClassName.equals( "gw.internal.xml.ws.typeprovider.WsdlTypeLoader" ) ) {
        for ( String ns : typeLoader.getAllSchemaNamespaces() ) {
          if ( !ns.startsWith( "wsi.local." ) ) {
            final XmlSchemaIndex<?> schemaForNamespace = typeLoader.getSchemaForNamespace( ns );
            final String path = schemaForNamespace.getXSDSourcePath();
            wsdls.add( path );
          }
        }
      } else {
        throw new RuntimeException( "Unrecognized typeloader class: " + tpClassName );
      }
    }
  }


  private Collection<String> getAllWebserviceTypes() {
    TypeSystem.lock();
    try {
      return _availableServices;
    } finally {
      TypeSystem.unlock();
    }
  }

  protected void doGetXSD( WebservicesResponseAdapter responseAdapter, String path ) throws ServletException {
    try {
      boolean found = false;
      OUTER: 
      for ( XmlSchemaResourceTypeLoaderBase<?> typeLoader : TypeSystem.getGlobalModule().getTypeLoaders( XmlSchemaResourceTypeLoaderBase.class ) ) {
        final Collection<String> namespaces = typeLoader.getAllSchemaNamespaces();
        for ( String ns : namespaces ) {
          final XmlSchemaIndex si = typeLoader.getSchemaForNamespace( ns );
          if ( si.getXSDSourcePath().equals( path ) ) {
            found = true;
            responseAdapter.setContentType( "text/xml" );
            StreamUtil.copy( si.getXSDSource().getInputStream( false ), responseAdapter.getOutputStream() );
            responseAdapter.setStatus( HttpServletResponse.SC_OK );
            break OUTER;
          }
        }
      }
      if ( !found ) {
        getILogger().info( "Webservices servlet: 404 Not Found (XSD): " + path );
        send404NotFound( responseAdapter );
      }
    } catch ( IOException ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
  }

  private void doGetWSDL( WebservicesResponseAdapter responseAdapter, IType type, String requestURL ) {
    try {
      String xsdRootURL = getXsdRootURL( type );
      WsiServiceInfo serviceInfo = WsiUtilities.generateWsdl( type, requestURL, xsdRootURL, this );
      responseAdapter.setContentType( "text/xml" );
      serviceInfo.getWsdl().writeTo( responseAdapter.getOutputStream(), new XmlSerializationOptions().withSort( false ) );
      responseAdapter.setStatus( HttpServletResponse.SC_OK );
    } catch ( IOException e ) {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  public static String checkWebServiceForErrors( IType type ) {
    if ( type == null ) {
      return "Null webservice type";
    }
    ITypeInfo typeInfo = type.getTypeInfo();
    if ( !( type instanceof IGosuClass ) ) {
      return "Type is not a Gosu class";
    }
    if ( !( typeInfo.hasAnnotation( _wsiWebServiceAnnotationType.get() ) ) ) {
      return "Type does not have @WsiWebService annotation";
    }
    if ( !type.isValid() ) {
      IGosuClass clazz = (IGosuClass) type;
      //noinspection ThrowableResultOfMethodCallIgnored
      return "Type is not valid." + ( clazz.getParseResultsException() == null ? "" : ( "\n\n" + GosuExceptionUtil.getStackTraceAsString( clazz.getParseResultsException() ) ) );
    }
    return null;
  }

  private boolean isWebserviceAvailable( IType type ) {
    if ( _wsiLocal ) {
      return true;
    }
    TypeSystem.lock();
    try {
      return _availableServices.contains( type.getName() );
    } finally {
      TypeSystem.unlock();
    }
  }

  private String getXsdRootURL( IType type ) {
    StringBuilder xsdRootURL = new StringBuilder();
    String[] parts = GosuClassUtil.getPackage( type.getName() ).split( "\\." );
    int numEntries = parts.length;
    for ( int i = 0; i < numEntries; i++ ) {
      xsdRootURL.append( "../" );
    }
    return xsdRootURL.toString();
  }

  @SuppressWarnings( { "StringConcatenationInsideStringBufferAppend" } )
  protected void doGetIndex( WebservicesResponseAdapter responseAdapter, String path ) {
    try {
      responseAdapter.setContentType( "text/html;charset=utf-8" );
      responseAdapter.setStatus( HttpServletResponse.SC_OK );
      PrintWriter out = new PrintWriter( StreamUtil.getOutputStreamWriter( responseAdapter.getOutputStream() ) );
      out.append( "<html><head><script type=\"text/javascript\" src=\"" + path + "resources.dftree/dftree.js\"></script>" );
      out.append( "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + path + "resources.dftree/dftree.css\">" );
      out.append( "<title>Available Webservice-related Resources</title></head><body>" );
      out.append( "<script language=\"javascript\">\n<!--\n" );
      out.append( "tree = new dFTree({name: 'tree',useIcons:true, icondir:'" + path + "resources.dftree/img'});\n" );
      DftreeNode root = new DftreeNode( "root" );
      printIndexBody( root, path );
      int[] nextId = { 1 };
      for ( DftreeNode dftreeNode : root.getChildren() ) {
        generateDftreeCode( dftreeNode, 0, out, nextId );
      }
      out.append( "tree.draw();\n" );
      out.append( "-->\n" );
      out.append( "</script></body></html>" );
      out.flush();
    } catch ( IOException e ) {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  @SuppressWarnings( { "StringConcatenationInsideStringBufferAppend" } )
  private void generateDftreeCode( DftreeNode dftreeNode, int parentId, PrintWriter out, int[] nextId ) {
    int myId = nextId[ 0 ]++;
    out.append( "tree.add(new dNode({id: '" + myId + "',caption: '" + dftreeNode.getName() + "'" );
    String targetPath = dftreeNode.getTargetPath();
    if ( targetPath != null ) {
      if ( targetPath.startsWith( "/" ) ) {
        targetPath = targetPath.substring( 1 ); // Weblogic workaround - we should remove this at some point and find the real problem
      }
      out.append( ", url: '" + targetPath + "'" );
    }
    if ( dftreeNode.getChildren() == null ) {
      out.append( ", isFolder:0" );
    }
    out.append( "})," + parentId + ");\n" );
    if ( dftreeNode.getChildren() != null ) {
      Collections.sort( dftreeNode.getChildren(), new Comparator<DftreeNode>() {
        @Override
        public int compare( DftreeNode o1, DftreeNode o2 ) {
          int result = Boolean.valueOf( o1.getChildren() == null ).compareTo( o2.getChildren() == null );
          if ( result == 0 ) {
            result = o1.getName().compareTo( o2.getName() );
          }
          return result;
        }
      } );
      for ( DftreeNode child : dftreeNode.getChildren() ) {
        generateDftreeCode( child, myId, out, nextId );
      }
    }
  }

  public static void setDefaultLocalWebservicesServletClass( Class<? extends WebservicesServletBase> clazz ) {
    _defaultLocalWebservicesServletClass = clazz;
    _defaultLocalWebservicesServlet.clear();
  }

  // Called from Gosu code
  @SuppressWarnings( { "UnusedDeclaration" } )
  public static WebservicesServletBase getDefaultLocalWebservicesServlet() {
    return _defaultLocalWebservicesServlet.get();
  }

  public boolean isWsiLocal() {
    return _wsiLocal;
  }

  public abstract void postConfigureWebservice( IType type, WsiServiceInfo serviceInfo );
  public String postConfigureWebservicePath( IType type, String xsdRootPath ) {
    return "";
  }

  public static Map<WsiRequestLocal, ?> getRequestLocalMapForCurrentThread() {
    Map<WsiRequestLocal, ?> ret = _requestLocals.get();
    if ( ret == null ) {
      throw new IllegalStateException( "The current thread is not a webservice request thread" );
    }
    return ret;
  }

  private XmlElement getHeadersFromEnvelope( XmlElement envelope, SoapVersion soapVersion ) {
    XmlElement headersFromEnvelope;
    if ( soapVersion == SoapVersion.SOAP_12 ) {
      gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope env = (gw.internal.schema.gw.xsd.w3c.soap12_envelope.Envelope) envelope;
      headersFromEnvelope = env.Header();
    } else {
      gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope env = (gw.internal.schema.gw.xsd.w3c.soap11_envelope.Envelope) envelope;
      headersFromEnvelope = env.Header();
    }
    return headersFromEnvelope;
  }

  @Override
  public void refreshedTypes( RefreshRequest request ) {
  }

  @Override
  public void refreshed() {
    onRefreshTypeSystem();
  }
}
