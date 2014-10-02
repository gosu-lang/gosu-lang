/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server.marshal;

import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TDefinitions_Import;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Import;
import gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.LocalElement;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.internal.xml.XmlConstants;
import gw.internal.xml.xsd.typeprovider.*;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.lang.reflect.IEnumType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Pair;
import gw.util.concurrent.LockingLazyVar;
import gw.xml.XmlElement;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import static java.lang.String.format;

/**
 * This is what the marshaller does, marshal, unmarshal, valid types, create schemas</desc>
 *
 * @author dandrews
 */
public class XmlMarshaller {
  
  public static final String DEFAULT_NAMESPACE_PREFIX = "http://example.com/";     // since this is a constant, assume that it always has a '/'

  private static final LockingLazyVar<IType> _xmlElementType = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.get( XmlElement.class );
    }
  };

  private static final LockingLazyVar<IType> _anyTypeType = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xsd.w3c.xmlschema.types.complex.AnyType" );
    }
  };

  private static final LockingLazyVar<IType> _anySimpleTypeType = new LockingLazyVar<IType>() {
    @Override
    protected IType init() {
      return TypeSystem.getByFullName( "gw.xsd.w3c.xmlschema.types.simple.AnySimpleType" );
    }
  };

  private static final HashMap<IType, MarshalInfoFactory> _customMarshalInfoFactories =
          new HashMap<IType, MarshalInfoFactory>();

  private XmlMarshaller() {
    
  }

  public static void addCustomMarshallerFactory(IType type, MarshalInfoFactory factory) {
    MarshalInfoFactory prev = _customMarshalInfoFactories.put(type, factory);
    if (prev != null && !prev.equals(factory)) {
      throw new IllegalArgumentException(format("Custom marshaller factory already registered for type %s", type));
    }
  }

  public static String createTargetNamespace( String prefix, IType type ) {
    return createTargetNamespace( prefix, type.getName().replace( '.', '/' ) );
  }

  public static String createTargetNamespace( String prefix, String path ) {
    if ( prefix == null ) {
      prefix = DEFAULT_NAMESPACE_PREFIX;
    }
    return prefix + path;
  }

  /** an attribute with the schema type of the property
   */
  public static QName XSI_TYPE = new QName( XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "type", "xsi");

  protected static final Comparator<IPropertyInfo> COMPARATOR = new Comparator<IPropertyInfo>() {
    @Override
    public int compare(IPropertyInfo o1, IPropertyInfo o2) {
      return o1.getName().compareTo(o2.getName());
    }
  };

  public static MarshalInfo addType( IType type, LocalElement element,
                              WsiServiceInfo serviceInfo
  ) throws Exception {
    MarshalInfo marshalInfo = getMarshalInfo( type, false, serviceInfo );
    if ( ! type.isValid() ) {
      throw new RuntimeException( "Type " + type + " is not valid" );
    }
    else if (marshalInfo == null) {
      throw new IllegalStateException( "XmlMarshaller doesn't know how to handle " + type.getName() );
    }
    else {
      marshalInfo.addType( element, serviceInfo );
    }
    return marshalInfo;
  }

  public static MarshalInfo getMarshalInfo( IType type, WsiServiceInfo serviceInfo ) {
    return getMarshalInfo( type, false, serviceInfo ); // only component types should use nillable for null
  }

  private static MarshalInfo getMarshalInfo( IType type, boolean isComponent, WsiServiceInfo serviceInfo ) {
    if (type == null) {
      return null;
    }
    MarshalInfoFactory factory = _customMarshalInfoFactories.get(type);
    if (factory != null) {
      return factory.createMarshalInfoForType(type, isComponent, serviceInfo);
    }
    if ( type.isEnum() && serviceInfo != null && ! serviceInfo.getExposeEnumAsStringTypes().contains( type ) ) {
       return new EnumTypeMarshalInfo((IEnumType)type, isComponent);
    }
    Pair<QName, XmlSimpleValueFactory> pair = XmlSchemaTypeToGosuTypeMappings.gosuToSchemaIfValid( type );
    if ( pair != null ) {
      QName qName = pair.getFirst();
      XmlSimpleValueFactory simpleValueFactory = pair.getSecond();
      String gwType = null;
      XmlSimpleValueFactory reverseSimpleValueFactory = XmlSchemaTypeToGosuTypeMappings.schemaToGosu( qName );
      IType boxType = type.isPrimitive() ? TypeSystem.getBoxType( type ) : type;
      if ( reverseSimpleValueFactory == null || ! reverseSimpleValueFactory.getGosuValueType().equals( boxType ) ) {
        if ( ! type.isEnum() ) { // don't expose enum types, since they might not match on the remote side
          gwType = simpleValueFactory.getGosuValueType().getName();
        }
      }
      return new SimpleValueMarshalInfo( type, qName, simpleValueFactory, gwType, isComponent );
    }
    else if ( type.isArray() ) { // Array
      MarshalInfo componentMarshalInfo = getMarshalInfo( type.getComponentType(), true, serviceInfo );
      if ( componentMarshalInfo == null ) {
        return null;
      }
      return new ArrayMarshalInfo( type.getComponentType(), componentMarshalInfo, isComponent );
    }
    else if ( JavaTypes.LIST().isAssignableFrom( type ) ) {
      final IType[] parameters = type.getTypeParameters();
      if (parameters == null || parameters.length != 1) {
        return null;
      }
      MarshalInfo componentMarshalInfo = getMarshalInfo( parameters[0], true, serviceInfo );
      if ( componentMarshalInfo == null ) {
        return null;
      }
      return new ListMarshalInfo( type.getTypeParameters()[0], componentMarshalInfo, isComponent );
    }
    else if ( _xmlElementType.get().isAssignableFrom( type ) ) {
      if ( type instanceof IXmlType ) {
        IXmlType xmlType = (IXmlType) type;
        IXmlSchemaElementTypeData typeData = (IXmlSchemaElementTypeData) xmlType.getTypeData();
        if ( typeData.isAnonymous() ) {
          return null; // can't take or return anonymous elements
        }
      }
      return new XmlElementMarshalInfo( type, isComponent );
    }
    else if ( _anyTypeType.get().isAssignableFrom( type ) && ! ( _anySimpleTypeType.get().isAssignableFrom( type ) ) ) {
      IXmlType xmlType = (IXmlType) type;
      IXmlSchemaTypeInstanceTypeData typeData = (IXmlSchemaTypeInstanceTypeData) xmlType.getTypeData();
      if ( typeData.isAnonymous() ) {
        return null; // can't take or return anonymous type instances
      }
      return new XmlTypeInstanceMarshalInfo( type, isComponent );
    }
    else if (type instanceof IGosuClass && ExportableMarshalInfo.isExportable(type)) {
      return new ExportableMarshalInfo(type, isComponent);
    }
    else if (RemotableMarshalInfo.isExportable(type)) {
      return new RemotableMarshalInfo(type, isComponent);
    }
    return null;
  }

  /**
   * This will either find the schema in the import section of the schema or add the import.
   *
   *
   *
   * @param schemaIndex the schema that we are looking for
   * @param serviceInfo info on the wsdl being created
   * @param namespaceURI
   * @throws java.net.URISyntaxException on error
   */
  public static void findOrImportSchema( XmlSchemaIndex schemaIndex,
                                         WsiServiceInfo serviceInfo,
                                         String namespaceURI ) throws URISyntaxException {
    XmlSchemaImportInfo importInfo = schemaIndex.getImportInfo();
    String declaredNamespace = importInfo.getTargetNamespace();
    // The reason for preserving the difference between a lack of a namespace and an empty string namespace is to
    // work around an issue with Apache XMLBeans
    String namespaceToDeclare = declaredNamespace == null ? XMLConstants.NULL_NS_URI : declaredNamespace;
    // first add wsdl import if needed
    if ( importInfo.isWsdl() ) {
      boolean needWsdlImport = true;
      for ( TDefinitions_Import child : serviceInfo.getWsdl().Import() ) {
        String childNamespace = child.Namespace() == null ? XMLConstants.NULL_NS_URI : child.Namespace().toString();
        if ( childNamespace.equals( namespaceToDeclare ) ) {
          needWsdlImport = false;
        }
      }
      if ( needWsdlImport ) {
        String location = getOrCreateLocation( schemaIndex, serviceInfo.getXsdRootURL() );
        serviceInfo.getSchemas().add( schemaIndex );
        TDefinitions_Import imprt = new TDefinitions_Import();
        imprt.setNamespace$( new URI( declaredNamespace == null ? XmlConstants.NULL_NS_URI : declaredNamespace ) );
        imprt.setLocation$( new URI( location ) );
        serviceInfo.getWsdl().getChildren().add( 0, imprt );
        serviceInfo.getWsdl().declareNamespace( new URI( namespaceToDeclare ), "imported" );
      }
      if ( namespaceURI == null ) {
        return; // just need the wsdl:import
      }
      namespaceToDeclare = namespaceURI;
      declaredNamespace = namespaceURI;
    }
    // now add schema import
    for ( Import child : serviceInfo.getSchema().Import() ) {
      String childNamespace = child.Namespace() == null ? XMLConstants.NULL_NS_URI : child.Namespace().toString();
      if ( childNamespace.equals( namespaceToDeclare ) ) {
        return;
      }
    }
    serviceInfo.getSchemas().add( schemaIndex );
    Import imprt = new Import();
    if ( declaredNamespace != null ) {
      imprt.setNamespace$( new URI( declaredNamespace ) );
    }
    if ( ! importInfo.isWsdl() ) {
      String location = getOrCreateLocation( schemaIndex, serviceInfo.getXsdRootURL() );
      imprt.setSchemaLocation$( new URI( location ) );
    }
    serviceInfo.getSchema().getChildren().add( 0, imprt );
    serviceInfo.getSchema().declareNamespace( new URI( namespaceToDeclare ), "imported" );
  }


  /** creates the schemaLocation
   *
   * @param schema the schema
   * @param xsdRootURI the root for the URL
   * @return the path
   */
  public static String getOrCreateLocation( XmlSchemaIndex schema, String xsdRootURI ) {
    return xsdRootURI + ( xsdRootURI.length() == 0 || xsdRootURI.endsWith("/") ? "" : "/" ) + schema.getXSDSourcePath();
  }


}
