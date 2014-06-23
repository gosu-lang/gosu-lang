/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.internal.ext.org.apache.commons.collections.map.AbstractReferenceMap;
import gw.internal.ext.org.apache.commons.collections.map.ReferenceMap;
import gw.internal.ext.org.apache.xerces.jaxp.validation.XMLSchemaFactory;
import gw.internal.schema.gw.xsd.config.xml.schemalocations.Schemalocations;
import gw.internal.schema.gw.xsd.config.xml.schemalocations.anonymous.elements.Schemalocations_Schema;
import gw.internal.schema.gw.xsd.w3c.wsdl.Definitions;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TDefinitions_Import;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.TDefinitions_Types;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Attribute;
import gw.internal.schema.gw.xsd.w3c.xmlschema.AttributeGroup;
import gw.internal.schema.gw.xsd.w3c.xmlschema.ComplexContent;
import gw.internal.schema.gw.xsd.w3c.xmlschema.ComplexType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Element;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Group;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Import;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Include;
import gw.internal.schema.gw.xsd.w3c.xmlschema.Redefine;
import gw.internal.schema.gw.xsd.w3c.xmlschema.SimpleContent;
import gw.internal.schema.gw.xsd.w3c.xmlschema.SimpleType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ComplexContent_Extension;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ComplexContent_Restriction;
import gw.internal.xml.XmlConstants;
import gw.internal.xml.XmlElementInternals;
import gw.internal.xml.XmlSchemaAccessImpl;
import gw.internal.xml.XmlSchemaLocalResourceResolver;
import gw.internal.xml.xsd.XmlSchemaSource;
import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.internal.xml.xsd.typeprovider.schema.WsdlBinding;
import gw.internal.xml.xsd.typeprovider.schema.WsdlDefinitions;
import gw.internal.xml.xsd.typeprovider.schema.WsdlImport;
import gw.internal.xml.xsd.typeprovider.schema.WsdlMessage;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPortType;
import gw.internal.xml.xsd.typeprovider.schema.WsdlTypes;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchema;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAny;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttributeGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaGroup;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaImport;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContentExtension;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeList;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaSimpleTypeUnion;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.internal.xml.xsd.typeprovider.schemaindexer.XmlSchemaIndexer;
import gw.internal.xml.xsd.typeprovider.schemaparser.XmlSchemaParseContext;
import gw.internal.xml.xsd.typeprovider.schemaparser.XmlSchemaParser;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSchemaEnumSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSchemaListSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleListValueValidator;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleNoopValueValidator;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleTypeSimpleValueValidator;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleUnionValueValidator;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleValueValidator;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IAsmJavaClassInfo;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;
import gw.util.Pair;
import gw.util.StreamUtil;
import gw.util.concurrent.LockingLazyVar;
import gw.util.fingerprint.FP64;
import gw.xml.XmlElement;
import gw.xml.XmlException;
import gw.xml.XmlNamespace;
import gw.xml.XmlSimpleValue;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * Represents an index of a schema or schemas retrieved from a particular file, which could be an XSD or a WSDL.
 * An XSD can by definition only contain one schema, but a WSDL can contain multiple schemas. Since every reference
 * to a QName is relative to the schema where the QName is located, it's important to go back to the original
 * XmlSchemaIndex where the QName is defined to look up the schema object ( group ref, attribute group, etc ).
 *
 * The bulk of this class is devoted to providing various ways to look up information about the schemas represented
 * by this schema index, such as looking up a top level xsd type by QName, or looking up an IType by XmlSchemaObject,
 * etc. All methods that take an XmlSchemaObject are static (or should be), since every XmlSchemaObject knows its
 * schema index. Any methods that return an XmlSchemaObject can possibly return one from a schema index
 * other than the one that was queried. This is due to the way that schemas can interact with each
 * other. For example, if this is a user-defined schema in the urn:foo namespace, asking the schema index for the
 * type xsd:int ( using getXmlSchemaTypeByQName() ) will in fact return an XmlSchemaObject that contains a
 * reference to the &quot;xml schema&quot; schema index ( the one in the gw.xsd.w3c.xmlschema gosu namespace ).
 */
public class XmlSchemaIndex<T> {

  public static final String SOAP11_WSDL_NAMESPACE = gw.internal.schema.gw.xsd.w3c.soap11.Body.$QNAME.getNamespaceURI();
  public static final String SOAP12_WSDL_NAMESPACE = gw.internal.schema.gw.xsd.w3c.soap12.Body.$QNAME.getNamespaceURI();
  private static final Map<String, String> _normalizedSchemaNamespacesByPath = new HashMap<String, String>();
  private static final Set<String> _normalizedSchemaNamespaces = new HashSet<String>();

  // {http://foo.bar} -> gw.xml.whatever -- for finding a target schema by XML namespace relative to this schema
  private LockingLazyVar<LinkedHashMap<String, LinkedHashSet<String>>> _gosuNamespacesByXMLNamespace = new LockingLazyVar<LinkedHashMap<String, LinkedHashSet<String>>>(TypeSystem.getGlobalLock()) {
    protected LinkedHashMap<String, LinkedHashSet<String>> init() {
      try {
        maybeIndex( null );
        LinkedHashMap<String, LinkedHashSet<String>> gosuNamespacesByXMLNamespace = new LinkedHashMap<String, LinkedHashSet<String>>();

        String targetGosuNamespace = _packageName;
        LinkedHashSet<XmlSchema> schemas = _collection.getXmlSchemas();
        addSchemasToNamespaceMappings( targetGosuNamespace, schemas, gosuNamespacesByXMLNamespace );

        if (getWsdlDefinitions() != null) {
          for ( WsdlImport parsedImport : getWsdlDefinitions().getWsdlImports() ){
            String location = parsedImport.getLocation();
            URL url = makeLocalIfValid( _schemaEF, location, parsedImport.getNameSpace(), getTypeLoader().getModule() );
            if ( url == null ) {
              throw new RuntimeException( "WSDL " + location + " with namespace " + parsedImport.getNameSpace() + " not found relative to " + _schemaEF );
            }
            String gosuNamespace = getGosuNamespace( url, getTypeLoader().getModule() );
            XmlSchemaIndex importIndex = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( getTypeLoader().getModule(), gosuNamespace );
            addSchemasToNamespaceMappings( gosuNamespace, importIndex.getXmlSchemaCollection().getXmlSchemas(), gosuNamespacesByXMLNamespace );
          }
          addGosuNamespaceByXmlNamespace( gosuNamespacesByXMLNamespace, "http://schemas.xmlsoap.org/soap/encoding/", "gw.xsd.w3c.soap11_encoding" );
        }

        return gosuNamespacesByXMLNamespace;
      }
      catch ( Exception ex ) {
        throw new RuntimeException( "Error indexing namespaces in schema " + _packageName, ex );
      }
    }
  };

  private LockingLazyVar<LinkedHashMap<String, String>> _gosuNamespacesByWsdlNamespace = new LockingLazyVar<LinkedHashMap<String, String>>() {
    protected LinkedHashMap<String, String> init() {
      try {
        maybeIndex( null );
        LinkedHashMap<String, String> gosuNamespacesByWsdlNamespace = new LinkedHashMap<String, String>();
        if (getWsdlDefinitions() != null){
          gosuNamespacesByWsdlNamespace.put( getWsdlDefinitions().getTargetNamespace(), _packageName );
          for ( WsdlImport parsedImport : getWsdlDefinitions().getWsdlImports() ){
            String location = parsedImport.getLocation();
            String importNamespace = parsedImport.getNameSpace();
            URL url = makeLocalIfValid( _schemaEF, location, importNamespace, getTypeLoader().getModule() );
            if ( url == null ) {
              throw new RuntimeException( "WSDL " + location + " with namespace " + importNamespace + " not found relative to " + _schemaEF );
            }
            String gosuNamespace = getGosuNamespace( url, getTypeLoader().getModule() );
            gosuNamespacesByWsdlNamespace.put( importNamespace, gosuNamespace );
          }
        }
        return gosuNamespacesByWsdlNamespace;
      }
      catch ( Exception ex ) {
        throw new RuntimeException( "Error indexing namespaces in WSDL " + _packageName, ex );
      }
    }
  };

  public static void clear() {
    _normalizedSchemaNamespacesByPath.clear();
    _normalizedSchemaNamespaces.clear();
    _multiCompiledSchemaCache.clear();
    _singleCompiledSchemaCache.clear();
    _defaultSchemaLocations.clear();
    _codegenSchemasByModuleRoot.clear();
    IGNORE_JAVA_CLASSES.clear();
  }

  //Storing the wsdl definition parsed from xml to use during lazy initialization
  private WsdlDefinitions _wsdlDefinition = null;

  // The package name of this schema index... For example gw.xsd.w3c.xmlschema
  private String _packageName;

  // The typeloader that created this XmlSchemaIndex
  protected final XmlSchemaResourceTypeLoaderBase<T> _typeLoader;

  // An optional context object - to be used by GX and other XSD-based systems
  private final T _context;

  // The source of this schema ( url it was loaded from, the actual schema bytes, etc )
  private final XmlSchemaSource _xmlSchemaSource;

  // A cache of all type names created by this schema index
  private final Set<String> _allTypeNames = new HashSet<String>();

  // A cache of all type data created by this schema index, indexed by name
  private final Map<String, IXmlSchemaTypeData> _typesByName = new HashMap<String, IXmlSchemaTypeData>();

  // Translation from a schema object ( such as a complexType ) to an IType representing it
  private final Map<XmlSchemaObject, IXmlSchemaTypeData> _typesBySchemaObject = new HashMap<XmlSchemaObject, IXmlSchemaTypeData>();

  // The original schema collection for the schemas represented by this schema index
  private final XmlSchemaCollection _collection;

  // The flattened possible child elements of each schema type ( complex/simple )
  private Map<XmlSchemaType, List<XmlSchemaFlattenedChild>> _flattenedChildrenBySchemaType = new HashMap<XmlSchemaType, List<XmlSchemaFlattenedChild>>();

  // The plurality of the children of each schema type
  private Map<XmlSchemaType, Map<Pair<XmlSchemaPropertyType, QName>, Boolean>> _childPluralityBySchemaType = new HashMap<XmlSchemaType, Map<Pair<XmlSchemaPropertyType, QName>, Boolean>>();

  // A flag to indicate whether or not this schema index has been indexed yet ( types created )
  private boolean _indexed = false;

  // xsd:anySimpleType - bootstrap type, not actually defined in the schema schema
  // resist the urge to replace this with AnySimpleType.$QNAME, as that would cause generated code to rely on generated code in the case of a codegen
  public static final QName ANY_SIMPLE_TYPE_QNAME = new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "anySimpleType", "xs" );

  // xsd:anyType
  // resist the urge to replace this with AnyType.$QNAME, as that would cause generated code to rely on generated code in the case of a codegen
  public static final QName ANY_TYPE_QNAME = new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, "anyType", "xs" );

  // Enumerations defined in the schema index, mapped by their xsd type
  private final Map<XmlSchemaType, XmlSchemaEnumerationTypeData> _enumerations = new HashMap<XmlSchemaType, XmlSchemaEnumerationTypeData>();

  public static final String WSDL_NAMESPACE = gw.internal.schema.gw.xsd.w3c.wsdl.Definitions.$QNAME.getNamespaceURI();

  // TODO dlank - could use a better caching strategy in the future
  private static final ReferenceMap _multiCompiledSchemaCache = new ReferenceMap( AbstractReferenceMap.SOFT, AbstractReferenceMap.HARD );
  private static final WeakHashMap<XmlSchemaIndex, Schema> _singleCompiledSchemaCache = new WeakHashMap<XmlSchemaIndex, Schema>();

  // the list of "built-in" datatypes. Not every simple type defined in the schema schema is considered present in
  // every schema, except the following. The only way to distinguish the simples types that are available from
  // those that are not available is an annotation in the schema schema that has some human-readable documentation
  // which says a particular simple type is not available, or by reading the schema specification. So I believe we
  // need to inline this list here. See XmlSchemaBuiltInTypesAreUpToDateTest, which ensures that this list is
  // up to date with what Xerces thinks it should be.
  public static Set<String> BUILT_IN_DATATYPES = Collections.unmodifiableSet( new HashSet<String>( Arrays.asList(
          "string", "boolean", "float", "double", "decimal", "duration", "dateTime", "time", "date",
          "gYearMonth", "gYear", "gMonthDay", "gDay", "gMonth", "hexBinary", "base64Binary",
          "anyURI", "QName", "NOTATION", "normalizedString", "token", "language", "IDREFS", "ENTITIES",
          "NMTOKEN", "NMTOKENS", "Name", "NCName", "ID", "IDREF", "ENTITY", "integer", "nonPositiveInteger",
          "negativeInteger", "long", "int", "short", "byte", "nonNegativeInteger", "unsignedLong",
          "unsignedInt", "unsignedShort", "unsignedByte", "positiveInteger",
          "anyType", "anySimpleType"
  )
  ) );
  private static final Map<IModule, XmlSchemaDefaultSchemaLocations> _defaultSchemaLocations = new HashMap<IModule, XmlSchemaDefaultSchemaLocations>();
  private static final Map<IDirectory,Set<String>> _codegenSchemasByModuleRoot = new HashMap<IDirectory, Set<String>>();
  private LockingLazyVar<XmlSchemaAccessImpl> _xmlSchemaAccess = new LockingLazyVar<XmlSchemaAccessImpl>() {
    @Override
    protected XmlSchemaAccessImpl init() {
      IType type = TypeSystem.getByFullName( getTypeLoader().getSchemaSchemaTypeName() );
      try {
        return new XmlSchemaAccessImpl( type, _xmlSchemaSource, XmlSchemaIndex.this );
      }
      catch ( URISyntaxException e ) {
        throw new RuntimeException( e );
      }
    }
  };
  public static final String REDEFINE_PREFIX = "::redefine";
  private final boolean _usesJavaBackedTypes;
  private final URL _schemaEF;

  private final LockingLazyVar<Long> _schemaFP64 = new LockingLazyVar<Long>() {
    @Override
    protected Long init() {
      try {
        byte[] bytes = StreamUtil.getContent( _xmlSchemaSource.getInputStream( true ) );
        FP64 fp64 = new FP64();
        for ( byte b : bytes ) {
          if ( b != 10 && b != 13 ) {
            fp64.extend( b ); // a failure to notice a difference is not catastrophic, so we just strip 13/10
          }
        }
        return fp64.getRawFingerprint();
      }
      catch ( IOException ex ) {
        throw new RuntimeException( ex );
      }
    }
  };
  private static final LockingLazyVar<Boolean> IGNORE_JAVA_CLASSES = new LockingLazyVar<Boolean>() {
    @Override
    protected Boolean init() {
      return System.getProperty( XmlSchemaResourceTypeLoaderBase.IGNORE_JAVA_CLASSES_PROPERTY ) != null;
    }
  };

  public XmlSchemaIndex( XmlSchemaResourceTypeLoaderBase<T> typeLoader, String packageName, XmlSchemaSource xmlSchemaSource, T context) {
       this( typeLoader, packageName, xmlSchemaSource, context, null );
  }

  public XmlSchemaIndex( XmlSchemaResourceTypeLoaderBase<T> typeLoader, String packageName, XmlSchemaSource xmlSchemaSource, T context, Map<Pair<URL,String>, XmlSchema> caches  ) {
    _typeLoader = typeLoader;
    _usesJavaBackedTypes = ! IGNORE_JAVA_CLASSES.get() && getAllCodegenSchemas().contains( packageName );
    _context = context;
    _packageName = packageName;
    _xmlSchemaSource = xmlSchemaSource;
    _collection = new XmlSchemaCollection();
    _schemaEF = _xmlSchemaSource.getBlueprintURL();


     //TODO APEX-PULL cauuses stack overflow
//    try {
//      InputStream inputStream = _xmlSchemaSource.getInputStream( true );
//      readSchema( _schemaEF, inputStream, null, caches );
//      if ( _wsdlDefinition == null ) {
//        parseSchemasButDoNotCache( Collections.<XmlSchemaIndex>singletonList( this ), null, null );
//      }
//      else {
//        // XmlElement additionalWsdl = XmlElement.parse( _xmlSchemaSource.getInputStream( true ) );
//        // parseSchemasButDoNotCache( Collections.<XmlSchemaIndex>emptyList(), additionalWsdl );
//      }
//    }
//    catch ( Exception ex ) {
//      ex.printStackTrace();
//      if ( getXSDSource().getBlueprintURL().getProtocol().equals( "jar" ) || getTypeLoader().getClass().getName().equals( "com.guidewire.commons.system.gx.GXTypeLoader" ) ) {
//        clearExternalJarXSD();
//      }
//      else {
//        StringBuilder sb = new StringBuilder();
//        sb.append( "Could not parse schema " );
//        sb.append( _xmlSchemaSource.getDescription() );
//        throw new XmlException( sb.toString(), ex );
//      }
//    }
  }

  public static Schema parseSchemas( List<XmlSchemaIndex> schemaIndexes ) throws SAXException, MalformedURLException, URISyntaxException {
    Schema compiledSchema;
    if ( schemaIndexes.size() == 1 ) {
      compiledSchema = _singleCompiledSchemaCache.get( schemaIndexes.get( 0 ) );
    }
    else {
      compiledSchema = (Schema) _multiCompiledSchemaCache.get( schemaIndexes );
    }
    if ( compiledSchema == null ) {
      compiledSchema = parseSchemasButDoNotCache( schemaIndexes, null, null );
      if ( schemaIndexes.size() == 1 ) {
        _singleCompiledSchemaCache.put( schemaIndexes.get( 0 ), compiledSchema );
      }
      else {
        _multiCompiledSchemaCache.put( schemaIndexes, compiledSchema );
      }
    }
    return compiledSchema;
  }

  public static Schema parseSchemasButDoNotCache( List<XmlSchemaIndex> schemaIndexes, XmlElement additionalWsdl, IType webserviceType ) throws MalformedURLException, URISyntaxException, SAXException {
    Map<String, List<XmlElement>> schemaGraph = getSchemaGraphAndInlineIncludes( schemaIndexes, additionalWsdl, webserviceType );
    Map<String, XmlElement> reducedSchemaGraph = reduceSchemaGraph( schemaGraph );
    return compileSchemas( reducedSchemaGraph, schemaIndexes, webserviceType, additionalWsdl );
  }

  private static Schema compileSchemas( Map<String, XmlElement> reducedSchemaGraph, List<XmlSchemaIndex> schemaIndexes, IType webserviceType, XmlElement additionalWsdl ) throws SAXException {

    Map<String, byte[]> reducedSchemaGraphBytes = new HashMap<String, byte[]>();
    List<Source> sources = new ArrayList<Source>();
    for ( Map.Entry<String, XmlElement> entry : reducedSchemaGraph.entrySet() ) {
      byte[] bytes = entry.getValue().bytes();
      reducedSchemaGraphBytes.put( entry.getKey(), bytes );
      sources.add( new StreamSource( new ByteArrayInputStream( bytes ), "{" + entry.getKey() + "}" ) );
    }
    XMLSchemaFactory schemaFactory = new XMLSchemaFactory();
    schemaFactory.setResourceResolver( new XmlSchemaCachedResourceResolver( reducedSchemaGraphBytes ) );
    // Our XSD featureset heavily assumes only valid schemas are being used, following all rules of the schema
    // specification, such as the Unique Particle Attribution rule.
    schemaFactory.setFeature( "http://apache.org/xml/features/validation/schema-full-checking", true );
    try {
      return schemaFactory.newSchema( sources.toArray( new Source[ sources.size() ] ) );
    }
    catch ( SAXParseException ex ) {
      System.out.println( "---------------------------------------" );
      System.out.println( "[XSD] Error encountered while parsing schema for namespace " + ex.getSystemId() );
      System.out.println( "[XSD]   " + ex.getMessage() );
      System.out.println( "[XSD]  at line " + ex.getLineNumber() + " column " + ex.getColumnNumber() );
      System.out.println( "[XSD] Gosu namespaces processed:" );
      for ( XmlSchemaIndex schemaIndex : schemaIndexes ) {
        System.out.println( "[XSD]   " + schemaIndex.getPackageName() );
      }
      if ( additionalWsdl != null ) {
        System.out.println( "[XSD]   Additional WSDL: " + webserviceType.getName() );
      }
      System.out.println( "---------------------------------------" );
      System.out.println( "[XSD] Begin Problematic schemas" );
      for ( XmlElement schema : reducedSchemaGraph.values() ) {
        System.out.println( "---------------------------------------" );
        schema.print();
      }
      System.out.println( "---------------------------------------" );
      System.out.println( "[XSD] End Problematic schemas" );
      System.out.println( "---------------------------------------" );
      ex.printStackTrace();
      throw ex;
    }
  }

  private static Map<String, XmlElement> reduceSchemaGraph( Map<String, List<XmlElement>> schemaGraph ) throws URISyntaxException {
    Map<String,XmlElement> reducedSchemaGraph = new HashMap<String, XmlElement>();
    for ( Map.Entry<String, List<XmlElement>> entry : schemaGraph.entrySet() ) {
      String targetNamespace = entry.getKey();
      List<XmlElement> schemas = entry.getValue();
      if ( schemas.size() == 1 ) {
        for ( XmlElement schema : schemas ) {
          for ( XmlElement importElement : schema.getChildren( Import.$QNAME ) ) {
            importElement.setAttributeValue( "schemaLocation", null ); // clear schemaLocation attribute
          }
        }
        reducedSchemaGraph.put( targetNamespace, schemas.get( 0 ) );
      }
      else {
        // combine schemas with same targetNamespace into a single schema
        XmlNamespace schemaNS = XmlNamespace.forQName( gw.internal.schema.gw.xsd.w3c.xmlschema.Schema.$QNAME );
        Map<String,Map<String,XmlElement>> componentTypeToNameToElementMap = new HashMap<String, Map<String, XmlElement>>(); 
        Set<String> importedNamespaces = new HashSet<String>();
        List<XmlElement> schemaComponentsToAdd = new ArrayList<XmlElement>();
        for ( XmlElement schema : schemas ) {
          for ( XmlElement child : schema.getChildren( schemaNS.qualify( "import" ) ) ) {
            importedNamespaces.add( child.getAttributeValue( "namespace" ) );
          }
          for ( XmlElement child : schema.getChildren() ) {
            String componentName = child.getAttributeValue( "name" );
            if ( componentName != null ) {
              String componentType = child.getQName().getLocalPart();
              if ( componentType.equals( "complexType" ) || componentType.equals( "simpleType" ) ) {
                componentType = "type"; // complexType and simpleType share a namespace
              }
              Map<String, XmlElement> nameToElementMap = componentTypeToNameToElementMap.get( componentType );
              if ( nameToElementMap == null ) {
                nameToElementMap = new HashMap<String, XmlElement>();
                componentTypeToNameToElementMap.put( componentType, nameToElementMap );
              }
              XmlElement oldChild = nameToElementMap.put( componentName, child );
              if ( oldChild == null ) {
                schemaComponentsToAdd.add( child );
              }
            }
          }
        }
        XmlElement newSchema = new XmlElement( schemaNS.qualify( "schema" ) );
        newSchema.setAttributeValue( "targetNamespace", targetNamespace );

        // create import statements
        for ( String importedNamespace : importedNamespaces ) {
          XmlElement importElement = new XmlElement( schemaNS.qualify( "import" ) );
          importElement.setAttributeValue( "namespace", importedNamespace );
          newSchema.addChild( importElement );
        }

        for ( XmlElement schemaComponent : schemaComponentsToAdd ) {
          newSchema.addChild( schemaComponent );
        }

        reducedSchemaGraph.put( targetNamespace, newSchema );
      }
    }
    // step through and change each "import namespace" and each "targetNamespace" from empty string to null
    for ( XmlElement schema : reducedSchemaGraph.values() ) {
      if ( "".equals( schema.getAttributeValue( "targetNamespace" ) ) ) {
        schema.setAttributeValue( "targetNamespace", null );
      }
      for ( XmlElement imprt : schema.getChildren( Import.$QNAME ) ) {
        if ( "".equals( imprt.getAttributeValue( "namespace" ) ) ) {
          imprt.setAttributeValue( "namespace", null );
        }
      }
    }
    return reducedSchemaGraph;
  }

  private static void walkSchemaBeforeCombination( XmlElement element, boolean qualifyElements, boolean qualifyAttributes, boolean topLevel, String elementBlockDefault, String complexTypeBlockDefault, String elementFinalDefault, String complexTypeFinalDefault, String simpleTypeFinalDefault ) {
    if ( element.getQName().equals( Element.$QNAME ) ) {
      if ( ! topLevel && qualifyElements && element.getAttributeValue( "name" ) != null && element.getAttributeValue( "form" ) == null ) {
        element.setAttributeValue( "form", "qualified" );
      }
      if ( elementBlockDefault != null && element.getAttributeValue( "block" ) == null && element.getAttributeValue( "name" ) != null ) {
        element.setAttributeValue( "block", elementBlockDefault );
      }
      if ( topLevel && elementFinalDefault != null && element.getAttributeValue( "final" ) == null ) {
        element.setAttributeValue( "final", elementFinalDefault );
      }
    }
    else if ( element.getQName().equals( Attribute.$QNAME ) ) {
      if ( ! topLevel && qualifyAttributes && element.getAttributeValue( "name" ) != null && element.getAttributeValue( "form" ) == null ) {
        element.setAttributeValue( "form", "qualified" );
      }
    }
    else if ( element.getQName().equals( ComplexType.$QNAME ) ) {
      if ( topLevel && complexTypeBlockDefault != null && element.getAttributeValue( "block" ) == null ) {
        element.setAttributeValue( "block", complexTypeBlockDefault );
      }
      if ( topLevel && complexTypeFinalDefault != null && element.getAttributeValue( "final" ) == null ) {
        element.setAttributeValue( "final", complexTypeFinalDefault );
      }
    }
    else if ( element.getQName().equals( SimpleType.$QNAME ) ) {
      if ( topLevel && simpleTypeFinalDefault != null && element.getAttributeValue( "final" ) == null ) {
        element.setAttributeValue( "final", simpleTypeFinalDefault );
      }
    }
    for ( XmlElement child : element.getChildren() ) {
      walkSchemaBeforeCombination( child, qualifyElements, qualifyAttributes, false, elementBlockDefault, complexTypeBlockDefault, elementFinalDefault, complexTypeFinalDefault, simpleTypeFinalDefault );
    }
  }

  private static Map<String, List<XmlElement>> getSchemaGraphAndInlineIncludes( List<XmlSchemaIndex> schemaIndexes, XmlElement additionalWsdl, IType webserviceType ) throws MalformedURLException {
    Set<Pair<URL,String>> newSchemaIndexesToProcess = new HashSet<Pair<URL, String>>();
    Map<URL,String> resourceUrls = new HashMap<URL, String>();
    Map<String,List<XmlElement>> schemasByTargetNamespace = new HashMap<String, List<XmlElement>>();

    if ( additionalWsdl != null ) {
      IModule currentModule = webserviceType.getTypeLoader().getModule();
      String webserviceTypePath = webserviceType.getName().replace( '.', '/' );
      String webserviceTypeResourcePath = webserviceTypePath + ".gs";
      IGosuClass gosuClass = (IGosuClass) webserviceType;
      URL schemaUrl = gosuClass.getTypeLoader().getRepository().findResource( webserviceTypeResourcePath );
      if ( schemaUrl == null ) {
        throw new IllegalStateException( "Unable to find resource " + webserviceTypeResourcePath + " in module " + currentModule );
      }
      for ( XmlElement wsdlImportElement : additionalWsdl.getChildren( TDefinitions_Import.$QNAME ) ) {
        String location = wsdlImportElement.getAttributeValue( "location" );
        URL newUrl = makeLocal( schemaUrl, location, null, currentModule );
        newSchemaIndexesToProcess.add( new Pair<URL, String>( newUrl, null ) );
      }

      for ( XmlElement typesElement : additionalWsdl.getChildren( TDefinitions_Types.$QNAME ) ) {
        for ( XmlElement schema : typesElement.getChildren() ) {
          addSchemaToSchemasByNamespaceMapAndInlineIncludes( schemasByTargetNamespace, schema, newSchemaIndexesToProcess, schemaUrl );
        }
      }
    }
    for ( XmlSchemaIndex<?> schemaIndexToProcess : schemaIndexes ) {
      URL blueprintURL = schemaIndexToProcess.getXSDSource().getBlueprintURL();
      newSchemaIndexesToProcess.add( new Pair<URL, String>( blueprintURL, null ) );
    }
    Set<Pair<URL,String>> schemaIndexesToProcess = newSchemaIndexesToProcess;
    while ( ! schemaIndexesToProcess.isEmpty() ) {
      newSchemaIndexesToProcess = new HashSet<Pair<URL, String>>();
      for ( Pair<URL, String> pair : schemaIndexesToProcess ) {
        URL blueprintUrl = pair.getFirst();
        String requiredTargetNamespace = pair.getSecond();
        if ( resourceUrls.containsKey( blueprintUrl ) ) {
          if ( requiredTargetNamespace != null ) {
            String oldTargetNamespace = resourceUrls.put( blueprintUrl, requiredTargetNamespace );
            if ( oldTargetNamespace != null ) {
              if ( ! requiredTargetNamespace.equals( oldTargetNamespace ) ) {
                throw new XmlException( "The namespace attribute, '" + requiredTargetNamespace + "', of an <import> element information item must be identical to the targetNamespace attribute, '" + oldTargetNamespace + "', of the imported document" );
              }
            }
          }
        }
        else {
          // convert URL to schema index and process it
          getSchemaAndInlineIncludes( blueprintUrl, schemasByTargetNamespace, newSchemaIndexesToProcess, requiredTargetNamespace, resourceUrls );
        }
      }
      schemaIndexesToProcess = newSchemaIndexesToProcess;
    }
    return schemasByTargetNamespace;
  }

  private static void getSchemaAndInlineIncludes( URL blueprintURL, Map<String, List<XmlElement>> schemasByTargetNamespace, Set<Pair<URL, String>> newSchemaIndexesToProcess, String requiredNamespace, Map<URL, String> resourceUrls ) throws MalformedURLException {
      IModule currentModule = TypeSystem.getGlobalModule();
      String gosuNamespace = XmlSchemaIndex.getGosuNamespace( blueprintURL, currentModule );
      XmlSchemaIndex<?> referencedSchemaIndex = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( currentModule, gosuNamespace );
      if ( referencedSchemaIndex == null ) {
        if ( CommonServices.getXmlSchemaCompatibilityConfig().useCompatibilityMode( gosuNamespace ) ) {
          throw new XmlException( "A non-compatibility-mode schema cannot import a compatibility-mode schema (" + gosuNamespace + ")" );
        }
        else {
          throw new IllegalStateException( "Schema not found for Gosu namespace " + gosuNamespace );
        }
      }
      XmlElement schemaOrWsdl = XmlElement.parse( referencedSchemaIndex.getXSDSource().getInputStream( false ) );
      if ( schemaOrWsdl.getQName().equals( Definitions.$QNAME ) ) {
        resourceUrls.put( blueprintURL, null );
        for ( XmlElement wsdlImportElement : schemaOrWsdl.getChildren( TDefinitions_Import.$QNAME ) ) {
          String location = wsdlImportElement.getAttributeValue( "location" );
          URL newUrl = makeLocal( blueprintURL, location, null, referencedSchemaIndex.getTypeLoader().getModule() );
          newSchemaIndexesToProcess.add( new Pair<URL, String>( newUrl, null ) );
        }
        for ( XmlElement typesElement : schemaOrWsdl.getChildren( TDefinitions_Types.$QNAME ) ) {
          for ( XmlElement schema : typesElement.getChildren() ) {
            addSchemaToSchemasByNamespaceMapAndInlineIncludes( schemasByTargetNamespace, schema, newSchemaIndexesToProcess, blueprintURL );
          }
        }
      }
      else {
        String tns = schemaOrWsdl.getAttributeValue( "targetNamespace" );
        if ( tns == null ) {
          tns = "";
        }
        if ( requiredNamespace != null ) {
          if ( ! tns.equals( requiredNamespace ) ) {
            throw new XmlException( "The namespace attribute, '" + requiredNamespace + "', of an <import> element information item must be identical to the targetNamespace attribute, '" + tns + "', of the imported document" );
          }
        }
        resourceUrls.put( blueprintURL, tns );
        addSchemaToSchemasByNamespaceMapAndInlineIncludes( schemasByTargetNamespace, schemaOrWsdl, newSchemaIndexesToProcess, blueprintURL );
      }
  }

  private static void addSchemaToSchemasByNamespaceMapAndInlineIncludes( Map<String, List<XmlElement>> schemasByTargetNamespace, XmlElement schema, Set<Pair<URL, String>> newSchemaIndexesToProcess, URL blueprintUrl ) throws MalformedURLException {
    String targetNamespace = schema.getAttributeValue( "targetNamespace" );
    if ( targetNamespace == null ) {
      targetNamespace = "";
    }

    Set<URL> alreadyIncluded = new HashSet<URL>();    
    Map<String,Map<String,XmlElement>> resultingComponents = new HashMap<String, Map<String, XmlElement>>();
    Set<String> resultingImportedNamespaces = new HashSet<String>();

    processIncludedSchema( schema, blueprintUrl, targetNamespace, resultingComponents, resultingImportedNamespaces, newSchemaIndexesToProcess, alreadyIncluded );

    XmlElement resultingSchema = new XmlElement( gw.internal.schema.gw.xsd.w3c.xmlschema.Schema.$QNAME );
    resultingSchema.setAttributeValue( "targetNamespace", targetNamespace );

    for ( String resultingImportedNamespace : resultingImportedNamespaces ) {
      XmlElement importElement = new XmlElement( Import.$QNAME );
      importElement.setAttributeValue( "namespace", resultingImportedNamespace );
      resultingSchema.addChild( importElement );
    }

    for ( Map<String, XmlElement> submap : resultingComponents.values() ) {
      for ( XmlElement componentElement : submap.values() ) {
        resultingSchema.addChild( componentElement );
      }
    }

    List<XmlElement> schemas = schemasByTargetNamespace.get( targetNamespace );
    if ( schemas == null ) {
      schemas = new ArrayList<XmlElement>();
      schemasByTargetNamespace.put( targetNamespace, schemas );
    }
    schemas.add( resultingSchema );

  }

  private static void processIncludedSchema( XmlElement schema, URL blueprintUrl, String targetNamespace, Map<String, Map<String, XmlElement>> resultingComponents, Set<String> resultingImportedNamespaces, Set<Pair<URL, String>> newSchemaIndexesToProcess, Set<URL> alreadyIncluded ) throws MalformedURLException {

    boolean elementFormDefault = "qualified".equals( schema.getAttributeValue( "elementFormDefault" ) );
    boolean attributeFormDefault = "qualified".equals( schema.getAttributeValue( "attributeFormDefault" ) );
    String blockDefault = schema.getAttributeValue( "blockDefault" );
    boolean blockDefaultExtension = blockDefault != null && ( blockDefault.contains( "extension" ) || blockDefault.contains( "#all" ) );
    boolean blockDefaultRestriction = blockDefault != null && ( blockDefault.contains( "restriction" ) || blockDefault.contains( "#all" ) );
    boolean blockDefaultSubstitution = blockDefault != null && ( blockDefault.contains( "substitution" ) || blockDefault.contains( "#all" ) );
    String finalDefault = schema.getAttributeValue( "finalDefault" );
    boolean finalDefaultExtension = finalDefault != null && ( finalDefault.contains( "extension" ) || finalDefault.contains( "#all" ) );
    boolean finalDefaultRestriction = finalDefault != null && ( finalDefault.contains( "restriction" ) || finalDefault.contains( "#all" ) );
    boolean finalDefaultList = finalDefault != null && ( finalDefault.contains( "list" ) || finalDefault.contains( "#all" ) );
    boolean finalDefaultUnion = finalDefault != null && ( finalDefault.contains( "union" ) || finalDefault.contains( "#all" ) );
    
    final String elementBlockDefault;
    final String complexTypeBlockDefault;
    final String elementFinalDefault;
    final String complexTypeFinalDefault;
    final String simpleTypeFinalDefault;

    if ( blockDefaultExtension && blockDefaultRestriction && blockDefaultSubstitution ) {
      elementBlockDefault = "#all";
    }
    else if ( blockDefaultExtension || blockDefaultRestriction || blockDefaultSubstitution ) {
      StringBuilder elementBlockValue = new StringBuilder();
      addToList( blockDefaultExtension, elementBlockValue, "extension" );
      addToList( blockDefaultRestriction, elementBlockValue, "restriction" );
      addToList( blockDefaultSubstitution, elementBlockValue, "substitution" );
      elementBlockDefault = elementBlockValue.toString();
    }
    else {
      elementBlockDefault = null;
    }

    if ( blockDefaultExtension && blockDefaultRestriction ) {
      complexTypeBlockDefault = "#all";
    }
    else if ( blockDefaultExtension || blockDefaultRestriction || blockDefaultSubstitution ) {
      StringBuilder complexTypeBlockValue = new StringBuilder();
      addToList( blockDefaultExtension, complexTypeBlockValue, "extension" );
      addToList( blockDefaultRestriction, complexTypeBlockValue, "restriction" );
      complexTypeBlockDefault = complexTypeBlockValue.toString();
    }
    else {
      complexTypeBlockDefault = null;
    }

    if ( finalDefaultExtension && finalDefaultRestriction ) {
      elementFinalDefault = "#all";
    }
    else if ( finalDefaultExtension || finalDefaultRestriction ) {
      StringBuilder elementFinalValue = new StringBuilder();
      addToList( finalDefaultExtension, elementFinalValue, "extension" );
      addToList( finalDefaultRestriction, elementFinalValue, "restriction" );
      elementFinalDefault = elementFinalValue.toString();
    }
    else {
      elementFinalDefault = null;
    }

    if ( finalDefaultExtension && finalDefaultRestriction ) {
      complexTypeFinalDefault = "#all";
    }
    else if ( finalDefaultExtension || finalDefaultRestriction ) {
      StringBuilder complexTypeFinalValue = new StringBuilder();
      addToList( finalDefaultExtension, complexTypeFinalValue, "extension" );
      addToList( finalDefaultRestriction, complexTypeFinalValue, "restriction" );
      complexTypeFinalDefault = complexTypeFinalValue.toString();
    }
    else {
      complexTypeFinalDefault = null;
    }

    if ( finalDefaultRestriction && finalDefaultList && finalDefaultUnion ) {
      simpleTypeFinalDefault = "#all";
    }
    else if ( finalDefaultRestriction && finalDefaultList && finalDefaultUnion ) {
      StringBuilder simpleTypeFinalValue = new StringBuilder();
      addToList( finalDefaultRestriction, simpleTypeFinalValue, "restriction" );
      addToList( finalDefaultList, simpleTypeFinalValue, "list" );
      addToList( finalDefaultUnion, simpleTypeFinalValue, "union" );
      simpleTypeFinalDefault = simpleTypeFinalValue.toString();
    }
    else {
      simpleTypeFinalDefault = null;
    }

    for ( XmlElement child : schema.getChildren() ) {
      walkSchemaBeforeCombination( child, elementFormDefault, attributeFormDefault, true, elementBlockDefault, complexTypeBlockDefault, elementFinalDefault, complexTypeFinalDefault, simpleTypeFinalDefault );
    }

    // move all components from included schema into "resulting components" list
    for ( XmlElement componentElement : schema.getChildren() ) {
      String componentName = componentElement.getAttributeValue( "name" );
      if ( componentName != null ) {
        String componentType = componentElement.getQName().getLocalPart();
        if ( componentType.equals( "complexType" ) || componentType.equals( "simpleType" ) ) {
          componentType = "type"; // complexType and simpleType share a namespace
        }
        Map<String, XmlElement> nameToComponentElementMap = resultingComponents.get( componentType );
        if ( nameToComponentElementMap == null ) {
          nameToComponentElementMap = new HashMap<String, XmlElement>();
          resultingComponents.put( componentType, nameToComponentElementMap );
        }
        nameToComponentElementMap.put( componentName, componentElement );
      }
    }
    for ( XmlElement importElement : schema.getChildren( Import.$QNAME ) ) {
      String tns = importElement.getAttributeValue( "namespace" );
      if ( tns == null ) {
        tns = "";
      }
      resultingImportedNamespaces.add( tns );
      String schemaLocation = importElement.getAttributeValue( "schemaLocation" );
      URL newUrl = makeLocal( blueprintUrl, schemaLocation, tns, TypeSystem.getExecutionEnvironment().getModule( blueprintUrl ) );
      if ( newUrl != null ) { // in the case of an xs:import without a specified schemaLocation, we won't be able to find the imported resource
        newSchemaIndexesToProcess.add( new Pair<URL, String>( newUrl, tns ) );
      }
    }
    for ( XmlElement include : schema.removeChildren( Include.$QNAME ) ) {
      inlineIncludeOrRedefine( blueprintUrl, targetNamespace, resultingComponents, resultingImportedNamespaces, newSchemaIndexesToProcess, include, alreadyIncluded );
    }
    for ( XmlElement redefine : schema.removeChildren( Redefine.$QNAME ) ) {
      inlineIncludeOrRedefine( blueprintUrl, targetNamespace, resultingComponents, resultingImportedNamespaces, newSchemaIndexesToProcess, redefine, alreadyIncluded );
    }
  }

  private static void addToList( boolean blockDefaultExtension, StringBuilder complexTypeBlockValue, String blockName ) {
    if ( blockDefaultExtension ) {
      if ( complexTypeBlockValue.length() > 0 ) {
        complexTypeBlockValue.append( ' ' );
      }
      complexTypeBlockValue.append( blockName );
    }
  }

  private static void inlineIncludeOrRedefine( URL blueprintUrl, String targetNamespace, Map<String, Map<String, XmlElement>> resultingComponents, Set<String> resultingImportedNamespaces, Set<Pair<URL, String>> newSchemaIndexesToProcess, XmlElement includeOrRedefine, Set<URL> alreadyIncluded ) throws MalformedURLException {
    String schemaLocation = includeOrRedefine.getAttributeValue( "schemaLocation" );
    URL includedUrl = makeLocal( blueprintUrl, schemaLocation, null, TypeSystem.getExecutionEnvironment().getModule( blueprintUrl ) );
    if ( ! alreadyIncluded.add( includedUrl ) ) {
      return; // already included
    }
    XmlElement includedSchema = XmlElement.parse( includedUrl );
    String includedTargetNamespace = includedSchema.getAttributeValue( "targetNamespace" );
    if ( includedTargetNamespace == null ) {
      includedTargetNamespace = "";
    }
    if ( includedTargetNamespace.equals( "" ) && ! targetNamespace.equals( "" ) ) {
      // perform chameleon transformation - all references without namespace gain the targetNamespace of the including schema
      performChameleonTransformation( includedSchema, targetNamespace );
    }
    else if ( ! includedTargetNamespace.equals( targetNamespace ) ) {
      throw new XmlException( "src-include.2.1: The targetNamespace of the referenced schema, '" + includedTargetNamespace + "', must be identical to that of the including schema, '" + targetNamespace + "'" );
    }
    if ( includeOrRedefine.getQName().equals( Redefine.$QNAME ) ) {
      processIncludedSchema( includedSchema, includedUrl, targetNamespace, resultingComponents, resultingImportedNamespaces, newSchemaIndexesToProcess, alreadyIncluded ); // recurse as needed
      // now actually redefine specified components
      for ( XmlElement complexTypeRedefinitionElement : includeOrRedefine.getChildren( ComplexType.$QNAME ) ) {
        XmlElement content = complexTypeRedefinitionElement.getChild( ComplexContent.$QNAME );
        if ( content == null ) {
          content = complexTypeRedefinitionElement.getChild( SimpleContent.$QNAME );
        }
        if ( content == null ) {
          throw new XmlException( "xs:complexType expected to contain either xs:complexContent or xs:simpleContent" );
        }
        XmlElement extensionOrRestriction = content.getChild( ComplexContent_Extension.$QNAME );
        if ( extensionOrRestriction == null ) {
          extensionOrRestriction = content.getChild( ComplexContent_Restriction.$QNAME );
        }
        if ( extensionOrRestriction == null ) {
          throw new XmlException( "xs:complexType expected to contain either xs:complexContent or xs:simpleContent containing either xs:extension or xs:restriction" );
        }
        String baseString = (String) extensionOrRestriction.getAttributeSimpleValue( "base" ).getGosuValue();
        QName baseQName = XmlSchemaParser.parseQName( baseString, extensionOrRestriction.getNamespaceContext(), targetNamespace, true );
        QName expectedQName = new QName( targetNamespace, complexTypeRedefinitionElement.getAttributeValue( "name" ) );
        if ( ! expectedQName.equals( baseQName ) ) {
          throw new XmlException( "Expected base type name to be " + expectedQName + ", but was " + baseQName );
        }
        // invent a new, unique name for the redefined base component
        String newBaseName = "Redefine-" + baseQName.getLocalPart() + "-" + UUID.randomUUID().toString();
        extensionOrRestriction.setAttributeSimpleValue( "base", XmlSimpleValue.makeQNameInstance( new QName( targetNamespace, newBaseName ) ) ); // rename the reference
        XmlElement oldComponent = resultingComponents.get( "type" ).put( baseQName.getLocalPart(), complexTypeRedefinitionElement );
        oldComponent.setAttributeValue( "name", newBaseName );
        resultingComponents.get( "type" ).put( newBaseName, oldComponent );
      }
      for ( XmlElement simpleTypeRedefinitionElement : includeOrRedefine.getChildren( SimpleType.$QNAME ) ) {
        XmlElement restriction = simpleTypeRedefinitionElement.getChild( ComplexContent_Restriction.$QNAME );
        if ( restriction == null ) {
          throw new XmlException( "xs:simpleType expected to contain xs:restriction" );
        }
        String baseString = (String) restriction.getAttributeSimpleValue( "base" ).getGosuValue();
        QName baseQName = XmlSchemaParser.parseQName( baseString, restriction.getNamespaceContext(), targetNamespace, true );
        QName expectedQName = new QName( targetNamespace, simpleTypeRedefinitionElement.getAttributeValue( "name" ) );
        if ( ! expectedQName.equals( baseQName ) ) {
          throw new XmlException( "Expected base type name to be " + expectedQName + ", but was " + baseQName );
        }
        // invent a new, unique name for the redefined base component
        String newBaseName = "Redefine-" + baseQName.getLocalPart() + "-" + UUID.randomUUID().toString();
        restriction.setAttributeSimpleValue( "base", XmlSimpleValue.makeQNameInstance( new QName( targetNamespace, newBaseName ) ) ); // rename the reference
        XmlElement oldComponent = resultingComponents.get( "type" ).put( baseQName.getLocalPart(), simpleTypeRedefinitionElement );
        oldComponent.setAttributeValue( "name", newBaseName );
        resultingComponents.get( "type" ).put( newBaseName, oldComponent );
      }
      for ( XmlElement groupRedefinitionElement : includeOrRedefine.getChildren( Group.$QNAME ) ) {
        String groupName = groupRedefinitionElement.getAttributeValue( "name" );
        String newBaseName = "Redefine-" + groupName + "-" + UUID.randomUUID().toString();
        // invent a new, unique name for the redefined base component
        redefineGroup( targetNamespace, groupRedefinitionElement, groupName, newBaseName );
      }
      for ( XmlElement attributeGroupRedefinitionElement : includeOrRedefine.getChildren( AttributeGroup.$QNAME ) ) {
        String groupName = attributeGroupRedefinitionElement.getAttributeValue( "name" );
        String newBaseName = "Redefine-" + groupName + "-" + UUID.randomUUID().toString();
        // invent a new, unique name for the redefined base component
        for ( XmlElement attributeGroupElement : attributeGroupRedefinitionElement.getChildren( AttributeGroup.$QNAME ) ) {
          XmlSimpleValue refValue = attributeGroupElement.getAttributeSimpleValue( "ref" );
          if ( refValue != null ) {
            String baseString = (String) refValue.getGosuValue();
            QName baseQName = XmlSchemaParser.parseQName( baseString, attributeGroupElement.getNamespaceContext(), targetNamespace, true );
            QName expectedQName = new QName( targetNamespace, groupName );
            if ( expectedQName.equals( baseQName ) ) {
              attributeGroupElement.setAttributeSimpleValue( "ref", XmlSimpleValue.makeQNameInstance( new QName( targetNamespace, newBaseName ) ) );
            }
          }
        }
      }

    }
    else {
      processIncludedSchema( includedSchema, includedUrl, targetNamespace, resultingComponents, resultingImportedNamespaces, newSchemaIndexesToProcess, alreadyIncluded ); // recurse as needed
    }
  }

  private static void redefineGroup( String targetNamespace, XmlElement groupRedefinitionElement, String groupName, String newBaseName ) {
    for ( XmlElement groupElement : groupRedefinitionElement.getChildren() ) {
      if ( groupElement.getQName().equals( Group.$QNAME ) ) {
        XmlSimpleValue refValue = groupElement.getAttributeSimpleValue( "ref" );
        if ( refValue != null ) {
          String baseString = (String) refValue.getGosuValue();
          QName baseQName = XmlSchemaParser.parseQName( baseString, groupElement.getNamespaceContext(), targetNamespace, true );
          QName expectedQName = new QName( targetNamespace, groupName );
          if ( expectedQName.equals( baseQName ) ) {
            groupElement.setAttributeSimpleValue( "ref", XmlSimpleValue.makeQNameInstance( new QName( targetNamespace, newBaseName ) ) );
          }
        }
      }
      redefineGroup( targetNamespace, groupElement, groupName, newBaseName );
    }
  }

  private static void performChameleonTransformation( XmlElement element, String targetNamespace ) {
    String refName = element.getAttributeValue( "ref" );
    if ( refName != null ) {
      element.setAttributeSimpleValue( "ref", XmlSimpleValue.makeQNameInstance( XmlSchemaParser.parseQName( refName, element.getNamespaceContext(), targetNamespace, true ) ) );
    }
    String typeName = element.getAttributeValue( "type" );
    if ( typeName != null ) {
      element.setAttributeSimpleValue( "type", XmlSimpleValue.makeQNameInstance( XmlSchemaParser.parseQName( typeName, element.getNamespaceContext(), targetNamespace, true ) ) );
    }
    String baseName = element.getAttributeValue( "base" );
    if ( baseName != null ) {
      element.setAttributeSimpleValue( "base", XmlSimpleValue.makeQNameInstance( XmlSchemaParser.parseQName( baseName, element.getNamespaceContext(), targetNamespace, true ) ) );
    }
    String itemTypeName = element.getAttributeValue( "itemType" );
    if ( itemTypeName != null ) {
      element.setAttributeSimpleValue( "itemType", XmlSimpleValue.makeQNameInstance( XmlSchemaParser.parseQName( itemTypeName, element.getNamespaceContext(), targetNamespace, true ) ) );
    }
    String referName = element.getAttributeValue( "refer" );
    if ( referName != null ) {
      element.setAttributeSimpleValue( "refer", XmlSimpleValue.makeQNameInstance( XmlSchemaParser.parseQName( referName, element.getNamespaceContext(), targetNamespace, true ) ) );
    }
    String memberTypes = element.getAttributeValue( "memberTypes" );
    if ( memberTypes != null ) {
      String[] parts = memberTypes.split( "\\w" );
      List<QName> qnames = new ArrayList<QName>( parts.length );
      for ( String part : parts ) {
        qnames.add( XmlSchemaParser.parseQName( part, element.getNamespaceContext(), targetNamespace, true ) );
      }
      element.setAttributeSimpleValue( "memberTypes", XmlSimpleValue.makeListOfQNameInstance( qnames ) );
    }
    for ( XmlElement child : element.getChildren() ) {
      performChameleonTransformation( child, targetNamespace );
    }
  }

  private static void getAllSchemas( URL schemaUrl, Set<URL> seenResources, List<Pair<XmlElement, String>> allSchemas, Map<String, String> fakeExternalLocationAliases, boolean isInclude ) {
    if ( ! seenResources.contains( schemaUrl ) ) {
      seenResources.add( schemaUrl );
      XmlElement schema;
      IModule currentModule = TypeSystem.getGlobalModule();
//      IModule currentModule = TypeSystem.getExecutionEnvironment().getModule( schemaUrl );
      if ( isInclude ) {
        schema = XmlElement.parse( schemaUrl );
        String fakeUrl = "file:/" + currentModule.getFileRepository().getResourceName( schemaUrl );
        addSchema( allSchemas, schema, fakeUrl, fakeExternalLocationAliases, seenResources, schemaUrl, currentModule );
      }
      else {
        String gosuNamespace = XmlSchemaIndex.getGosuNamespace( schemaUrl, currentModule );
        XmlSchemaIndex<?> schemaIndex = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( currentModule, gosuNamespace );
        if ( schemaIndex == null ) {
          if ( CommonServices.getXmlSchemaCompatibilityConfig().useCompatibilityMode( gosuNamespace ) ) {
            throw new XmlException( "A non-compatibility-mode schema cannot import a compatibility-mode schema (" + gosuNamespace + ")" );
          }
          else {
            throw new IllegalStateException( "Schema not found for Gosu namespace " + gosuNamespace );
          }
        }
        schema = XmlElement.parse( schemaIndex.getXSDSource().getInputStream( false ) );
        addSchemaOrWsdl( schemaUrl, seenResources, allSchemas, fakeExternalLocationAliases, schema, currentModule );
      }
    }
  }

  private static void addSchemaOrWsdl( URL schemaUrl, Set<URL> seenResources, List<Pair<XmlElement, String>> allSchemas, Map<String, String> fakeExternalLocationAliases, XmlElement schema, IModule currentModule ) {
    String fakeUrl = "file:/" + currentModule.getFileRepository().getResourceName( schemaUrl );
    if ( schema.getQName().equals( Definitions.$QNAME ) ) {
      for ( XmlElement importElement : schema.getChildren( TDefinitions_Import.$QNAME ) ) {
        String wsdlLocation = importElement.getAttributeValue( "location" );
        URL url = makeLocal( schemaUrl, wsdlLocation, null, currentModule );
        getAllSchemas( url, seenResources, allSchemas, fakeExternalLocationAliases, false );
      }
      // extract schemas from WSDL
      for ( XmlElement typesElement : schema.getChildren( TDefinitions_Types.$QNAME ) ) {
        for ( XmlElement schemaElement : typesElement.getChildren( gw.internal.schema.gw.xsd.w3c.xmlschema.Schema.$QNAME ) ) {
          addSchema( allSchemas, schemaElement, fakeUrl, fakeExternalLocationAliases, seenResources, schemaUrl, currentModule );
        }
      }
    }
    else if ( schema.getQName().equals( gw.internal.schema.gw.xsd.w3c.xmlschema.Schema.$QNAME ) ) {
      addSchema( allSchemas, schema, fakeUrl, fakeExternalLocationAliases, seenResources, schemaUrl, currentModule );
    }
    else {
      throw new XmlException( "Unexpected root element " + schema.getQName() + ", expected " + gw.internal.schema.gw.xsd.w3c.xmlschema.Schema.$QNAME + " or " + Definitions.$QNAME );
    }
  }

  private static void addSchema( List<Pair<XmlElement, String>> allSchemas, XmlElement schema, String fakeUrl, Map<String, String> fakeExternalLocationAliases, Set<URL> seenResources, URL schemaUrl, IModule currentModule ) {
    allSchemas.add( new Pair<XmlElement, String>( schema, fakeUrl ) );
    Map<String,String> externalLocationAliases = new HashMap<String, String>();
    for ( XmlElement include : schema.getChildren( Include.$QNAME ) ) {
      String schemaLocation = include.getAttributeValue( "schemaLocation" );
      URL url = makeLocal( schemaUrl, schemaLocation, null, currentModule, externalLocationAliases );
      getAllSchemas( url, seenResources, allSchemas, fakeExternalLocationAliases, true );
    }
    for ( XmlElement redefine : schema.getChildren( Redefine.$QNAME ) ) {
      String schemaLocation = redefine.getAttributeValue( "schemaLocation" );
      URL url = makeLocal( schemaUrl, schemaLocation, null, currentModule, externalLocationAliases );
      getAllSchemas( url, seenResources, allSchemas, fakeExternalLocationAliases, true );
    }
    for ( XmlElement imprt : schema.getChildren( Import.$QNAME ) ) {
      String schemaLocation = imprt.getAttributeValue( "schemaLocation" );
      String namespace = imprt.getAttributeValue( "namespace" );
      // Work around possible Xerces bug. If imported schema has an empty string namespace, the importing schema
      // *can not* specify the namespace attribute
      if ( namespace != null && namespace.equals( XmlConstants.NULL_NS_URI ) ) {
        namespace = null;
        imprt.setAttributeValue( "namespace", null );
      }
      URL url = makeLocal( schemaUrl, schemaLocation, namespace, currentModule, externalLocationAliases );
      if ( url != null ) {
        // url can be null in the case of an <xs:import> without a schemaLocation, such as if the imported schema
        // is mapped as an external location, or if another imported schema specifies the schemaLocation
        getAllSchemas( url, seenResources, allSchemas, fakeExternalLocationAliases, false );
      }
    }
    for ( Map.Entry<String, String> entry : externalLocationAliases.entrySet()) {
      try {
        String externalLocation = entry.getKey();
        String localPath = entry.getValue();
        String fakeUrl2 = "file:/" + currentModule.getFileRepository().getResourceName( new URL( localPath ) );
        fakeExternalLocationAliases.put( externalLocation, fakeUrl2 );
      }
      catch ( MalformedURLException ex ) {
        throw GosuExceptionUtil.forceThrow( ex );
      }
    }

  }

  public StreamSource getSchemaStreamSource() {
    URL schemaURL = _xmlSchemaSource.getBlueprintURL();
    String schemaEF = schemaURL == null ? null : schemaURL.toExternalForm();
    return new StreamSource( _xmlSchemaSource.getInputStream( false ), schemaEF );
  }

  private void readSchema( URL schemaEF, String tns, Map<Pair<URL,String>, XmlSchema> caches ) throws ParserConfigurationException, SAXException, IOException {
    LocationMap locationMap = _xmlSchemaSource.createLocationMap();
    XmlElement rootElement;
    InputStream inputStream = _xmlSchemaSource.getInputStream( true );
    try {
      rootElement = XmlElementInternals.instance().parse( inputStream, schemaEF, new XmlSchemaLocalResourceResolver(),
              new LocationMapCallback(locationMap) );
    }
    finally {
      inputStream.close();
    }
    XmlSchemaParseContext context = new XmlSchemaParseContext();
    context.setLocationMap( locationMap );
    WsdlDefinitions def;
    List<XmlSchema> schemas;
    if ( rootElement.getQName().equals( gw.internal.schema.gw.xsd.w3c.xmlschema.Schema.$QNAME ) ) {
      schemas = Collections.singletonList( XmlSchemaParser.parseSchema( this, rootElement, tns, schemaEF, context, false, caches ) );
    }
    else if ( rootElement.getQName().equals( gw.internal.schema.gw.xsd.w3c.wsdl.Definitions.$QNAME ) ) {
      def = XmlSchemaParser.parseWsdlDefinitions( this, rootElement, schemaEF, context, locationMap );
      _wsdlDefinition = def;
      List<XmlSchema> result = new ArrayList<XmlSchema>();
      for ( WsdlTypes types : def.getTypes()){
        result.addAll( types.getSchemas());
      }
      schemas = result;
    }
    else {
      throw new IllegalArgumentException( "Expected an xs:schema or wsdl:definitions, but found " + rootElement.getQName() );
    }
    _collection.addSchemas( schemas );
  }

  private void maybeIndex( Map<Pair<URL, String>, XmlSchema> caches ) {
    if ( _indexed ) {
      return;
    }
    TypeSystem.lock();
    try {
      if ( _indexed ) {
        return;
      }
      _indexed = true;
      try {
        readSchema( _schemaEF, null, caches );
        if (!CommonServices.getPlatformHelper().isInIDE()) {
          validate( caches );
        }
        String schemaAccessTypeName = _packageName + ".util";
        _allTypeNames.addAll( getAdditionalTypeNames() );
        _allTypeNames.add( schemaAccessTypeName );
        _typesByName.put( schemaAccessTypeName, new XmlSchemaAccessTypeData<T>( schemaAccessTypeName, getTypeLoader().getSchemaSchemaTypeName(), getContext(), this ) );
        List<Runnable> todo = new ArrayList<Runnable>();
        for ( XmlSchema schema : _collection.getXmlSchemas() ) {
          index( schema.getTypes(), todo );
          index( schema.getElements(), todo );
          index( schema.getAttributes(), todo );
        }
        for ( Runnable runnable : todo ) {
          runnable.run();
        }
      }
      finally {
        // memory optimization - initialize all type data so we can throw out the intermediary indexing info
        Runnable runnable = new Runnable() {
          public void run() {
            IExecutionEnvironment env = TypeSystem.getExecutionEnvironment();
            TypeSystem.pushModule(env.getGlobalModule());
            TypeSystem.lock();
            try {
              XmlSchemaIndex<?> schemaIndex = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( getTypeLoader().getModule(), _packageName );
              // ensure the wsdl actually validated and has not been removed before executing the following code
              //noinspection ObjectEquality
              if ( schemaIndex == XmlSchemaIndex.this ) {
                for ( IXmlSchemaTypeData typeData : _typesByName.values() ) {
                  typeData.maybeInit(); // initialize all type data before clearing intermediary indexing info
                }
                _flattenedChildrenBySchemaType = null;
                _childPluralityBySchemaType = null;
              }
            }
            finally {
              TypeSystem.unlock();
              TypeSystem.popModule(env.getGlobalModule());
            }
          }
        };
        MemoryCleanerThread.invokeLater( runnable );
      }
    }
    catch ( Exception ex ) {
      getTypeLoader().schemaIndexingExceptionOccurred( _packageName, getXSDSource().getResourceFile(), ex );
      clearExternalJarXSD();
      if ( ! getXSDSource().getBlueprintURL().getProtocol().equals( "jar" ) ) {
        //noinspection ThrowableInstanceNeverThrown
        new XmlException( "Error indexing schema '" + _xmlSchemaSource.getDescription() + "'", ex ).printStackTrace();
        // we can't throw here
      }
    }
    finally {
      TypeSystem.unlock();
    }
  }

  private void clearExternalJarXSD() {
    // pretend like this xsd never existed
    _allTypeNames.clear();
    _typesByName.clear();
    _typesBySchemaObject.clear();
    _flattenedChildrenBySchemaType.clear();
    _childPluralityBySchemaType.clear();
  }

  private void index( Map<QName, ? extends XmlSchemaObject> schemaObjects, List<Runnable> todo ) {
    for ( XmlSchemaObject xmlSchemaObject : schemaObjects.values() ) {
      XmlSchemaIndexer.invokeIndexer( xmlSchemaObject, "", new ArrayList<XmlSchemaFlattenedChild>(), true, false, null, true, new HashMap<QName, XmlSchemaElementTypeData>(), new HashMap<Pair<XmlSchemaPropertyType, QName>, Boolean>(), getContext(), todo );
    }
  }

  public static String getGosuNamespace( URL resourceURL, IModule module ) {
    for (IModule m : module.getModuleTraversalList()) {
      // Skip global module, but only if requested module is not global module itself
      if (m == TypeSystem.getGlobalModule() && module != m) {
        continue;
      }
      IFile file = CommonServices.getFileSystem().getIFile(resourceURL);
      String resourceName = m.pathRelativeToRoot(file);
      if (resourceName != null) {
        return normalizeSchemaNamespace( getGosuNamespaceWithoutNormalization( resourceURL, m ), resourceName );
      }
    }
    throw new IllegalArgumentException("Cannot find resource " + resourceURL);
  }

  public static String getGosuNamespaceWithoutNormalization( URL resourceURL, IModule module ) {
    if (isLocal(resourceURL)) {
      IFileSystemGosuClassRepository fileRepository = module.getFileRepository();
      String path = fileRepository.getResourceName(resourceURL);
      if ( ! Character.isLetter( path.charAt( 0 ) ) ) {
        // logic below relies on this... It's changed over time to starting with ./, /, etc
        throw new IllegalStateException( "Expected " + path + " to start with a letter" );
      }
      return path.replace( '.', '_' ).replace( '/', '.' );
    }
    return null;
  }

  public String makeUniqueTypeName( String packageName, String originalTypeName ) {
    originalTypeName = packageName + '.' + normalizeName( originalTypeName, NormalizationMode.PROPERCASE );
    String typeName = originalTypeName;
    int suffix = 2;
    while ( _allTypeNames.contains( typeName ) || _typeLoader.hasNamespace( typeName ) ) {
      typeName = originalTypeName + suffix++;
    }
    _allTypeNames.add( typeName );

    // FIXME-isd: too hacky?
    // Need to record newly created type since there will be no refresh on them!
    getTypeLoader().createdType( typeName );
    return typeName;
  }

  public static String normalizeName( String name, NormalizationMode mode ) {
    StringBuilder sb = new StringBuilder();
    boolean firstChar = true;
    for ( int i = 0; i < name.length(); i++ ) {
      char ch = name.charAt( i );
      if ( mode == NormalizationMode.LOWERCASE ) {
        ch = Character.toLowerCase( ch );
      }
      else if ( mode == NormalizationMode.UPPERCASE ) {
        ch = Character.toUpperCase( ch );
      }
      if ( firstChar ) {
        if ( mode == NormalizationMode.PROPERCASE ) {
          ch = Character.toUpperCase( ch );
        }
        boolean ignore = false;
        if ( ! Character.isJavaIdentifierStart( ch ) ) {
          if ( Character.isJavaIdentifierPart( ch ) ) {
            // valid part, but not valid start - prepend underscore
            sb.append( '_' );
          }
          else {
            // not valid in any form - ignore
            ignore = true;
          }
        }
        if ( ! ignore ) {
          sb.append( ch );
          firstChar = false;
        }
      }
      else {
        if ( Character.isJavaIdentifierPart( ch ) ) {
          sb.append( ch );
        }
        else {
          sb.append( '_' );
        }
      }
    }
    if ( firstChar ) {
      sb.append( "_" ); // no valid characters in name - ouch
    }
    return sb.toString();
  }

  public XmlSchemaSource getXSDSource() {
    return _xmlSchemaSource;
  }

  public T getContext() {
    return _context;
  }

  public String getPackageName() {
    return _packageName;
  }

  public String getGosuNamespaceByWsdlNamespace( String wsdlNamespace ) {
    return getGosuNamespaceByWsdlNamespace( wsdlNamespace, new HashSet<String>() );
  }

  private String getGosuNamespaceByWsdlNamespace( String xmlNamespace, Set<String> checkedGosuNamespaces ) {
    String gosuNamespace = _gosuNamespacesByWsdlNamespace.get().get( xmlNamespace );
    if ( gosuNamespace == null ) {
      for ( String childGosuNamespace : _gosuNamespacesByWsdlNamespace.get().values() ) {
        if ( checkedGosuNamespaces.add( childGosuNamespace ) ) {
          XmlSchemaIndex<?> schemaIndex = getSchemaIndexForGosuNamespace( childGosuNamespace );
          gosuNamespace = schemaIndex.getGosuNamespaceByWsdlNamespace( xmlNamespace, checkedGosuNamespaces );
          if ( gosuNamespace != null ) {
            break;
          }
        }
      }
    }
    return gosuNamespace;
  }

  public Set<String> getGosuNamespacesByXMLNamespace( String xmlNamespace ) {
    List<Set<String>> results = new ArrayList<Set<String>>();
    getGosuNamespacesByXMLNamespace( xmlNamespace, new HashSet<String>(), results );
    if ( results.size() == 0 ) {
      return Collections.emptySet();
    }
    else if ( results.size() == 1 ) {
      return results.get( 0 );
    }
    else {
      LinkedHashSet<String> allGosuNamespaces = new LinkedHashSet<String>();
      for ( Set<String> result : results ) {
        allGosuNamespaces.addAll( result );
      }
      return allGosuNamespaces;
    }
  }

  private void getGosuNamespacesByXMLNamespace( String xmlNamespace, Set<String> checkedGosuNamespaces, List<Set<String>> results ) {
    checkedGosuNamespaces.add( _packageName );
    LinkedHashMap<String, LinkedHashSet<String>> gosuNamespacesByXmlNamespace = _gosuNamespacesByXMLNamespace.get();
    Set<String> gosuNamespaces = gosuNamespacesByXmlNamespace.get( xmlNamespace );
    if ( gosuNamespaces != null ) {
      results.add( gosuNamespaces );
    }
    for ( Set<String> childGosuNamespaces : gosuNamespacesByXmlNamespace.values() ) {
      for ( String childGosuNamespace : childGosuNamespaces ) {
        if ( checkedGosuNamespaces.add( childGosuNamespace ) ) {
          XmlSchemaIndex<?> schemaIndex = getSchemaIndexForGosuNamespace( childGosuNamespace );
          schemaIndex.getGosuNamespacesByXMLNamespace( xmlNamespace, checkedGosuNamespaces, results );
        }
      }
    }
  }

  public Set<String> getAllTypeNames( Map<Pair<URL, String>, XmlSchema> caches ) {
    maybeIndex( caches );
    return Collections.unmodifiableSet( _allTypeNames );
  }

  public IXmlTypeData getTypeData( String fullyQualifiedTypeName ) {
    maybeIndex( null );
    IXmlTypeData typeData = _typesByName.get( fullyQualifiedTypeName );
    if ( typeData == null ) {
      typeData = getAdditionalTypeData( fullyQualifiedTypeName );
    }
    return typeData;
  }

  public XmlSchemaIndex<?> getSchemaIndexForWsdlNamespace( String namespaceURI ) {
    String gosuNamespace = getGosuNamespaceByWsdlNamespace( namespaceURI );
    if ( gosuNamespace == null ) {
      throw new NotFoundException( "Wsdl {" + namespaceURI + "} not available in wsdl " + getPackageName() );
    }
    return getSchemaIndexForGosuNamespace( gosuNamespace );
  }

  private XmlSchemaIndex<?> getSchemaIndexForGosuNamespace( String gosuNamespace ) {
    XmlSchemaIndex<?> schemaForNamespace = XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( getTypeLoader().getModule(), gosuNamespace );
    if ( schemaForNamespace == null ) {
      throw new NotFoundException( "Schema not found for Gosu namespace " + gosuNamespace + " in schema " + _packageName );
    }
    return schemaForNamespace;
  }

  public Set<XmlSchemaIndex<?>> getSchemaIndexesForXmlNamespace( String namespaceURI ) {
    Set<String> gosuNamespaces = getGosuNamespacesByXMLNamespace( namespaceURI );
    Set<XmlSchemaIndex<?>> ret = Collections.emptySet();
    if ( gosuNamespaces != null ) {
      for ( String gosuNamespace : gosuNamespaces ) {
        XmlSchemaIndex<?> schemaIndex = getSchemaIndexForGosuNamespace( gosuNamespace );
        if ( schemaIndex == null ) {
          throw new IllegalStateException( "Expected schema index to be found for gosu namespace: " + gosuNamespace );
        }
        if ( ret.isEmpty() ) {
          ret = Collections.<XmlSchemaIndex<?>>singleton( schemaIndex );
        }
        else {
          if ( ret.size() == 1 ) {
            ret = new LinkedHashSet<XmlSchemaIndex<?>>( ret );
          }
          ret.add( schemaIndex );
        }
      }
    }
    return ret;
  }

  public static IType getGosuTypeBySchemaObject( XmlSchemaObject schemaObject ) {
    XmlSchemaIndex<?> schemaIndex = schemaObject.getSchemaIndex();
    schemaIndex.maybeIndex( null );
    if ( schemaObject instanceof XmlSchemaAny ) {
      return TypeSystem.get( XmlElement.class );
    }
    IXmlSchemaTypeData typeData = schemaIndex._typesBySchemaObject.get( schemaObject );
    IType type = typeData.getType();
    if ( type == null ) {
      throw new NotFoundException( "Type not found for schema object: " + schemaObject );
    }
    return type;
  }

  public static IXmlSchemaTypeData getGosuTypeDataBySchemaObject( XmlSchemaObject schemaObject ) {
    schemaObject.getSchemaIndex().maybeIndex( null );
    return getGosuTypeDataBySchemaObjectWithoutIndexingSchema( schemaObject );
  }

  public static IXmlSchemaTypeData getGosuTypeDataBySchemaObjectWithoutIndexingSchema( XmlSchemaObject schemaObject ) {
    return (IXmlSchemaTypeData)schemaObject.getSchemaIndex()._typesBySchemaObject.get( schemaObject );
  }

  public static XmlSchemaElement getActualElement( XmlSchemaElement ref ) {
    if ( ref.getRefName() == null ) {
      return ref; // not a reference
    }
    return ref.getSchemaIndex().getXmlSchemaElementByQName( ref.getRefName() );
  }

  public static XmlSchemaAttribute getActualAttribute( XmlSchemaAttribute ref ) {
    if ( ref.getRefName() == null ) {
      return ref; // not a reference
    }
    return ref.getSchemaIndex().getXmlSchemaAttributeByQName( ref.getRefName() );
  }

  public XmlSchemaGroup getXmlSchemaGroupByQName( QName qname ) {
    maybeIndex( null );
    Set<XmlSchemaIndex<?>> schemaIndexes = getSchemaIndexesForXmlNamespace( qname.getNamespaceURI() );
    for ( XmlSchemaIndex<?> schemaIndex : schemaIndexes ) {
      for ( XmlSchema schema : schemaIndex.getXmlSchemaCollection().getXmlSchemas()) {
        if ( schema.getTargetNamespace().equals( qname.getNamespaceURI() ) ) {
          XmlSchemaGroup group = schema.getGroups().get( qname );
          if ( group != null ) {
            return group;
          }
        }
      }
    }
    throw throwNotFoundException( "Group", qname );
  }

  public XmlSchemaAttributeGroup getXmlSchemaAttributeGroupByQName( QName qname ) {
    maybeIndex( null );
    Set<XmlSchemaIndex<?>> schemaIndexes = getSchemaIndexesForXmlNamespace( qname.getNamespaceURI() );
    for ( XmlSchemaIndex<?> schemaIndex : schemaIndexes ) {
      for (XmlSchema schema : schemaIndex.getXmlSchemaCollection().getXmlSchemas()) {
        if ( schema.getTargetNamespace().equals( qname.getNamespaceURI() ) ) {
          XmlSchemaAttributeGroup group = schema.getAttributeGroups().get( qname );
          if ( group != null ) {
            return group;
          }
        }
      }
    }
    throw throwNotFoundException( "AttributeGroup", qname );
  }

  public XmlSchemaResourceTypeLoaderBase<T> getTypeLoader() {
    return _typeLoader;
  }

  public void putTypeDataByName( String typeName, IXmlSchemaTypeData typeData ) {
    IXmlSchemaTypeData oldTypeData = _typesByName.put( typeName, typeData );
    if ( oldTypeData != null ) {
      throw new IllegalArgumentException( "Type name already registered: " + typeName );
    }
  }

  public void putTypeDataBySchemaObject( XmlSchemaObject xsdObject, IXmlSchemaTypeData typeData ) {
    IXmlSchemaTypeData oldTypeData = _typesBySchemaObject.put( xsdObject, typeData );
    if ( oldTypeData != null ) {
      throw new IllegalArgumentException( "Schema object already registered. Old: " + oldTypeData.getName() + ", New: " + typeData.getName() );
    }
  }

  public XmlSchemaElement getXmlSchemaElementByQNameIfValid( QName schemaElementName ) {
    maybeIndex( null );
    Set<XmlSchemaIndex<?>> schemaIndexes = getSchemaIndexesForXmlNamespace( schemaElementName.getNamespaceURI() );
    for ( XmlSchemaIndex<?> schemaIndex : schemaIndexes ) {
      XmlSchemaElement element = schemaIndex.getXmlSchemaCollection().getElementByQNameIfValid( schemaElementName );
      if ( element != null ) {
        return element;
      }
    }
    return null;
  }

  public XmlSchemaElement getXmlSchemaElementByQName( QName schemaElementName ) {
    XmlSchemaElement element = getXmlSchemaElementByQNameIfValid( schemaElementName );
    if ( element != null ) {
      return element;
    }
    throw throwNotFoundException( "Element", schemaElementName );
  }

  public XmlSchemaAttribute getXmlSchemaAttributeByQNameIfValid( QName schemaAttributeName ) {
    maybeIndex( null );
    Set<XmlSchemaIndex<?>> schemaIndexes = getSchemaIndexesForXmlNamespace( schemaAttributeName.getNamespaceURI() );
    for ( XmlSchemaIndex<?> schemaIndex : schemaIndexes ) {
      XmlSchemaAttribute attribute = schemaIndex.getXmlSchemaCollection().getAttributeByQNameIfValid( schemaAttributeName );
      if ( attribute != null ) {
        return attribute;
      }
    }
    return null;
  }

  public XmlSchemaAttribute getXmlSchemaAttributeByQName( QName schemaAttributeName ) {
    XmlSchemaAttribute attribute = getXmlSchemaAttributeByQNameIfValid( schemaAttributeName );
    if ( attribute != null ) {
      return attribute;
    }
    throw throwNotFoundException( "Attribute", schemaAttributeName );
  }

  public WsdlBinding getWsdlBindingByQName( QName bindingName ) {
    maybeIndex( null );
    XmlSchemaIndex<?> schemaIndex = getSchemaIndexForWsdlNamespace( bindingName.getNamespaceURI() );
    WsdlBinding binding = schemaIndex.getWsdlDefinitions().getBindingByQName(bindingName);
    if ( binding == null ) {
      throw throwNotFoundException( "Binding", bindingName );
    }
    return binding;
  }

  public WsdlMessage getWsdlMessageByQName( QName messageQName ) {
    maybeIndex( null );
    XmlSchemaIndex<?> schemaIndex = getSchemaIndexForWsdlNamespace( messageQName.getNamespaceURI() );
    WsdlMessage message = schemaIndex.getWsdlDefinitions().getMessageByQName( messageQName );
    if ( message == null ) {
      throw throwNotFoundException( "Message", messageQName );
    }
    return message;
  }


  public WsdlPortType getWsdlPortTypeByQName( QName portTypeName ) {
    maybeIndex( null );
    XmlSchemaIndex<?> schemaIndex = getSchemaIndexForWsdlNamespace( portTypeName.getNamespaceURI() );
    WsdlPortType portType = schemaIndex.getWsdlDefinitions().getPortTypeByQName(portTypeName);
    if ( portType == null ) {
      throw throwNotFoundException( "PortType", portTypeName );
    }
    return portType;
  }

  public XmlSchemaType getXmlSchemaTypeByQName( QName schemaTypeName ) {
    XmlSchemaType type = getXmlSchemaTypeByQNameIfValid( schemaTypeName );
    if ( type != null ) {
      return type;
    }
    throw throwNotFoundException( "Type", schemaTypeName );
  }

  public XmlSchemaType getXmlSchemaTypeByQNameIfValid( QName schemaTypeName ) {
    maybeIndex( null );
    if ( isBuiltInDatatype( schemaTypeName ) ) {
      XmlSchemaIndex<?> schemaIndex = getSchemaIndexForGosuNamespace( "gw.xsd.w3c.xmlschema" );
      return schemaIndex.getXmlSchemaCollection().getTypeByQName( schemaTypeName );
    }
    else {
      Set<XmlSchemaIndex<?>> schemaIndexes = getSchemaIndexesForXmlNamespace( schemaTypeName.getNamespaceURI() );
      for ( XmlSchemaIndex<?> schemaIndex : schemaIndexes ) {
        XmlSchemaType type = schemaIndex.getXmlSchemaCollection().getTypeByQNameIfValid( schemaTypeName );
        if ( type != null ) {
          return type;
        }
      }
      return null;
    }
  }

  public static boolean isBuiltInDatatype( QName schemaTypeName ) {
    return schemaTypeName.getNamespaceURI().equals( XMLConstants.W3C_XML_SCHEMA_NS_URI ) && BUILT_IN_DATATYPES.contains(schemaTypeName.getLocalPart());
  }

  public static boolean isBuiltInDatatype( IType type ) {
    XmlSchemaTypeSchemaInfo schemaInfo = getSchemaInfoByType( type );
    if ( schemaInfo == null ) {
      return false;
    }
    QName qname = schemaInfo.getXsdType().getQName();
    return qname != null && isBuiltInDatatype( qname );
  }

  public XmlSchemaSimpleType getXmlSchemaSimpleTypeByQName( QName schemaTypeName ) {
    if ( schemaTypeName == null ) {
      schemaTypeName = ANY_SIMPLE_TYPE_QNAME; // the default
    }
    XmlSchemaType xsdType = getXmlSchemaTypeByQName( schemaTypeName );
    if ( ! ( xsdType instanceof XmlSchemaSimpleType ) ) {
      throw new NotFoundException( "Expected simple type, but found " + xsdType );
    }
    return (XmlSchemaSimpleType) xsdType;
  }

  public XmlSchemaComplexType getXmlSchemaComplexTypeByQName( QName schemaTypeName ) {
    if ( schemaTypeName == null ) {
      return null;
    }
    XmlSchemaType xsdType = getXmlSchemaTypeByQName( schemaTypeName );
    if ( ! ( xsdType instanceof XmlSchemaComplexType ) ) {
      throw new NotFoundException( "Expected complex type, but found " + xsdType );
    }
    return (XmlSchemaComplexType) xsdType;
  }

  public List<XmlSchemaFlattenedChild> getFlattenedChildrenBySchemaType( XmlSchemaType xsdType ) {
    maybeIndex( null );
    return _flattenedChildrenBySchemaType.get(xsdType);
  }

  public boolean getChildPluralityBySchemaType( XmlSchemaType xsdType, XmlSchemaPropertyType propertyType, QName childName ) {
    maybeIndex( null );
    return _childPluralityBySchemaType.get( xsdType ).get( new Pair<XmlSchemaPropertyType, QName>( propertyType, childName ) );
  }

  public void registerFlattenedChildrenBySchemaType( XmlSchemaType xsdType, List<XmlSchemaFlattenedChild> flattenedChildren ) {
    _flattenedChildrenBySchemaType.put( xsdType, flattenedChildren );
  }

  public void registerChildrenPluralityBySchemaType( XmlSchemaType xsdType, Map<Pair<XmlSchemaPropertyType, QName>, Boolean> pluralityMap ) {
    _childPluralityBySchemaType.put( xsdType, pluralityMap );
  }

  private static XmlSimpleValueFactory getSimpleValueFactoryForSimpleType( XmlSchemaSimpleType simpleType ) {
    if ( simpleType.getGwTypeName() != null ) {
      return XmlSchemaTypeToGosuTypeMappings.gosuToSchema( TypeSystem.getByFullName( simpleType.getGwTypeName() ) ).getSecond();
    }
    XmlSchemaEnumerationTypeData enumType = simpleType.getSchemaIndex().getEnumerationForSchemaType( simpleType );
    if ( enumType != null ) {
      return new XmlSchemaEnumSimpleValueFactory( enumType );
    }
    XmlSimpleValueFactory factory = XmlSchemaTypeToGosuTypeMappings.schemaToGosu( simpleType.getQName() );
    if ( factory == null ) {
      if ( simpleType.getContent() instanceof XmlSchemaSimpleTypeUnion) {
        factory = getSimpleValueFactoryForSimpleTypeUnion( simpleType.getSchemaIndex(), (XmlSchemaSimpleTypeUnion) simpleType.getContent() );
      }
      else if ( simpleType.getContent() instanceof XmlSchemaSimpleTypeList ) {
        factory = getSimpleValueFactoryForSimpleTypeList( simpleType.getSchemaIndex(), (XmlSchemaSimpleTypeList) simpleType.getContent() );
      }
      else {
        XmlSchemaSimpleType baseType = getSimpleTypeBase( simpleType );
        factory = getSimpleValueFactoryForSimpleType( baseType );
      }
    }
    return factory;
  }

  private XmlSchemaEnumerationTypeData getEnumerationForSchemaType( XmlSchemaType simpleType ) {
    maybeIndex( null );
    return _enumerations.get( simpleType );
  }

  private static XmlSchemaSimpleType getSimpleTypeBase( final XmlSchemaSimpleType simpleType ) {
    XmlSchemaSimpleType baseType = null;
    if ( simpleType.getContent() instanceof XmlSchemaSimpleTypeRestriction) {
      XmlSchemaSimpleTypeRestriction restriction = (XmlSchemaSimpleTypeRestriction) simpleType.getContent();
      baseType = restriction.getBaseType();
      if ( baseType == null ) {
        baseType = simpleType.getSchemaIndex().getXmlSchemaSimpleTypeByQName( restriction.getBaseTypeName() );
      }
    }
    if ( baseType == null && ! ANY_SIMPLE_TYPE_QNAME.equals( simpleType.getQName() ) ) {
      baseType = simpleType.getSchemaIndex().getXmlSchemaSimpleTypeByQName( ANY_SIMPLE_TYPE_QNAME );
    }

    return baseType;
  }

  private static XmlSimpleValueFactory getSimpleValueFactoryForSimpleTypeList(  XmlSchemaIndex<?> schemaIndex, XmlSchemaSimpleTypeList simpleTypeList ) {
    XmlSchemaSimpleType itemType;
    if ( simpleTypeList.getItemTypeName() != null ) {
      itemType = schemaIndex.getXmlSchemaSimpleTypeByQName( simpleTypeList.getItemTypeName() );
    }
    else {
      itemType = simpleTypeList.getItemType();
    }
    return new XmlSchemaListSimpleValueFactory( getSimpleValueFactoryForSimpleType( itemType ), getSimpleValueValidatorForType( itemType ) );
  }

  private static XmlSimpleValueFactory getSimpleValueFactoryForSimpleTypeUnion( XmlSchemaIndex<?> schemaIndex, XmlSchemaSimpleTypeUnion xmlSchemaSimpleTypeUnion ) {
    List<LinkedList<XmlSimpleValueFactory>> hierarchies = new ArrayList<LinkedList<XmlSimpleValueFactory>>();
    if ( xmlSchemaSimpleTypeUnion.getMemberTypesQNames() != null ) {
      for ( QName simpleTypeName : xmlSchemaSimpleTypeUnion.getMemberTypesQNames() ) {
        LinkedList<XmlSimpleValueFactory> hierarchy = getSimpleTypeHierarchy( schemaIndex.getXmlSchemaSimpleTypeByQName( simpleTypeName ) );
        hierarchies.add(hierarchy);
      }
    }
    for ( XmlSchemaSimpleType xmlSchemaSimpleType : xmlSchemaSimpleTypeUnion.getBaseTypes() ) {
      hierarchies.add( getSimpleTypeHierarchy( xmlSchemaSimpleType ) );
    }
    Iterator<LinkedList<XmlSimpleValueFactory>> iter = hierarchies.iterator();
    LinkedList<XmlSimpleValueFactory> remainingHierarchy = iter.next();
    while ( iter.hasNext() ) {
      List<XmlSimpleValueFactory> nextHierarchy = iter.next();
      XmlSimpleValueFactory commonWrapper = null;
      for ( XmlSimpleValueFactory wrapper : nextHierarchy ) {
        if ( remainingHierarchy.contains( wrapper ) ) {
          commonWrapper = wrapper;
          break;
        }
      }
      Iterator<XmlSimpleValueFactory> it = remainingHierarchy.iterator();
      while ( it.hasNext() ) {
        XmlSimpleValueFactory wrapper = it.next();
        if ( wrapper.equals( commonWrapper ) ) {
          break;
        }
        it.remove();
      }
    }
    return remainingHierarchy.getFirst();
  }

  private static LinkedList<XmlSimpleValueFactory> getSimpleTypeHierarchy( XmlSchemaSimpleType xmlSchemaSimpleType ) {
    LinkedList<XmlSimpleValueFactory> hierarchy = new LinkedList<XmlSimpleValueFactory>();
    while ( xmlSchemaSimpleType != null ) {
      XmlSimpleValueFactory typeWrapper = getSimpleValueFactoryForSimpleType( xmlSchemaSimpleType );
      hierarchy.add( typeWrapper );
      xmlSchemaSimpleType = getSimpleTypeBase( xmlSchemaSimpleType );
    }
    return hierarchy;
  }

  public static XmlSchemaType getSchemaTypeForElement( XmlSchemaElement xsdElement ) {
    xsdElement.getSchemaIndex().maybeIndex( null );
    xsdElement = getActualElement(xsdElement);
    XmlSchemaType schemaTypeSpec;
    QName schemaTypeName = xsdElement.getSchemaTypeName();
    if ( schemaTypeName != null ) {
      schemaTypeSpec = xsdElement.getSchemaIndex().getXmlSchemaTypeByQName(schemaTypeName);
    }
    else if ( xsdElement.getSchemaType() != null ){
      schemaTypeSpec = xsdElement.getSchemaType();
    }
    else if ( xsdElement.getSubstitutionGroup() != null ) {
      // pick up type from substitution group head by default
      return getSchemaTypeForElement( xsdElement.getSchemaIndex().getXmlSchemaElementByQName( xsdElement.getSubstitutionGroup() ) );
    }
    else {
      schemaTypeName = ANY_TYPE_QNAME;
      schemaTypeSpec = xsdElement.getSchemaIndex().getXmlSchemaTypeByQName( schemaTypeName );
    }
    return schemaTypeSpec;
  }

  public static XmlSchemaType getSchemaTypeForAttribute( XmlSchemaAttribute xsdAttribute ) {
    xsdAttribute.getSchemaIndex().maybeIndex( null );
    xsdAttribute = getActualAttribute( xsdAttribute );
    XmlSchemaType schemaTypeSpec;
    if ( xsdAttribute.getSchemaType() != null ) {
      schemaTypeSpec = xsdAttribute.getSchemaType();
    }
    else {
      QName schemaTypeName = xsdAttribute.getSchemaTypeName();
      if ( schemaTypeName == null ) {
        schemaTypeName = ANY_SIMPLE_TYPE_QNAME;
      }
      schemaTypeSpec = xsdAttribute.getSchemaIndex().getXmlSchemaTypeByQName( schemaTypeName );
    }
    return schemaTypeSpec;
  }

  public static URL makeLocalIfValid( URL baseURL, String systemId, String namespaceURI, IModule module ) {
    return makeLocalIfValid( baseURL, systemId, namespaceURI, module, null );
  }

  public static URL makeLocalIfValid( URL baseURL, String systemId, String namespaceURI, IModule module, Map<String, String> externalLocationAliases ) {
    if ( systemId == null && namespaceURI != null ) {
      return getUrlForNamespace( namespaceURI, module );
    }
    else if ( baseURL == null ) {
      return null;
    }
    URL url;
    try {
      URL targetURL = new URL(baseURL, systemId);
      if ( !isLocal(targetURL) ) {
        url = getUrlForExternalLocation( systemId, module );
        if ( url != null && externalLocationAliases != null ) {
          externalLocationAliases.put( systemId, url.toExternalForm() );
        }
      }
      else {
        IFileSystemGosuClassRepository fileRepository = module.getFileRepository();
        String resourceName = fileRepository.getResourceName(targetURL);
        IFile foundFile = findFile(module, resourceName);
        if ( foundFile == null ) {
          throw new XmlException( "Unable to resolve resource with location '" + systemId + "' relative to '" + baseURL + "' with namespace '" + namespaceURI + "'" );
        }
        url = foundFile.toURI().toURL();
      }
    }
    catch ( MalformedURLException ex ) {
      throw new RuntimeException( "Unable to resolve resource " + systemId + " relative to " + baseURL,  ex );
    }
    return url;
  }

  private static IFile findFile(IModule module, String resourceName) {
    for (IModule m : module.getModuleTraversalList()) {
      IFile firstFile = m.getFileRepository().findFirstFile(resourceName);
      if (firstFile != null) {
        return firstFile;
      }
    }
    return null;
  }

  private static URL getUrlForNamespace( String namespaceURI, IModule module ) {
    XmlSchemaDefaultSchemaLocations locations = getDefaultSchemaLocations( module );
    IFile iFile = locations.getLocalResourcePathByXmlNamespace().get( namespaceURI );
    if ( iFile == null ) {
      return null;
    }
    try {
      return iFile.toURI().toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException( e );
    }
  }

  public static URL getUrlForExternalLocation( String externalLocation, IModule module ) {
    XmlSchemaDefaultSchemaLocations locations = getDefaultSchemaLocations( module );
    IFile iFile = locations.getLocalResourcePathsByExternalLocation().get( externalLocation );
    if ( iFile == null ) {
      throw new RuntimeException( "External location " + externalLocation + " is not configured in schemalocations.xml." );
    }
    try {
      return iFile.toURI().toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException( e );
    }
  }

  private static XmlSchemaDefaultSchemaLocations getDefaultSchemaLocations( IModule module ) {
    TypeSystem.lock();
    try {
      XmlSchemaDefaultSchemaLocations defaultSchemaLocations = _defaultSchemaLocations.get( module );
      if ( defaultSchemaLocations == null ) {
        defaultSchemaLocations = new XmlSchemaDefaultSchemaLocations();
        for ( IFile file : getSchemaLocationsFiles(module)) {
          try {
            // We can't use Schemalocations.parse() here, since that would cause a lookup of types, yet this
            // gets called while resolving types
            XmlElement root = XmlElement.parse( file.openInputStream() );
            QName rootQName = Schemalocations.$QNAME;
            if ( ! root.getQName().equals( rootQName ) ) {
              throw new RuntimeException( file + " has wrong root element. Expected: " + rootQName + ", Actual: " + root.getQName() );
            }
            for ( XmlElement child : root.getChildren( Schemalocations_Schema.$QNAME ) ) {
              String xmlNamespace = child.getAttributeValue( Schemalocations_Schema.$ATTRIBUTE_QNAME_XmlNamespace );
              String resourcePath = child.getAttributeValue( Schemalocations_Schema.$ATTRIBUTE_QNAME_ResourcePath );
              String externalLocations = child.getAttributeValue( Schemalocations_Schema.$ATTRIBUTE_QNAME_ExternalLocations );
              String[] parts = externalLocations.split( " " );
              IFile resource = findFile(module, resourcePath);
              for ( String part : parts ) {
                defaultSchemaLocations.getLocalResourcePathsByExternalLocation().put( part, resource );
              }
              defaultSchemaLocations.getLocalResourcePathByXmlNamespace().put( xmlNamespace, resource );
              defaultSchemaLocations.getExternalLocationsByLocalResourcePath().put( resourcePath, Arrays.asList(externalLocations.split(" ")) );
            }
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        }
        _defaultSchemaLocations.put( module, defaultSchemaLocations );
      }
      return defaultSchemaLocations;
    }
    finally {
      TypeSystem.unlock();
    }
  }

  private static Iterable<? extends IFile> getSchemaLocationsFiles(IModule module) {
    if (CommonServices.getPlatformHelper().isInIDE()) {
      module = TypeSystem.getGlobalModule();
    }
    Set<IFile> result = new LinkedHashSet<IFile>();
    result.addAll(findAllFiles("config/xml/schemalocations.xml", module.getRoots()));

    // XML module itself now has this file inside "sources path"
    result.addAll(findAllFiles("xml/schemalocations.xml", module.getSourcePath()));
    return result;
  }

  private static List<? extends IFile> findAllFiles(String resourceName, List<? extends IDirectory> searchPath) {
    List<IFile> results = new ArrayList<IFile>();

    for (IDirectory dir : searchPath) {
      IFile file = dir.file(resourceName);
      if (file != null && file.exists()) {
        results.add(file);
      }
    }

    return results;
  }

  public static boolean isLocal( URL url ) {
    return url.getProtocol().equals("file") || url.getProtocol().equals("jar");
  }

  public IType getGosuTypeByXmlSchemaElementQName( QName qname ) {
    XmlSchemaElement element = getXmlSchemaElementByQName( qname );
    if ( element == null ) {
      throw new NotFoundException( "Element not found: " + qname );
    }
    return XmlSchemaIndex.getGosuTypeBySchemaObject( element );
  }

  public XmlSchemaTypeSchemaInfo getElementInfoByXmlSchemaElementQName( QName qname ) {
    // first find the actual non-typeref element type for this QName
    IType actualElementRef = getGosuTypeByXmlSchemaElementQName( qname );
    XmlSchemaIndex<?> actualElementSchemaIndex = XmlSchemaIndex.getSchemaIndexByType( actualElementRef );
    XmlSchemaElementTypeData<?> actualElementTypeData = (XmlSchemaElementTypeData) actualElementSchemaIndex._typesByName.get( actualElementRef.getName() );

    // then find the actual non-typeref typeinstance type for that element
    IType actualTypeTypeRef = XmlSchemaIndex.getGosuTypeBySchemaObject( actualElementTypeData.getXsdType() );
    XmlSchemaIndex<?> actualTypeSchemaIndex = XmlSchemaIndex.getSchemaIndexByType( actualTypeTypeRef );
    XmlSchemaTypeInstanceTypeData actualTypeType = (XmlSchemaTypeInstanceTypeData) actualTypeSchemaIndex._typesByName.get( actualTypeTypeRef.getName() );
    return actualTypeType.getSchemaInfo();
  }

  public XmlSchemaTypeSchemaInfo getSchemaInfoByTypeName( String typeName ) {
    IXmlSchemaTypeData actualType = _typesByName.get( typeName );
    return actualType instanceof XmlSchemaTypeInstanceTypeData ? ( (XmlSchemaTypeInstanceTypeData) actualType ).getSchemaInfo() : null;
  }

  // TODO - this duplicates another method in this class, getTypeData() - remove?
  public IXmlSchemaTypeData getActualTypeByName( String name ) {
    return _typesByName.get( name );
  }

  public static void clearNormalizedSchemaNamespaces() {
    _normalizedSchemaNamespaces.clear();
  }

  public static String normalizeSchemaNamespace( String ns, String path ) {
    TypeSystem.lock();
    try {
      String normalizedSchemaNamespace = _normalizedSchemaNamespacesByPath.get( path );
      if ( normalizedSchemaNamespace == null ) {
        normalizedSchemaNamespace = normalizeSchemaNamespace( ns );
        if ( _normalizedSchemaNamespaces.contains( normalizedSchemaNamespace ) ) {
          throw new XmlException( "Multiple schemas found with package name " + normalizedSchemaNamespace );
        }
        _normalizedSchemaNamespacesByPath.put( path, normalizedSchemaNamespace );
        _normalizedSchemaNamespaces.add( normalizedSchemaNamespace );
      }
      return normalizedSchemaNamespace;
    }
    finally {
      TypeSystem.unlock();
    }
  }

  public static String normalizeSchemaNamespace( String ns ) {
    String normalizedSchemaNamespace;
    StringBuilder ret = new StringBuilder( ns.length() );
    String[] parts = ns.split( "\\." );
    for ( String part : parts ) {
      part = XmlSchemaIndex.normalizeName( part, NormalizationMode.LOWERCASE );
      if ( ret.length() > 0 ) {
        ret.append( '.' );
      }
      ret.append( part );
    }
    normalizedSchemaNamespace = ret.toString();
    return normalizedSchemaNamespace;
  }

  private static XmlSimpleValueValidator getSimpleValueValidatorForSimpleContent( XmlSchemaSimpleContent simpleContent, XmlSchemaComplexType complexType ) {
    XmlSimpleValueValidator validator;
    final XmlSchemaContent content = simpleContent.getContent();
    if ( content instanceof XmlSchemaSimpleContentRestriction ) {
      XmlSchemaSimpleContentRestriction restriction = (XmlSchemaSimpleContentRestriction) content;
      List<XmlSchemaFacet> facets = new ArrayList<XmlSchemaFacet>();
      for ( XmlSchemaFacet xmlSchemaFacet : restriction.getFacets() ) {
        facets.add( xmlSchemaFacet );
      }
      XmlSchemaType baseTypeSpec = simpleContent.getSchemaIndex().getXmlSchemaTypeByQName( restriction.getBaseTypeName() );
      XmlSimpleValueValidator parentValidator = getSimpleValueValidatorForType( baseTypeSpec );
      XmlSimpleValueFactory valueFactory = getSimpleValueFactoryForSimpleContent( complexType );
      validator = new XmlSimpleTypeSimpleValueValidator( null, parentValidator, facets, valueFactory );
    }
    else if ( content instanceof XmlSchemaSimpleContentExtension ) {
      // simple content extension can only add attributes
      XmlSchemaSimpleContentExtension extension = (XmlSchemaSimpleContentExtension) content;
      XmlSchemaType baseTypeSpec = simpleContent.getSchemaIndex().getXmlSchemaTypeByQName( extension.getBaseTypeName() );
      return getSimpleValueValidatorForSchemaType( baseTypeSpec );
    }
    else {
      throw new RuntimeException( "Unknown simple content content: " + content.getClass() );
    }
    return validator;
  }

  private static XmlSimpleValueValidator getSimpleValueValidatorForType( XmlSchemaType type ) {
    XmlSimpleValueValidator validator = null;
    if ( type instanceof XmlSchemaSimpleType ) {
      XmlSchemaSimpleType simpleType = (XmlSchemaSimpleType) type;
      if ( simpleType.getContent() instanceof XmlSchemaSimpleTypeRestriction ) {
        XmlSchemaSimpleTypeRestriction restriction = (XmlSchemaSimpleTypeRestriction) simpleType.getContent();
        List<XmlSchemaFacet> facets = new ArrayList<XmlSchemaFacet>();
        for ( XmlSchemaFacet facet : restriction.getFacets() ) {
          facets.add( facet );
        }
        XmlSchemaSimpleType baseTypeSpec = getSimpleTypeBase( simpleType );
        XmlSimpleValueValidator parentValidator = getSimpleValueValidatorForType( baseTypeSpec );
        XmlSimpleValueFactory valueFactory = getSimpleValueFactoryForSimpleType( simpleType );
        validator = new XmlSimpleTypeSimpleValueValidator( XmlSchemaPrimitiveType.get( simpleType.getQName() ), parentValidator, facets, valueFactory );
      }
      else if ( simpleType.getContent() instanceof XmlSchemaSimpleTypeUnion ) {
        List<XmlSimpleValueValidator> validators = new ArrayList<XmlSimpleValueValidator>();
        XmlSchemaSimpleTypeUnion xmlSchemaSimpleTypeUnion = (XmlSchemaSimpleTypeUnion) simpleType.getContent();
        if ( xmlSchemaSimpleTypeUnion.getMemberTypesQNames() != null ) {
          for ( QName simpleTypeName : xmlSchemaSimpleTypeUnion.getMemberTypesQNames() ) {
            XmlSchemaSimpleType baseType = simpleType.getSchemaIndex().getXmlSchemaSimpleTypeByQName( simpleTypeName );
            validators.add( getSimpleValueValidatorForType( baseType ) );
          }
        }
        for ( XmlSchemaSimpleType baseType : xmlSchemaSimpleTypeUnion.getBaseTypes() ) {
          validators.add( getSimpleValueValidatorForType( baseType ) );
        }
        validator = new XmlSimpleUnionValueValidator( validators );
      }
      else if ( simpleType.getContent() instanceof XmlSchemaSimpleTypeList ) {
        XmlSchemaSimpleTypeList simpleTypeList = (XmlSchemaSimpleTypeList) simpleType.getContent();
        XmlSchemaSimpleType itemType;
        if ( simpleTypeList.getItemTypeName() != null ) {
          itemType = simpleType.getSchemaIndex().getXmlSchemaSimpleTypeByQName( simpleTypeList.getItemTypeName() );
        }
        else {
          itemType = simpleTypeList.getItemType();
        }
        validator = new XmlSimpleListValueValidator( getSimpleValueValidatorForType( itemType ) );
      }
    }
    else {
      XmlSchemaComplexType complexType = (XmlSchemaComplexType) type;
      XmlSchemaSimpleContent content = (XmlSchemaSimpleContent) complexType.getContentModel();
      XmlSimpleValueFactory valueFactory = getSimpleValueFactoryForSimpleContent( complexType );
      if ( content.getContent() instanceof XmlSchemaSimpleContentRestriction ) {
        XmlSchemaSimpleContentRestriction restriction = (XmlSchemaSimpleContentRestriction) content.getContent();
        List<XmlSchemaFacet> facets = new ArrayList<XmlSchemaFacet>();
        for ( XmlSchemaFacet facet : restriction.getFacets() ) {
          facets.add( facet );
        }
        XmlSchemaType baseType = complexType.getSchemaIndex().getXmlSchemaTypeByQName( restriction.getBaseTypeName() );
        XmlSimpleValueValidator parentValidator = getSimpleValueValidatorForType( baseType );
        validator = new XmlSimpleTypeSimpleValueValidator( XmlSchemaPrimitiveType.get( complexType.getQName() ), parentValidator, facets, valueFactory );
      }
      else {
        XmlSchemaSimpleContentExtension extension = (XmlSchemaSimpleContentExtension) content.getContent();
        XmlSchemaType baseType = complexType.getSchemaIndex().getXmlSchemaTypeByQName( extension.getBaseTypeName() );
        XmlSimpleValueValidator parentValidator = getSimpleValueValidatorForType( baseType );
        validator = new XmlSimpleTypeSimpleValueValidator( XmlSchemaPrimitiveType.get( complexType.getQName() ), parentValidator, Collections.<XmlSchemaFacet>emptyList(), valueFactory );
      }
    }
    if ( validator == null ) {
      validator = new XmlSimpleNoopValueValidator();
    }
    return validator;
  }

  private static XmlSimpleValueFactory getSimpleValueFactoryForSimpleContent( XmlSchemaComplexType complexType ) {
    XmlSchemaEnumerationTypeData enumTypeData = complexType.getSchemaIndex().getEnumerationForSchemaType( complexType );
    if ( enumTypeData != null ) {
      return new XmlSchemaEnumSimpleValueFactory( enumTypeData );
    }
    final QName baseTypeName;
    final XmlSchemaContent content = complexType.getContentModel().getContent();
    if ( content instanceof XmlSchemaSimpleContentRestriction ) {
      baseTypeName = ( (XmlSchemaSimpleContentRestriction) content ).getBaseTypeName();
    }
    else if ( content instanceof XmlSchemaSimpleContentExtension ) {
      baseTypeName = ( (XmlSchemaSimpleContentExtension) content ).getBaseTypeName();
    }
    else {
      throw new RuntimeException( "Unknown simple content content: " + content );
    }
    XmlSchemaType baseType = complexType.getSchemaIndex().getXmlSchemaTypeByQName( baseTypeName );
    return getSimpleValueFactoryForSchemaType( baseType );
  }

  public static boolean hasSimpleContent( XmlSchemaObject xsdTypeOrElement ) {
    boolean hasSimpleContent = false;
    if ( xsdTypeOrElement instanceof XmlSchemaElement ) {
      xsdTypeOrElement = getSchemaTypeForElement( (XmlSchemaElement) xsdTypeOrElement );
    }
    if ( xsdTypeOrElement instanceof XmlSchemaSimpleType ) {
      hasSimpleContent = true;
    }
    else {
      XmlSchemaComplexType complexType = (XmlSchemaComplexType) xsdTypeOrElement;
      if ( complexType.getContentModel() instanceof XmlSchemaSimpleContent ) {
        hasSimpleContent = true;
      }
    }
    return hasSimpleContent;
  }

  public static XmlSimpleValueFactory getSimpleValueFactoryForSchemaType( XmlSchemaType schemaType ) {
    XmlSimpleValueFactory factory = null;
    if ( schemaType instanceof XmlSchemaSimpleType ) {
      factory = getSimpleValueFactoryForSimpleType( (XmlSchemaSimpleType) schemaType );
    }
    else {
      XmlSchemaComplexType complexType = (XmlSchemaComplexType) schemaType;
      if ( complexType.getContentModel() instanceof XmlSchemaSimpleContent ) {
        factory = getSimpleValueFactoryForSimpleContent( complexType );
      }
    }
    return factory;
  }

  public static XmlSimpleValueValidator getSimpleValueValidatorForSchemaType( XmlSchemaType schemaType ) {
    XmlSimpleValueValidator factory = null;
    if ( schemaType instanceof XmlSchemaSimpleType ) {
      factory = getSimpleValueValidatorForType( schemaType );
    }
    else {
      XmlSchemaComplexType complexType = (XmlSchemaComplexType) schemaType;
      if ( complexType.getContentModel() instanceof XmlSchemaSimpleContent ) {
        factory = getSimpleValueValidatorForSimpleContent( ( XmlSchemaSimpleContent ) complexType.getContentModel(), complexType );
      }
    }
    return factory;
  }

  public void registerEnumeration( XmlSchemaType xmlSchemaObject, XmlSchemaEnumerationTypeData enumTypeData ) {
    _enumerations.put( xmlSchemaObject, enumTypeData );
  }

  public static XmlSchemaIndex<?> getSchemaIndexByType( IType type ) {
    for ( XmlSchemaResourceTypeLoaderBase typeLoader : type.getTypeLoader().getModule().getTypeLoaders( XmlSchemaResourceTypeLoaderBase.class ) ) {
      XmlSchemaIndex<?> schemaIndex = typeLoader.getSchemaForType( type.getName() );
      if ( schemaIndex != null ) {
        return schemaIndex;
      }
    }
    return null;
  }

  public static XmlSchemaTypeSchemaInfo getSchemaInfoByType( IType type ) {
    XmlSchemaIndex<?> schemaIndex = getSchemaIndexByType( type );
    return schemaIndex == null ? null : schemaIndex.getSchemaInfoByTypeName( type.getName() );
  }

  public Map<String, LinkedHashSet<String>> getGosuNamespacesByXmlNamespace() {
    return Collections.unmodifiableMap(_gosuNamespacesByXMLNamespace.get());
  }

  @Override
  public String toString() {
    return _packageName;
  }

  public XmlSchemaAccessImpl getXmlSchemaAccess() {
    maybeIndex( null );
    return _xmlSchemaAccess.get();
  }

  public static String makeCamelCase( String name, NormalizationMode mode ) {
    if ( mode == NormalizationMode.PRESERVECASE ) {
      return name;
    }
    if ( name == null ) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    boolean makeNextCharUppercase = true;
    for ( int i = 0; i < name.length(); i++ ) {
      char ch = name.charAt( i );
      if ( makeNextCharUppercase ) {
        ch = Character.toUpperCase( ch );
        makeNextCharUppercase = false;
      }
      if ( ch == '_' || ! Character.isJavaIdentifierPart( ch ) ) {
        if ( i + 1 < name.length() ) {
          char nextChar = name.charAt( i + 1 );
          if ( Character.toUpperCase( nextChar ) != nextChar ) {
            makeNextCharUppercase = true;
            continue;
          }
        }
      }
      sb.append( ch );
    }
    if ( sb.length() == 0 ) {
      sb.append( "_" );
    }
    return sb.toString();
  }

  public Set<String> getAllCodegenSchemas() {
    TypeSystem.lock();
    try {
      Set<String> schemas = new HashSet<String>();
      for ( IDirectory root : getTypeLoader().getModule().getSourcePath() ) {
        schemas.addAll( getCodegenSchemasForDir(root) );
      }
      return schemas;
    }
    finally {
      TypeSystem.unlock();
    }
  }

  public static Set<String> getCodegenSchemasForDir(IDirectory dir) {
    TypeSystem.lock();
    try {
      Set<String> schemas = _codegenSchemasByModuleRoot.get( dir );
      if ( schemas == null ) {
        schemas = new HashSet<String>();
        IFile configFile = dir.file( "gw/internal/xml/config/xsd-codegen.xml" );
        if ( configFile == null || !configFile.exists() ) {
          return Collections.emptySet();
        }
        XmlElement config;
        try {
          config = XmlElement.parse( configFile.openInputStream() );
        }
        catch ( IOException ex ) {
          throw new RuntimeException( ex );
        }
        List<XmlElement> schemaElements = config.getChildren( new QName( "http://guidewire.com/config/xml/xsd-codegen", "schema" ) );
        for ( XmlElement schemaElement : schemaElements ) {
          schemas.add( schemaElement.getAttributeValue( "namespace" ) );
        }
        _codegenSchemasByModuleRoot.put( dir, schemas );
      }
      return schemas;
    }
    finally {
      TypeSystem.unlock();
    }
  }

  public long getFingerprint() {
    return _schemaFP64.get();
  }

  public IJavaClassInfo getGeneratedClass( String name ) {
    if ( ! _usesJavaBackedTypes ) {
      return null;
    }
    String className = "gw.internal.schema." + name;
    IJavaClassInfo clazz = TypeSystem.getJavaClassInfo(className, getTypeLoader().getModule());
    if (clazz == null) {
      throw new RuntimeException( "Class " + className + " does not exist. Please generate it using xsd-codegen." );
    }
    if (!(clazz instanceof IAsmJavaClassInfo) && clazz.getBackingClass() != null) {
      try {
        Field field = clazz.getBackingClass().getDeclaredField( "FINGERPRINT" );
        field.setAccessible( true );
        long fingerprint = (Long) field.get( null );
        if ( fingerprint != getFingerprint() ) {
          throw new RuntimeException( "Class " + clazz.getName() + " is out of date. Please regenerate it using xsd-codegen." );
        }
      }
      catch ( NoSuchFieldException ex ) {
        throw new RuntimeException( ex );
      }
      catch ( IllegalAccessException ex ) {
        throw new RuntimeException( ex );
      }
    }
    return clazz;
  }

  public String getTargetNamespaceAsDeclaredInSchema() {
    if ( getXmlSchemaCollection().getXmlSchemas().size() != 1 ) {
      throw new UnsupportedOperationException( "Only supported for single-schema indexes, such as those based on an XSD" );
    }
    return getXmlSchemaCollection().getXmlSchemas().iterator().next().getDeclaredTargetNamespace();
  }

  public String getXSDSourcePath() {
    IFileSystemGosuClassRepository fileRepository = getTypeLoader().getModule().getFileRepository();
    String resourceName = fileRepository.getResourceName( _xmlSchemaSource.getBlueprintURL() );
    if ( resourceName.startsWith( "/" ) ) {
      resourceName = resourceName.substring( 1 );
    }
    return resourceName;
  }

  public WsdlDefinitions getWsdlDefinitions() {
    maybeIndex( null );
    return _wsdlDefinition;
  }

  public void validate( Map<Pair<URL, String>, XmlSchema> caches ) {
    try {
      if ( getWsdlDefinitions() == null || System.getProperty( "gw.internal.xml.do_not_validate_wsdl" ) == null ) {
        parseSchemasButDoNotCache( Collections.<XmlSchemaIndex>singletonList( this ), null, null );
      }
    }
    catch ( Exception ex ) {
      if ( getXSDSource().getBlueprintURL().getProtocol().equals( "jar" ) || getTypeLoader().getClass().getName().equals( "com.guidewire.commons.system.gx.GXTypeLoader" ) ) {
        clearExternalJarXSD();
      }
      else {
        StringBuilder sb = new StringBuilder();
        sb.append( "Could not parse schema " );
        sb.append( _xmlSchemaSource.getDescription() );
        throw new XmlException( sb.toString(), ex );
      }
    }
  }

  public void removeTypeName( String typeName ) {
    _allTypeNames.remove( typeName );
  }

  public XmlSchemaImportInfo getImportInfo() {
    return new XmlSchemaImportInfo( getTargetNamespaceAsDeclaredInSchema(), getXSDSourcePath(), false );
  }

  public enum NormalizationMode {
    PROPERCASE, PRESERVECASE, UPPERCASE, LOWERCASE
  }

  public XmlSchemaCollection getXmlSchemaCollection() {
    maybeIndex( null );
    return _collection;
  }

  private void addSchemasToNamespaceMappings( String myGosuNamespace, LinkedHashSet<XmlSchema> schemas, LinkedHashMap<String, LinkedHashSet<String>> gosuNamespacesByXMLNamespace ) {
    Set<String> myNamespaces = new HashSet<String>();
    if ( getWsdlDefinitions() != null ) {
      // intra-wsdl import support
      for ( XmlSchema schema : schemas ) {
        myNamespaces.add( schema.getTargetNamespace() );
      }
    }
    for ( XmlSchema schema : schemas ) {
      // if schema does not define any top level components, don't add it -- this supports WSDL's empty-xsd-with-only-imports style
      if ( ! schema.isEmpty() ) {
        String targetNamespace = schema.getTargetNamespace();
        addGosuNamespaceByXmlNamespace( gosuNamespacesByXMLNamespace, targetNamespace, myGosuNamespace );
      }
      for ( XmlSchemaImport imprt : schema.getImports() ) {
        if ( myNamespaces.contains( imprt.getNamespaceURI() ) ) {
          // intra-wsdl import support
          addGosuNamespaceByXmlNamespace( gosuNamespacesByXMLNamespace, imprt.getNamespaceURI(), myGosuNamespace );
        }
        else {
          String targetNamespace = imprt.getNamespaceURI();
          String schemaLocation = imprt.getSchemaLocation();
          URL url = makeLocal( imprt.getBaseUrl(), schemaLocation, targetNamespace, getTypeLoader().getModule() );
          if ( url != null ) {
            String targetGosuNamespace = getGosuNamespace( url, getTypeLoader().getModule() );
            if ( targetGosuNamespace == null ) {
              throw new RuntimeException( "Gosu namespace not found for imported schema " + url );
            }
            addGosuNamespaceByXmlNamespace( gosuNamespacesByXMLNamespace, targetNamespace, targetGosuNamespace );
          }
        }
      }
    }
  }

  private void addGosuNamespaceByXmlNamespace( LinkedHashMap<String, LinkedHashSet<String>> gosuNamespacesByXMLNamespace, String xmlNamespace, String gosuNamespace ) {
    LinkedHashSet<String> nsList = gosuNamespacesByXMLNamespace.get( xmlNamespace );
    if ( nsList == null ) {
      nsList = new LinkedHashSet<String>();
      gosuNamespacesByXMLNamespace.put( xmlNamespace, nsList );
    }
    nsList.add ( gosuNamespace );
  }

  /**
   * This method will return null only if schemaLocation is null, and targetNamespace is not registered
   * in schemalocations.xml.
   */
  public static URL makeLocal( URL schemaEF, String schemaLocation, String targetNamespace, IModule module ) {
    return makeLocal( schemaEF, schemaLocation, targetNamespace, module, null );
  }

  /**
   * This method will return null only if schemaLocation is null, and targetNamespace is not registered
   * in schemalocations.xml.
   */
  public static URL makeLocal( URL schemaEF, String schemaLocation, String targetNamespace, IModule module, Map<String, String> externalLocationAliases ) {
    URL url = makeLocalIfValid( schemaEF, schemaLocation, targetNamespace, module, externalLocationAliases );
    if ( url == null && schemaLocation != null ) {
      throw new RuntimeException( "Unable to map imported schema " + schemaLocation + " with targetNamespace " + targetNamespace + " relative to " + schemaEF + " to a local resource. This can be fixed by adding it to schemalocations.xml." );
    }
    return url;
  }

  public static XmlSchemaIndex getSchemaIndexForFilePath( String testPath ) {
    XmlSchemaIndex source = null;
    OUTER:
    for ( XmlSchemaResourceTypeLoaderBase<?> typeLoader : TypeSystem.getGlobalModule().getTypeLoaders( XmlSchemaResourceTypeLoaderBase.class ) ) {
      final Collection<String> namespaces = typeLoader.getAllSchemaNamespaces();
      for (String ns : namespaces) {
        final XmlSchemaIndex si = typeLoader.getSchemaForNamespace(ns);
        if (si.getXSDSourcePath().equals(testPath)) {
          source = si;
          break OUTER;
        }
      }
    }
    return source;
  }

  public IXmlTypeData getAdditionalTypeData( String fullyQualifiedName ) {
    return null;
  }

  public Set<String> getAdditionalTypeNames() {
    return Collections.emptySet();
  }

  // used by tests written in Gosu
  @SuppressWarnings( { "UnusedDeclaration" } )
  public String getExternalLocationForTesting() {
    int pos = _packageName.lastIndexOf('.');
    String path = _packageName.substring(0, pos).replace('.','/') + "/" + _xmlSchemaSource.getResourceFile().getName();
    XmlSchemaDefaultSchemaLocations locations = getDefaultSchemaLocations( getTypeLoader().getModule() );
    final List<String> stringList = locations.getExternalLocationsByLocalResourcePath().get(path);
    if (stringList != null && stringList.size() > 0) {
      return stringList.get(0);
    }
    return null;
  }

  public static String makeUniquePropertyName( Set<String> usedPropertyNames, String propertyName, XmlSchemaIndex.NormalizationMode normalizationMode ) {
    propertyName = XmlSchemaIndex.normalizeName( propertyName, normalizationMode );
    if ( usedPropertyNames.contains( propertyName ) ) {
      int suffix = 2;
      String newPropertyName = propertyName;
      while ( usedPropertyNames.contains( newPropertyName ) ) {
        newPropertyName = propertyName + suffix++;
      }
      propertyName = newPropertyName;
    }
    usedPropertyNames.add( propertyName );
    return propertyName;
  }

  private NotFoundException throwNotFoundException( String type, QName name ) {
    throw new NotFoundException( type + " " + name + " not found in schema " + _packageName );
  }

  public URL getSchemaURL() {
    return _schemaEF;
  }

}
