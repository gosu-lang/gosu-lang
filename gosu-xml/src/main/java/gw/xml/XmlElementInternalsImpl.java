/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.config.CommonServices;
import gw.internal.ext.org.apache.xerces.jaxp.SAXParserFactoryImpl;
import gw.internal.ext.org.apache.xerces.jaxp.SAXParserImpl;
import gw.internal.xml.IXMLWriter;
import gw.internal.xml.TeeOutputStream;
import gw.internal.xml.XMLWriterFactory;
import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlElementInternals;
import gw.internal.xml.XmlMixedContentList;
import gw.internal.xml.XmlParserCallback;
import gw.internal.xml.XmlSchemaAccessImpl;
import gw.internal.xml.XmlSchemaLocalResourceResolver;
import gw.internal.xml.XmlSerializationContext;
import gw.internal.xml.XmlSimpleValueBase;
import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.XmlTypeInstanceInternals;
import gw.internal.xml.XmlTypeResolver;
import gw.internal.xml.XmlUtil;
import gw.internal.xml.xsd.typeprovider.IXmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.IXmlSchemaTypeInstanceTypeData;
import gw.internal.xml.xsd.typeprovider.IXmlType;
import gw.internal.xml.xsd.typeprovider.NotFoundException;
import gw.internal.xml.xsd.typeprovider.XmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertySpec;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeInstanceTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAnyAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaAttribute;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaCollection;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.IDREFSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleValueValidator;
import gw.internal.xml.xsd.typeprovider.xmlmatcher.MatchFoundException;
import gw.internal.xml.xsd.typeprovider.xmlmatcher.XmlMatchHandler;
import gw.internal.xml.xsd.typeprovider.xmlmatcher.XmlSchemaAnyMatchHandler;
import gw.internal.xml.xsd.typeprovider.xmlsorter.XmlSorter;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.Pair;
import gw.util.StreamUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

@SuppressWarnings( { "UnusedDeclaration" } )
class XmlElementInternalsImpl extends XmlElementInternals {

  private static final Pair<IType, IType> EMPTY_PAIR = new Pair<IType, IType>( null, null );
  private static final XmlTypeResolver DEFAULT_TYPE_RESOLVER = new XmlTypeResolver() {
    @Override
    public Pair<IType, IType> resolveTypes( List<QName> elementStack ) {
      return EMPTY_PAIR;
    }

    @Override
    public Schema getSchemaForValidation() {
      return null;
    }

    @Override
    public String getValidationSchemasDescription() {
      return null;
    }

  };

  private static final QName XSI_TYPE_ATTRIBUTE_QNAME = new QName( XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "type" );
  private static final QName XSI_NIL_ATTRIBUTE_QNAME = new QName( XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil" );
  private static final Method _isSpecifiedMethod;
  private static final XmlParseOptions DEFAULT_PARSE_OPTIONS = new XmlParseOptions();

  static {
    try {
      Class<?> clazz = Class.forName( "gw.internal.ext.org.apache.xerces.parsers.AbstractSAXParser$AttributesProxy" );
      _isSpecifiedMethod = clazz.getMethod( "isSpecified", int.class );
    }
    catch ( Exception ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
  }

  @Override
  public void writeTo( final XmlElement element, OutputStream out, XmlSerializationOptions options ) {
    if ( options == null ) {
      options = new XmlSerializationOptions();
    }
    boolean validate;
    final IType type = element._type;
    final XmlSchemaIndex<?> schemaIndex = type == null ? null : XmlSchemaIndex.getSchemaIndexByType( type );
    if ( type == null || ! options.getValidate()) {
      validate = false;
    }
    else {
      XmlSchemaElementTypeData typeData = (XmlSchemaElementTypeData) schemaIndex.getTypeData( type.getName() );
      validate = ! typeData.isAnonymous();
    }

    if ( ! validate ) {
      try {
        writeWithoutValidation( element, out, new XmlSerializationContext(), options );
      }
      catch ( IOException ex ) {
        throw GosuExceptionUtil.convertToRuntimeException( ex );
      }
    }
    else {
      try {
        final XmlSerializationContext context = new XmlSerializationContext();
        TeeOutputStream teeOut;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        teeOut = new TeeOutputStream( out, baos );
        writeWithoutValidation( element, teeOut, context, options );
        validate( context.getRequiredSchemas(), type, new ByteArrayInputStream( baos.toByteArray() ), element.toString(), false, null );
      }
      catch ( IOException ex ) {
        throw new XmlException( "Unable to generate XML", ex );
      }
    }
  }

  private static int _cacheHits = 0;
  private static int _cacheMisses = 0;

  @Override
  public void doDeclareNamespace( XmlElement element, String nsuri, String suggestedPrefix, Map<String, URI> _uriCache ) {
    if ( nsuri == null ) {
      throw new IllegalArgumentException("The value for the namespace URI must not be null");
    }
    if ( suggestedPrefix == null ) {
      throw new IllegalArgumentException("The value for the suggestedPrefix parameter must not be null");
    }
    URI uri;
    if ( _uriCache == null ) {
      try {
        uri = new URI( nsuri );
      }
      catch ( URISyntaxException e) {
        throw GosuExceptionUtil.convertToRuntimeException( e );
      }
    }
    else {
      uri = _uriCache.get( nsuri );
      if ( uri == null ) {
        try {
          uri = new URI( nsuri );
        }
        catch ( URISyntaxException e) {
          throw GosuExceptionUtil.convertToRuntimeException( e );
        }
        _uriCache.put( nsuri, uri );
      }
    }
    element._declaredNamespaceBindings.add( new Pair<String, URI>( suggestedPrefix, uri ) );
  }

  private void writeWithoutValidation( XmlElement element, OutputStream out, XmlSerializationContext context, XmlSerializationOptions options ) throws IOException {
    IXMLWriter writer = XMLWriterFactory.newDefaultXMLWriter( out, options );
    writeTo( element, writer, context, false );
    writer.finish();
  }

  void writeTo( XmlElement element, IXMLWriter writer, XmlSerializationContext context, boolean mixed ) throws IOException {

    context.pushScope(); // at beginning of each element, push namespace scope
    context.setCurrentElement( element ); // so other parts of serialization can reference the current element

    if ( element._comment != null ) {
      writer.writeComment( " " + element._comment + " " );
    }

    writeAttributes( element, writer, context );

    context.addRequiredSchema( element._type );
    XmlTypeInstance typeInstance = element.getTypeInstance();
    context.addRequiredSchema( XmlTypeInstanceInternals.instance().getType( typeInstance ) );

    if ( typeInstance._schemaInfo != null ) {
      mixed = typeInstance._schemaInfo.isMixed();
    }
    if ( mixed ) {
      boolean pretty = writer.getWriterOptions().getPretty();
      writer.getWriterOptions().setPretty( false );
      writeContents( element, writer, context, mixed );
      writer.endElement();
      writer.getWriterOptions().setPretty( pretty );
    }
    else {
      writeContents( element, writer, context, mixed );
      writer.endElement();
    }
    context.popScope();
  }

  private void writeContents( XmlElement element, IXMLWriter writer, XmlSerializationContext context, boolean mixed ) throws IOException {

    for ( XmlElement child : element.getChildren() ) {
      context.addRequiredSchema( getType( child ) );
      context.addRequiredSchema( XmlTypeInstanceInternals.instance().getType( child.getTypeInstance() ) );
    }

    // now, write out the element's ( possibly mixed ) contents
    List<? extends IXmlMixedContent> childList = element.getTypeInstance()._children;
    if ( ! mixed && XmlTypeInstanceInternals.instance().getType( element.getTypeInstance() ) != null && writer.getWriterOptions().getSort() ) {
      // this element is based on an XSD - possibly sort it's contents to match the XSD
      final XmlSchemaType xsdType = XmlTypeInstanceInternals.instance().getSchemaInfo( element.getTypeInstance() ).getXsdType();
      // only sort contents for complex types
      if ( xsdType instanceof XmlSchemaComplexType ) {
        try {
          LinkedList<XmlElement> remainingChildren = new LinkedList<XmlElement>( element.getChildren() );
          childList = XmlSorter.sort( element.isNil() ? null : xsdType, remainingChildren, context.getRequiredSchemas() );
        }
        catch ( XmlSortException ex ) {
          wrapAndThrowSortException( element, ex );
        }
      }
    }
    if ( element.getSimpleValue() == null ) {
      for ( IXmlMixedContent child : childList ) {
        if ( child instanceof XmlElement ) {
          writeTo( (XmlElement) child, writer, context, mixed );
        }
        else if ( mixed ) {
          writer.addText( ( (XmlMixedContentText) child ).getText() );
        }
      }
    }
    else {
      assert element.getChildren().isEmpty();
      ( (XmlSimpleValueBase) element.getSimpleValue() ).writeTo( writer, context );
    }
  }

  private void wrapAndThrowSortException( XmlElement element, XmlSortException ex ) {
    final StringBuilder errorMessage = new StringBuilder( "Unable to process children of element " + element.getQName() + "." );
    List<QName> missingChildren = ex.getQNames();
    if ( ! missingChildren.isEmpty() ) {
      errorMessage.append( " Expected one of " );
      Map<String,Set<String>> sortedMissingChildren = new TreeMap<String, Set<String>>();
      for ( QName qname : missingChildren ) {
        Set<String> localParts = sortedMissingChildren.get( qname.getNamespaceURI() );
        if ( localParts == null ) {
          localParts = new TreeSet<String>();
          sortedMissingChildren.put( qname.getNamespaceURI(), localParts );
        }
        localParts.add( qname.getLocalPart() );
      }
      boolean firstNsuri = true;
      for ( Map.Entry<String,Set<String>> entry : sortedMissingChildren.entrySet() ) {
        String nsuri = entry.getKey();
        Set<String> localParts = entry.getValue();
        if ( ! firstNsuri ) {
          errorMessage.append( ", " );
        }
        firstNsuri = false;
        errorMessage.append( "{" );
        if ( ! nsuri.equals( XMLConstants.NULL_NS_URI ) ) {
          errorMessage.append( "\"" );
        }
        errorMessage.append( nsuri );
        if ( ! nsuri.equals( XMLConstants.NULL_NS_URI ) ) {
          errorMessage.append( "\":" );
        }
        boolean firstLocalPart = true;
        for ( String localPart : localParts ) {
          if ( ! firstLocalPart ) {
            errorMessage.append( ", " );
          }
          firstLocalPart = false;
          errorMessage.append( localPart );
        }
        errorMessage.append( "}" );
      }
      throw new XmlSortException( errorMessage.toString() );
    }
    else {
      //noinspection ThrowableResultOfMethodCallIgnored
      throw new XmlSortException( errorMessage.toString(), (XmlSortException) GosuExceptionUtil.findExceptionCause( ex ) );
    }
  }

  private void writeAttributes( XmlElement element, IXMLWriter writer, XmlSerializationContext context ) throws IOException {
    // This is the list of namespaces to declare at this level. Some namespaces will already have been declared
    // on ancestor elements, and need not be redeclared under most circumstances.
    TreeMap<String,String> namespacesToDeclare = new TreeMap<String, String>();

    // If null namespace is used anywhere in this element, the default namespace must be undeclared
    declareNullNamespaceIfRequired( element, context, namespacesToDeclare );

    // add any additional user-declared namespaces at this level
    for ( Pair<String, URI> pair : element._declaredNamespaceBindings) {
      addNamespace(context, pair.getFirst(), pair.getSecond().toString(), namespacesToDeclare, false, true );
    }

    IXmlType xmlType = (IXmlType) XmlTypeInstanceInternals.instance().getType( element.getTypeInstance() );
    IXmlSchemaTypeInstanceTypeData typeData = xmlType == null ? null : (IXmlSchemaTypeInstanceTypeData) xmlType.getTypeData();
    LinkedHashMap<QName, XmlSimpleValue> attributes = element.getTypeInstance()._attributes;
    Map<QName, XmlSimpleValue> localAttributes = attributes == null ? null : new LinkedHashMap<QName, XmlSimpleValue>( attributes );
    XmlSchemaTypeSchemaInfo schemaInfo = typeData == null ? null : typeData.getSchemaInfo();

    // Attributes are special in that if they don't have a prefix, they don't inherit the default namespace...
    // Therefore, check them first... If we're going to declare a namespace, declare it based on the attribute
    // prefix naming rules... Then that namespace could be reused elsewhere in the element if necessary... Without
    // doing this, we can end up with multiple namespace declarations on a single element for the same namespace.
    //
    // For example, we could end up with <root ns0:attr="value" xmlns="foo" xmlns:ns0="foo"/>
    // This would happen because we checked the namespace of the element first, decided it needed to be declared,
    // declared it as mapped to the empty prefix, then when we got to the attribute, we can't use the empty prefix
    // for an attribute obviously, and must declare it again.
    if ( localAttributes != null ) {
      for ( QName attributeQName : localAttributes.keySet() ) {
        // No point in declaring the null namespace for an attribute since it gets it by default anyway when no prefix is specified
        if ( ! XMLConstants.NULL_NS_URI.equals( attributeQName.getNamespaceURI() ) ) {
          addNamespace( context, attributeQName.getPrefix(), attributeQName.getNamespaceURI(), namespacesToDeclare, true, false );
        }
      }
    }

    // add element namespace to namespaces ( may or may not cause declaration )
    String elementQNamePrefix = addNamespace( context, element.getQName().getPrefix(), element.getQName().getNamespaceURI(), namespacesToDeclare, false, false );
    writer.startElement( makeQNameString( elementQNamePrefix, element.getQName().getLocalPart() ) );
    // write out attributes of this element
    // first write attributes declared in schema, in the order they are declared in schema
    if ( localAttributes != null ) {
      if ( schemaInfo != null ) {
        for ( QName attributeQName : schemaInfo.getAttributeNames() ) {
          XmlSimpleValue attributeSimpleValue = localAttributes.remove( attributeQName );
          if ( attributeSimpleValue != null ) {
            writeAttribute( writer, context, namespacesToDeclare, attributeQName, attributeSimpleValue );
          }
        }
      }
      for ( Map.Entry<QName, XmlSimpleValue> entry : localAttributes.entrySet() ) {
        QName attributeQName = entry.getKey();
        XmlSimpleValue attributeSimpleValue = entry.getValue();
        writeAttribute( writer, context, namespacesToDeclare, attributeQName, attributeSimpleValue );
      }
    }
    // add namespaces for any qnames of any of the element's simple value contents
    if ( element.getSimpleValue() != null ) {
      for ( QName qname : element.getSimpleValue().getQNames() ) {
        addNamespace( context, qname.getPrefix(), qname.getNamespaceURI(), namespacesToDeclare, false, false );
      }
    }
    // add xsi:type if required
    QName typeInstanceQName = getExplicitTypeInstanceQName( element );
    if ( typeInstanceQName != null ) {
      String xsiPrefix = addNamespace( context, "xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, namespacesToDeclare, true, false );
      String typeInstancePrefix = addNamespace( context, "", typeInstanceQName.getNamespaceURI(), namespacesToDeclare, false, false );
      writer.addAttribute( makeQNameString( xsiPrefix, "type" ), makeQNameString( typeInstancePrefix, typeInstanceQName.getLocalPart() ) );
    }

    // add xsi:nil if required
    if ( element.isNil() ) {
      String xsiPrefix = addNamespace( context, "xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, namespacesToDeclare, true, false );
      writer.addAttribute( makeQNameString( xsiPrefix, "nil" ), "true" );
    }

    // finally, actually declare any namespaces that must be declared
    for ( Map.Entry<String,String> entry : namespacesToDeclare.entrySet() ) {
      writer.addAttribute( makeXmlnsString( entry.getKey() ), entry.getValue() );
    }
  }

  private QName getExplicitTypeInstanceQName( XmlElement element ) {
    IType typeInstanceTypeRef = XmlTypeInstanceInternals.instance().getType( element.getTypeInstance() );
    boolean includeXsiType = ( element._schemaDefinedTypeInstanceType == null && ! isAnyType( typeInstanceTypeRef ) )
            || ( element._schemaDefinedTypeInstanceType != null && ! element._schemaDefinedTypeInstanceType.equals( typeInstanceTypeRef ) );
    if ( includeXsiType ) {
      String typeInstanceTypeName = typeInstanceTypeRef.getName();
      XmlSchemaIndex<?> typeInstanceSchemaIndex = XmlSchemaIndex.getSchemaIndexByType( typeInstanceTypeRef );
      XmlSchemaTypeInstanceTypeData typeInstanceTypeData = (XmlSchemaTypeInstanceTypeData) typeInstanceSchemaIndex.getActualTypeByName( typeInstanceTypeName );
      if ( ! typeInstanceTypeData.isAnonymous() ) {
        return typeInstanceTypeData.getSchemaInfo().getXsdType().getQName();
      }
    }
    return null;
  }

  private void writeAttribute( IXMLWriter writer, XmlSerializationContext context, TreeMap<String, String> namespacesToDeclare, QName attributeQName, XmlSimpleValue attributeSimpleValue ) throws IOException {
    final String attributeQNamePrefix;
    if ( attributeQName.getNamespaceURI().equals( XMLConstants.NULL_NS_URI ) ) {
      attributeQNamePrefix = XMLConstants.DEFAULT_NS_PREFIX; // unqualified attributes are always in the null namespace
    }
    else {
      attributeQNamePrefix = addNamespace( context, attributeQName.getPrefix(), attributeQName.getNamespaceURI(), namespacesToDeclare, true, false );
    }
    // add namespaces for any qnames of the attribute's simple value ( xsd:QName derived by xsd:list could contain multiple )
    for ( QName qname : attributeSimpleValue.getQNames() ) {
      addNamespace( context, qname.getPrefix(), qname.getNamespaceURI(), namespacesToDeclare, false, false );
    }
    // finally, write the attribute itself
    writer.addAttribute( makeQNameString( attributeQNamePrefix, attributeQName.getLocalPart() ), ( (XmlSimpleValueBase) attributeSimpleValue ).serialize( context ) );
  }

  private void declareNullNamespaceIfRequired( XmlElement element, XmlSerializationContext context, TreeMap<String, String> namespacesToDeclare ) throws IOException {

    // The null ns uri can only be assigned to default prefix. The reason is that, according to the spec, a
    // declaration such as xmlns="" is actually *undeclaring* the namespace (restoring the "lack of a namespace" as
    // the default namespace). A declaration like xmlns:foo="", however, undefines the "foo" prefix, making it
    // unusable. I will refer to this "lack of a namespace" as "being in the null namespace" since it's an easier
    // concept to understand (at least for me). I read that XML 2.0 is planning on actually removing the concept
    // of a "lack of a namespace" altogether, and replacing it with the concept of an "empty-string" namespace,
    // which could be assigned to any prefix, which is essentially XMLConstants.NULL_NS_URI
    // See http://www.w3.org/TR/xml-names11/ for more information on undeclaring namespaces

    // So therefore, if this element is in the null namespace, and since the null namespace can only be
    // used as a default namespace (not prefixed), then any existing default namespace MUST be undeclared.
    // Unfortunately, this concept applies to all attribute values that are xsd:QName simple values...
    // AND to the element content itself if it contains any xsd:QName simple values. (We allow multiple)
    // It also applies to any simple values derived from QName, including those derived by xsd:list, which
    // means a single list entry of a single attribute value of type xsd:QName-derivedby-xsd:List could possibly
    // contain a single QName entry in the null namespace, requiring us to undeclare the default namespace...
    // All of these places must be checked, and all of the following logic is simply to determine whether we should
    // add an xmlns="" declaration (undeclaration) to this element, and all of this has to be done before
    // writing out the first non-namespace-declaration attribute.

    // We need not check attribute names, however, since an attribute name is always in the null
    // namespace unless explicitly prefixed

    boolean nullNsUriUsed = element.getQName().getNamespaceURI().equals( XMLConstants.NULL_NS_URI ); // check element

    if ( ! nullNsUriUsed ) {
      // check simple value contents
      if ( element.getSimpleValue() != null ) {
        for ( QName qname : element.getSimpleValue().getQNames() ) {
          if ( qname.getNamespaceURI().equals( XMLConstants.NULL_NS_URI ) ) {
            nullNsUriUsed = true;
            break;
          }
        }
      }
    }

    if ( ! nullNsUriUsed ) {
      // check attribute simple value contents
      LinkedHashMap<QName, XmlSimpleValue> attributes = element.getTypeInstance()._attributes;
      if ( attributes != null ) {
        OUTER:
        for ( XmlSimpleValue content : attributes.values() ) {
          for ( QName qname : content.getQNames() ) {
            if ( qname.getNamespaceURI().equals( XMLConstants.NULL_NS_URI ) ) {
              nullNsUriUsed = true;
              break OUTER;
            }
          }
        }
      }
    }

    if ( ! nullNsUriUsed ) {
      // check declared namespace bindings
      for ( Pair<String, URI> pair : element._declaredNamespaceBindings ) {
        if ( pair.getSecond().toString().equals( XMLConstants.NULL_NS_URI ) ) {
          nullNsUriUsed = true;
          break;
        }
      }
    }

    // if we are to include an xsi:type, the namespace of the explicit type might be in the null namespace
    if ( ! nullNsUriUsed ) {
      final QName typeInstanceQName = getExplicitTypeInstanceQName( element );
      if ( typeInstanceQName != null && typeInstanceQName.getNamespaceURI().equals( XMLConstants.NULL_NS_URI ) ) {
        nullNsUriUsed = true;
      }
    }

    if ( nullNsUriUsed ) {
      // undeclare the default namespace
      addNamespace( context, XMLConstants.DEFAULT_NS_PREFIX, XMLConstants.NULL_NS_URI, namespacesToDeclare, false, false );
    }

  }

  private String makeQNameString(String prefix, String localPart) {
    String qnameString;
    if ( prefix.equals( XMLConstants.DEFAULT_NS_PREFIX ) ) {
      qnameString = localPart;
    }
    else {
      qnameString = prefix + ':' + localPart;
    }
    return qnameString;
  }

  private String makeXmlnsString( String prefix ) {
    String xmlnsString;
    if ( prefix.equals( XMLConstants.DEFAULT_NS_PREFIX ) ) {
      xmlnsString = "xmlns";
    }
    else {
      xmlnsString = "xmlns:" + prefix;
    }
    return xmlnsString;
  }

  private String addNamespace( XmlSerializationContext context, String suggestedPrefix, String nsuri, TreeMap<String, String> namespacesToDeclare, boolean isAttribute, boolean redefineIfAlreadyDeclared ) throws IOException {
    if ( XMLConstants.NULL_NS_URI.equals( nsuri ) ) {
      suggestedPrefix = XMLConstants.DEFAULT_NS_PREFIX; //Non-default namespace can not map to empty URI (as per Namespace 1.0 # 2) in XML 1.0 documents
    }
    else if ( XMLConstants.XML_NS_URI.equals( nsuri ) ) {
      return XMLConstants.XML_NS_PREFIX; // See http://www.w3.org/XML/1998/namespace - no need to declare specifically
    }
    else if ( suggestedPrefix.toLowerCase().startsWith( "xml" ) ) {
      suggestedPrefix = ""; // prefixes starting with "[Xx][Mm][Ll]" are all reserved
    }
    // use suggested prefix if already defined as this nsuri
    boolean declareNamespaceAtThisLevel = false;
    String newPrefix = suggestedPrefix;
    String existingNSURI = ( isAttribute && suggestedPrefix.equals( XMLConstants.DEFAULT_NS_PREFIX ) ) ? XMLConstants.NULL_NS_URI : context.getPrefixToNamespaceUriMap().get( suggestedPrefix );
    if ( ! nsuri.equals( existingNSURI ) ) {
      // use an existing prefix defined as this nsuri if possible
      TreeSet<String> availablePrefixes = redefineIfAlreadyDeclared ? null : context.getNamespaceUriToPrefixMap().get( nsuri ); // use existing prefix if possible
      boolean mustCreateNewPrefix = true;
      if ( availablePrefixes != null && ! availablePrefixes.isEmpty() ) {
        for ( String prefix : availablePrefixes ) {
          if ( isAttribute && prefix.equals( XMLConstants.DEFAULT_NS_PREFIX ) && ! nsuri.equals( XMLConstants.NULL_NS_URI ) ) {
            continue; // we can't use the default ns prefix as an attribute prefix unless that attribute exists in the null namespace
          }
          newPrefix = prefix;
          mustCreateNewPrefix = false;
          break;
        }
      }
      if ( mustCreateNewPrefix ) {
        // if we got here, then we must create a new prefix... first use suggested prefix if not already defined at this level
        newPrefix = suggestedPrefix;
        if ( context.getPrefixToNamespaceUriMap().containsKeyDirect( newPrefix ) || ( isAttribute && newPrefix.equals( XMLConstants.DEFAULT_NS_PREFIX ) && ! nsuri.equals( XMLConstants.NULL_NS_URI ) ) ) {
          // there's no point in declaring a minted prefix if the namespace is already declared directly on this element using some other prefix
          boolean mustReallyCreateNewPrefix = true;
          TreeSet<String> existingPrefixes = context.getNamespaceUriToPrefixMap().getDirect( nsuri );
          if ( existingPrefixes != null && ! existingPrefixes.isEmpty() ) {
            for ( String prefix : existingPrefixes ) {
              if ( isAttribute && prefix.equals( XMLConstants.DEFAULT_NS_PREFIX ) && ! nsuri.equals( XMLConstants.NULL_NS_URI ) ) {
                continue; // we can't use the default ns prefix as an attribute prefix unless that attribute exists in the null namespace
              }
              newPrefix = prefix;
              mustReallyCreateNewPrefix = false;
              break;
            }
          }
          if ( mustReallyCreateNewPrefix ) {
            newPrefix = makeNewNamespacePrefix( context, newPrefix );
            declareNamespaceAtThisLevel = true;
          }
        }
        else {
          declareNamespaceAtThisLevel = true;
        }
      }
    }

    if ( declareNamespaceAtThisLevel ) {
      String old = namespacesToDeclare.put( newPrefix, nsuri );
      if ( old != null ) {
        throw new IllegalStateException( "Expected old namespace to be null, but was " + old );
      }
    }

    // redefine at this level even if already defined, to ensure we don't try to redefine prefix again at this level
    String oldNsUri = context.getPrefixToNamespaceUriMap().get( newPrefix );
    if ( oldNsUri != null ) {
      // the old nsuri to prefix mapping is no longer valid
      context.getNamespaceUriToPrefixMap().put( oldNsUri, null );
    }
    TreeSet<String> prefixes = context.getNamespaceUriToPrefixMap().get( nsuri );
    if ( prefixes == null ) {
      prefixes = new TreeSet<String>();
      context.getNamespaceUriToPrefixMap().put( nsuri, prefixes );
    }
    else {
      prefixes = new TreeSet<String>( prefixes ); // make copy at this level so that we don't affect the exiting set
    }
    prefixes.add( newPrefix );
    context.getPrefixToNamespaceUriMap().put( newPrefix, nsuri );
    return newPrefix;
  }

  private String makeNewNamespacePrefix(XmlSerializationContext context, String newPrefix) {
    // last resort, invent a new prefix and use it
    String prefixBase;
    int suffix;
    if ( newPrefix.length() == 0 ) {
      prefixBase = "ns";
      suffix = 0;
    }
    else {
      prefixBase = newPrefix;
      suffix = 2;
    }
    do {
      // prefix already in use
      newPrefix = prefixBase + suffix++;
    } while ( context.getPrefixToNamespaceUriMap().containsKey( newPrefix ) );
    return newPrefix;
  }

  public XmlElement parse( final IType type, InputSource source, final String description, List<XmlSchemaIndex> schemaIndexes, XmlTypeResolver typeResolver, XmlParseOptions options, final XmlParserCallback callback, final URL schemaEF ) {
    if ( options == null ) {
      options = DEFAULT_PARSE_OPTIONS;
    }
    final XmlParseOptions foptions = options;
    SAXParserImpl parser;
    try {
      SAXParserFactoryImpl factory = new SAXParserFactoryImpl();
      XmlSchemaIndex index = null;
      if ( type != null ) {
        IXmlSchemaElementTypeData<?> typeData = (IXmlSchemaElementTypeData<?>) type;
        if ( typeData.isAnonymous() ) {
          throw new XmlException( "parse() cannot be called on an anonymous element type" );
        }
        index = XmlSchemaIndex.getSchemaIndexByType( type );
      }
      schemaIndexes = addSchemaIndexesFromList( index, schemaIndexes );
      //noinspection deprecation
      if ( options.getValidate() ) {
        if ( typeResolver != null && typeResolver.getSchemaForValidation() != null ) {
          factory.setSchema( typeResolver.getSchemaForValidation() );
        }
        else if ( schemaIndexes.size() > 0 ) {
          factory.setSchema( XmlSchemaIndex.parseSchemas( schemaIndexes ) );
        }
      }
      factory.setNamespaceAware( true );
      factory.setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd", false );
      // validation in the xerces sense refers to DTD validation, and has nothing to do with schemas
      factory.setFeature( "http://xml.org/sax/features/validation", false );
      parser = (SAXParserImpl) factory.newSAXParser();
    }
    catch ( Throwable t ) {
      throw new XmlException( "Unable to create parser for " + description + ( type == null ? "" : " using schema root " + type.getName() ), t );
    }
    final Locator[] documentLocator = { null };
    try {
      final XmlTypeResolver[] ftypeResolver = { typeResolver == null ? DEFAULT_TYPE_RESOLVER : typeResolver };
      final List<XmlSchemaIndex> fschemaIndexes = schemaIndexes;
      final XmlElement[] rootElement = { null };
      final IType xmlElementType = JavaTypes.getSystemType(XmlElement.class);
      final XmlSchemaCollection collection = new XmlSchemaCollection();
      for ( XmlSchemaIndex schemaIndex : fschemaIndexes ) {
        addSchemaToCollection( collection, schemaIndex );
      }
      //parser.setProperty( "http://apache.org/xml/properties/internal/entity-resolver", resolver );
      parser.parse( source, new DefaultHandler2() {

        private LinkedList<QName> _elementStack = new LinkedList<QName>();
        private XmlDeserializationContext _context = new XmlDeserializationContext( new XmlDeserializationContext( null ) );
        private LinkedHashMap<String, String> _locallyDeclaredNamespaces = new LinkedHashMap<String, String>();
        private Map<String,URI> _uriCache = new HashMap<String, URI>();

        {
          _context.setMatchHandler( new XmlSchemaAnyMatchHandler( null ) ); // for root element
        }

        @Override
        public void setDocumentLocator( Locator locator ) {
          documentLocator[0] = locator;
        }

        @Override
        public void endDocument() throws SAXException {
          if ( ! _elementStack.isEmpty() ) {
            throw new IllegalStateException( "Expected element stack to be empty" );
          }
          // do an assignable check, since this might be a member of this element's substitution group
          // schema does not make a distinction, since any top level element is a valid root, but we can
          IType expectedRootElementType = type;
          if ( expectedRootElementType == null ) {
            expectedRootElementType = xmlElementType;
          }
          IType actualType = rootElement[0].getIntrinsicType();
          if ( ! expectedRootElementType.isAssignableFrom( actualType ) ) {
            throw new XmlException( "Invalid root element. Expected " + expectedRootElementType + ", but was " + actualType + ", QName " + rootElement[0].getQName() );
          }
          linkIdrefs( _context );
        }

        @Override
        public void endElement( String uri, String localName, String qName ) throws SAXException {
          _elementStack.removeLast();
          XmlElement parent = _context.getCurrentElement();
          XmlTypeInstance typeInstance = parent.getTypeInstance();
          // the only time we don't add a simple value for an element with no child elements is when
          // this element is based on a schema, and its type is defined as a complex type
          XmlSchemaTypeSchemaInfo schemaInfo = _context.getSchemaInfo();
          if ( ( schemaInfo == null || schemaInfo.hasSimpleContent() ) && typeInstance._children.isEmpty() ) {
            // simple type
            XmlSimpleValue simpleValue;
            if ( schemaInfo != null ) {
              String text = schemaInfo.getValidator().collapseWhitespace( _context.getAllText().toString(), new XmlSimpleValueValidationContext() );
              // if text length is zero, and this element has a default value, the default value is used
              if ( ! parent.isNil() ) {
                simpleValue = schemaInfo.getSimpleValueFactory().deserialize( _context, text, false );
                typeInstance.setSimpleValue( simpleValue );
              }
            }
            else {
              simpleValue = XmlSimpleValue.makeStringInstance( _context.getAllText().toString() );
              typeInstance.setSimpleValue( simpleValue );
            }
          }
          _context = _context.getParent();
        }

        @Override
        public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException {
          try {
            XmlElement element;
            String[] parts = XmlUtil.splitQName( qName );
            QName elementName = new QName( uri, parts[1], parts[0] );
            _elementStack.add( elementName );
            Pair<IType, IType> types = ftypeResolver[0].resolveTypes( _elementStack );
            XmlMatchHandler matchHandler = _context.getMatchHandler();
            IType actualType;
            if ( matchHandler == null ) {
              actualType = null;
            }
            else {
              try {
                matchHandler.match( elementName, collection );
                //noinspection deprecation
                if ( foptions.getValidate() ) {
                  // if we managed to validate the xml against the schema, and we still can't match
                  // this element to the schema, we did something wrong internally
                  throw new IllegalStateException( "Unable to determine IType for element " + elementName );
                }
                else {
                  actualType = null;
                }
              }
              catch ( MatchFoundException ex ) {
                actualType = ex.getType();
              }
            }
            if ( actualType == null ) {
              actualType = types.getFirst();
              if ( actualType == null ) {
                actualType = xmlElementType;
              }
            }
            if ( actualType.equals( xmlElementType ) ) {
              element = new XmlElement( elementName );
            }
            else {

              ITypeInfo typeInfo = actualType.getTypeInfo();
              if( typeInfo == null )
              {
                throw new IllegalStateException( "No type information found for " + actualType.getName() );
              }
              IConstructorInfo ci = typeInfo.getConstructor();
              if( ci == null )
              {
                throw new IllegalStateException( actualType.getName() + " has no default constructor" );
              }
              element = (XmlElement) ci.getConstructor().newInstance();
            }
            if ( rootElement[0] == null ) {
              rootElement[0] = element;
            }
            else {
              _context.getCurrentElement().addChild( element );
            }
            _context = new XmlDeserializationContext( _context );
            _context.setCurrentElement( element );
            for ( Map.Entry<String, String> entry : _locallyDeclaredNamespaces.entrySet() ) {
              _context.addNamespace( entry.getKey(), entry.getValue() );
            }
            XmlElementInternals.instance().addNamespacesToElementFromParse( element, _context.getNamespaces(), _uriCache );
            resolveXsiTypeAndNil( element, fschemaIndexes, types.getSecond(), attributes, _context );
            XmlTypeInstance typeInstance = element.getTypeInstance();
            XmlSchemaTypeSchemaInfo schemaInfo = XmlTypeInstanceInternalsImpl.instance().getSchemaInfo( typeInstance );
            _context.setSchemaInfo( schemaInfo );
            if ( schemaInfo != null ) {
              _context.setMixed( schemaInfo.isMixed() );
              _context.setMatchHandler( XmlMatchHandler.getMatchHandler( _context.getSchemaInfo().getXsdType() ) );
            }
            for ( int i = 0; i < attributes.getLength(); i++ ) {
              if ( ! (Boolean) _isSpecifiedMethod.invoke( attributes, i ) ) {
                continue;
              }
              String attrQName = attributes.getQName( i );
              parts = XmlUtil.splitQName( attrQName );
              QName attributeName = new QName( attributes.getURI( i ), parts[1], parts[0] );
              if ( typeInstance._type != null && attributeName.equals( XSI_TYPE_ATTRIBUTE_QNAME ) ) {
                continue; // we handle xsi:type, not the user
              }
              else if ( attributeName.equals( XSI_NIL_ATTRIBUTE_QNAME ) ) {
                continue; // we handle xsi:nil, not the user
              }
              String attributeValue = attributes.getValue( i );
              boolean wroteValue = false;
              if ( schemaInfo != null ) {
                XmlSchemaPropertySpec property = schemaInfo.getPropertyByAttributeName( attributeName );
                if ( property != null ) {
                  attributeValue = property.getValidator().collapseWhitespace( attributeValue, new XmlSimpleValueValidationContext() );
                  XmlSimpleValue storageValue = property.getSimpleValueFactory().deserialize( _context, attributeValue, false );
                  typeInstance.setAttributeSimpleValue( attributeName, storageValue );
                  wroteValue = true;
                }
                else if ( schemaInfo.getXsdType().getAnyAttributeRecursiveIncludingSupertypes() != null ) {
                  XmlSchemaAnyAttribute anyAttribute = schemaInfo.getXsdType().getAnyAttributeRecursiveIncludingSupertypes();
                  if ( anyAttribute.getProcessContents() != XmlSchemaAnyAttribute.ProcessContents.skip ) {
                    XmlSchemaAttribute attribute = null;
                    for ( XmlSchemaIndex schemaIndex : fschemaIndexes ) {
                      try {
                        attribute = schemaIndex.getXmlSchemaAttributeByQName( attributeName );
                        break;
                      }
                      catch ( NotFoundException ex ) {
                        // continue
                      }
                    }
                    if ( attribute != null ) {
                      XmlSchemaType schemaType = XmlSchemaIndex.getSchemaTypeForAttribute( attribute );
                      XmlSimpleValueValidator simpleValueValidator = XmlSchemaIndex.getSimpleValueValidatorForSchemaType( schemaType );
                      XmlSimpleValueFactory simpleValueFactory = XmlSchemaIndex.getSimpleValueFactoryForSchemaType( schemaType );
                      attributeValue = simpleValueValidator.collapseWhitespace( attributeValue, new XmlSimpleValueValidationContext() );
                      XmlSimpleValue storageValue = simpleValueFactory.deserialize( _context, attributeValue, false );
                      typeInstance.setAttributeSimpleValue( attributeName, storageValue );
                      wroteValue = true;
                    }
                    else if ( anyAttribute.getProcessContents() == XmlSchemaAnyAttribute.ProcessContents.strict ) {
                      throw new XmlException( "The attribute " + attributeName + " matching xs:anyAttribute could not be resolved to an attribute definition." );
                    }
                  }
                }
              }
              if ( ! wroteValue ) {
                // fall back to non-typesafe write
                typeInstance.setAttributeValue( attributeName, attributeValue );
              }
            }
            if( callback != null ) {
              callback.onStartElement( documentLocator[0], element, schemaEF );
            }
          }
          catch ( Throwable t ) {
            throw new XmlException( "Unable to parse XML from " + description + ( type == null ? "" : " using schema root " + type.getName() ), t );
          }
        }

        @Override
        public void characters( char[] ch, int start, int length ) throws SAXException {
          if ( _context.isMixed() ) {
            // Unfortunately, Xerces doesn't do what we're doing here, it can send us a big block of text in multiple chunks.
            // This isn't the most efficient way to do this, but hopefully it doesn't happen that often.
            String text = String.valueOf( ch, start, length );
            XmlMixedContentList children = _context.getCurrentElement().getTypeInstance()._children;
            if ( ! children.isEmpty() ) {
              IXmlMixedContent previousMixedContent = children.get( children.size() - 1 );
              if ( previousMixedContent instanceof XmlMixedContentText ) {
                text = ( (XmlMixedContentText) previousMixedContent ).getText() + text;
                children.set( children.size() - 1, new XmlMixedContentText( text ) ); // replace existing mixed content text with new version that has additional text appended to it
                return;
              }
            }
            children.add( new XmlMixedContentText( text ) );
          }
          else {
            _context.getAllText().append( ch, start, length );
          }
        }

        @Override
        public void startPrefixMapping( String prefix, String uri ) throws SAXException {
          _locallyDeclaredNamespaces.put( prefix, uri );
        }

        @Override
        public void endPrefixMapping( String prefix ) throws SAXException {
          _locallyDeclaredNamespaces.remove( prefix );
        }

        @Override
        public void error( SAXParseException e ) throws SAXException {
          throw e;
        }

        @Override
        public void fatalError( SAXParseException e ) throws SAXException {
          throw e;
        }

        @Override
        public void warning( SAXParseException e ) throws SAXException {
          /* ignore */
        }

      } );
      return rootElement[0];
    }
    catch ( Exception ex ) {
      StringBuilder sb = new StringBuilder();
      sb.append( "Unable to parse XML from " );
      sb.append( description );
      boolean firstUsing = true;
      if ( typeResolver != null && typeResolver.getValidationSchemasDescription() != null ) {
        if ( firstUsing ) {
          sb.append( " using " );
        }
        else {
          sb.append( " and " );
        }
        firstUsing = false;
        sb.append( typeResolver.getValidationSchemasDescription() );
      }
      else if ( ! schemaIndexes.isEmpty() ) {
        TreeSet<String> sortedSet = new TreeSet<String>();
        for ( XmlSchemaIndex schemaIndex : schemaIndexes ) {
          sortedSet.add( schemaIndex.getPackageName() );
        }
        if ( firstUsing ) {
          sb.append( " using" );
        }
        else {
          sb.append( " and" );
        }
        firstUsing = false;
        sb.append( " schemas [" );
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
      if ( type != null ) {
        if ( firstUsing ) {
          sb.append( " using" );
        }
        else {
          sb.append( " and" );
        }
        //noinspection UnusedAssignment
        firstUsing = false;
        sb.append( " schema root " );
        sb.append( type.getName() );
      }
      if ( documentLocator[0] != null ) {
        sb.append( " at line " );
        sb.append( documentLocator[ 0 ].getLineNumber() );
        sb.append( " column " );
        sb.append( documentLocator[ 0 ].getColumnNumber() );
      }
      throw new XmlException( sb.toString(), ex );
    }
  }

  private void addSchemaToCollection( XmlSchemaCollection collection, XmlSchemaIndex<?> schemaIndex ) {
    addSchemaToCollection( collection, schemaIndex, new HashSet<String>() );
  }

  private void addSchemaToCollection( XmlSchemaCollection collection, XmlSchemaIndex<?> schemaIndex, Set<String> addedGosuNamespaces ) {
    if ( addedGosuNamespaces.add( schemaIndex.getPackageName() ) ) {
      collection.addSchemas( schemaIndex.getXmlSchemaCollection().getXmlSchemas() );
      for ( Set<String> gosuNamespaces : schemaIndex.getGosuNamespacesByXmlNamespace().values() ) {
        for ( String gosuNamespace : gosuNamespaces ) {
          addSchemaToCollection( collection, XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace(schemaIndex.getTypeLoader().getModule(), gosuNamespace), addedGosuNamespaces );
        }
      }
    }
  }

  @Override
  public void addNamespacesToElementFromParse( XmlElement element, Map<String, String> allNamespaces, Map<String, URI> _uriCache ) {
    for ( Map.Entry<String, String> entry : allNamespaces.entrySet() ) {
      String prefix = entry.getKey();
      String nsUri = entry.getValue();
      if ( prefix.equals( "xmlns" ) ) {
        // Xerces gives us this prefix, but we shouldn't declare it explicitly
        continue;
      }
      if ( prefix.equals( "xml" ) ) {
        // Xerces gives us this prefix, but we shouldn't declare it explicitly
        continue;
      }
      element.declareNamespace( new XmlNamespace( nsUri, prefix ) );
    }

  }

  public void resolveXsiTypeAndNil( XmlElement element, List<XmlSchemaIndex> schemaIndexes, IType typeInstanceType, Attributes attributes, XmlDeserializationContext context ) {
    String xsiType = attributes.getValue( XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "type" );
    IType gosuType;
    if ( xsiType != null ) {
      XmlSchemaType xsdType = null;
      String[] parts = XmlUtil.splitQName( xsiType );
      String nsURI = context.getNamespaces().get( parts[0] );
      QName qname = new QName( nsURI, parts[1] );
      for ( XmlSchemaIndex schemaIndex : schemaIndexes ) {
        try {
          xsdType = schemaIndex.getXmlSchemaTypeByQName( qname );
          break;
        }
        catch ( NotFoundException ex ) {
          // not a match
        }
      }
      if ( xsdType == null ) {
//        throw new XmlException( "Unable to resolve xsi:type " + qname );
        return; // same as finding an element that matches an xsd:any with processContents=lax which is not found in the schema?
      }
      gosuType = XmlSchemaIndex.getGosuTypeBySchemaObject( xsdType );
    }
    else {
      gosuType = typeInstanceType;
    }
    if ( gosuType != null ) {
      XmlTypeInstance typeInstance = (XmlTypeInstance) gosuType.getTypeInfo().getConstructor().getConstructor().newInstance();
      element.setTypeInstance( typeInstance );
    }
    String nil = attributes.getValue( XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil" );
    if ( nil != null ) {
      nil = nil.trim();
      if ( nil.equals( "true" ) || nil.equals( "1" ) ) {
        element.setNil( true );
      }
    }
  }

  private void linkIdrefs( XmlDeserializationContext context ) {
    final Map<String, XmlElement> ids = context.getIds();
    for ( Pair<String, IDREFSimpleValueFactory.Value> pair : context.getIdrefs() ) {
      final String id = pair.getFirst();
      XmlElement element = ids.get( id );
      if ( element == null ) {
        throw new XmlException( "Unable to find ID for IDREF " + id );
      }
      final IDREFSimpleValueFactory.Value idref = pair.getSecond();
      idref.setElement( element );
    }
  }

  public XmlElement parse( IType type, InputStream stream, String description, boolean validating, HashMap<String, BinaryData> attachments, XmlParseOptions options, String systemId ) {
    return parseInternal( type, stream, description, options );
  }

  public XmlElement parse( IType type, Reader stream, String description, boolean validating, HashMap<String, BinaryData> attachments, XmlParseOptions options ) {
    return parseInternal( type, stream, description, options );
  }

  public XmlElement parseInternal( IType type, Object stream, String description, XmlParseOptions options ) {
    final XmlSchemaIndex<?> schemaIndex = type == null ? null : XmlSchemaIndex.getSchemaIndexByType( type );
    List<XmlSchemaIndex> schemaIndexes = addSchemaIndexesFromParseOptions( schemaIndex, options );
    InputSource source;
    if ( stream instanceof InputStream ) {
      source = new InputSource( (InputStream) stream );
    }
    else {
      source = new InputSource( (Reader) stream );
    }
    return parse( type, source, description, schemaIndexes, null, options, null, null );
  }

  StreamSource validate( List<XmlSchemaIndex> schemaIndexes, IType type, InputStream stream, String description, boolean isXOP, String systemId ) {
    try {
      byte[] content = StreamUtil.getContent(stream);

      XMLInputFactory factory = XMLInputFactory.newInstance();
      StreamSource streamSource = new StreamSource( new ByteArrayInputStream( content ), systemId );
      if ( isXOP ) {
        XMLStreamReader parser = factory.createXMLStreamReader( streamSource );
        validate( schemaIndexes, type, new StAXSource( wrapInXOPFilter( factory, parser ) ), description );
      }
      else {
        validate( schemaIndexes, type, streamSource, description );
      }
      return new StreamSource( new ByteArrayInputStream( content ), systemId );
    }
    catch ( Exception ex ) {
      throw new XmlException( "Could not parse " + description, ex );
    }
  }

  StreamSource validate( List<XmlSchemaIndex> schemaIndexes, IType type, Reader stream, String description, boolean isXOP ) {
    try {
      String content = StreamUtil.getContent(stream);

      XMLInputFactory factory = XMLInputFactory.newInstance();
      XMLStreamReader parser = factory.createXMLStreamReader( new StringReader( content ) );
      if ( isXOP ) {
        validate( schemaIndexes, type, new StAXSource( wrapInXOPFilter( factory, parser ) ), description );
      }
      else {
        validate( schemaIndexes, type, new StreamSource( new StringReader( content ) ), description );
      }
      return new StreamSource( new StringReader( content ) );
    }
    catch ( Exception ex ) {
      throw new XmlException( "Could not parse " + description, ex );
    }
  }

  private XMLStreamReader wrapInXOPFilter( XMLInputFactory factory, XMLStreamReader parser ) throws XMLStreamException {
    parser = factory.createFilteredReader( parser, new StreamFilter() {
      private int _ignoreLevel = 0;
      @Override
      public boolean accept( XMLStreamReader reader ) {
        switch ( reader.getEventType() ) {
          case XMLStreamConstants.START_ELEMENT: {
            if ( _ignoreLevel > 0 || reader.getName().equals( XmlElement.XOP_INCLUDE_QNAME ) ) {
              _ignoreLevel++;
            }
            break;
          }
          case XMLStreamConstants.END_ELEMENT: {
            if ( _ignoreLevel == 1 ) {
              _ignoreLevel--;
              return false;
            }
            if ( --_ignoreLevel < 0 ) {
              _ignoreLevel = 0;
            }
            break;
          }
        }
        return _ignoreLevel == 0;
      }
    } );
    return parser;
  }

  private List<XmlSchemaIndex> addSchemaIndexesFromList( XmlSchemaIndex schemaIndex, List<XmlSchemaIndex> list ) {
    if ( list == null || list.isEmpty() ) {
      if ( schemaIndex == null ) {
        return Collections.emptyList();
      }
      else {
        return Collections.singletonList( schemaIndex );
      }
    }
    LinkedHashSet<XmlSchemaIndex> schemaIndexes = new LinkedHashSet<XmlSchemaIndex>();
    if ( schemaIndex != null ) {
      schemaIndexes.add( schemaIndex );
    }
    schemaIndexes.addAll( list );
    return new ArrayList<XmlSchemaIndex>( schemaIndexes );
  }

  private List<XmlSchemaIndex> addSchemaIndexesFromParseOptions( XmlSchemaIndex<?> schemaIndex, XmlParseOptions options ) {
    List<XmlSchemaIndex> schemaIndexes;
    if ( options != null && ! options.getAdditionalSchemas().isEmpty() ) {
      schemaIndexes = new ArrayList<XmlSchemaIndex>();
      if ( schemaIndex != null ) {
        schemaIndexes.add( schemaIndex );
      }
      for ( XmlSchemaAccess schemaAccess : options.getAdditionalSchemas() ) {
        schemaIndexes.add( ( (XmlSchemaAccessImpl) schemaAccess ).getSchemaIndex() );
      }
    }
    else if ( schemaIndex == null ) {
      schemaIndexes = Collections.emptyList();
    }
    else {
      schemaIndexes = Collections.<XmlSchemaIndex>singletonList( schemaIndex );
    }
    return schemaIndexes;
  }

  public void validate( List<XmlSchemaIndex> schemaIndexes, IType type, Source source, String description ) {
    try {
      Schema schema = XmlSchemaIndex.parseSchemas( schemaIndexes );
      Validator validator = schema.newValidator();
      validator.setResourceResolver( new XmlSchemaLocalResourceResolver() );
      validator.validate( source );
    }
    catch ( SAXParseException ex ) {
      throw new XmlException( "Failed to validate XML from " + description + " using schema root " + type.getName() + " at line " + ex.getLineNumber() +  " column " + ex.getColumnNumber(), ex );
    }
    catch ( Exception ex ) {
      throw new XmlException( "Failed to validate XML from " + description + " using schema root " + type.getName(), ex );
    }
  }

  public IType getType( XmlElement element ) {
    return element._type;
  }

  @Override
  public void validateQName( QName qname ) {
    XmlUtil.validateQName( qname );
  }

  public XmlElement parse( InputStream inputStream, URL schemaEF, XmlSchemaLocalResourceResolver resolver, XmlParserCallback callback ) {
    return parse( null, new InputSource( inputStream ), schemaEF == null ? null : schemaEF.toExternalForm(), null, null, null, callback, schemaEF );
  }

  @Override
  public boolean isAnyType( IType type ) {
    return type == null || type.getName().equals( "gw.xsd.w3c.xmlschema.types.complex.AnyType" );
  }

  public void checkSetTypeInstance( XmlElement element, XmlTypeInstance xmlTypeInstance ) {
    // supertype will perform null check, no need to repeat it here
    if ( xmlTypeInstance != null ) {
      if ( element._schemaDefinedTypeInstanceType != null && ! element._schemaDefinedTypeInstanceType.equals( XmlTypeInstanceInternals.instance().getType( xmlTypeInstance ) ) ) {
        IType typeInstanceType = XmlTypeInstanceInternals.instance().getType( xmlTypeInstance );
        final XmlSchemaIndex<?> schemaIndex = XmlSchemaIndex.getSchemaIndexByType( typeInstanceType );
        XmlSchemaTypeInstanceTypeData typeData = (XmlSchemaTypeInstanceTypeData) schemaIndex.getTypeData( typeInstanceType.getName() );
        if ( typeData.isAnonymous() ) {
          throw new XmlException( "Anonymous type instances can only be assigned to their schema-defined containing elements, or to elements of type xs:anyType." );
        }
        // static typing doesn't always help us here ( cast to XmlElement then assign ), so we have to perform a runtime assignable check
        if ( ! element._schemaDefinedTypeInstanceType.isAssignableFrom( typeInstanceType ) ) {
          throw new ClassCastException( xmlTypeInstance + " cannot be converted to " + element._schemaDefinedTypeInstanceType );
        }
      }
    }
  }

  private List<XmlSchemaIndex> getSchemaIndexes( XmlParseOptions options ) {
    List<XmlSchemaIndex> indexes;
    if ( options == null ) {
      indexes = null;
    }
    else {
      indexes = new ArrayList<XmlSchemaIndex>( options.getAdditionalSchemas().size() );
      for ( XmlSchemaAccess xmlSchemaAccess : options.getAdditionalSchemas() ) {
        indexes.add( ( (XmlSchemaAccessImpl) xmlSchemaAccess ).getSchemaIndex() );
      }

    }
    return indexes;
  }

  @Override
  public XmlElement parse( InputStream stream ) {
    return parse( stream, null );
  }

  @Override
  public XmlElement parse( InputStream stream, XmlParseOptions options ) {
    return parse( null, new InputSource( stream ), "input stream", getSchemaIndexes( options ), null, options, null, null );
  }

  @Override
  public XmlElement parse( InputStream stream, XmlParseOptions options, XmlTypeResolver typeResolver ) {
    return parse( null, new InputSource( stream ), "input stream", getSchemaIndexes( options ), typeResolver, options, null, null );
  }

  @Override
  public XmlElement parse( Reader reader, XmlParseOptions options, XmlTypeResolver typeResolver ) {
    return parse( null, new InputSource( reader ), "input stream", getSchemaIndexes( options ), typeResolver, options, null, null );
  }

  @Override
  public XmlElement parse( byte[] bytes ) {
    return parse( bytes, null );
  }

  @Override
  public XmlElement parse( byte[] bytes, XmlParseOptions options ) {
    return parse( null, new InputSource( new ByteArrayInputStream( bytes ) ), "byte array", getSchemaIndexes( options ), null, options, null, null );
  }

  @Override
  public XmlElement parse( File file ) {
    return parse( file, null );
  }

  @Override
  public XmlElement parse( File file, XmlParseOptions options ) {
    FileInputStream inputStream;
    try {
      inputStream = new FileInputStream(file);
      return parse( null, new InputSource( inputStream ), file.getCanonicalPath(), getSchemaIndexes( options ), null, options, null, null );
    }
    catch ( IOException ex ) {
      throw new XmlException( "Unable to parse file " + file, ex );
    }
  }

  @Override
  public XmlElement parse( URL url ) {
    return parse( url, null );
  }

  @Override
  public XmlElement parse( URL url, XmlParseOptions options ) {
    InputStream inputStream;
    String urlEF = url.toExternalForm();
    try {
      //      try {
//        return CommonServices.getFileSystem().getIFile(new File(url.toURI()));
//      } catch (URISyntaxException e) {
//        throw new RuntimeException(e);
//      }
      inputStream = CommonServices.getFileSystem().getIFile(url).openInputStream();
      return parse( null, new InputSource( inputStream ), urlEF, getSchemaIndexes( options ), null, options, null, null );
    }
    catch ( IOException ex ) {
      throw new XmlException( "Unable to parse file " + urlEF, ex );
    }
  }

  @Override
  public XmlElement parse( String string ) {
    return parse( string, null );
  }

  @Override
  public XmlElement parse( String string, XmlParseOptions options ) {
    if ( string.length() > 0 && string.indexOf( '<' ) < 0 ) {
      throw new XmlException( "Please use XmlElement.parse( java.io.File ) to parse a file" );
    }
    return parse( null, new InputSource( new StringReader( string ) ), "string", getSchemaIndexes( options ), null, options, null, null );
  }

  @Override
  public XmlElement parse( Reader reader ) {
    return parse( reader, null );
  }

  @Override
  public XmlElement parse( Reader reader, XmlParseOptions options ) {
    try {
      return parse( null, new InputSource( reader ), "input reader", getSchemaIndexes( options ), null, options, null, null );
    }
    finally {
      try {
        reader.close();
      }
      catch (IOException e) {
        // ignore
      }
    }
  }

  public XmlElement create( QName qName, IType type, IType xmlTypeInstanceType, XmlTypeInstance xmlTypeInstance ) {
    return new XmlElement( qName, type, xmlTypeInstanceType, xmlTypeInstance );
  }

}
