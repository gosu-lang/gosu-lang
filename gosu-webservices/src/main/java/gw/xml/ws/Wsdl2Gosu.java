/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

import gw.config.CommonServices;
import gw.internal.schema.gw.xsd.w3c.soap11.Binding;
import gw.internal.schema.gw.xsd.w3c.soap11.Body;
import gw.internal.schema.gw.xsd.w3c.soap11.enums.TStyleChoice;
import gw.internal.schema.gw.xsd.w3c.wsdl.Definitions;
import gw.internal.schema.gw.xsd.w3c.wsdl.anonymous.elements.*;
import gw.internal.schema.gw.xsd.w3c.wsdl.types.complex.TParam;
import gw.internal.schema.gw.xsd.w3c.xmlschema.*;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ExplicitGroup_Element;
import gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.TopLevelElement_ComplexType;
import gw.internal.schema.gw.xsd.w3c.xmlschema.enums.FormChoice;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;
import gw.lang.PublishInGosu;
import gw.lang.PublishedType;
import gw.lang.PublishedTypes;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;
import gw.xml.*;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * This class can be used to recursively fetch a WSDL and its dependencies from a remote URL. In Gosu, there is
 * no need to generate code to use a WSDL. Simply drop the WSDL and it's associated resources into any Gosu
 * classpath.
 */
@PublishInGosu
@PublishedTypes({
    @PublishedType(fromType = "java.util.LinkedHashMap<java.lang.String, gw.internal.schema.gw.xsd.w3c.wsdl.Definitions>", toType = "java.util.LinkedHashMap<java.lang.String, gw.xsd.w3c.wsdl.Definitions>"),
    @PublishedType(fromType = "java.util.LinkedHashMap<java.lang.String, gw.internal.schema.gw.xsd.w3c.xmlschema.Schema>", toType = "java.util.LinkedHashMap<java.lang.String, gw.xsd.w3c.xmlschema.Schema>")
})
public final class Wsdl2Gosu {

  /**
   * Recursively fetches the WSDLs and associated resources at the specified location. In Gosu, there is no
   * codegen phase, simply drop the fetched files into your Gosu classpath.
   * @param uri the uri of the WSDL or schema
   * @param outputDir the output directory, which will be created if necessary, and existing XSD and WSDL files will be overwritten here
   */
  public static void fetch( URI uri, File outputDir ) throws IOException, URISyntaxException {
    fetch( Collections.singletonList( uri ), outputDir );
  }

  /**
   * Recursively fetches the WSDLs and associated resources at the specified locations. In Gosu, there is no
   * codegen phase, simply drop the fetched files into your Gosu classpath.
   * @param urls the urls of the WSDLs and/or schemas
   * @param outputDir the output directory, which will be created if necessary, and existing XSD and WSDL files will be overwritten here
   */
  public static void fetch( List<URI> urls, File outputDir ) throws IOException, URISyntaxException {
    LinkedHashMap<String, Definitions> definitions = new LinkedHashMap<String, Definitions>();
    LinkedHashMap<String, Schema> schemas = new LinkedHashMap<String, Schema>();
    for ( URI uri : urls ) {
      XmlElement root = parseDocumentAtUri( uri );
      if ( root.getQName().equals( Schema.$QNAME ) ) {
        fetch( uri, schemas );
      }
      else if ( root.getQName().equals( Definitions.$QNAME ) ) {
        fetch( uri, definitions, schemas );
      }
      else {
        throw new XmlException( "Unrecognized root element of XML at " + uri + "; Expected " + Definitions.$QNAME + " or " + Schema.$QNAME + ", but was " + root.getQName() );
      }
    }
    writeResults( outputDir, definitions, schemas );
    CommonServices.getFileSystem().clearAllCaches();
    XmlSchemaResourceTypeLoaderBase.refreshSchemasFromAllTypeLoaders();
    System.out.println( "Wsdl2Gosu - Done writing results" );
  }

  private static <T extends XmlElement> T parseDocumentAtUri( URI uri, Class<T> clazz, XmlParseOptions options ) {
    try {
      if ( "local".equals( uri.getScheme() ) ) {
        ITypeInfo clientConnectorTypeInfo = TypeSystem.getByFullName("gw.internal.xml.ws.LocalWebservicesClientConnector").getTypeInfo();
        IConstructorInfo ctor = clientConnectorTypeInfo.getCallableConstructor(TypeSystem.get(URI.class));
        Object conn = ctor.getConstructor().newInstance(uri);

        ITypeInfo responseTypeInfo = TypeSystem.getByFullName("gw.internal.xml.ws.WebservicesResponse").getTypeInfo();
        Object response = clientConnectorTypeInfo.getProperty("Response").getAccessor().getValue(conn);
        InputStream is = (InputStream) responseTypeInfo.getProperty("InputStream").getAccessor().getValue(response);
        //noinspection unchecked
        return (T) clazz.getMethod( "parse", InputStream.class, XmlParseOptions.class ).invoke( null, is, options );
      }
      else {
        //noinspection unchecked
        return (T) clazz.getMethod( "parse", URL.class, XmlParseOptions.class ).invoke( null, uri.toURL(), options );
      }
    }
    catch ( InvocationTargetException itex ) {
      throw GosuExceptionUtil.forceThrow( itex.getTargetException() );
    }
    catch ( Exception ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
  }

  private static <T extends XmlElement> T parseDocumentAtUri( URI uri, Class<T> clazz ) {
    return parseDocumentAtUri( uri, clazz, null );
  }

  private static XmlElement parseDocumentAtUri( URI uri ) {
    return parseDocumentAtUri( uri, XmlElement.class );
  }

  /**
   * Recursively fetches the WSDLs and associated schemas at the specified location. In Gosu, there is no
   * codegen phase, simply drop the fetched files into your Gosu classpath.
   * @param uri the uri of the WSDL
   * @param definitions a map into which to place the fetched WSDL definitions
   * @param schemas a map into which to place the fetched schemas
   */
  public static void fetch(
          URI uri,
          LinkedHashMap<String, Definitions> definitions,
          LinkedHashMap<String, Schema> schemas ) throws IOException, URISyntaxException {
    fetch( uri, new HashMap<URI,Map<Boolean,String>>(), new HashSet<String>(), FileType.WSDL, definitions, schemas, false );
    convertRpcToDocument( definitions, schemas );
    System.out.println( "Wsdl2Gosu - Done fetching schemas" );
  }

  /**
   * Recursively fetches the schemas at the specified location. In Gosu, there is no
   * codegen phase, simply drop the fetched files into your Gosu classpath.
   * @param uri the uri of the WSDL
   * @param schemas a map into which to place the fetched schemas
   */
  public static void fetch(
          URI uri,
          LinkedHashMap<String, Schema> schemas ) throws IOException, URISyntaxException {
    fetch( uri, new HashMap<URI,Map<Boolean,String>>(), new HashSet<String>(), FileType.SCHEMA, new LinkedHashMap<String, Definitions>(), schemas, false );
    System.out.println( "Wsdl2Gosu - Done fetching schemas" );
  }

  private static void writeResults( File outputDir, LinkedHashMap<String, Definitions> definitions, LinkedHashMap<String, Schema> schemas ) throws FileNotFoundException {
    System.out.println( "Wsdl2Gosu - Writing results to " + outputDir );
    //noinspection ResultOfMethodCallIgnored
    outputDir.mkdirs();
    for ( Map.Entry<String, Definitions> entry : definitions.entrySet() ) {
      FileOutputStream fos = new FileOutputStream( new File( outputDir, entry.getKey() ) );
      try {
        entry.getValue().writeTo( fos );
      }
      finally {
        try {
          fos.close();
        }
        catch ( IOException e ) {
          //noinspection ThrowFromFinallyBlock
          throw GosuExceptionUtil.forceThrow( e );
        }
      }
    }
    for ( Map.Entry<String, Schema> entry : schemas.entrySet() ) {
      FileOutputStream fos = new FileOutputStream( new File( outputDir, entry.getKey() ) );
      try {
        XmlSerializationOptions options = new XmlSerializationOptions();
        options.setSort( false );
        options.setValidate( false );
        entry.getValue().writeTo( fos, options );
      }
      finally {
        try {
          fos.close();
        }
        catch ( IOException e ) {
          //noinspection ThrowFromFinallyBlock
          throw GosuExceptionUtil.forceThrow( e );
        }
      }
    }
  }

  private static void convertRpcToDocument( LinkedHashMap<String, Definitions> definitions, LinkedHashMap<String, Schema> schemas ) throws URISyntaxException {
    System.out.println( "Wsdl2Gosu - Converting RPC style to Document style if necessary" );
    List<Runnable> todo = new ArrayList<Runnable>();
    for ( Definitions def : definitions.values() ) {
      for ( TDefinitions_Binding binding : def.Binding() ) {
        Binding soapBinding = (Binding) binding.getChild( Binding.$QNAME );
        if ( soapBinding != null && soapBinding.Style() == TStyleChoice.Rpc ) {
          Definitions portTypeWsdl = findWsdlForNamespace( binding.Type(), def, definitions );
          TDefinitions_PortType portType = findPortType( binding.Type(), portTypeWsdl );
          soapBinding.setComment( "Converted from rpc style to document style by Wsdl2Gosu" );
          soapBinding.setStyle$( TStyleChoice.Document );
          for ( final TBinding_Operation bindingOperation : binding.Operation() ) {
            Body soapBody = (Body) bindingOperation.Input().getChild( Body.$QNAME );
            final URI bodyNamespace = soapBody.Namespace();
            Schema schema = findSchemaForNamespace( bodyNamespace.toString(), def, definitions, schemas );
            if ( schema == null ) {
              schema = new Schema();
              schema.setTargetNamespace$( bodyNamespace );
              if ( def.Types().isEmpty() ) {
                def.Types().add( new TDefinitions_Types() );
              }
              def.Types().get( 0 ).addChild( schema );
            }
            // now we have a schema for this target namespace that we can modify
            TPortType_Operation operation = findOperation( bindingOperation.Name(), portType );
            if ( operation.Input() != null ) {
              processParam( definitions, todo, portTypeWsdl, bodyNamespace, schema, operation.Input().getTypeInstance(), bindingOperation.Name() );
            }
            if ( operation.Output() != null ) {
              processParam( definitions, todo, portTypeWsdl, bodyNamespace, schema, operation.Output().getTypeInstance(), bindingOperation.Name() + "Response" );
            }
          }
        }
      }
    }
    for ( Runnable runnable : todo ) {
      runnable.run();
    }
  }

  private static void processParam( LinkedHashMap<String, Definitions> definitions, List<Runnable> todo, Definitions portTypeWsdl, final URI bodyNamespace, Schema schema, TParam tparam, final String elementName ) {
    Element element = null;
    if ( findElement( schema, elementName ) == null ) {
      element = new Element();
      element.setComment( "Created by Wsdl2Gosu" );
      element.setName$( elementName );
      element.setComplexType$( new TopLevelElement_ComplexType() );
      element.ComplexType().setSequence$( new Sequence() );
      schema.Element().add( element );
    }
    QName messageName = tparam.Message();
    Definitions messageWsdl = findWsdlForNamespace( messageName, portTypeWsdl, definitions );
    final TDefinitions_Message message = findMessage( messageName, messageWsdl );
    if ( element != null ) {
      // convert multiple parts to a single part
      for ( TMessage_Part part : message.Part() ) {
        ExplicitGroup_Element paramElement = new ExplicitGroup_Element();
        paramElement.setName$( part.Name() );
        paramElement.setType$( part.Type() );
        if ( schema.ElementFormDefault() == FormChoice.Qualified ) {
          paramElement.setForm$( FormChoice.Unqualified );
        }
        element.ComplexType().Sequence().Element().add( paramElement );
      }
    }
    todo.add( new Runnable() {
      public void run() {
        TMessage_Part part = new TMessage_Part();
        part.setName$( "input" );
        part.setElement$( new QName( bodyNamespace.toString(), elementName ) );
        message.setPart$( Collections.singletonList( part ) );
      }
    } );
  }

  private static TPortType_Operation findOperation( String name, TDefinitions_PortType portType ) {
    for ( TPortType_Operation operation : portType.Operation() ) {
      if ( operation.Name().equals( name ) ) {
        return operation;
      }
    }
    throw new RuntimeException( "Unable to find portType operation named " + name );
  }

  private static Element findElement( Schema schema, String name ) {
    for ( Element element : schema.Element() ) {
      if ( element.Name().equals( name ) ) {
        return element;
      }
    }
    return null;
  }

  private static Schema findSchemaForNamespace( String namespace, Definitions def, LinkedHashMap<String, Definitions> definitions, LinkedHashMap<String, Schema> schemas ) {
    Schema targetSchema = findSchemaForNamespaceShallow( namespace, def, schemas );
    if ( targetSchema != null ) {
      return targetSchema;
    }
    for ( TDefinitions_Import imprt : def.Import() ) {
      Definitions targetWsdl = definitions.get( imprt.Location().toString() );
      if ( targetWsdl == null ) {
        throw new RuntimeException( "Unable to find referenced WSDL, namespace " + imprt.Namespace() + " at location " + imprt.Location() );
      }
      targetSchema = findSchemaForNamespaceShallow( namespace, targetWsdl, schemas );
      if ( targetSchema != null ) {
        return targetSchema;
      }
    }
    return null;
  }

  private static Schema findSchemaForNamespaceShallow( String namespace, Definitions def, LinkedHashMap<String, Schema> schemas ) {
    for ( TDefinitions_Types types : def.Types() ) {
      for ( XmlElement element : types.getChildren( Schema.$QNAME ) ) {
        Schema schema = (Schema) element;
        String targetNamespace = schema.TargetNamespace() == null ? XMLConstants.NULL_NS_URI : schema.TargetNamespace().toString();
        if ( targetNamespace.equals( namespace ) ) {
          return schema;
        }
        for ( Import imprt : schema.Import() ) {
          String importNamespace = imprt.Namespace() == null ? XMLConstants.NULL_NS_URI : imprt.Namespace().toString();
          if ( importNamespace.equals( namespace ) ) {
            Schema importedSchema = schemas.get( imprt.SchemaLocation().toString() );
            if ( importedSchema == null ) {
              throw new RuntimeException( "Unable to find referenced schema, namespace " + imprt.Namespace() + " at location " + imprt.SchemaLocation() );
            }
            return importedSchema;
          }
        }
      }
    }
    return null;
  }

  private static TDefinitions_PortType findPortType( QName portTypeName, Definitions targetWsdl ) {
    for ( TDefinitions_PortType portType : targetWsdl.PortType() ) {
      if ( portType.Name().equals( portTypeName.getLocalPart() ) ) {
        return portType;
      }
    }
    throw new RuntimeException( "Unable to find wsdl:portType " + portTypeName );
  }

  private static TDefinitions_Message findMessage( QName messageName, Definitions targetWsdl ) {
    for ( TDefinitions_Message message : targetWsdl.Message() ) {
      if ( message.Name().equals( messageName.getLocalPart() ) ) {
        return message;
      }
    }
    throw new RuntimeException( "Unable to find wsdl:message " + messageName );
  }

  private static Definitions findWsdlForNamespace( QName qName, Definitions def, LinkedHashMap<String, Definitions> definitions ) {
    String myTargetNamespace = def.TargetNamespace() == null ? XMLConstants.NULL_NS_URI : def.TargetNamespace().toString();
    String requiredTargetNamespace = qName.getNamespaceURI();
    if ( myTargetNamespace.equals( requiredTargetNamespace ) ) {
      return def;
    }
    for ( TDefinitions_Import imprt : def.Import() ) {
      String importedNamespace = imprt.Namespace() == null ? XMLConstants.NULL_NS_URI : imprt.Namespace().toString();
      if ( importedNamespace.equals( requiredTargetNamespace ) ) {
        Definitions targetWsdl = definitions.get( imprt.Location().toString() );
        if ( targetWsdl == null ) {
          throw new RuntimeException( "Unable to find referenced WSDL, namespace " + imprt.Namespace() + " at location " + imprt.Location() );
        }
        return targetWsdl;
      }
    }
    throw new RuntimeException( "Unable to find WSDL for " + qName );
  }

  private static String fetch( URI uri, HashMap<URI, Map<Boolean,String>> alreadyFetched, HashSet<String> usedNamespaces, FileType fileType, LinkedHashMap<String, Definitions> definitions, LinkedHashMap<String, Schema> schemas, boolean isInclude ) throws IOException, URISyntaxException {
    Map<Boolean, String> isIncludeMap = alreadyFetched.get( uri );
    if ( isIncludeMap == null ) {
      isIncludeMap = new HashMap<Boolean, String>();
      alreadyFetched.put( uri, isIncludeMap );
    }
    String filename = isIncludeMap.get( isInclude );
    String extension = null;
    if ( filename == null ) {
      filename = uri.getPath();
      int idx = filename.lastIndexOf( '/' );
      if ( idx >= 0 ) {
        filename = filename.substring( idx + 1 );
      }
      idx = filename.lastIndexOf( '?' );
      if ( idx >= 0 ) {
        filename = filename.substring( 0, idx );
      }
      // remove extension if any
      idx = filename.lastIndexOf( '.' );
      if ( idx >= 0 ) {
        extension = filename.substring( idx + 1 );
        filename = filename.substring( 0, idx );
      }
      if ( filename.length() == 0 ) {
        filename = "_";
      }
      try {
        String suffix = "";
        int suffixNum = 2;
        while ( true ) {
          if ( usedNamespaces.add( isInclude + "-" + XmlSchemaIndex.normalizeSchemaNamespace( filename + suffix ).toLowerCase() ) ) {
            filename += suffix;
            break;
          }
          if ( extension != null ) {
            filename += '_' + extension;
            extension = null;
          }
          else {
            suffix = String.valueOf( suffixNum++ );
          }
        }
      }
      catch ( Exception ex ) {
        throw new RuntimeException( ex );
      }
      switch ( fileType ) {
        case SCHEMA:
          filename = filename.replace( '.', '_' );
          filename += ( isInclude ? ".xsdi" : ".xsd" );
          break;
        case WSDL:
          filename = filename.replace( '.', '_' );
          filename += ".wsdl";
          break;
      }
      System.out.println( "Wsdl2Gosu - Fetching " + uri + " -> " + filename );
      isIncludeMap.put( isInclude, filename );
      switch ( fileType ) {
        case SCHEMA: {
          Schema schema = parseDocumentAtUri( uri, Schema.class );
          fetchDependentSchemas( uri, alreadyFetched, usedNamespaces, schema, definitions, schemas );
          schemas.put( filename, schema );
          break;
        }
        case WSDL: {
          XmlParseOptions options = new XmlParseOptions();
          IModule webservicesModule = TypeSystem.getGlobalModule();
          List<XmlSchemaAccess> additionalSchemas = new ArrayList<XmlSchemaAccess>();
          additionalSchemas.add( XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( webservicesModule, "gw.xsd.w3c.xmlschema" ).getXmlSchemaAccess() );
          additionalSchemas.add( XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( webservicesModule, "gw.xsd.w3c.soap11" ).getXmlSchemaAccess() );
          additionalSchemas.add( XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( webservicesModule, "gw.xsd.w3c.soap12" ).getXmlSchemaAccess() );
          additionalSchemas.add( XmlSchemaResourceTypeLoaderBase.findSchemaForNamespace( webservicesModule, "gw.xsd.w3c.soap11_encoding" ).getXmlSchemaAccess() );
          options.setAdditionalSchemas( additionalSchemas );
          Definitions wsdlDefinitions = parseDocumentAtUri( uri, Definitions.class, options );
          fetchDependentWsdls( uri, alreadyFetched, usedNamespaces, wsdlDefinitions, definitions, schemas );
          for ( TDefinitions_Types type : wsdlDefinitions.Types() ) {
            for ( XmlElement schema : type.getChildren( Schema.$QNAME ) ) {
              fetchDependentSchemas( uri, alreadyFetched, usedNamespaces, (Schema) schema, definitions, schemas );
            }
          }
          definitions.put( filename, wsdlDefinitions );
          break;
        }
      }
    }
    return filename;
  }

  private static void fetchDependentWsdls( URI uri, HashMap<URI, Map<Boolean, String>> alreadyFetched, HashSet<String> usedNamespaces, Definitions wsdlDefinitions, LinkedHashMap<String, Definitions> definitions, LinkedHashMap<String, Schema> schemas ) throws IOException, URISyntaxException {
    for ( TDefinitions_Import imprt : wsdlDefinitions.Import() ) {
      URI child = uri.resolve( imprt.Location() );
      String filename = fetch( child, alreadyFetched, usedNamespaces, FileType.WSDL, definitions, schemas, false );
      imprt.setLocation$( new URI( filename ) );
    }
  }

  private static void fetchDependentSchemas( URI uri, HashMap<URI, Map<Boolean, String>> alreadyFetched, HashSet<String> usedNamespaces, Schema schema, LinkedHashMap<String, Definitions> definitions, LinkedHashMap<String, Schema> schemas ) throws IOException, URISyntaxException {
    for ( Import imprt : schema.Import() ) {
      if ( imprt.SchemaLocation() != null ) {
        URI child = uri.resolve( imprt.SchemaLocation() );
        String filename = fetch( child, alreadyFetched, usedNamespaces, FileType.SCHEMA, definitions, schemas, false );
        imprt.setSchemaLocation$( new URI( filename ) );
      }
    }
    for ( Include include : schema.Include() ) {
      URI child = uri.resolve( include.SchemaLocation() );
      String filename = fetch( child, alreadyFetched, usedNamespaces, FileType.SCHEMA, definitions, schemas, true );
      include.setSchemaLocation$( new URI( filename ) );
    }
    for ( Redefine redefine : schema.Redefine() ) {
      URI child = uri.resolve( redefine.SchemaLocation() );
      String filename = fetch( child, alreadyFetched, usedNamespaces, FileType.SCHEMA, definitions, schemas, true );
      redefine.setSchemaLocation$( new URI( filename ) );
    }
  }

  private static enum FileType { WSDL, SCHEMA }

}
