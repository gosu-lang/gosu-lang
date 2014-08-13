/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider;

import gw.fs.IFile;
import gw.internal.xml.config.XmlServices;
import gw.internal.xml.ws.WsiAdditions;
import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.internal.xml.xsd.typeprovider.XmlSchemaResourceTypeLoaderBase;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchema;
import gw.lang.reflect.module.IModule;
import gw.util.ILogger;
import gw.util.Pair;

import java.net.URL;
import java.util.Map;

public class WsdlTypeLoader extends XmlSchemaResourceTypeLoaderBase<Object> {

  private ILogger _logger;

  // This constructor is called reflectively
  @SuppressWarnings( { "UnusedDeclaration" } )
  public WsdlTypeLoader( IModule module ) {
    super( "wsdl", module );
  }

  public ILogger getLogger() {
    if (_logger == null) {
      _logger = XmlServices.getLogger( XmlServices.Category.Loading );
    }
    return _logger;
  }

  @Override
  public String getSchemaSchemaTypeName() {
    return "gw.xsd.w3c.wsdl.Definitions";
  }

  protected XmlSchemaIndex<Object> loadSchemaForNamespace( String namespace, IFile resourceFile, Map<Pair<URL,String>, XmlSchema> caches  ) {
    Wsdl ret = null;
    try {
      // WSDL loading does not use schema caches
      ret = WsiAdditions.getInstance().createWsdl( this, namespace, resourceFile, caches );
    }
    catch ( Exception ex ) {
      ex.printStackTrace();
    }
    return ret;
  }
  
  @Override
  public String getElementsNamespacePrefix() {
    return ".elements";
  }

  @Override
  public String getTypesNamespacePrefix() {
    return ".types";
  }

  @Override
  public String getEnumerationsNamespacePrefix() {
    return ".enums";
  }

  @Override
  public String getAnonymousNamespacePrefix() {
    return ".anonymous";
  }

  @Override
  public boolean handlesNonPrefixLoads() {
    return true;
  }

  protected String[] getAdditionalSubPackages() {
    return new String[] { "faults", "ports", "soapheaders" };
  }

}
