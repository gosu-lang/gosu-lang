/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.fs.IFile;
import gw.internal.xml.xsd.ResourceFileXmlSchemaSource;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchema;
import gw.lang.reflect.module.IModule;
import gw.util.Pair;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 */
public class XmlSchemaResourceTypeLoader extends XmlSchemaResourceTypeLoaderBase<Object> {

  public XmlSchemaResourceTypeLoader( IModule module ) {
    super( "xsd", module );
  }

  protected XmlSchemaIndex<Object> loadSchemaForNamespace( String namespace, IFile resourceFile, Map<Pair<URL,String>, XmlSchema> caches  ) {
    XmlSchemaIndex<Object> ret = null;
    try {
      ret = new XmlSchemaIndex<Object>( this, namespace, new ResourceFileXmlSchemaSource( resourceFile ), null );
    }
    catch ( RuntimeException ex ) {
      ex.printStackTrace();
      for ( IXmlSchemaExceptionListener listener : _exceptionListeners ) {
        listener.exceptionOccurred( namespace, resourceFile, ex );
      }
    }
    return ret;
  }

  @Override
  public boolean handlesNonPrefixLoads() {
    return true;
  }

}
