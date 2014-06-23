/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schemaparser;

import gw.config.CommonServices;
import gw.internal.schema.gw.xsd.gw.gw_pl_wsdl_additions.Address;

import gw.internal.xml.XmlConstants;
import gw.internal.xml.XmlElementInternals;
import gw.internal.xml.xsd.typeprovider.LocationMap;
import gw.internal.xml.xsd.typeprovider.LocationMapCallback;
import gw.lang.reflect.LocationInfo;
import gw.util.Pair;
import gw.internal.xml.XmlSimpleValueInternals;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.schema.WsdlBinding;
import gw.internal.xml.xsd.typeprovider.schema.WsdlBindingInput;
import gw.internal.xml.xsd.typeprovider.schema.WsdlBindingOperation;
import gw.internal.xml.xsd.typeprovider.schema.WsdlBindingOutput;
import gw.internal.xml.xsd.typeprovider.schema.WsdlDefinitions;
import gw.internal.xml.xsd.typeprovider.schema.WsdlGwAddress;
import gw.internal.xml.xsd.typeprovider.schema.WsdlImport;
import gw.internal.xml.xsd.typeprovider.schema.WsdlMessage;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPart;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPort;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPortType;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPortTypeFault;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPortTypeInput;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPortTypeOperation;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPortTypeOutput;
import gw.internal.xml.xsd.typeprovider.schema.WsdlService;
import gw.internal.xml.xsd.typeprovider.schema.WsdlSoapAddress;
import gw.internal.xml.xsd.typeprovider.schema.WsdlSoapBinding;
import gw.internal.xml.xsd.typeprovider.schema.WsdlSoapBody;
import gw.internal.xml.xsd.typeprovider.schema.WsdlSoapHeader;
import gw.internal.xml.xsd.typeprovider.schema.WsdlSoapOperation;
import gw.internal.xml.xsd.typeprovider.schema.WsdlTypes;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchema;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAll;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAny;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAnyAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttributeGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttributeOrAttributeGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaChoice;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaContentModel;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaEnumerationFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaFractionDigitsFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaImport;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaInclude;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaLengthFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMaxExclusiveFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMaxInclusiveFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMaxLengthFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMinExclusiveFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMinInclusiveFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaMinLengthFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaParticle;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaPatternFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSequence;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeList;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeUnion;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaTotalDigitsFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaWhiteSpaceFacet;
import gw.util.GosuExceptionUtil;
import gw.xml.XmlElement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import gw.xml.XmlException;
import gw.xml.XmlParseOptions;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

public abstract class XmlSchemaParser {

  private static final Map<QName, Class<? extends XmlSchemaFacet>> _facetClasses = new HashMap<QName, Class<? extends XmlSchemaFacet>>();
  private static final QName ATTRIBUTE_QNAME = new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "attribute" );
  private static final QName ATTRIBUTEGROUP_QNAME = new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "attributeGroup" );
  private static final QNameComparator QNAME_COMPARATOR = new QNameComparator();
  private static final PortComparator PORT_COMPARATOR = new PortComparator();

  public static final QName VIEWAS_ATTRIBUTE_NAME = new QName( "http://guidewire.com/xsd", "viewas", "gw" );
  public static final String VIEWAS_LIST = "list";
  public static final String VIEWAS_ARRAY = "array";
  private static final QName LEGACY_WSDL_ADDRESS_QNAME = new QName( "http://guidewire.com/xsd", "address" );
  private static final LocationInfo LOCATION_NOT_YET_DEFINED = null; // marker constant for later implementation of schema object location infos

  static {
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "minExclusive" ), XmlSchemaMinExclusiveFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "minInclusive" ), XmlSchemaMinInclusiveFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "maxExclusive" ), XmlSchemaMaxExclusiveFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "maxInclusive" ), XmlSchemaMaxInclusiveFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "totalDigits" ), XmlSchemaTotalDigitsFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "fractionDigits" ), XmlSchemaFractionDigitsFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "length" ), XmlSchemaLengthFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "minLength" ), XmlSchemaMinLengthFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "maxLength" ), XmlSchemaMaxLengthFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "enumeration" ), XmlSchemaEnumerationFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "whiteSpace" ), XmlSchemaWhiteSpaceFacet.class );
    _facetClasses.put( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "pattern" ), XmlSchemaPatternFacet.class );
  }

  public static XmlSchema parseSchema( XmlSchemaIndex schemaIndex, XmlElement element, String tns, URL baseURL, XmlSchemaParseContext context, boolean isRedefine, Map<Pair<URL, String>, XmlSchema> caches ) throws IOException {
     if ( context.pushIncludedSchema( baseURL ) ) {
      throw new IllegalStateException( "Internal Error: Unhandled circularity in schemas" ); // this used to be a circular include error, but we should now detect circular includes, and we should never get here
    }
    try {
      // if tns is non-null, perform chameleon transformation
      String declaredTNS = element.getAttributeValue( "targetNamespace" );
      boolean chameleon = tns != null && ( declaredTNS == null || declaredTNS.equals( XMLConstants.NULL_NS_URI ) );
      String targetNamespace = tns == null ? declaredTNS : tns;
      if ( targetNamespace == null ) {
        targetNamespace = XMLConstants.NULL_NS_URI;
      }
      boolean elementQualifiedDefault = isQualified( element.getAttributeValue( "elementFormDefault" ), false );
      boolean attributeQualifiedDefault = isQualified( element.getAttributeValue( "attributeFormDefault" ), false );

      Map<QName, XmlSchemaElement> topLevelElements = new HashMap<QName, XmlSchemaElement>();
      for ( XmlElement topLevelElement : element.getChildren( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "element" ) ) ) {
        XmlSchemaElement schemaElement = parseElement( schemaIndex, topLevelElement, true, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, null, null );
        topLevelElements.put( schemaElement.getQName(), schemaElement );
      }
      Map<QName, XmlSchemaType> topLevelTypes = new HashMap<QName, XmlSchemaType>();
      for ( XmlElement topLevelType : element.getChildren( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "complexType" ) ) ) {
        XmlSchemaComplexType schemaType = parseComplexType( schemaIndex, topLevelType, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, isRedefine, context, null, null );
        topLevelTypes.put( schemaType.getQName(), schemaType );
      }
      for ( XmlElement topLevelType : element.getChildren( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "simpleType" ) ) ) {
        XmlSchemaSimpleType schemaType = parseSimpleType( schemaIndex, topLevelType, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, isRedefine, context, null, null );
        topLevelTypes.put( schemaType.getQName(), schemaType );
      }
      Map<QName, XmlSchemaAttribute> topLevelAttributes = new HashMap<QName, XmlSchemaAttribute>();
      for ( XmlElement topLevelAttribute : element.getChildren( ATTRIBUTE_QNAME ) ) {
        XmlSchemaAttribute attribute = parseAttribute( schemaIndex, topLevelAttribute, true, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, null, null );
        topLevelAttributes.put( attribute.getQName(), attribute );
      }
      Map<QName, XmlSchemaGroup> topLevelGroups = new HashMap<QName, XmlSchemaGroup>();
      for ( XmlElement topLevelGroup : element.getChildren( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "group" ) ) ) {
        XmlSchemaGroup group = parseGroup( schemaIndex, topLevelGroup, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, isRedefine, context, null, null );
        topLevelGroups.put( group.getName(), group );
      }
      Map<QName, XmlSchemaAttributeGroup> topLevelAttributeGroups = new HashMap<QName, XmlSchemaAttributeGroup>();
      for ( XmlElement topLevelAttributeGroup : element.getChildren( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "attributeGroup" ) ) ) {
        XmlSchemaAttributeGroup group = parseAttributeGroup( schemaIndex, topLevelAttributeGroup, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, isRedefine, context, null, null );
        topLevelAttributeGroups.put( group.getName(), group );
      }
      List<XmlSchemaImport> imports = new ArrayList<XmlSchemaImport>();
      for ( XmlElement importElement : element.getChildren( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "import" ) ) ) {
        XmlSchemaImport imprt = parseImport( schemaIndex, importElement, baseURL );
        imports.add( imprt );
      }
      for ( XmlElement includeElement : element.getChildren( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "include" ) ) ) {
        XmlSchemaInclude include = parseInclude( schemaIndex, includeElement );
        String schemaLocation = include.getSchemaLocation();
        URL targetUrl = XmlSchemaIndex.makeLocalIfValid( baseURL, schemaLocation, null, schemaIndex.getTypeLoader().getModule() );
        if ( context.isSchemaAlreadyIncluded( targetUrl ) ) {
          continue; // skip already included schema (circular include or redefine)
        }
        XmlSchema schema = loadSchemaWithCache( schemaIndex, targetUrl, targetNamespace, context, caches);
        for ( XmlSchemaImport imprt : schema.getImports() ) {
          imports.add( imprt );
        }
        for ( Map.Entry<QName, XmlSchemaType> entry : schema.getTypes().entrySet() ) {
          topLevelTypes.put( entry.getKey(), entry.getValue() );
        }
        for ( Map.Entry<QName, XmlSchemaElement> entry : schema.getElements().entrySet() ) {
          topLevelElements.put( entry.getKey(), entry.getValue() );
        }
        for ( Map.Entry<QName, XmlSchemaGroup> entry : schema.getGroups().entrySet() ) {
          topLevelGroups.put( entry.getKey(), entry.getValue() );
        }
        for ( Map.Entry<QName, XmlSchemaAttributeGroup> entry : schema.getAttributeGroups().entrySet() ) {
          topLevelAttributeGroups.put( entry.getKey(), entry.getValue() );
        }
        for ( Map.Entry<QName, XmlSchemaAttribute> entry : schema.getAttributes().entrySet() ) {
          topLevelAttributes.put( entry.getKey(), entry.getValue() );
        }
      }
      for ( XmlElement redefineElement : element.getChildren( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "redefine" ) ) ) {
        String schemaLocation = redefineElement.getAttributeValue( "schemaLocation" );
        URL url = XmlSchemaIndex.makeLocalIfValid( baseURL, schemaLocation, null, schemaIndex.getTypeLoader().getModule() );
        if ( context.isSchemaAlreadyIncluded( url ) ) {
          continue; // skip already included schema (circular include or redefine)
        }
        context.popIncludedSchema( baseURL ); // to avoid circular schema error when reparsing this same baseURL
        XmlSchema overrideSchema = parseSchema( schemaIndex, redefineElement, targetNamespace, baseURL, context, true, caches );
        context.pushIncludedSchema( baseURL );
        XmlSchema schema = loadSchemaWithCache( schemaIndex, url, targetNamespace, context, caches);
        for ( XmlSchemaImport imprt : schema.getImports() ) {
          imports.add( imprt );
        }
        for ( Map.Entry<QName, XmlSchemaType> entry : schema.getTypes().entrySet() ) {
          QName typeName = entry.getKey();
          XmlSchemaType schemaType = entry.getValue();
          QName redefineName = context.getRedefinedTypes().get( typeName );
          if ( redefineName != null ) {
            typeName = redefineName;
            schemaType.setName( redefineName.getLocalPart() );
            schemaType.setQName( redefineName );
          }
          topLevelTypes.put( typeName, schemaType );
        }
        for ( Map.Entry<QName, XmlSchemaElement> entry : schema.getElements().entrySet() ) {
          topLevelElements.put( entry.getKey(), entry.getValue() );
        }
        for ( Map.Entry<QName, XmlSchemaGroup> entry : schema.getGroups().entrySet() ) {
          QName groupName = entry.getKey();
          XmlSchemaGroup group = entry.getValue();
          QName redefineName = context.getRedefinedGroups().get( groupName );
          if ( redefineName != null ) {
            groupName = redefineName;
            group.setName( redefineName );
          }
          topLevelGroups.put( groupName, group );
        }
        for ( Map.Entry<QName, XmlSchemaAttributeGroup> entry : schema.getAttributeGroups().entrySet() ) {
          QName groupName = entry.getKey();
          XmlSchemaAttributeGroup group = entry.getValue();
          QName redefineName = context.getRedefinedAttributeGroups().get( groupName );
          if ( redefineName != null ) {
            groupName = redefineName;
            group.setName( redefineName );
          }
          topLevelAttributeGroups.put( groupName, group );
        }
        for ( Map.Entry<QName, XmlSchemaAttribute> entry : schema.getAttributes().entrySet() ) {
          topLevelAttributes.put( entry.getKey(), entry.getValue() );
        }

        for ( Map.Entry<QName, XmlSchemaType> entry : overrideSchema.getTypes().entrySet() ) {
          QName typeName = entry.getKey();
          topLevelTypes.put( typeName, entry.getValue() );
        }
        for ( Map.Entry<QName, XmlSchemaGroup> entry : overrideSchema.getGroups().entrySet() ) {
          topLevelGroups.put( entry.getKey(), entry.getValue() );
        }
        for ( Map.Entry<QName, XmlSchemaAttributeGroup> entry : overrideSchema.getAttributeGroups().entrySet() ) {
          topLevelAttributeGroups.put( entry.getKey(), entry.getValue() );
        }
      }
      if ( targetNamespace.equals( XMLConstants.W3C_XML_SCHEMA_NS_URI ) ) {
        // add the anySimpleType simple type, since the schema schema doesn't define it
        topLevelTypes.put( XmlSchemaIndex.ANY_SIMPLE_TYPE_QNAME, new XmlSchemaSimpleType( schemaIndex, LOCATION_NOT_YET_DEFINED, "anySimpleType", XmlSchemaIndex.ANY_SIMPLE_TYPE_QNAME, null, null ) );
      }
      List<XmlSchemaImport> uniqueImports = new ArrayList<XmlSchemaImport>();
      Set<Pair<String,URL>> handledImports = new HashSet<Pair<String, URL>>();
      for ( XmlSchemaImport imprt : imports ) {
        URL url = XmlSchemaIndex.makeLocalIfValid( imprt.getBaseUrl(), imprt.getSchemaLocation(), imprt.getNamespaceURI(), imprt.getSchemaIndex().getTypeLoader().getModule() );
        Pair<String,URL> pair = new Pair<String, URL>( imprt.getNamespaceURI(), url );
        if ( handledImports.add( pair ) ) {
          uniqueImports.add( imprt );
        }
      }
      return new XmlSchema( schemaIndex, LOCATION_NOT_YET_DEFINED, uniqueImports, topLevelElements, targetNamespace, topLevelTypes, topLevelAttributes, topLevelGroups, topLevelAttributeGroups, declaredTNS );
    }
    finally {
      context.popIncludedSchema( baseURL );
    }
  }

  private static XmlSchema loadSchemaWithCache(XmlSchemaIndex schemaIndex, URL targetUrl, String targetNamespace, XmlSchemaParseContext context, Map<Pair<URL, String>, XmlSchema> caches) throws IOException {
    Pair<URL, String> key = new Pair<URL, String>(targetUrl, targetNamespace == null ? XmlConstants.NULL_NS_URI : targetNamespace);
    XmlSchema schema = (caches == null) ? null : caches.get(key);
    if (schema == null) {
      //      try {
//        return CommonServices.getFileSystem().getIFile(new File(url.toURI()));
//      } catch (URISyntaxException e) {
//        throw new RuntimeException(e);
//      }
      InputStream stream = CommonServices.getFileSystem().getIFile(targetUrl).openInputStream();
      try {
        XmlElement schemaElement = XmlElementInternals.instance().parse( stream, targetUrl, null, null );
        schema = parseSchema(schemaIndex, schemaElement, targetNamespace, targetUrl, context, false, caches );
      } finally {
        if (stream != null) {
          stream.close();
        }
      }
      if (caches != null) {
        caches.put(key, schema);
      }
    } else {
      schema = schema.copy(schemaIndex);
    }
    return schema;
  }

  private static XmlSchemaInclude parseInclude( XmlSchemaIndex schemaIndex, XmlElement element ) {
    String schemaLocation = element.getAttributeValue( "schemaLocation" );
    return new XmlSchemaInclude( schemaIndex, LOCATION_NOT_YET_DEFINED, schemaLocation );
  }

  private static XmlSchemaImport parseImport( XmlSchemaIndex schemaIndex, XmlElement element, URL baseURL ) {
    String namespaceURI = element.getAttributeValue( "namespace" );
    String schemaLocation = element.getAttributeValue( "schemaLocation" );
    URL url = XmlSchemaIndex.makeLocal( baseURL, schemaLocation, namespaceURI, schemaIndex.getTypeLoader().getModule() );
    if ( url != null ) {
      String gosuNamespace = XmlSchemaIndex.getGosuNamespace( url, schemaIndex.getTypeLoader().getModule() );
      if ( gosuNamespace == null ) {
        throw new XmlException( "Gosu namespace not found for imported schema " + url );
      }
    }
    return new XmlSchemaImport( schemaIndex, LOCATION_NOT_YET_DEFINED, namespaceURI, schemaLocation, baseURL );
  }

  private static XmlSchemaAttributeGroup parseAttributeGroup( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, boolean redefine, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    String name = element.getAttributeValue( "name" );
    QName qname = name == null ? null : new QName( targetNamespace, name );
    if ( redefine ) {
      assert qname != null;
      context.getRedefinedAttributeGroups().put( qname, new QName( qname.getNamespaceURI(), context.getNextRedefineName(), qname.getPrefix() ) );
    }
    QName newRedefinedAttributeGroupName = redefine ? qname : redefinedAttributeGroupName;
    List<XmlSchemaAttributeOrAttributeGroup> attributes = parseEmbeddedAttributes( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, newRedefinedAttributeGroupName );
    QName refName = parseQName( element.getAttributeValue( "ref" ), element.getNamespaceContext(), targetNamespace, chameleon );
    if ( refName != null && refName.equals( redefinedAttributeGroupName ) ) {
      refName = context.getRedefinedAttributeGroups().get( refName );
    }
    XmlSchemaAnyAttribute anyAttribute = parseEmbeddedAnyAttribute( schemaIndex, element );
    return new XmlSchemaAttributeGroup( schemaIndex, LOCATION_NOT_YET_DEFINED, attributes, qname, refName, anyAttribute );
  }

  private static XmlSchemaElement parseElement( XmlSchemaIndex schemaIndex, XmlElement element, boolean topLevel, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    String defaultValue = element.getAttributeValue( "default" );
    String name = element.getAttributeValue( "name" );
    String fixedValue = element.getAttributeValue( "fixed" );
    boolean nillable = parseBoolean( element.getAttributeValue( "nillable" ), false );
    String refString = element.getAttributeValue( "ref" );
    QName refName = parseQName( refString, element.getNamespaceContext(), targetNamespace, chameleon );
    QName schemaTypeName = parseQName( element.getAttributeValue( "type" ), element.getNamespaceContext(), targetNamespace, chameleon );
    String gwTypeName = element.getAttributeValue( new QName( "http://guidewire.com/xsd", "type" ) );
    QName substitutionGroup = parseQName( element.getAttributeValue( "substitutionGroup" ), element.getNamespaceContext(), targetNamespace, chameleon );
    boolean qualified = topLevel || isQualified( element.getAttributeValue( "form" ), elementQualifiedDefault );
    QName qname = makeQName( name, targetNamespace, qualified, element.getNamespaceContext() );
    XmlSchemaType schemaType = parseEmbeddedType( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    long minOccurs = getMinOccurs( element );
    long maxOccurs = getMaxOccurs( element );
    if ( gwTypeName != null ) {
      XmlSchemaSimpleTypeContent content = new XmlSchemaSimpleTypeRestriction( schemaIndex, LOCATION_NOT_YET_DEFINED, null, schemaTypeName, Collections.<XmlSchemaFacet>emptyList() );
      schemaType = new XmlSchemaSimpleType( schemaIndex, LOCATION_NOT_YET_DEFINED, null, null, content, gwTypeName );
      schemaTypeName = null;
    }
    String gwViewAs = element.getAttributeValue( VIEWAS_ATTRIBUTE_NAME );
    return new XmlSchemaElement( schemaIndex, context.getLocationMap().get( element ), schemaTypeName, schemaType, substitutionGroup, refName, qname, name, nillable, minOccurs, maxOccurs, gwViewAs, topLevel );
  }

  private static long getMaxOccurs( XmlElement element ) {
    String value = element.getAttributeValue( "maxOccurs" );
    if ( value == null ) {
      return 1;
    }
    else if ( value.equals( "unbounded" ) ) {
      return Long.MAX_VALUE;
    }
    else {
      return Long.parseLong( value );
    }
  }

  private static long getMinOccurs( XmlElement element ) {
    String value = element.getAttributeValue( "minOccurs" );
    if ( value == null ) {
      return 1;
    }
    else {
      return Long.parseLong( value );
    }
  }

  private static XmlSchemaType parseEmbeddedType( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlElement child = element.getChild(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "complexType"));
    if ( child != null ) {
      return parseComplexType( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, false, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "simpleType" ) );
    if ( child != null ) {
      return parseSimpleType( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, false, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    return null;
  }

  private static List<XmlSchemaSimpleType> parseEmbeddedSimpleTypes( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    List<XmlElement> children = element.getChildren(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "simpleType"));
    List<XmlSchemaSimpleType> results = new ArrayList<XmlSchemaSimpleType>( children.size() );
    for ( XmlElement child : children ) {
      results.add( parseSimpleType( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, false, context, redefinedGroupName, redefinedAttributeGroupName ) );
    }
    return results;
  }

  private static XmlSchemaComplexType parseComplexType( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, boolean redefine, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    String name = element.getAttributeValue( "name" );
    String mixed = element.getAttributeValue( "mixed" );
    QName qname = name == null ? null : makeQName( name, targetNamespace, true, element.getNamespaceContext() );
    if ( redefine ) {
      assert qname != null;
      context.getRedefinedTypes().put( qname, new QName( qname.getNamespaceURI(), context.getNextRedefineName(), qname.getPrefix() ) );
    }
    QName redefinedTypeName = redefine ? qname : null;
    XmlSchemaParticle particle = parseEmbeddedParticle(schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName);
    XmlSchemaContentModel contentModel = parseEmbeddedContentModel( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, redefinedTypeName, context, redefinedGroupName, redefinedAttributeGroupName );
    if ( contentModel == null ) {
      List<XmlSchemaAttributeOrAttributeGroup> attributes = parseEmbeddedAttributes( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
      XmlSchemaAnyAttribute anyAttribute = parseEmbeddedAnyAttribute( schemaIndex, element );
      QName baseTypeQName;
      if ( qname != null && qname.equals( XmlSchemaIndex.ANY_TYPE_QNAME ) ) {
        baseTypeQName = null;
      }
      else {
        baseTypeQName = XmlSchemaIndex.ANY_TYPE_QNAME;
      }
      XmlSchemaComplexContentRestriction restriction = new XmlSchemaComplexContentRestriction( schemaIndex, LOCATION_NOT_YET_DEFINED, particle, attributes, baseTypeQName, anyAttribute );
      contentModel = new XmlSchemaComplexContent( schemaIndex, LOCATION_NOT_YET_DEFINED, restriction );
    }
    return new XmlSchemaComplexType( schemaIndex, context.getLocationMap().get( element ), name, qname, contentModel, null, mixed != null && ( mixed.equals( "true" ) || mixed.equals( "1" ) ) );
  }

  private static XmlSchemaAnyAttribute parseEmbeddedAnyAttribute( XmlSchemaIndex schemaIndex, XmlElement element ) {
    XmlElement child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "anyAttribute" ) );
    if ( child != null ) {
      String processContentsString = child.getAttributeValue( "processContents" );
      XmlSchemaAnyAttribute.ProcessContents processContents = processContentsString == null ? XmlSchemaAnyAttribute.ProcessContents.strict : XmlSchemaAnyAttribute.ProcessContents.valueOf( processContentsString );
      return new XmlSchemaAnyAttribute( schemaIndex, LOCATION_NOT_YET_DEFINED, processContents );
    }
    return null;
  }

  private static XmlSchemaSimpleType parseSimpleType( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, boolean redefine, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    String name = element.getAttributeValue( "name" );
    QName qname = name == null ? null : makeQName( name, targetNamespace, true, element.getNamespaceContext() );
    if ( redefine ) {
      assert qname != null;
      context.getRedefinedTypes().put( qname, new QName( qname.getNamespaceURI(), context.getNextRedefineName(), qname.getPrefix() ) );
    }
    QName redefinedTypeName = redefine ? qname : null;
    XmlSchemaSimpleTypeContent content = parseEmbeddedSimpleTypeContent( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedTypeName, redefinedGroupName, redefinedAttributeGroupName );
    return new XmlSchemaSimpleType( schemaIndex, context.getLocationMap().get( element ), name, qname, content, null );
  }

  private static XmlSchemaSimpleTypeContent parseEmbeddedSimpleTypeContent( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedTypeName, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlElement child = element.getChild(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "restriction"));
    if ( child != null ) {
      return parseSimpleTypeRestriction(schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedTypeName, redefinedGroupName, redefinedAttributeGroupName);
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "union" ) );
    if ( child != null ) {
      return parseSimpleTypeUnion( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "list" ) );
    if ( child != null ) {
      return parseSimpleTypeList( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    return null;
  }

  private static XmlSchemaSimpleTypeList parseSimpleTypeList( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    QName itemTypeName = parseQName(element.getAttributeValue("itemType"), element.getNamespaceContext(), targetNamespace, chameleon);
    XmlSchemaSimpleType itemType = (XmlSchemaSimpleType) parseEmbeddedType( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    return new XmlSchemaSimpleTypeList( schemaIndex, LOCATION_NOT_YET_DEFINED, itemTypeName, itemType );
  }

  private static XmlSchemaSimpleTypeUnion parseSimpleTypeUnion( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    List<QName> memberTypesQNames = parseQNames(element.getAttributeValue("memberTypes"), element.getNamespaceContext(), targetNamespace, chameleon);
    List<XmlSchemaSimpleType> baseTypes = parseEmbeddedSimpleTypes( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    return new XmlSchemaSimpleTypeUnion( schemaIndex, LOCATION_NOT_YET_DEFINED, memberTypesQNames, baseTypes );
  }

  private static List<QName> parseQNames( String value, Map<String, String> context, String targetNamespace, boolean chameleon ) {
    if ( value == null ) {
      return Collections.emptyList();
    }
    String[] values = value.split(" ");
    List<QName> results = new ArrayList<QName>( values.length );
    for ( String qnameString : values ) {
      results.add( parseQName( qnameString, context, targetNamespace, chameleon ) );
    }
    return results;
  }

  private static XmlSchemaSimpleTypeRestriction parseSimpleTypeRestriction( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedTypeName, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlSchemaSimpleType baseType = (XmlSchemaSimpleType) parseEmbeddedType( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    QName baseTypeName = parseQName( element.getAttributeValue( "base" ), element.getNamespaceContext(), targetNamespace, chameleon );
    if ( baseTypeName != null && baseTypeName.equals( redefinedTypeName ) ) {
      baseTypeName = context.getRedefinedTypes().get( baseTypeName );
    }
    List<XmlSchemaFacet> facets = parseEmbeddedFacets( schemaIndex, element );
    return new XmlSchemaSimpleTypeRestriction( schemaIndex, LOCATION_NOT_YET_DEFINED, baseType, baseTypeName, facets );
  }

  private static List<XmlSchemaFacet> parseEmbeddedFacets( XmlSchemaIndex schemaIndex, XmlElement element ) {
    ArrayList<XmlSchemaFacet> facets = null;
    for ( XmlElement child : element.getChildren() ) {
      Class<? extends XmlSchemaFacet> facetClass = _facetClasses.get( child.getQName() );
      if ( facetClass != null ) {
        if ( facets == null ) {
          facets = new ArrayList<XmlSchemaFacet>();
        }
        String value = child.getAttributeValue( "value" );
        try {
          //noinspection ObjectEquality
          if ( facetClass == XmlSchemaEnumerationFacet.class ) {
            facets.add( facetClass.getConstructor( XmlSchemaIndex.class, LocationInfo.class, String.class, Map.class ).newInstance( schemaIndex, LOCATION_NOT_YET_DEFINED, value, child.getNamespaceContext() ) );
          }
          else {
            facets.add( facetClass.getConstructor( XmlSchemaIndex.class, LocationInfo.class, String.class ).newInstance( schemaIndex, LOCATION_NOT_YET_DEFINED, value ) );
          }
        }
        catch ( Exception ex ) {
          throw GosuExceptionUtil.forceThrow( ex );
        }
      }
    }
    if ( facets != null ) {
      facets.trimToSize();
      return facets;
    }
    else {
      return Collections.emptyList();
    }
  }

  private static List<XmlSchemaAttributeOrAttributeGroup> parseEmbeddedAttributes( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    List<XmlElement> children = element.getChildren();
    List<XmlSchemaAttributeOrAttributeGroup> results = new ArrayList<XmlSchemaAttributeOrAttributeGroup>();
    for ( XmlElement child : children ) {
      if ( ATTRIBUTE_QNAME.equals( child.getQName() ) ) {
        results.add( parseAttribute( schemaIndex, child, false, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName ) );
      }
      else if ( ATTRIBUTEGROUP_QNAME.equals( child.getQName() ) ) {
        results.add( parseAttributeGroup( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, false, context, redefinedGroupName, redefinedAttributeGroupName ) );
      }
    }
    return results;
  }

  private static XmlSchemaAttribute parseAttribute( XmlSchemaIndex schemaIndex, XmlElement element, boolean topLevel, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    boolean prohibited = "prohibited".equals(element.getAttributeValue("use"));
    String defaultValue = element.getAttributeValue( "default" );
    String name = element.getAttributeValue( "name" );
    String fixedValue = element.getAttributeValue( "fixed" );
    QName refName = parseQName( element.getAttributeValue( "ref" ), element.getNamespaceContext(), targetNamespace, chameleon );
    QName schemaTypeName = parseQName( element.getAttributeValue( "type" ), element.getNamespaceContext(), targetNamespace, chameleon );
    boolean qualified = topLevel || isQualified( element.getAttributeValue( "form" ), attributeQualifiedDefault );
    QName qname = makeQName( name, targetNamespace, qualified, element.getNamespaceContext() );
    XmlSchemaSimpleType schemaType = (XmlSchemaSimpleType) parseEmbeddedType( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    String gwTypeName = element.getAttributeValue( new QName( "http://guidewire.com/xsd", "type" ) );
    if ( gwTypeName != null ) {
      XmlSchemaSimpleTypeContent content = new XmlSchemaSimpleTypeRestriction( schemaIndex, LOCATION_NOT_YET_DEFINED, null, schemaTypeName, Collections.<XmlSchemaFacet>emptyList() );
      schemaType = new XmlSchemaSimpleType( schemaIndex, LOCATION_NOT_YET_DEFINED, null, null, content, gwTypeName );
      schemaTypeName = null;
    }
    return new XmlSchemaAttribute( schemaIndex, context.getLocationMap().get( element ), schemaType, schemaTypeName, refName, qname, fixedValue, defaultValue, prohibited );
  }


  private static List<XmlSchemaParticle> parseEmbeddedParticles( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    ArrayList<XmlSchemaParticle> results = new ArrayList<XmlSchemaParticle>();
    for ( XmlElement child : element.getChildren() ) {
      if ( child.getQName().equals( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "element" ) ) ) {
        results.add( parseElement( schemaIndex, child, false, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName ) );
      }
      else if ( child.getQName().equals( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "group" ) ) ) {
        results.add( parseGroup( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, false, context, redefinedGroupName, redefinedAttributeGroupName ) );
      }
      else if ( child.getQName().equals( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "sequence" ) ) ) {
        results.add( parseSequence( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName ) );
      }
      else if ( child.getQName().equals( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "choice" ) ) ) {
        results.add( parseChoice( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName ) );
      }
      else if ( child.getQName().equals( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "any" ) ) ) {
        results.add( parseAny( schemaIndex, child, targetNamespace ) );
      }
    }
    results.trimToSize();
    return results;
  }

  private static XmlSchemaChoice parseChoice( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    List<XmlSchemaParticle> items = parseEmbeddedParticles( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    long minOccurs = getMinOccurs(element);
    long maxOccurs = getMaxOccurs( element );
    return new XmlSchemaChoice( schemaIndex, LOCATION_NOT_YET_DEFINED, items, minOccurs, maxOccurs );
  }

  private static XmlSchemaParticle parseEmbeddedParticle( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlElement child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "element" ) );
    if ( child != null ) {
      return parseElement( schemaIndex, child, false, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "group" ) );
    if ( child != null ) {
      return parseGroup( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, false, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "sequence" ) );
    if ( child != null ) {
      return parseSequence( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "all" ) );
    if ( child != null ) {
      return parseAll( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "choice" ) );
    if ( child != null ) {
      return parseChoice( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "any" ) );
    if ( child != null ) {
      return parseAny( schemaIndex, child, targetNamespace );
    }
    return null;
  }

  private static XmlSchemaGroup parseGroup( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, boolean redefine, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    QName name = makeQName( element.getAttributeValue( "name" ), targetNamespace, true, element.getNamespaceContext() );
    if ( redefine ) {
      assert name != null;
      context.getRedefinedGroups().put( name, new QName( name.getNamespaceURI(), context.getNextRedefineName(), name.getPrefix() ) );
    }
    QName newRedefinedGroupName = redefine ? name : redefinedGroupName;
    XmlSchemaParticle particle = parseEmbeddedParticle( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, newRedefinedGroupName, redefinedAttributeGroupName );
    QName refName = parseQName( element.getAttributeValue( "ref" ), element.getNamespaceContext(), targetNamespace, chameleon );
    if ( refName != null && refName.equals( redefinedGroupName ) ) {
      refName = context.getRedefinedGroups().get( refName );
    }
    long minOccurs = getMinOccurs( element );
    long maxOccurs = getMaxOccurs( element );
    return new XmlSchemaGroup( schemaIndex, LOCATION_NOT_YET_DEFINED, particle, name, refName, minOccurs, maxOccurs );
  }

  private static XmlSchemaSequence parseSequence( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    List<XmlSchemaParticle> items = parseEmbeddedParticles(schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName);
    long minOccurs = getMinOccurs( element );
    long maxOccurs = getMaxOccurs( element );
    return new XmlSchemaSequence( schemaIndex, LOCATION_NOT_YET_DEFINED, items, minOccurs, maxOccurs );
  }

  private static XmlSchemaAll parseAll( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    // an xs:all can only contain xs:elements per spec
    @SuppressWarnings( {"unchecked", "RedundantCast"} )
    List<XmlSchemaElement> items = (List) parseEmbeddedParticles( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    long minOccurs = getMinOccurs(element);
    long maxOccurs = getMaxOccurs(element);
    return new XmlSchemaAll( schemaIndex, LOCATION_NOT_YET_DEFINED, items, minOccurs, maxOccurs );
  }

  private static XmlSchemaAny parseAny( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace ) {
    String namespace = element.getAttributeValue( "namespace" );
    long minOccurs = getMinOccurs( element );
    long maxOccurs = getMaxOccurs( element );
    String processContentsString = element.getAttributeValue( "processContents" );
    XmlSchemaAny.ProcessContents processContents = processContentsString == null ? XmlSchemaAny.ProcessContents.lax : XmlSchemaAny.ProcessContents.valueOf( processContentsString );
    return new XmlSchemaAny( schemaIndex, LOCATION_NOT_YET_DEFINED, namespace, targetNamespace, minOccurs, maxOccurs, processContents );
  }

  private static XmlSchemaContentModel parseEmbeddedContentModel( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, QName redefinedTypeName, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlElement child = element.getChild(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "complexContent"));
    if ( child != null ) {
      return parseComplexContent( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, redefinedTypeName, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "simpleContent" ) );
    if ( child != null ) {
      return parseSimpleContent( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, redefinedTypeName, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    return null;
  }

  private static XmlSchemaSimpleContent parseSimpleContent( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, QName redefinedTypeName, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlSchemaContent content = parseEmbeddedSimpleContentContent( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, redefinedTypeName, context, redefinedGroupName, redefinedAttributeGroupName );
    return new XmlSchemaSimpleContent( schemaIndex, LOCATION_NOT_YET_DEFINED, content );
  }

  private static XmlSchemaContent parseEmbeddedComplexContentContent( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, QName redefinedTypeName, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlElement child = element.getChild(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "extension"));
    if ( child != null ) {
      return parseComplexContentExtension( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, redefinedTypeName, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "restriction" ) );
    if ( child != null ) {
      return parseComplexContentRestriction( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, redefinedTypeName, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    return null;
  }

  private static XmlSchemaComplexContentRestriction parseComplexContentRestriction( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, QName redefinedTypeName, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlSchemaParticle particle = parseEmbeddedParticle(schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName);
    List<XmlSchemaAttributeOrAttributeGroup> attributes = parseEmbeddedAttributes(schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName);
    QName baseTypeName = parseQName( element.getAttributeValue( "base" ), element.getNamespaceContext(), targetNamespace, chameleon );
    if ( baseTypeName.equals( redefinedTypeName ) ) {
      baseTypeName = context.getRedefinedTypes().get( baseTypeName );
    }
    XmlSchemaAnyAttribute anyAttribute = parseEmbeddedAnyAttribute(schemaIndex, element);
    return new XmlSchemaComplexContentRestriction( schemaIndex, LOCATION_NOT_YET_DEFINED, particle, attributes, baseTypeName, anyAttribute );
  }

  private static XmlSchemaComplexContentExtension parseComplexContentExtension( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, QName redefinedTypeName, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlSchemaParticle particle = parseEmbeddedParticle( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    List<XmlSchemaAttributeOrAttributeGroup> attributes = parseEmbeddedAttributes( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    QName baseTypeName = parseQName(element.getAttributeValue("base"), element.getNamespaceContext(), targetNamespace, chameleon);
    if ( baseTypeName.equals( redefinedTypeName ) ) {
      baseTypeName = context.getRedefinedTypes().get( baseTypeName );
    }
    XmlSchemaAnyAttribute anyAttribute = parseEmbeddedAnyAttribute(schemaIndex, element);
    return new XmlSchemaComplexContentExtension( schemaIndex, LOCATION_NOT_YET_DEFINED, particle, attributes, baseTypeName, anyAttribute );
  }

  private static XmlSchemaContent parseEmbeddedSimpleContentContent( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, QName redefinedTypeName, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlElement child = element.getChild(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "extension"));
    if ( child != null ) {
      return parseSimpleContentExtension( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, redefinedTypeName, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    child = element.getChild( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "restriction" ) );
    if ( child != null ) {
      return parseSimpleContentRestriction( schemaIndex, child, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, redefinedTypeName, context, redefinedGroupName, redefinedAttributeGroupName );
    }
    return null;
  }

  private static XmlSchemaSimpleContentRestriction parseSimpleContentRestriction( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, QName redefinedTypeName, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    List<XmlSchemaAttributeOrAttributeGroup> attributes = parseEmbeddedAttributes(schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName);
    QName baseTypeName = parseQName( element.getAttributeValue( "base" ), element.getNamespaceContext(), targetNamespace, chameleon );
    if ( baseTypeName.equals( redefinedTypeName ) ) {
      baseTypeName = context.getRedefinedTypes().get( baseTypeName );
    }
    XmlSchemaType baseType = parseEmbeddedType( schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName );
    List<XmlSchemaFacet> facets = parseEmbeddedFacets( schemaIndex, element );
    return new XmlSchemaSimpleContentRestriction( schemaIndex, LOCATION_NOT_YET_DEFINED, attributes, baseTypeName, baseType, facets );
  }

  private static XmlSchemaSimpleContentExtension parseSimpleContentExtension( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, QName redefinedTypeName, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    List<XmlSchemaAttributeOrAttributeGroup> attributes = parseEmbeddedAttributes(schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, context, redefinedGroupName, redefinedAttributeGroupName);
    QName baseTypeName = parseQName( element.getAttributeValue( "base" ), element.getNamespaceContext(), targetNamespace, chameleon );
    if ( baseTypeName.equals( redefinedTypeName ) ) {
      baseTypeName = context.getRedefinedTypes().get( baseTypeName );
    }
    return new XmlSchemaSimpleContentExtension( schemaIndex, LOCATION_NOT_YET_DEFINED, attributes, baseTypeName );
  }

  private static XmlSchemaComplexContent parseComplexContent( XmlSchemaIndex schemaIndex, XmlElement element, String targetNamespace, boolean elementQualifiedDefault, boolean attributeQualifiedDefault, boolean chameleon, QName redefinedTypeName, XmlSchemaParseContext context, QName redefinedGroupName, QName redefinedAttributeGroupName ) {
    XmlSchemaContent content = parseEmbeddedComplexContentContent(schemaIndex, element, targetNamespace, elementQualifiedDefault, attributeQualifiedDefault, chameleon, redefinedTypeName, context, redefinedGroupName, redefinedAttributeGroupName);
    return new XmlSchemaComplexContent( schemaIndex, LOCATION_NOT_YET_DEFINED, content );
  }

  private static QName makeQName( String localPart, String targetNamespace, boolean qualified, Map<String,String> namespaceContext ) {
    if ( localPart == null ) {
      return null;
    }
    if ( qualified ) {
      String prefix;
      if ( targetNamespace.equals( XMLConstants.NULL_NS_URI ) ) {
        prefix = XMLConstants.DEFAULT_NS_PREFIX;
      }
      else {
        prefix = null;
        for ( Map.Entry<String, String> entry : namespaceContext.entrySet() ) {
          if ( entry.getValue().equals( targetNamespace ) ) {
            prefix = entry.getKey();
            break;
          }
        }
      }
      if ( prefix == null ) {
        prefix = XMLConstants.DEFAULT_NS_PREFIX;
      }
      return new QName( targetNamespace, localPart, prefix );
    }
    else {
      return new QName( localPart );
    }
  }

  private static boolean isQualified( String formValue, boolean qualifiedDefault ) {
    if ( formValue == null ) {
      return qualifiedDefault;
    }
    return formValue.equals( "qualified" );
  }

  private static QName parseQNameForWsdl( String attributeValue, Map<String,String> context ) {
    return parseQName( attributeValue, context, null, false );
  }

  public static QName parseQName( String attributeValue, Map<String,String> context, String targetNamespace, boolean chameleon ) {
    if ( attributeValue == null ) {
      return null;
    }
    QName qname = (QName) XmlSimpleValueInternals.instance().makeQNameInstance( attributeValue, context, true ).getGosuValue();
    if ( chameleon && qname.getNamespaceURI().equals( XMLConstants.NULL_NS_URI ) ) {
      qname = new QName( targetNamespace, qname.getLocalPart(), qname.getPrefix() ); // perform chameleon transformation
    }
    return qname;
  }

  private static boolean parseBoolean( String attributeValue, boolean defaultIfNull ) {
    if ( attributeValue == null ) {
      return defaultIfNull;
    }
    return (Boolean) XmlSimpleValueInternals.instance().makeBooleanInstance( attributeValue ).getGosuValue();
  }

  public static WsdlDefinitions parseWsdlDefinitions( XmlSchemaIndex<?> schemaIndex, XmlElement element, URL baseURL, XmlSchemaParseContext context, LocationMap locationMap ) throws IOException {
    String wsdlTargetNamespace = element.getAttributeValue( "targetNamespace" );
    List<WsdlTypes> types = parseWsdlType( schemaIndex, element, baseURL, context, locationMap );
    List<WsdlImport> imports = new ArrayList<WsdlImport>();
    Map<QName, WsdlService> services = new TreeMap<QName, WsdlService>( QNAME_COMPARATOR );
    Map<QName, WsdlBinding> bindings = new TreeMap<QName, WsdlBinding>( QNAME_COMPARATOR );
    Map<QName, WsdlPortType> portTypes = new TreeMap<QName, WsdlPortType>( QNAME_COMPARATOR );
    Map<QName, WsdlMessage> messages = new TreeMap<QName, WsdlMessage>( QNAME_COMPARATOR );
    for ( XmlElement imprt : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "import" ) ) ) {
      imports.add( parseWsdlImport( schemaIndex, imprt ) );
    }
    for ( XmlElement serviceElement : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "service" ) ) ) {
      WsdlService service = parseWsdlService( schemaIndex, serviceElement, context, wsdlTargetNamespace );
      services.put( service.getQName(), service );
    }
    for ( XmlElement bindingElement : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "binding" ) ) ) {
      WsdlBinding binding = parseWsdlBinding( schemaIndex, bindingElement, wsdlTargetNamespace );
      bindings.put( binding.getQName(), binding );
    }
    for ( XmlElement portTypeElement : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "portType" ) ) ) {
      WsdlPortType portType = parseWsdlPortType( schemaIndex, context, portTypeElement, wsdlTargetNamespace );
      portTypes.put( portType.getQName(), portType );
    }
    for ( XmlElement messageElement : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "message" ) ) ) {
      WsdlMessage message = parseWsdlMessage( schemaIndex, messageElement, wsdlTargetNamespace );
      messages.put( message.getQName(), message );
    }
    return new WsdlDefinitions( schemaIndex, LOCATION_NOT_YET_DEFINED, types, imports, services, wsdlTargetNamespace, bindings, portTypes, messages );
  }

  private static WsdlMessage parseWsdlMessage( XmlSchemaIndex<?> schemaIndex, XmlElement element, String wsdlTargetNamespace ) {
    String name = element.getAttributeValue( "name" );
    QName qname = new QName( wsdlTargetNamespace, name );
    Map<String, WsdlPart> partsByName = new TreeMap<String, WsdlPart>();
    List<WsdlPart> parts = new ArrayList<WsdlPart>();
    for ( XmlElement partElement : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "part" ) ) ) {
      WsdlPart part = parseWsdlPart( schemaIndex, partElement );
      partsByName.put( part.getName(), part );
      parts.add( part );
    }
    return new WsdlMessage( schemaIndex, LOCATION_NOT_YET_DEFINED, qname, partsByName, parts );
  }

  private static WsdlPart parseWsdlPart( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    String name = element.getAttributeValue( "name" );
    QName elementQName = parseQNameForWsdl( element.getAttributeValue( "element" ), element.getNamespaceContext() );
    QName typeQName = parseQNameForWsdl( element.getAttributeValue( "type" ), element.getNamespaceContext() );
    return new WsdlPart( schemaIndex, LOCATION_NOT_YET_DEFINED, elementQName, name, typeQName );
  }

  private static WsdlService parseWsdlService( XmlSchemaIndex<?> schemaIndex, XmlElement element, XmlSchemaParseContext context, String wsdlTargetNamespace ) {
    String name = element.getAttributeValue( "name" );
    QName qname = new QName( wsdlTargetNamespace, name );
    List<WsdlPort> ports = new ArrayList<WsdlPort>();
    WsdlService wsdlService = new WsdlService( schemaIndex, LOCATION_NOT_YET_DEFINED, qname, ports );
    for ( XmlElement portElement : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "port" ) ) ) {
      ports.add( parseWsdlPort( schemaIndex, portElement, context, wsdlTargetNamespace, wsdlService ) );
    }
    Collections.sort( ports, PORT_COMPARATOR );
    return wsdlService;
  }

  private static WsdlBinding parseWsdlBinding( XmlSchemaIndex<?> schemaIndex, XmlElement element, String wsdlTargetNamespace ) {
    String name = element.getAttributeValue( "name" );
    QName qname = new QName( wsdlTargetNamespace, name );
    QName portTypeQName = parseQNameForWsdl( element.getAttributeValue( "type" ), element.getNamespaceContext() );
    SoapVersion soapVersion = null;
    WsdlSoapBinding soapBinding = null;
    XmlElement soapBindingElement = element.getChild( new QName( XmlSchemaIndex.SOAP11_WSDL_NAMESPACE, "binding" ) );
    if ( soapBindingElement != null ) {
      soapVersion = SoapVersion.SOAP_11;
      soapBinding = parseWsdlSoapBinding( schemaIndex, soapBindingElement, SoapVersion.SOAP_11 );
    }
    else {
      soapBindingElement = element.getChild( new QName( XmlSchemaIndex.SOAP12_WSDL_NAMESPACE, "binding" ) );
      if ( soapBindingElement != null ) {
        soapVersion = SoapVersion.SOAP_12;
        soapBinding = parseWsdlSoapBinding( schemaIndex, soapBindingElement, SoapVersion.SOAP_12 );
      }
    }
    List<WsdlBindingOperation> bindingOperations = new ArrayList<WsdlBindingOperation>();
    if ( soapVersion != null ) {
      for ( XmlElement bindingOperationElement : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "operation" ) ) ) {
        bindingOperations.add( parseWsdlBindingOperation( schemaIndex, bindingOperationElement, soapVersion ) );
      }
    }
    return new WsdlBinding( schemaIndex, LOCATION_NOT_YET_DEFINED, qname, soapBinding, bindingOperations, portTypeQName );
  }

  private static WsdlPortType parseWsdlPortType( XmlSchemaIndex<?> schemaIndex, XmlSchemaParseContext context, XmlElement element, String wsdlTargetNamespace ) {
    String name = element.getAttributeValue( "name" );
    QName qname = new QName( wsdlTargetNamespace, name );
    Map<String, WsdlPortTypeOperation> portTypeOperations = new TreeMap<String, WsdlPortTypeOperation>();
    for ( XmlElement portTypeOperationElement : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "operation" ) ) ) {
      WsdlPortTypeOperation wsdlPortTypeOperation = parseWsdlPortTypeOperation( schemaIndex, context, portTypeOperationElement );
      portTypeOperations.put( wsdlPortTypeOperation.getName(), wsdlPortTypeOperation );
    }
    return new WsdlPortType( schemaIndex, LOCATION_NOT_YET_DEFINED, qname, portTypeOperations );
  }

  private static WsdlBindingOperation parseWsdlBindingOperation( XmlSchemaIndex<?> schemaIndex, XmlElement element, SoapVersion soapVersion ) {
    String name = element.getAttributeValue( "name" );
    XmlElement soapOperationElement = element.getChild(new QName(XmlSchemaIndex.SOAP12_WSDL_NAMESPACE, "operation"));
    if ( soapOperationElement == null ) {
      soapOperationElement = element.getChild( new QName( XmlSchemaIndex.SOAP11_WSDL_NAMESPACE, "operation" ) );
    }
    WsdlSoapOperation soapOperation = null;
    if ( soapOperationElement != null ) {
      soapOperation = parseWsdlSoapOperation( schemaIndex, soapOperationElement );
    }
    XmlElement bindingInputElement = element.getChild( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "input" ) );
    WsdlBindingInput bindingInput = null;
    if ( bindingInputElement != null ) {
      bindingInput = parseWsdlBindingInput( schemaIndex, bindingInputElement, soapVersion );
    }
    XmlElement bindingOutputElement = element.getChild( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "output" ) );
    WsdlBindingOutput bindingOutput = null;
    if ( bindingOutputElement != null ) {
      bindingOutput = parseWsdlBindingOutput( schemaIndex, bindingOutputElement, soapVersion );
    }
    return new WsdlBindingOperation( schemaIndex, LOCATION_NOT_YET_DEFINED, name, soapOperation, bindingOutput, bindingInput );
  }

  private static WsdlBindingOutput parseWsdlBindingOutput( XmlSchemaIndex<?> schemaIndex, XmlElement element, SoapVersion soapVersion ) {
    WsdlSoapBody soapBody = null;
    List<WsdlSoapHeader> soapHeaders = new ArrayList<WsdlSoapHeader>();
    if ( soapVersion == SoapVersion.SOAP_11 ) {
      XmlElement child = element.getChild( gw.internal.schema.gw.xsd.w3c.soap11.Body.$QNAME );
      if ( child != null ) {
        soapBody = parseWsdlSoapBody( schemaIndex, child );
      }
      for ( XmlElement soapHeaderElement : element.getChildren( new QName( XmlSchemaIndex.SOAP11_WSDL_NAMESPACE, "header" ) ) ) {
        soapHeaders.add( parseWsdlSoapHeader( schemaIndex, soapHeaderElement ) );
      }
    }
    else {
      XmlElement child = element.getChild( gw.internal.schema.gw.xsd.w3c.soap12.Body.$QNAME );
      if ( child != null ) {
        soapBody = parseWsdlSoapBody( schemaIndex, child );
      }
      for ( XmlElement soapHeaderElement : element.getChildren( new QName( XmlSchemaIndex.SOAP12_WSDL_NAMESPACE, "header" ) ) ) {
        soapHeaders.add( parseWsdlSoapHeader( schemaIndex, soapHeaderElement ) );
      }
    }
    return new WsdlBindingOutput( schemaIndex, LOCATION_NOT_YET_DEFINED, soapBody, soapHeaders );
  }

  private static WsdlSoapBody parseWsdlSoapBody( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    String use = element.getAttributeValue( "use" );
    String partsString = element.getAttributeValue("parts");
    Set<String> parts;
    if ( partsString == null ) {
      parts = null;
    }
    else {
      parts = new HashSet<String>( Arrays.asList( partsString.split( "\\s+" ) ) );
    }
    return new WsdlSoapBody( schemaIndex, LOCATION_NOT_YET_DEFINED, use, parts );
  }

  private static WsdlBindingInput parseWsdlBindingInput( XmlSchemaIndex<?> schemaIndex, XmlElement element, SoapVersion soapVersion ) {
    WsdlSoapBody soapBody = null;
    List<WsdlSoapHeader> soapHeaders = new ArrayList<WsdlSoapHeader>();
    if ( soapVersion == SoapVersion.SOAP_11 ) {
      XmlElement child = element.getChild( gw.internal.schema.gw.xsd.w3c.soap11.Body.$QNAME );
      if ( child != null ) {
        soapBody = parseWsdlSoapBody( schemaIndex, child );
      }
      for ( XmlElement soapHeaderElement : element.getChildren( new QName( XmlSchemaIndex.SOAP11_WSDL_NAMESPACE, "header" ) ) ) {
        soapHeaders.add( parseWsdlSoapHeader( schemaIndex, soapHeaderElement ) );
      }
    }
    else {
      XmlElement child = element.getChild( gw.internal.schema.gw.xsd.w3c.soap12.Body.$QNAME );
      if ( child != null ) {
        soapBody = parseWsdlSoapBody( schemaIndex, child );
      }
      for ( XmlElement soapHeaderElement : element.getChildren( new QName( XmlSchemaIndex.SOAP12_WSDL_NAMESPACE, "header" ) ) ) {
        soapHeaders.add( parseWsdlSoapHeader( schemaIndex, soapHeaderElement ) );
      }
    }
    return new WsdlBindingInput( schemaIndex, LOCATION_NOT_YET_DEFINED, soapBody, soapHeaders );
  }

  private static WsdlSoapHeader parseWsdlSoapHeader( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    String use = element.getAttributeValue( "use" );
    String partName = element.getAttributeValue( "part" );
    QName messageQName = parseQNameForWsdl( element.getAttributeValue( "message" ), element.getNamespaceContext() );
    return new WsdlSoapHeader( schemaIndex, LOCATION_NOT_YET_DEFINED, partName, use, messageQName );
  }

  private static WsdlPortTypeOperation parseWsdlPortTypeOperation( XmlSchemaIndex<?> schemaIndex, XmlSchemaParseContext context, XmlElement element ) {
    String name = element.getAttributeValue( "name" );
    XmlElement inputElement = element.getChild( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "input" ) );
    WsdlPortTypeInput input = null;
    if ( inputElement != null ) {
      input = parseWsdlPortTypeInput( schemaIndex, inputElement );
    }
    XmlElement outputElement = element.getChild( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "output" ) );
    WsdlPortTypeOutput output = null;
    if ( outputElement != null ) {
      output = parseWsdlPortTypeOutput( schemaIndex, outputElement );
    }
    List<WsdlPortTypeFault> faults = new ArrayList<WsdlPortTypeFault>();
    for ( XmlElement faultElement : element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "fault" ) ) ) {
      faults.add(parseWsdlPortTypeFault(schemaIndex, faultElement));
    }
    return new WsdlPortTypeOperation( schemaIndex, context.getLocationMap().get( element ), name, input, output, faults );
  }

  private static WsdlPortTypeFault parseWsdlPortTypeFault( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    String name = element.getAttributeValue( "name" );
    QName messageQName = parseQNameForWsdl(element.getAttributeValue("message"), element.getNamespaceContext());
    return new WsdlPortTypeFault( schemaIndex, LOCATION_NOT_YET_DEFINED, name, messageQName );
  }

  private static WsdlPortTypeOutput parseWsdlPortTypeOutput( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    QName messageQName = parseQNameForWsdl( element.getAttributeValue( "message" ), element.getNamespaceContext() );
    return new WsdlPortTypeOutput( schemaIndex, LOCATION_NOT_YET_DEFINED, messageQName );
  }

  private static WsdlPortTypeInput parseWsdlPortTypeInput( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    QName messageQName = parseQNameForWsdl(element.getAttributeValue("message"), element.getNamespaceContext());
    return new WsdlPortTypeInput( schemaIndex, LOCATION_NOT_YET_DEFINED, messageQName );
  }

  private static WsdlSoapOperation parseWsdlSoapOperation( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    String soapAction = element.getAttributeValue( "soapAction" );
    return new WsdlSoapOperation( schemaIndex, LOCATION_NOT_YET_DEFINED, soapAction );
  }

  private static WsdlPort parseWsdlPort( XmlSchemaIndex<?> schemaIndex, XmlElement element, XmlSchemaParseContext context, String wsdlTargetNamespace, WsdlService wsdlService ) {
    QName name = new QName( wsdlTargetNamespace, element.getAttributeValue( "name" ) );
    QName bindingName = parseQNameForWsdl(element.getAttributeValue("binding"), element.getNamespaceContext());
    List<WsdlSoapAddress> soapAddresses = new ArrayList<WsdlSoapAddress>();
    for ( XmlElement soapElement : element.getChildren( new QName( XmlSchemaIndex.SOAP11_WSDL_NAMESPACE, "address" ) ) ) {
      soapAddresses.add( parseWsdlSoapAddress( schemaIndex, soapElement ) );
    }
    for ( XmlElement soapElement : element.getChildren( new QName( XmlSchemaIndex.SOAP12_WSDL_NAMESPACE, "address" ) ) ) {
      soapAddresses.add( parseWsdlSoapAddress( schemaIndex, soapElement ) );
    }
    XmlElement gwAddressElement = element.getChild( Address.$QNAME );
    if ( gwAddressElement == null ) {
      // the original implementation of WSI webservices used a slightly different gw:address element
      // so we look for that here for backwards compatibility so people don't *have* to update their WSDLs.
      gwAddressElement = element.getChild( LEGACY_WSDL_ADDRESS_QNAME );
    }
    WsdlGwAddress wsdlGwAddress = gwAddressElement == null ? null : parseWsdlGwAddress(schemaIndex, gwAddressElement);
    return new WsdlPort( schemaIndex, context.getLocationMap().get( element ), name, bindingName, soapAddresses, wsdlGwAddress, wsdlService );
  }

  private static WsdlSoapBinding parseWsdlSoapBinding( XmlSchemaIndex<?> schemaIndex, XmlElement element, SoapVersion soapVersion ) {
    String style = element.getAttributeValue( "style" );
    String transport = element.getAttributeValue("transport");
    return new WsdlSoapBinding( schemaIndex, LOCATION_NOT_YET_DEFINED, style, transport, soapVersion );
  }

  private static WsdlSoapAddress parseWsdlSoapAddress( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    String location = element.getAttributeValue("location");
    return new WsdlSoapAddress( schemaIndex, LOCATION_NOT_YET_DEFINED, location );
  }

  private static WsdlGwAddress parseWsdlGwAddress( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    String location = element.getAttributeValue("location");
    return new WsdlGwAddress( schemaIndex, LOCATION_NOT_YET_DEFINED, location );
  }

  private static WsdlImport parseWsdlImport( XmlSchemaIndex<?> schemaIndex, XmlElement element ) {
    String location = element.getAttributeValue( "location" );
    String nameSpace = element.getAttributeValue("namespace");
    return new WsdlImport( schemaIndex, LOCATION_NOT_YET_DEFINED, nameSpace, location );
  }


  private static List<WsdlTypes> parseWsdlType( XmlSchemaIndex<?> schemaIndex, XmlElement element, URL baseURL, XmlSchemaParseContext context, LocationMap locationMap ) throws IOException {
    List<WsdlTypes> result = new ArrayList<WsdlTypes>();
    List<XmlElement> types = element.getChildren( new QName( XmlSchemaIndex.WSDL_NAMESPACE, "types" ) );
    if (types != null){
      for (XmlElement xe : types){
        List<XmlSchema> schemas = parseXmlSchemasForTypes( schemaIndex, xe, baseURL, context, locationMap );
        WsdlTypes xmlType = new WsdlTypes( schemaIndex, LOCATION_NOT_YET_DEFINED, schemas );
        result.add(xmlType);
      }
    }
    return result;
  }

  private static List<XmlSchema> parseXmlSchemasForTypes( XmlSchemaIndex<?> schemaIndex, XmlElement element, URL baseURL, XmlSchemaParseContext context, LocationMap locationMap ) throws IOException {
    List<XmlSchema> results = new ArrayList<XmlSchema>();
    if ( element != null ) {
      List<XmlElement> schemas = element.getChildren( new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "schema" ) );
      results = new ArrayList<XmlSchema>( schemas.size() );
      for ( XmlElement schema : schemas ) {
        results.add( parseSchema( schemaIndex, schema, null, baseURL, context, false, null ) );
      }
    }
    return results;
  }

  private static class QNameComparator implements Comparator<QName> {
    public int compare(QName o1, QName o2) {
      return o1.toString().compareTo(o2.toString());
    }
  }

  private static class PortComparator implements Comparator<WsdlPort> {
    @Override
    public int compare( WsdlPort o1, WsdlPort o2 ) {
      return o1.getQName().toString().compareTo( o2.getQName().toString() );
    }
  }

}
