/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider;
/**
 * This represents a wsdl, which is a combination of schema, service definitions, and
 * other types.  One of these objects exist for each wsdl found.
 */

import gw.fs.IFile;
import gw.internal.xml.ws.typeprovider.validator.WsdlValidator;
import gw.internal.xml.xsd.ResourceFileXmlSchemaSource;
import gw.internal.xml.xsd.typeprovider.IXmlTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaImportInfo;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.*;
import gw.internal.xml.xsd.typeprovider.schemaparser.SoapVersion;
import gw.util.ILogger;
import gw.util.Pair;
import gw.xml.ws.WsdlValidationException;
import gw.lang.reflect.IType;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.*;

public class Wsdl extends XmlSchemaIndex<Object> {

  private Map<String, IXmlTypeData> _types;
  private Set<String> _allTypeNames;
  private IFile _resourceFile;
  private Map<QName,WsdlFaultTypeData> _definedFaultTypes;

  /**
   * This will create a new WSDL object for a wsdl file.
   *
   *
   * @param typeProvider the wsdl type loader for this module
   * @param gwNamespace the namespace
   * @param resourceFile the file containing the wsdl
   * @throws java.io.IOException if can not read the resource file
   */
  public Wsdl( WsdlTypeLoader typeProvider, String gwNamespace, IFile resourceFile ) throws IOException {
    super( typeProvider, gwNamespace, new ResourceFileXmlSchemaSource( resourceFile ), null );
    _definedFaultTypes = new HashMap<QName, WsdlFaultTypeData>();
    try {
      _resourceFile = resourceFile;
//      getLogger().debug("Wsdl for " + resourceFile.getPath() + " package=" + gwNamespace);
    }
    catch ( Throwable t ) {
      throw new WsdlValidationException( "Unable to process WSDL " + resourceFile, t );
    }
  }

  public ILogger getLogger() {
    return getTypeProvider().getLogger();
  }

  /**
   * If the types need to be initialized, this will create the parser and
   * parse the wsdl document.
   */
  private void maybeInit() {
    if ( _types != null ) {  // already inited
      return;
    }
    try {
      _allTypeNames = new HashSet<String>();
      createTypes();
      return;
    }
    catch (Exception ex) {
      getLogger().warn("Unable to parse WSDL: " + _resourceFile.getPath(), ex);
    }
    _types = Collections.emptyMap();
    _allTypeNames = Collections.emptySet();
  }

  /**
   * Once the wsdl has been parsed, then we need to create the various types
   * The first level of this is the Service, within each service is a port definition.
   *
   * This first pass just creates the types it does not populate the type info.  They will be populated
   * when the actual type is referenced (typically the service).
   */
  private void createTypes() {
    getLogger().debug("Wsdl.createTypes from " + _resourceFile);
    @SuppressWarnings({"unchecked"})
    Map<QName, WsdlService> services = getWsdlDefinitions().getServices();
    _types = new HashMap<String, IXmlTypeData>();
    for ( Map.Entry<QName, WsdlService> entry : services.entrySet() ) {
      QName qname = entry.getKey();
      WsdlService service = entry.getValue();
      if ( getLogger().isDebugEnabled()) {
        getLogger().debug("Wsdl.createTypes on " + qname + " service=" + service.getQName());
      }
      try {
        Map<WsdlBindingOperation,WsdlSoapHeadersTypeDataClass> soapHeadersTypeDataByBindingOperation = Collections.emptyMap();
        Map<Pair<String,Set<QName>>,WsdlSoapHeadersTypeDataClass> existingSoapHeadersTypeDatasByBindingOperationNameAndHeaders = new HashMap<Pair<String, Set<QName>>, WsdlSoapHeadersTypeDataClass>();
        List<WsdlPort> ports = service.getPorts();
        WsdlPort preferredPort = null;
        for ( WsdlPort port : ports ) {
          boolean foundSupportedBinding = false;
          WsdlBinding binding = port.getBinding();
          if ( binding.getSoapBinding() != null ) {
            WsdlSoapBinding soapBinding = binding.getSoapBinding();
            if ("rpc".equals(soapBinding.getStyle())) {
              getLogger().warn("Skipping rpc style binding " + binding.getQName() + " in " + _resourceFile);
            }
            else if ( soapBinding.getSoapVersion() == SoapVersion.SOAP_12 ) {
              preferredPort = port;
              foundSupportedBinding = true;
            }
            else if ( soapBinding.getSoapVersion() == SoapVersion.SOAP_11 ) {
              if ( preferredPort == null ) {
                preferredPort = port;
              }
              foundSupportedBinding = true;
            }
          }
          if ( foundSupportedBinding ) {
            String portName = port.getQName().getLocalPart();
            String serviceName = service.getQName().getLocalPart();
            if ( portName.startsWith( serviceName ) ) {
              portName = portName.substring( serviceName.length() );
            }
            if ( portName.startsWith( "_" ) || portName.startsWith( "-" ) ) {
              portName = portName.substring( 1 );
            }
            String fqPortName = makeUniqueTypeName( getPackageName() + ".ports", XmlSchemaIndex.makeCamelCase( serviceName, NormalizationMode.PROPERCASE ) + "_" + XmlSchemaIndex.makeCamelCase( portName, NormalizationMode.PROPERCASE ) );
            fqPortName = addUniqueToAllTypeNames( fqPortName );
            // create types for any header sets for each operation
            for ( WsdlBindingOperation bindingOperation : binding.getBindingOperations() ) {
              final WsdlBindingInput bindingOperationInput = bindingOperation.getBindingInput();
              if ( bindingOperationInput != null ) {
                List<WsdlSoapHeader> soapHeaders = bindingOperationInput.getSoapHeaders();
                if ( ! soapHeaders.isEmpty() ) {
                  List<XmlSchemaElement> headerElements = new ArrayList<XmlSchemaElement>( soapHeaders.size() );
                  Set<QName> soapHeaderQNames = new HashSet<QName>();
                  for ( WsdlSoapHeader soapHeader : soapHeaders ) {
                    XmlSchemaElement headerElement = soapHeader.getMessage().getPartByName( soapHeader.getPartName() ).getElement();
                    headerElements.add( headerElement );
                    soapHeaderQNames.add( headerElement.getQName() );
                  }
                  Pair<String, Set<QName>> pair = new Pair<String, Set<QName>>( bindingOperation.getName(), soapHeaderQNames );
                  WsdlSoapHeadersTypeDataClass headersTypeData = existingSoapHeadersTypeDatasByBindingOperationNameAndHeaders.get( pair );
                  if ( headersTypeData == null ) {
                    // create a type to represent the set of headers for this operation
                    String bindingOperationNameForType = bindingOperation.getName();
                    bindingOperationNameForType = Character.toUpperCase( bindingOperationNameForType.charAt( 0 ) ) + bindingOperationNameForType.substring( 1 );
                    String headersTypeName = makeUniqueTypeName( getPackageName() + ".soapheaders", XmlSchemaIndex.makeCamelCase( bindingOperationNameForType + "Headers", NormalizationMode.PROPERCASE ) );
                    headersTypeName = addUniqueToAllTypeNames( headersTypeName );
                    headersTypeData = createWsdlSoapHeadersTypeData( headersTypeName, port, this, service, _resourceFile, false, headerElements );
                    _types.put( headersTypeName, headersTypeData );
                    existingSoapHeadersTypeDatasByBindingOperationNameAndHeaders.put( pair, headersTypeData );
                  }
                  if ( soapHeadersTypeDataByBindingOperation.isEmpty() ) {
                    soapHeadersTypeDataByBindingOperation = new HashMap<WsdlBindingOperation, WsdlSoapHeadersTypeDataClass>();
                  }
                  soapHeadersTypeDataByBindingOperation.put( bindingOperation, headersTypeData );
                }
              }
            }
            _types.put( fqPortName, createWsdlPortTypeData( fqPortName, port, this, service, _resourceFile, false, soapHeadersTypeDataByBindingOperation ) );
          }
        }
        if ( preferredPort != null ) {
          String typeName = makeUniqueTypeName( getPackageName(), XmlSchemaIndex.makeCamelCase( service.getQName().getLocalPart(), NormalizationMode.PROPERCASE ) );
          typeName = addUniqueToAllTypeNames(typeName);
          IXmlTypeData type = createWsdlPortTypeData( typeName, preferredPort, this, service, _resourceFile, true, soapHeadersTypeDataByBindingOperation );
          _types.put( typeName, type );
        }
      } catch (Exception e) {
        getLogger().error("Wsdl.createTypes on " + qname + " service=" + service.getQName() + ":", e);
      }
    }

    for ( WsdlPortType wsdlPortType : getWsdlDefinitions().getPortTypes() ) {
      for ( WsdlPortTypeOperation wsdlPortTypeOperation : wsdlPortType.getOperations() ) {
        for ( WsdlPortTypeFault wsdlPortTypeFault : wsdlPortTypeOperation.getFaults() ) {
          WsdlMessage message = wsdlPortTypeFault.getMessage();
          Collection<WsdlPart> parts = message.getParts();
          if ( parts.size() != 1 ){
            getLogger().warn( "In WSDL " + getPackageName() + ", fault " + wsdlPortTypeFault.getName() + " was ignored since multiple fault parts are not supported" );
          }
          else {
            WsdlPart part = parts.iterator().next();
            QName faultElementQName = part.getElementName();
            WsdlFaultTypeData faultTypeData = _definedFaultTypes.get( faultElementQName );
            if ( faultTypeData == null ) {
              String faultName = faultElementQName.getLocalPart();
              String fqFaultTypeName = makeUniqueTypeName( getPackageName() + ".faults", XmlSchemaIndex.makeCamelCase(faultName, null) );
              faultTypeData = new WsdlFaultTypeData( this, fqFaultTypeName, part );
              _types.put( fqFaultTypeName, faultTypeData );
              _definedFaultTypes.put( faultElementQName, faultTypeData );
            }
          }
        }
      }
    }
  }

  public IType getWsdlFaultTypeByElementQName(QName name) {
    WsdlFaultTypeData data = _definedFaultTypes.get(name);
    return data == null ? null : data.getType();
  }
  
  protected WsdlPortTypeData createWsdlPortTypeData( String typeName, WsdlPort preferredPort, Wsdl wsdl, WsdlService service, IFile resourceFile, boolean isService, Map<WsdlBindingOperation, WsdlSoapHeadersTypeDataClass> soapHeadersTypeDataByBindingOperation ) {
    return new WsdlPortTypeData( typeName, preferredPort, wsdl, service, resourceFile, isService, soapHeadersTypeDataByBindingOperation );
  }

  protected WsdlSoapHeadersTypeDataClass createWsdlSoapHeadersTypeData( String typeName, WsdlPort preferredPort, Wsdl wsdl, WsdlService service, IFile resourceFile, boolean isService, List<XmlSchemaElement> headerElements ) {
    return new WsdlSoapHeadersTypeDataClass( typeName, preferredPort, wsdl, service, resourceFile, isService, headerElements );
  }

  /**
   * This ensures that the type is unique for this wsdl
   *
   * @param typeName the type name to ensure uniqueness
   * @return a unique type name
   */
  private String addUniqueToAllTypeNames(String typeName) {
    if ( _allTypeNames.contains( typeName ) ) {
      int suffix = 2;
      String newTypeName;
      do {
        newTypeName = typeName + suffix++;
      } while ( _allTypeNames.contains( newTypeName ) );
      typeName = newTypeName;
      if ( getLogger().isDebugEnabled() ) {
        getLogger().debug("Wsdl.addUniqueToAllTypeNames type was not unique " + typeName);
      }
    }
    _allTypeNames.add( typeName );
    return typeName;
  }

  /**
   * Given a fully qualified name it will return a reference to the type from the typesystem
   *
   * @param fullyQualifiedName the typename
   * @return a reference to the type
   */
  @Override
  public IXmlTypeData getAdditionalTypeData( String fullyQualifiedName ) {
    maybeInit();
    return _types.get( fullyQualifiedName );
  }

  /**
   * This will return all types from the wsdl
   *
   * @return set of string
   */
  @Override
  public Set<String> getAdditionalTypeNames() {
    maybeInit();
    return _allTypeNames;
  }

  public XmlSchemaImportInfo getImportInfo() {
    return new XmlSchemaImportInfo( getWsdlDefinitions().getTargetNamespace(), getXSDSourcePath(), true );
  }

  /**
   * Get the typeloader
   *
   * @return this will get the type loader for this wsdl
   */
  public WsdlTypeLoader getTypeProvider() {
    return (WsdlTypeLoader) super.getTypeLoader();
  }

  public void validate( Map<Pair<URL, String>, XmlSchema> caches ) {
    super.validate( caches );
    WsdlValidator.validateWsdl( getWsdlDefinitions() );
  }

  public long getFingerprint() {
    return 0L; // TODO
  }

}
