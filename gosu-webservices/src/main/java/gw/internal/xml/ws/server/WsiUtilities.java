/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import gw.internal.schema.gw.xsd.w3c.wsdl.Definitions;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TBindingOperation_Fault;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TBindingOperation_Input;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TBindingOperation_Output;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TBinding_Operation;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TDefinitions_Binding;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TDefinitions_Message;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TDefinitions_PortType;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TDefinitions_Service;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TDefinitions_Types;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TDocumented_Documentation;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TMessage_Part;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TOperation_Fault;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TOperation_Input;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TOperation_Output;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TPortType_Operation;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TService_Port;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Element;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Schema;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Sequence;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ExplicitGroup_Element;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.TopLevelElement_ComplexType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.enums.FormChoice;
import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.server.marshal.MarshalInfo;
import gw.internal.xml.XmlSchemaAccessImpl;
import gw.internal.xml.ws.server.marshal.ClassBasedMarshalInfo;
import gw.internal.xml.ws.server.marshal.XmlMarshaller;
import gw.lang.function.Function2;
import gw.lang.function.Function3;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.XmlNamespace;
import gw.xml.XmlSchemaAccess;
import gw.xml.XmlSerializationOptions;
import gw.xml.ws.WebServiceException;
import gw.xml.ws.WsiAuthenticationException;

import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;

public class WsiUtilities {

  public static final LockingLazyVar<IType> WSI_WEB_SERVICE_ANNOTATION_TYPE = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName("gw.xml.ws.annotation.WsiWebService");
    }
  };

  public static final LockingLazyVar<IType> WSI_SCHEMA_TRANSFORM_ANNOTATION_TYPE = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiSchemaTransform" );
    }
  };

  public static final LockingLazyVar<IType> WEB_METHOD_ANNOTATION_TYPE = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiWebMethod" );
    }
  };

  public static final LockingLazyVar<IType> WSI_ADDITIONAL_SCHEMAS_ANNOTATION_TYPE = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiAdditionalSchemas" );
    }
  };
  private static final LockingLazyVar<IType> WSI_INVOCATION_CONTEXT_TYPE = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.WsiInvocationContext" );
    }
  };
  private static final LockingLazyVar<IType> WSI_EXPOSE_ENUM_AS_STRING_ANNOTATION_TYPE = new LockingLazyVar<IType>( TypeSystem.getGlobalLock() ) {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xml.ws.annotation.WsiExposeEnumAsString" );
    }
  };

  private static final Comparator<IType> COMPARATOR = new Comparator<IType>() {
    @Override
    public int compare(IType o1, IType o2) {
      return o1.getRelativeName().compareTo(o2.getRelativeName());
    }
  };
  public static final LockingLazyVar<Collection<IType>> EXCEPTIONS_THROWN_BY_INFRASTRUCTURE = new LockingLazyVar<Collection<IType>>( TypeSystem.getGlobalLock() ) {
    @Override
    protected Collection<IType> init() {
      TreeSet<IType> rtn = new TreeSet<IType>(COMPARATOR);
      rtn.add(TypeSystem.get(WsiAuthenticationException.class));
      return rtn;
    }
  };

  public static WsiServiceInfo generateWsdl( IType type, String requestURL, String xsdRootURL ) {
    return generateWsdl( type, requestURL, xsdRootURL, WebservicesServletBase.getDefaultLocalWebservicesServlet() );
  }

  public static WsiServiceInfo generateWsdl( IType type, String requestURL, String xsdRootURL, WebservicesServletBase servlet ) {
    if (xsdRootURL == null) {
      xsdRootURL = "";
    }
    IType wsiInvocationContextType = WSI_INVOCATION_CONTEXT_TYPE.get();
    Definitions wsdl = new Definitions();
    wsdl.setComment( "Generated WSDL for " + type.getName() + " web service" );
    try {
      type.isValid();
      Set<IType> exposeEnumAsStringTypes = new HashSet<IType>();
      for ( IAnnotationInfo wsiExposeEnumAsStringAnnotationInfo : type.getTypeInfo().getAnnotationsOfType( WSI_EXPOSE_ENUM_AS_STRING_ANNOTATION_TYPE.get() ) ) {
        IWsiExposeEnumAsString exposeEnumAsStringAnnotation = (IWsiExposeEnumAsString)wsiExposeEnumAsStringAnnotationInfo.getInstance();
        exposeEnumAsStringTypes.add( exposeEnumAsStringAnnotation.getType() );
      }
      IAnnotationInfo wsiWebServiceAnnotationInfo = type.getTypeInfo().getAnnotationsOfType( WSI_WEB_SERVICE_ANNOTATION_TYPE.get() ).get( 0 );
      IWsiWebService clsAnnot = (IWsiWebService)wsiWebServiceAnnotationInfo.getInstance();
      wsdl.declareNamespace( gw.internal.schema.gw.xsd.gw.gw_pl_wsdl_additions.Address.$QNAME );
      String namespace = XmlServices.createTargetNamespace( type );
      QName serviceName = new QName(namespace, clsAnnot.getServiceNameLocalPart() == null ? type.getRelativeName() : clsAnnot.getServiceNameLocalPart());
      XmlNamespace tns = new XmlNamespace( serviceName.getNamespaceURI(), "tns" );
      wsdl.setTargetNamespace$( new URI(serviceName.getNamespaceURI()  ) );
      wsdl.declareNamespace( wsdl.TargetNamespace(), "tns" );
      wsdl.declareNamespace( new URI( gw.internal.schema.gw.xsd.w3c.soap11.Binding.$QNAME.getNamespaceURI() ), "soap11" );
      wsdl.declareNamespace( new URI( gw.internal.schema.gw.xsd.w3c.soap12.Binding.$QNAME.getNamespaceURI() ), "soap12" );
      wsdl.setName$( serviceName.getLocalPart() );

      Schema schema = new Schema();
      schema.setTargetNamespace$(wsdl.TargetNamespace());
      schema.setElementFormDefault$(FormChoice.Qualified);
      schema.declareNamespace( wsdl.TargetNamespace(), "tns" );

      WsiServiceInfo serviceInfo = new WsiServiceInfo( wsdl, schema, xsdRootURL, type, exposeEnumAsStringTypes );

      List<IAnnotationInfo> parseOptionsAnnotations = type.getTypeInfo().getAnnotationsOfType( WSI_ADDITIONAL_SCHEMAS_ANNOTATION_TYPE.get() );
      if ( ! parseOptionsAnnotations.isEmpty() ) {
        IAnnotationInfo parseOptionsAnnotationInfo = parseOptionsAnnotations.get( 0 );
        IWsiAdditionalSchemas additionalSchemasAnnotation = (IWsiAdditionalSchemas)parseOptionsAnnotationInfo.getInstance();
        for ( XmlSchemaAccess additionalSchema : additionalSchemasAnnotation.getAdditionalSchemas() ) {
          XmlSchemaAccessImpl impl = (XmlSchemaAccessImpl) additionalSchema;
          XmlMarshaller.findOrImportSchema( impl.getSchemaIndex(), serviceInfo, null );
        }
      }

      wsdl.Types().add( new TDefinitions_Types() );
      wsdl.Types().get( 0 ).addChild( schema );

      TDefinitions_PortType portType = new TDefinitions_PortType();
      portType.setName$( serviceName.getLocalPart() + "PortType" );
      wsdl.PortType().add( portType );

      TDefinitions_Binding soap12WsdlBinding = new TDefinitions_Binding();
      soap12WsdlBinding.setName$( serviceName.getLocalPart() + "Soap12Binding" );
      soap12WsdlBinding.setType$( tns.qualify( serviceName.getLocalPart() + "PortType" ) );
      wsdl.Binding().add( soap12WsdlBinding );

      gw.internal.schema.gw.xsd.w3c.soap12.Binding soap12Binding = new gw.internal.schema.gw.xsd.w3c.soap12.Binding();
      soap12Binding.setStyle$( gw.internal.schema.gw.xsd.w3c.soap12.enums.TStyleChoice.Document );
      soap12Binding.setTransport$( new URI( "http://schemas.xmlsoap.org/soap/http" ) );
      soap12WsdlBinding.addChild( soap12Binding );

      TDefinitions_Binding soap11WsdlBinding = new TDefinitions_Binding();
      soap11WsdlBinding.setName$( serviceName.getLocalPart() + "Soap11Binding" );
      soap11WsdlBinding.setType$( tns.qualify( serviceName.getLocalPart() + "PortType" ) );
      wsdl.Binding().add( soap11WsdlBinding );

      gw.internal.schema.gw.xsd.w3c.soap11.Binding soap11Binding = new gw.internal.schema.gw.xsd.w3c.soap11.Binding();
      soap11Binding.setStyle$( gw.internal.schema.gw.xsd.w3c.soap11.enums.TStyleChoice.Document );
      soap11Binding.setTransport$( new URI( "http://schemas.xmlsoap.org/soap/http" ) );
      soap11WsdlBinding.addChild( soap11Binding );

      TDefinitions_Service service = new TDefinitions_Service();
      service.setName$( serviceName.getLocalPart() );

      TService_Port soap12Port = new TService_Port();
      soap12Port.setName$( serviceName.getLocalPart() + "Soap12Port" );
      soap12Port.setBinding$( tns.qualify( serviceName.getLocalPart() + "Soap12Binding" ) );
      service.Port().add( soap12Port );
      gw.internal.schema.gw.xsd.w3c.soap12.Address soap12Address = new gw.internal.schema.gw.xsd.w3c.soap12.Address();
      soap12Address.setLocation$( new URI( requestURL ) );
      soap12Port.addChild( soap12Address );

      TService_Port soap11Port = new TService_Port();
      soap11Port.setName$( serviceName.getLocalPart() + "Soap11Port" );
      soap11Port.setBinding$( tns.qualify( serviceName.getLocalPart() + "Soap11Binding" ) );
      service.Port().add( soap11Port );
      gw.internal.schema.gw.xsd.w3c.soap11.Address soap11Address = new gw.internal.schema.gw.xsd.w3c.soap11.Address();
      soap11Address.setLocation$( new URI( requestURL + "/soap11" ) );
      soap11Port.addChild( soap11Address );

      wsdl.Service().add( service );

      Set<String> seenElements = new HashSet<String>();

      // Iterate each method and check the args+return type
      for ( IMethodInfo m : ( (IGosuClassTypeInfo) type.getTypeInfo() ).getDeclaredMethods() ) {
        List<IAnnotationInfo> annotations = m.getAnnotationsOfType( WEB_METHOD_ANNOTATION_TYPE.get() );
        if ( annotations.size() > 1 ) {
          throw new RuntimeException( "Multiple @WsiWebMethod annotations found on method " + m.getName() + " on class " + type.getName() );
        }
        IWsiWebMethod annotation = annotations.size() == 1 ? (IWsiWebMethod) annotations.get( 0 ).getInstance() : null;
        if ( isOperation( m, annotation ) ) {
          createWsdlForOperation( m, annotation, tns, serviceInfo, soap12WsdlBinding, soap11WsdlBinding, seenElements, wsiInvocationContextType );
        }
      }

      servlet.postConfigureWebservice( type, serviceInfo );

      // Processing this annotation needs to be the last thing that occurs before the WSDL is returned
      for ( IAnnotationInfo wsiSchemaTransformAnnotationInfo : type.getTypeInfo().getAnnotationsOfType( WSI_SCHEMA_TRANSFORM_ANNOTATION_TYPE.get() ) ) {
        Object schemaTransformAnnotation = wsiSchemaTransformAnnotationInfo.getInstance();
        Function3 transform3 = (Function3) WSI_SCHEMA_TRANSFORM_ANNOTATION_TYPE.get().getTypeInfo().getProperty( "GWTransform" ).getAccessor().getValue( schemaTransformAnnotation );
        if (transform3 != null ) {
          transform3.invoke( wsdl, schema, servlet.postConfigureWebservicePath(type, xsdRootURL) );
        }
        else {
          Function2 transform2 = (Function2) WSI_SCHEMA_TRANSFORM_ANNOTATION_TYPE.get().getTypeInfo().getProperty( "Transform" ).getAccessor().getValue( schemaTransformAnnotation );
          transform2.invoke( wsdl, schema );
        }
      }

      return serviceInfo;
    }
    catch ( Exception ex ) {
      XmlServices.getLogger( XmlServices.Category.Runtime )
        .debug("On " + type + " " + wsdl.asUTFString( XmlSerializationOptions.debug() ) + " caught " + ex);
      throw GosuExceptionUtil.forceThrow( ex );
    }
  }

  public static boolean isOperation( IMethodInfo method, IWsiWebMethod annotation ) {
    return method.isPublic() && !method.isHidden() && !method.getName().startsWith("@") && !method.isStatic() && ( annotation == null || !annotation.isExclude() );
  }

  private static void createWsdlForOperation(
          IMethodInfo m,
          IWsiWebMethod webmethodAnnotation,
          XmlNamespace tns,
          WsiServiceInfo serviceInfo,
          TDefinitions_Binding soap12WsdlBinding,
          TDefinitions_Binding soap11WsdlBinding,
          Set<String> seenElements, IType wsiInvocationContextType ) throws Exception {
    String name = makeUniqueName( m.getDisplayName(), seenElements );
    if ( webmethodAnnotation != null && webmethodAnnotation.getOperationName() != null ) {
      name = webmethodAnnotation.getOperationName();
    }

    QName operationQName = tns.qualify( name );

    IMethodInfo oldMethod = serviceInfo.getOriginalMethods().put( operationQName, m );
    if ( oldMethod != null ) {
      throw new WebServiceException( "Multiple methods found with the name " + name );
    }

    Map<QName,MarshalInfo> paramMarshalInfoMap = serviceInfo.getMarshalInfoMap().get( operationQName );
    if ( paramMarshalInfoMap == null ) {
      paramMarshalInfoMap = new HashMap<QName, MarshalInfo>();
      serviceInfo.getMarshalInfoMap().put( operationQName, paramMarshalInfoMap );
    }

    // create port operation
    TPortType_Operation portTypeOperation = new TPortType_Operation();
    if (m.isDeprecated()) {
      TDocumented_Documentation documentation = new TDocumented_Documentation();
      documentation.setText("@deprecated: " + m.getDeprecatedReason());
      portTypeOperation.setDocumentation$(documentation);
    }
    portTypeOperation.setName$( name );
    portTypeOperation.setInput$( new TOperation_Input() );
    portTypeOperation.Input().setName$( name );
    portTypeOperation.Input().setMessage$( tns.qualify( name ) );
    serviceInfo.getWsdl().PortType().get( 0 ).Operation().add( portTypeOperation );

    // create the binding to operation
    TBinding_Operation soap12BindingOp = new TBinding_Operation();
    soap12BindingOp.setName$( name );
    gw.internal.schema.gw.xsd.w3c.soap12.Operation soap12Operation = new gw.internal.schema.gw.xsd.w3c.soap12.Operation();
    soap12Operation.setStyle$( gw.internal.schema.gw.xsd.w3c.soap12.enums.TStyleChoice.Document );
    soap12Operation.setSoapActionRequired$(Boolean.FALSE);
    soap12BindingOp.addChild( soap12Operation );
    soap12BindingOp.setInput$( new TBindingOperation_Input() );
    soap12BindingOp.Input().setName$( name );
    gw.internal.schema.gw.xsd.w3c.soap12.Body soap12Body = new gw.internal.schema.gw.xsd.w3c.soap12.Body();
    soap12Body.setUse$( gw.internal.schema.gw.xsd.w3c.soap12.enums.UseChoice.Literal );
    soap12BindingOp.Input().addChild( soap12Body );
    soap12WsdlBinding.Operation().add( soap12BindingOp );

    // create the binding to operation
    TBinding_Operation soap11BindingOp = new TBinding_Operation();
    soap11BindingOp.setName$( name );
    gw.internal.schema.gw.xsd.w3c.soap11.Operation soap11Operation = new gw.internal.schema.gw.xsd.w3c.soap11.Operation();
    soap11Operation.setStyle$( gw.internal.schema.gw.xsd.w3c.soap11.enums.TStyleChoice.Document );
    soap11BindingOp.addChild( soap11Operation );
    soap11BindingOp.setInput$( new TBindingOperation_Input() );
    soap11BindingOp.Input().setName$( name );
    gw.internal.schema.gw.xsd.w3c.soap11.Body soap11Body = new gw.internal.schema.gw.xsd.w3c.soap11.Body();
    soap11Body.setUse$( gw.internal.schema.gw.xsd.w3c.soap11.enums.UseChoice.Literal );
    soap11BindingOp.Input().addChild( soap11Body );
    soap11WsdlBinding.Operation().add( soap11BindingOp );

    // Input
    TDefinitions_Message reqMsg = new TDefinitions_Message();
    reqMsg.setName$( name );
    serviceInfo.getWsdl().Message().add( reqMsg );
    reqMsg.Part().add( new TMessage_Part() );
    reqMsg.Part().get( 0 ).setElement$( tns.qualify( name ) );
    reqMsg.Part().get( 0 ).setName$( "parameters" );
    Element element = new Element();
    element.setName$( name );
    element.setComment( makeMethodSignatureForXmlComment( m )  );
    boolean addedGWXsd = false;
    element.setComplexType$( new TopLevelElement_ComplexType() );
    if ( m.getParameters().length > 0 ) {
      for ( IParameterInfo param : m.getParameters() ) {
        if ( param.getFeatureType().equals( wsiInvocationContextType ) ) {
          continue; // doesn't appear in WSDL
        }
        if ( element.ComplexType().Sequence() == null ) {
          element.ComplexType().setSequence$( new Sequence() );
        }
        ExplicitGroup_Element typeEl = new ExplicitGroup_Element();
        String paramName = param.getName();
        typeEl.setName$( paramName );
        if (!addedGWXsd && JavaTypes.DATE().isAssignableFrom(param.getFeatureType())) {
          ClassBasedMarshalInfo.addGWNamespaceIfNotAlreadyPresent(serviceInfo.getSchema());
          addedGWXsd = true;
        }
        MarshalInfo marshalInfo = XmlMarshaller.addType( param.getFeatureType(), typeEl.getTypeInstance(), serviceInfo );
        element.ComplexType().Sequence().Element().add( typeEl );
        paramMarshalInfoMap.put( tns.qualify( paramName ), marshalInfo );
      }
    }

    serviceInfo.getSchema().Element().add( element );

    // Output
    String responseName = name + "Response";

    soap12BindingOp.setOutput$( new TBindingOperation_Output() );
    soap12BindingOp.Output().setName$( responseName );
    gw.internal.schema.gw.xsd.w3c.soap12.Body soap12ResponseBody = new gw.internal.schema.gw.xsd.w3c.soap12.Body();
    soap12ResponseBody.setUse$( gw.internal.schema.gw.xsd.w3c.soap12.enums.UseChoice.Literal );
    soap12BindingOp.Output().addChild( soap12ResponseBody );

    soap11BindingOp.setOutput$( new TBindingOperation_Output() );
    soap11BindingOp.Output().setName$( responseName );
    gw.internal.schema.gw.xsd.w3c.soap11.Body soap11ResponseBody = new gw.internal.schema.gw.xsd.w3c.soap11.Body();
    soap11ResponseBody.setUse$( gw.internal.schema.gw.xsd.w3c.soap11.enums.UseChoice.Literal );
    soap11BindingOp.Output().addChild( soap11ResponseBody );

    portTypeOperation.setOutput$( new TOperation_Output() );
    portTypeOperation.Output().setName$( responseName );
    portTypeOperation.Output().setMessage$( tns.qualify( responseName ) );
    TDefinitions_Message respMsg = new TDefinitions_Message();
    respMsg.setName$( responseName );
    serviceInfo.getWsdl().Message().add( respMsg );
    respMsg.Part().add( new TMessage_Part() );
    respMsg.Part().get( 0 ).setElement$( tns.qualify( responseName ) );
    respMsg.Part().get( 0 ).setName$( "parameters" );
    element = new Element();
    element.setName$( responseName );

    element.setComplexType$( new TopLevelElement_ComplexType() );
    if ( !JavaTypes.pVOID().equals( m.getReturnType() ) ) {
      element.ComplexType().setSequence$( new Sequence() );
      ExplicitGroup_Element typeEl = new ExplicitGroup_Element();
      if (!addedGWXsd && JavaTypes.DATE().isAssignableFrom(m.getReturnType())) {
        ClassBasedMarshalInfo.addGWNamespaceIfNotAlreadyPresent(serviceInfo.getSchema());
      }
      typeEl.setName$( "return" );
      XmlMarshaller.addType( m.getReturnType(), typeEl.getTypeInstance(), serviceInfo );
      element.ComplexType().Sequence().Element().add( typeEl );
    }

    serviceInfo.getSchema().Element().add( element );
    TreeSet<IType> expected = new TreeSet<IType>(COMPARATOR);
    expected.addAll( EXCEPTIONS_THROWN_BY_INFRASTRUCTURE.get());

    for ( IExceptionInfo exceptionInfo : m.getExceptions() ) {
      IType exceptionType = exceptionInfo.getExceptionType();
      expected.remove(exceptionType);
      addAFault(exceptionType, exceptionInfo.getDescription(), tns, serviceInfo, seenElements, portTypeOperation, soap12BindingOp, soap11BindingOp);
    }
    for (IType missing : expected) {
      addAFault(missing, null, tns, serviceInfo, seenElements, portTypeOperation, soap12BindingOp, soap11BindingOp);
    }
  }

  private static void addAFault(IType exceptionType, String description, XmlNamespace tns, WsiServiceInfo serviceInfo, Set<String> seenElements, TPortType_Operation portTypeOperation, TBinding_Operation soap12BindingOp, TBinding_Operation soap11BindingOp) {
    String faultName = exceptionType.getRelativeName();
    TOperation_Fault fault = new TOperation_Fault();
    fault.setName$( faultName );
    fault.setComment(description);
    fault.setMessage$( tns.qualify( faultName ) );

    TBindingOperation_Fault soap11BindingFault = new TBindingOperation_Fault();
    soap11BindingOp.Fault().add( soap11BindingFault );
    soap11BindingFault.setName$( faultName );
    gw.internal.schema.gw.xsd.w3c.soap11.Fault soap11Fault = new gw.internal.schema.gw.xsd.w3c.soap11.Fault();
    soap11Fault.setName$( faultName );
    soap11Fault.setUse$( gw.internal.schema.gw.xsd.w3c.soap11.enums.UseChoice.Literal );
    soap11BindingFault.addChild( soap11Fault );

    TBindingOperation_Fault soap12BindingFault = new TBindingOperation_Fault();
    soap12BindingOp.Fault().add( soap12BindingFault );
    soap12BindingFault.setName$( faultName );
    gw.internal.schema.gw.xsd.w3c.soap12.Fault soap12Fault = new gw.internal.schema.gw.xsd.w3c.soap12.Fault();
    soap12Fault.setName$( faultName );
    soap12Fault.setUse$( gw.internal.schema.gw.xsd.w3c.soap12.enums.UseChoice.Literal );
    soap12BindingFault.addChild( soap12Fault );

    if ( seenElements.add( faultName ) ) {
      TDefinitions_Message faultMessage = new TDefinitions_Message();
      faultMessage.setName$( faultName );
      serviceInfo.getWsdl().Message().add( faultMessage );
      TMessage_Part part = new TMessage_Part();
      faultMessage.Part().add( part );
      part.setElement$( tns.qualify( faultName ) );
      part.setName$( faultName );
      Element faultElement = new Element();
      faultElement.setName$( faultName );
      faultElement.setComplexType$( new TopLevelElement_ComplexType() );
      // TODO - should we include any properties in the exception element? Some thought needs to go into how this would work
      // We don't want to unnecessarily include random things, such as Message, because the customer might not
      // wish to include it. Perhaps an annotation where they explicitly add properties to be included?
//        faultElement.ComplexType().setSequence$( new Sequence() );
//        for ( IPropertyInfo prop : exceptionType.getTypeInfo().getProperties() ) {
//          if ( ! prop.getOwnersType().equals( prop.getFeatureType() ) ) {
//            continue;
//          }
//          ExplicitGroup_Element propElement = new ExplicitGroup_Element();
//          propElement.setName$( prop.getName() );
//          MarshalInfo marshalInfo = XmlMarshaller.addType( prop.getFeatureType(), propElement.getTypeInstance(), createInfo );
//          if ( marshalInfo != null ) {
//            faultElement.ComplexType().Sequence().Element().add( propElement );
//          }
//        }
      serviceInfo.getSchema().Element().add( faultElement );
    }
    portTypeOperation.Fault().add( fault );
  }

  private static String makeMethodSignatureForXmlComment( IMethodInfo m ) {
    StringBuilder signature = new StringBuilder( m.getDisplayName() );
    signature.append( '(' );
    boolean first = true;
    for ( IParameterInfo param : m.getParameters() ) {
      IType paramFeatureType = param.getFeatureType();
      if ( WSI_INVOCATION_CONTEXT_TYPE.get().equals( paramFeatureType ) ) {
        continue;
      }
      if ( ! first ) {
        signature.append( ", " );
      }
      signature.append( paramFeatureType.getRelativeName() );
      first = false;
    }
    signature.append( ')' );
    IType returnType = m.getReturnType();
    if ( ! returnType.equals( JavaTypes.pVOID() ) ) {
      signature.append( " : " );
      signature.append( returnType.getRelativeName() );
    }
    return signature.toString();
  }

  private static String makeUniqueName( String name, Set<String> seenElements ) {
    if ( ! seenElements.add( name ) ) {
      int suffix = 2;
      while ( true ) {
        String newName = name + suffix++;
        if ( seenElements.add( newName ) ) {
          name = newName;
          break;
        }
      }
    }
    return name;
  }

}
