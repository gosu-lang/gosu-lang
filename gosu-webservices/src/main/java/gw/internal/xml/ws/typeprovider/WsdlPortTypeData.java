/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider;

/**
 * This class represents a port definition within a wsdl, there is a corresponding info
 * that contains the properties and methods information that is lazy loaded.
 */

import gw.config.CommonServices;
import gw.fs.IFile;
import gw.internal.xml.ws.IWsdlConfig;
import gw.internal.xml.ws.WsdlSoapHeaders;
import gw.internal.xml.ws.WsiAdditions;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlTypeData;
import gw.internal.xml.xsd.typeprovider.schema.*;
import gw.internal.xml.xsd.typeprovider.schemaparser.SoapVersion;
import gw.internal.xml.ws.rt.WsdlOperationInfo;
import gw.internal.xml.ws.rt.WsdlPortImpl;
import gw.internal.xml.ws.typeprovider.paraminfo.AnonymousElementException;
import gw.internal.xml.ws.typeprovider.paraminfo.ArrayWsdlOperationParameterInfo;
import gw.internal.xml.ws.typeprovider.paraminfo.WsdlOperationParameterInfo;
import gw.internal.xml.xsd.typeprovider.IWsdlPortTypeData;
import gw.internal.xml.xsd.typeprovider.IXmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.IXmlType;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertySpec;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.lang.reflect.ConstructorInfoBuilder;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFileBasedFeature;
import gw.lang.reflect.ILocationAwareFeature;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.LocationInfo;
import gw.lang.reflect.MethodInfoBuilder;
import gw.lang.reflect.ParameterInfoBuilder;
import gw.lang.reflect.PropertyInfoBuilder;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.ILogger;
import gw.util.Pair;
import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.AnyType;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.XmlElement;
import gw.xml.XmlException;

import java.util.*;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.namespace.QName;

public class WsdlPortTypeData extends XmlTypeData implements IWsdlPortTypeData, ILocationAwareFeature {

  private final Wsdl _wsdl;
  protected final WsdlPort _wsdlDefPort;
  private final String _name;
  private boolean _initialized;
  private List<IPropertyInfo> _props;
  private List<IMethodInfo> _methods;
  private WsdlService _service;
  private List<IConstructorInfo> _ctors;
  private final IFile _resourceFile;
  private final boolean _isService;
  private LockingLazyVar<URI> _address = new LockingLazyVar<URI>() {
    @Override
    protected URI init() {
      try {
        return extractAddress( _wsdlDefPort );
      }
      catch ( URISyntaxException e ) {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }
  };
  protected static LockingLazyVar<IType> _wsdlConfigType = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.WsdlConfig" );
    }
  };
  protected static LockingLazyVar<IType> _wsdlPortType = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.IWsdlPort" );
    }
  };
  protected static LockingLazyVar<IType> _anyType = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xsd.w3c.xmlschema.types.complex.AnyType" );
    }
  };
  private final Map<WsdlBindingOperation, WsdlSoapHeadersTypeDataClass> _soapHeadersTypeDataByBindingOperation;


  /**
   * Create a new port definition
   *
   * @param name the name of this port
   * @param wsdlDefPort the Port element from the wsdl
   * @param wsdl the wsdl that this port is defined in
   * @param service  the service this port is on
   * @param resourceFile the resource file that contains this port
   * @param isService is service
   * @param soapHeadersTypeDataByBindingOperation
   */
  public WsdlPortTypeData( String name, WsdlPort wsdlDefPort, final Wsdl wsdl, WsdlService service, IFile resourceFile, boolean isService, Map<WsdlBindingOperation, WsdlSoapHeadersTypeDataClass> soapHeadersTypeDataByBindingOperation ) {
    _name = name;
    _wsdlDefPort = wsdlDefPort;
    _wsdl = wsdl;
    _service = service;
    _resourceFile = resourceFile;
    _isService = isService;
    _soapHeadersTypeDataByBindingOperation = soapHeadersTypeDataByBindingOperation;
    if ( getLogger().isDebugEnabled()) {
      getLogger().debug("WsdlPortTypeInfo created for " + name);
    }
  }

  public Wsdl getWsdl() {
    return _wsdl;
  }

  public ILogger getLogger() {
    return _wsdl.getLogger();
  }

  public void maybeInit() {
    if ( ! _initialized ) {
      _methods = new ArrayList<IMethodInfo>();
      _props = new ArrayList<IPropertyInfo>();
      addPortPropertiesAndMethods( this, _wsdl, _props, _methods, _wsdlDefPort );
      _ctors = makeConstructors();
      getLogger().debug("WsdlPortTypeInfo for " + _name + " properties=" + _props);
      getLogger().debug("WsdlPortTypeInfo for " + _name + " methods=" + _methods);
      _initialized = true;
    }
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public List<IPropertyInfo> getDeclaredProperties() {
    maybeInit();
    return _props;
  }

  @Override
  public List<IMethodInfo> getDeclaredMethods() {
    maybeInit();
    return _methods;
  }

  @Override
  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    maybeInit();
    return _ctors;
  }

  private List<IConstructorInfo> makeConstructors() {
    List<IConstructorInfo> list = new ArrayList<IConstructorInfo>();
    if ( getLogger().isDebugEnabled()) {
      getLogger().debug("WsdlServiceTypeInfo.getDeclaredConstructors for " + _wsdlDefPort.getQName());
    }

    final IType configIType = TypeSystem.getByFullName( "gw.xml.ws.WsdlConfig" );
    list.add(new ConstructorInfoBuilder()
            .withParameters(new ParameterInfoBuilder().withName("config").withDescription("This is the xml representing the configuration of this client").withType(configIType))
            .withConstructorHandler(new IConstructorHandler() {
              @Override
              public Object newInstance(Object... args) {
                return constructPort(args[0]);
              }
            }).build(this));
    list.add(new ConstructorInfoBuilder()
            .withConstructorHandler(new IConstructorHandler() {
                @Override
                public Object newInstance(Object... args) {
                  return constructPort( null );
               }
              }).build(this));
    if ( getLogger().isDebugEnabled() ) {
      getLogger().debug("WsdlServiceTypeInfo for " + _service.getQName() + " constructors=" + list);
    }
    return list;
  }

  private Object constructPort( Object arg ) {
    QName serviceQName = _service.getQName();
    QName portQName = _wsdlDefPort.getQName();
    IWsdlConfig config = (IWsdlConfig) arg;
    if ( config == null ) {
      config = (IWsdlConfig) _wsdlConfigType.get().getTypeInfo().getConstructor().getConstructor().newInstance();
    }
    configure( serviceQName, portQName, config );
    return new WsdlPortImpl( getType(), _resourceFile, serviceQName, getWsdl(), portQName, config, _wsdlDefPort );
  }

  public URI getAddress() {
    return _address.get();
  }

  public QName getPortName() {
    return _wsdlDefPort.getQName();
  }

  public QName getServiceName() {
    return _service.getQName();
  }

  @Override
  public boolean isFinal() {
    return true;
  }

  @Override
  public boolean isEnum() {
    return false;
  }

  @Override
  public IType getSuperType() {
    return JavaTypes.OBJECT();
  }

  @Override
  public boolean prefixSuperProperties() {
    return false;
  }

  @Override
  public long getFingerprint() {
    return 0; // TODO
  }

  @Override
  public Class getBackingClass() {
    return WsdlPortImpl.class;
  }

  @Override
  public IJavaClassInfo getBackingClassInfo() {
    return JavaTypes.getSystemType(WsdlPortImpl.class).getBackingClassInfo();
  }

  @Override
  public XmlSchemaIndex<?> getSchemaIndex() {
    return _wsdlDefPort.getSchemaIndex();
  }

  /** This will add ehte port properties and methods to either the port class or
   * the service class.
   *
   * @param container  the class that will contain these methods and properties
   * @param wsdl the wsdl in which the prot or service was defined
   * @param props the properties array for the prot or service
   * @param methods the methods array for the port or service
   * @param port the port Wsdl definition     @return the address for this port
   */
  protected void addPortPropertiesAndMethods( IFeatureInfo container, final Wsdl wsdl, final List<IPropertyInfo> props, final List<IMethodInfo> methods, final WsdlPort port ) {
    addMethodForOperations(container, methods, port, wsdl);
    addProperties( container, props );
    props.add( new PropertyInfoBuilder().withName( "ADDRESS" )
          .withType( TypeSystem.get( URI.class ) )
          .withWritable( false )
          .withStatic()
          .withAccessor( new IPropertyAccessor() {
            @Override
            public Object getValue( Object ctx ) {
              return getAddress();
            }

            @Override
            public void setValue( Object ctx, Object value ) {
              throw new UnsupportedOperationException();
            }
          } ).build(container));
    props.add( new PropertyInfoBuilder().withName( "PORT_QNAME" )
          .withType( TypeSystem.get( QName.class ) )
          .withWritable( false )
          .withStatic()
          .withAccessor( new IPropertyAccessor() {
            @Override
            public Object getValue( Object ctx ) {
              return getPortName();
            }
            @Override
            public void setValue( Object ctx, Object value ) {
              throw new UnsupportedOperationException();
            }
          } ).build(container));
    props.add( new PropertyInfoBuilder().withName( "SERVICE_QNAME" )
          .withType( TypeSystem.get( QName.class ) )
          .withWritable( false )
          .withStatic()
          .withAccessor( new IPropertyAccessor() {
            @Override
            public Object getValue( Object ctx ) {
              return getServiceName();
            }

            @Override
            public void setValue( Object ctx, Object value ) {
              throw new UnsupportedOperationException();
            }
          } ).build(container));
  }

  private void addProperties( IFeatureInfo container, List<IPropertyInfo> props ) {
    IType logType = TypeSystem.getJavaType( ILogger.class);
    props.add(new PropertyInfoBuilder().withName("Logger")
            .withLocation(getLocationInfo())
            .withType(logType)
            .withWritable(true)
            .withAccessor(new IPropertyAccessor() {
              @Override
              public Object getValue(Object ctx) {
                return ((WsdlPortImpl) ctx).getLogger();
              }
              @Override
              public void setValue(Object ctx, Object value) {
                ((WsdlPortImpl) ctx).setLogger((ILogger) value);
              }
            }).build(container));
    props.add(new PropertyInfoBuilder().withName("Config")
            .withLocation(getLocationInfo())
            .withType(TypeSystem.getByFullName("gw.xml.ws.WsdlConfig"))
            .withWritable(false)
            .withAccessor(new IPropertyAccessor() {
              @Override
              public Object getValue(Object ctx) {
                return ((WsdlPortImpl) ctx).getConfig();
              }

              @Override
              public void setValue(Object ctx, Object value) {
                throw new UnsupportedOperationException();
              }
            }).build(container));
    props.add( new PropertyInfoBuilder().withName( "PortQName" )
            .withLocation(getLocationInfo())
          .withType(TypeSystem.get(QName.class))
          .withWritable( false )
          .withAccessor( new IPropertyAccessor() {
            @Override
            public Object getValue( Object ctx ) {
              return ((WsdlPortImpl)ctx).getPortQName();
            }
            @Override
            public void setValue( Object ctx, Object value ) {
              throw new UnsupportedOperationException();
            }
          } ).build(container));
    props.add( new PropertyInfoBuilder().withName( "ServiceQName" )
          .withLocation(getLocationInfo())
          .withType(TypeSystem.get(QName.class))
          .withWritable( false )
          .withAccessor( new IPropertyAccessor() {
            @Override
            public Object getValue( Object ctx ) {
              return ((WsdlPortImpl)ctx).getServiceQName();
            }

            @Override
            public void setValue( Object ctx, Object value ) {
              throw new UnsupportedOperationException();
            }
          } ).build(container));
  }

  private void addMethodForOperations( IFeatureInfo container, List<IMethodInfo> methods, final WsdlPort wsdlPort, final Wsdl wsdl ) {
    WsdlBinding binding = wsdlPort.getBinding();
    WsdlPortType portType = binding.getPortType();
    final List<WsdlBindingOperation> list = binding.getBindingOperations();
    int incomingSize = methods.size();
    for (WsdlBindingOperation bindingOp : list) {
      final WsdlPortTypeOperation op = portType.getOperationByName( bindingOp.getName() );
      if (op == null) {
        getLogger().warn("On " + _service.getQName() + " operation=" + bindingOp.getName() + ": no operation in port");
        continue;
      }
      final WsdlBindingInput bindingInput = bindingOp.getBindingInput();
      final WsdlBindingOutput bindingOutput = bindingOp.getBindingOutput();
      final WsdlSoapBody soapBody = bindingInput == null ? null : bindingInput.getSoapBody();

      if ( bindingInput != null) {
        if (checkUseEncoded(bindingOp, soapBody )) {
          continue;
        }
      }
      if ( bindingOutput != null) {
        if (checkUseEncoded(bindingOp, bindingOutput.getSoapBody())) {
          continue;
        }
      }
      final String opName = op.getName();
      final WsdlPortTypeInput inputWsdl = op.getInput();
      final List<WsdlPart> inputParts = getIncludedParts( inputWsdl.getMessage().getParts(), soapBody );
      final String soapAction = getSoapAction(bindingOp);
      final WsdlOperationInputInfo input = convertInputParametersXMLType( op, inputParts, wsdl );
      if ( input == null ) {
        getLogger().warn("On " + _service.getQName() + " operation=" + bindingOp.getName() + ": cannot convert request parameters");
        continue; // skip this operation
      }
      WsdlOperationOutputInfo outputInfo = null;
      if ( op.getOutput() != null && bindingOutput != null ) {
        final WsdlPortTypeOutput outputWsdl = op.getOutput();
        final List<WsdlSoapHeader> outExtList = bindingOutput.getSoapHeaders();
        @SuppressWarnings({"unchecked"})
        final List<WsdlPart> outputParts = outputWsdl.getMessage().getParts();
        //noinspection LoopStatementThatDoesntLoop
        for ( WsdlSoapHeader el : outExtList ) {
          for (Iterator<WsdlPart> itr = outputParts.iterator(); itr.hasNext(); ) {
            WsdlPart part = itr.next();
            if (part.getName().equals(el.getPartName())) {
              itr.remove();
              break;
            }
          }
        }
        outputInfo = convertOutputToReturnType( op, outputParts, wsdl );
        if ( outputInfo == null ) {
          getLogger().warn("On " + _service.getQName() + " operation=" + bindingOp.getName() + ": cannot convert response");
          continue; // skip this operation
        }
      }
      final WsdlOperationInfo opTypeData = new WsdlOperationInfo( opName, soapAction, input, outputInfo, wsdlPort );

      final MethodInfoBuilder opBldr = new MethodInfoBuilder()
         .withLocation( getLocationInfo() )
         .withName( XmlSchemaIndex.normalizeName( opName, XmlSchemaIndex.NormalizationMode.PRESERVECASE ) );
      opBldr.withReturnType( outputInfo == null ? JavaTypes.pVOID() : outputInfo.getReturnType() );
      List<ParameterInfoBuilder> params = addParameters( input );

      // methodName( params... )
      opBldr.withParameters( params.toArray( new ParameterInfoBuilder[ params.size() ] ) );
      opBldr.withCallHandler(new IMethodCallHandler() {
        @Override
        public Object handleCall(Object ctx, Object... args) {
          return handleOperationCall((WsdlPortImpl) ctx, opTypeData, args);
        }
      });
      methods.add(opBldr.build(container));

      // methodName( params..., soapheadersobject )
      final WsdlSoapHeadersTypeDataClass wsdlSoapHeadersTypeData = _soapHeadersTypeDataByBindingOperation.get( bindingOp );
      if ( wsdlSoapHeadersTypeData != null ) {
        opBldr.withParameters( addHeadersToParams(wsdlSoapHeadersTypeData, params) );
        opBldr.withCallHandler( new IMethodCallHandler() {
          @Override
          public Object handleCall( Object ctx, Object... args ) {
            return handleOperationCall( (WsdlPortImpl)ctx, opTypeData, args );
          }
        } );
        methods.add( opBldr.build( container ) );
      }
      addAsyncOperationToMethods(container, methods, opName, opTypeData, params, _wsdlDefPort.getBinding().getSoapBinding().getSoapVersion(), wsdlSoapHeadersTypeData);
    }

    if (incomingSize == methods.size()) {
      getLogger().warn("On " + _service.getQName() + ": no supported operations");
    }

    final MethodInfoBuilder opBldr = new MethodInfoBuilder()
            .withLocation( getLocationInfo() )
            .withName("document_literal");
    opBldr.withReturnType( TypeSystem.get( XmlElement.class ) );
    opBldr.withParameters( new ParameterInfoBuilder().withName( "document" ).withType( XmlElement.class ) );
    opBldr.withCallHandler(new IMethodCallHandler() {
                    @Override
                    public Object handleCall(Object ctx, Object... args) {
                      return handleOperationCall( (WsdlPortImpl)ctx, null, args );
                    }
                  });
    methods.add(opBldr.build(container));

    final MethodInfoBuilder opAsyncBldr = new MethodInfoBuilder()
            .withLocation( getLocationInfo() )
            .withName("async_document_literal");
    opAsyncBldr.withReturnType( WsdlPortImpl.getParameterizedAsyncResponseType( null, _wsdlDefPort.getBinding().getSoapBinding().getSoapVersion(), false ) );
    opAsyncBldr.withParameters( new ParameterInfoBuilder().withName( "document" ).withType( XmlElement.class ) );
    opAsyncBldr.withCallHandler(new IMethodCallHandler() {
                    @Override
                    public Object handleCall(Object ctx, Object... args) {
                      return handleOperationAsyncCall((WsdlPortImpl)ctx, null, args );
                    }
                  });
    methods.add(opAsyncBldr.build(container));
  }

  private List<WsdlPart> getIncludedParts( List<WsdlPart> parts, WsdlSoapBody soapBody ) {
    if ( soapBody == null || soapBody.getParts() == null ) {
      return parts;
    }
    List<WsdlPart> includedParts = new ArrayList<WsdlPart>( parts.size() );
    for ( WsdlPart part : parts ) {
      if ( soapBody.getParts().contains( part.getName() ) ) {
        includedParts.add( part );
      }
    }
    return includedParts;
  }

  private boolean checkUseEncoded(WsdlBindingOperation bindingOp, WsdlSoapBody body) {
    if (body != null) {
      if ("encoded".equals(body.getUse())) {
        getLogger().warn("On " + _service.getQName() + " operation=" + bindingOp.getName() + ": use=\"encoded\" not supported");
        return true;
      }
    }
    return false;
  }

  // Add an async method to the methods list
  private void addAsyncOperationToMethods( IFeatureInfo container,
                                           List<IMethodInfo> methods,
                                           final String opName,
                                           final WsdlOperationInfo opTypeData,
                                           List<ParameterInfoBuilder> params,
                                           final SoapVersion soapVersion,
                                           WsdlSoapHeadersTypeDataClass wsdlSoapHeadersTypeData) {

    final MethodInfoBuilder opAsyncBldr = new MethodInfoBuilder()
            .withLocation( getLocationInfo() )
            .withName("async_" + XmlSchemaIndex.normalizeName(opName, XmlSchemaIndex.NormalizationMode.PRESERVECASE));
    opAsyncBldr.withReturnType(WsdlPortImpl.getParameterizedAsyncResponseType(opTypeData, soapVersion, false));

    // methodName( params... )
    opAsyncBldr.withParameters(params.toArray(new ParameterInfoBuilder[params.size()]));
    opAsyncBldr.withCallHandler(new IMethodCallHandler() {
      @Override
      public Object handleCall(Object ctx, Object... args) {
        return handleOperationAsyncCall((WsdlPortImpl) ctx, opTypeData, args);
      }
    });
    methods.add(opAsyncBldr.build(container));

    // methodName( params..., soapheadersobject )
    if ( wsdlSoapHeadersTypeData != null ) {
      opAsyncBldr.withParameters(addHeadersToParams(wsdlSoapHeadersTypeData, params));
      opAsyncBldr.withCallHandler( new IMethodCallHandler() {
        @Override
        public Object handleCall( Object ctx, Object... args ) {
          return handleOperationAsyncCall((WsdlPortImpl) ctx, opTypeData, args );
        }
      } );
      methods.add( opAsyncBldr.build( container ) );
    }
  }

  private ParameterInfoBuilder[] addHeadersToParams(WsdlSoapHeadersTypeDataClass wsdlSoapHeadersTypeData, List<ParameterInfoBuilder> paramsSource) {
    List<ParameterInfoBuilder> params = new ArrayList<ParameterInfoBuilder>(paramsSource);
    params.add(new ParameterInfoBuilder()
        .withName("soapheaders")
        .withType(wsdlSoapHeadersTypeData.getType())
    );
    return params.toArray(new ParameterInfoBuilder[params.size()]);
  }

  protected Object handleOperationCall(WsdlPortImpl ctx, WsdlOperationInfo opTypeData, Object[] args) {
    return ctx.invoke(opTypeData, args);
  }

  protected Object handleOperationAsyncCall(WsdlPortImpl ctx, WsdlOperationInfo opTypeData, Object[] args) {
    return ctx.invokeAsync( opTypeData, args );
  }

  private static List<ParameterInfoBuilder> addParameters( WsdlOperationInputInfo input ) {
    List<ParameterInfoBuilder> params = new ArrayList<ParameterInfoBuilder>();
    for ( Pair<WsdlOperationParameterInfo,Boolean> param : input.getParameterInfos() ) {
      IType type = param.getFirst().getType();
      if ( param.getSecond() ) {
        type = JavaTypes.LIST().getGenericType().getParameterizedType( type );
      }
      params.add( new ParameterInfoBuilder().withName( param.getFirst().getName() ).withType( type ) );
    }
    return params;
  }

  private static String getSoapAction( WsdlBindingOperation bindingOp ) {
    WsdlSoapOperation wsdlSoapOperation = bindingOp.getSoapOperation();
    return wsdlSoapOperation == null ? "" : wsdlSoapOperation.getSoapAction();
  }

  private static WsdlOperationOutputInfo convertOutputToReturnType( WsdlPortTypeOperation op, List<WsdlPart> parts, final Wsdl wsdl ) {
    WsdlOperationOutputInfo outputInfo = null;
    if ( parts.size() == 1 ) { // not a single part = not unwrapable
      WsdlPart part = parts.get( 0 );
      QName elementName = part.getElementName();
      IXmlType type = (IXmlType) part.getSchemaIndex().getGosuTypeByXmlSchemaElementQName( elementName );
      IXmlSchemaElementTypeData typeData = (IXmlSchemaElementTypeData) type.getTypeData();
      if ( hasNoAnys( typeData ) ) {
        outputInfo = getUnwrappedReturnTypeIfPossible(op, (Wsdl) part.getSchemaIndex(), outputInfo, elementName, type);
      }
      if ( outputInfo == null ) {
        try {
          outputInfo = new WsdlOperationOutputInfo( type, elementName ); // fall back to just returning the Response element
          outputInfo.setUseParentElement( true );          
        }
        catch ( AnonymousElementException ex ) {
          throw new RuntimeException( "On '" + op.getName() + "'", ex ); // should never happen since this is a top-level element assigned to a part
        }
      }
      return outputInfo;
    }
    else {
      wsdl.getLogger().warn( "In WSDL " + wsdl.getPackageName() + ", operation " + op.getName() + " was ignored since multiple output parts are not supported" );
      return null;
    }
  }

  private static WsdlOperationOutputInfo getUnwrappedReturnTypeIfPossible(WsdlPortTypeOperation op, Wsdl wsdl, WsdlOperationOutputInfo outputInfo, QName elementName, IXmlType type) {
    try {
      WsdlOperationOutputInfo unwrappedOutputInfo = new WsdlOperationOutputInfo( type, elementName ); // in case we are able to unwrap request
      String operationName = op.getName() + "Response";
      if ( operationName.equals( elementName.getLocalPart() ) ) { // operation name + "Response" has to equal element name for unwrapable
        XmlSchemaTypeSchemaInfo schemaInfo = wsdl.getElementInfoByXmlSchemaElementQName( elementName );
        if (!isUnwrappable(schemaInfo)) {
          return null;
        }
        if ( schemaInfo.getProperties().size() <= 1 ) {
          unwrappedOutputInfo.addUnwrapLevel( type, elementName ); // unwrap the Response element
          unwrappedOutputInfo.setSchemaInfo( schemaInfo );
          outputInfo = unwrappedOutputInfo;
        }
      }
      if ( outputInfo != null ) {
        if ( outputInfo.getSchemaInfo().getProperties().size() > 0 ) {
          // we're now totally unwrapped to the level where the parameters are (only 1 parameter in case of response)
          XmlSchemaPropertySpec firstProperty = outputInfo.getSchemaInfo().getProperties().get( 0 );
          WsdlOperationParameterInfo paramInfo = WsdlOperationParameterInfo.create( firstProperty );
          if ( firstProperty.isPlural() ) {
            outputInfo = new WsdlOperationOutputInfo( type, elementName );
            outputInfo.setReturnParameterInfo( new ArrayWsdlOperationParameterInfo( type, elementName, paramInfo ) );
            outputInfo.setUseParentElement( true );
          }
          else {
            outputInfo.setReturnParameterInfo( paramInfo );
          }
        }
        else {
          outputInfo.setReturnParameterInfo( null );
        }
      }
    }
    catch ( AnonymousElementException ignored ) {
      outputInfo = null;
    }
    return outputInfo;
  }

  private static boolean hasNoAnys( IXmlSchemaElementTypeData typeData ) {
    XmlSchemaTypeSchemaInfo schemaInfo = typeData.getSchemaInfo();
    XmlSchemaType xsdType = schemaInfo.getXsdType();
    return schemaInfo.getAttributeNames().isEmpty() && xsdType.getAnyRecursiveIncludingSupertypes() == null && xsdType.getAnyAttributeRecursiveIncludingSupertypes() == null;
  }

  private static WsdlOperationInputInfo convertInputParametersXMLType( WsdlPortTypeOperation op, List<WsdlPart> parts, final Wsdl wsdl) {
    WsdlOperationInputInfo inputInfo;
    if ( parts.size() == 1 ) { // not a single part = not unwrapable
      WsdlPart part = parts.get( 0 );
      QName elementName = part.getElementName();
      IXmlType type = (IXmlType) part.getSchemaIndex().getGosuTypeByXmlSchemaElementQName( elementName );
      IXmlSchemaElementTypeData typeData = (IXmlSchemaElementTypeData) type.getTypeData();
      inputInfo = getUnwrappedParametersIfPossible(op, (Wsdl) part.getSchemaIndex(), elementName, type, typeData);
      if ( inputInfo == null ) {
        try {
          inputInfo = new WsdlOperationInputInfo( type, elementName ); // fall back to just returning the Response element
        }
        catch ( AnonymousElementException ex ) {
          throw new RuntimeException( "On '" + op.getName() + "'", ex ); // should never happen since this is a top-level element assigned to a part
        }
      }
      return inputInfo;
    }
    else {
      wsdl.getLogger().warn( "In WSDL " + wsdl.getPackageName() + ", operation " + op.getName() + " was ignored since multiple input parts are not supported" );
      return null;
    }
  }

  private static WsdlOperationInputInfo getUnwrappedParametersIfPossible(WsdlPortTypeOperation op, Wsdl wsdl, QName elementName, IXmlType type, IXmlSchemaElementTypeData typeData) {
    WsdlOperationInputInfo inputInfo = null;
    if ( hasNoAnys( typeData ) ) {
      try {
        String operationName = op.getName();
        if ( operationName.equals( elementName.getLocalPart() ) ) { // operation name has to equal element name for unwrapable
          inputInfo = new WsdlOperationInputInfo( type, elementName ); // in case we are able to unwrap request
          XmlSchemaTypeSchemaInfo schemaInfo = wsdl.getElementInfoByXmlSchemaElementQName( elementName );
          inputInfo.setSchemaInfo( schemaInfo );
          if (!isUnwrappable(schemaInfo)) {
            return null;
          }
          // we're now totally unwrapped to the level where the parameters are
          List<Pair<WsdlOperationParameterInfo,Boolean>> paramInfos = new ArrayList<Pair<WsdlOperationParameterInfo, Boolean>>( inputInfo.getSchemaInfo().getProperties().size() );
          for ( XmlSchemaPropertySpec propertySpec : inputInfo.getSchemaInfo().getProperties() ) {
            paramInfos.add( new Pair<WsdlOperationParameterInfo, Boolean>( WsdlOperationParameterInfo.create( propertySpec ), propertySpec.isPlural() ) );
          }
          inputInfo.setParameterInfos( paramInfos );
        }
      }
      catch ( AnonymousElementException ignored ) {
        return null;
      }
    }
    return inputInfo;
  }

  private static boolean isUnwrappable(XmlSchemaTypeSchemaInfo schemaInfo) {
    if (!(schemaInfo.getXsdType() instanceof XmlSchemaComplexType)) {
      return false;
    }
    if (schemaInfo.isMixed()) {
      return false;
    }
    XmlSchemaComplexType complexSchemaInfo = (XmlSchemaComplexType) schemaInfo.getXsdType();
    XmlSchemaContentModel model = complexSchemaInfo.getContentModel();
    if (!(model instanceof XmlSchemaComplexContent)) {
      return false;
    }
    XmlSchemaComplexContent complexContent = (XmlSchemaComplexContent) model;
    if (!(complexContent.getContent() instanceof XmlSchemaComplexContentRestriction)) {
      return false;
    }
    XmlSchemaComplexContentRestriction contentRestriction = (XmlSchemaComplexContentRestriction) complexContent.getContent();

    return contentRestriction.getBaseTypeName().equals(AnyType.$QNAME);
  }

  protected URI extractAddress( WsdlPort port ) throws URISyntaxException {
    URI uri = null;
    if (port != null) {
      String locationString = port.getLocation();
      if ( locationString != null ) {
        uri = new URI( locationString );
      }
      locationString = getOverrideAddress();
      WsdlGwAddress gwAddress = port.getGwAddress();
      if ( locationString == null && gwAddress != null ) {
        locationString = gwAddress.getLocation();
      }
      if ( locationString != null ) {
        URI gwUri = WsiAdditions.getInstance().substituteProductCode( locationString );
        if ( gwUri != null ) {
          uri = gwUri;
        }
      }
    }
    return uri;
  }

  protected String getOverrideAddress() {
    return null;
  }

  protected void configure( QName serviceName, QName portName, IWsdlConfig config ) {
  }

  @Override
  public List<Class<?>> getAdditionalInterfaces() {
    return Arrays.asList(IWsdlPortTypeData.class, ILocationAwareFeature.class);
  }

  @Override
  public boolean isService() {
    return _isService;
  }

  public WsdlPort getWsdlDefPort() {
    return _wsdlDefPort;
  }

  @Override
  public List<? extends IType> getInterfaces() {
    List<IType> list = new ArrayList<IType>(super.getInterfaces());
    list.add(_wsdlPortType.get());
    return list;
  }

  @Override
  public LocationInfo getLocationInfo() {
    return _wsdlDefPort.getLocationInfo();
  }
}
