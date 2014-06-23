/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws;

import gw.fs.IFile;
import gw.internal.xml.ws.server.marshal.XmlMarshaller;
import gw.internal.xml.ws.typeprovider.Wsdl;
import gw.internal.xml.ws.typeprovider.WsdlTypeLoader;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchema;
import gw.lang.reflect.IType;
import gw.util.Pair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class WsiAdditions {

  private static WsiAdditions _instance = new WsiAdditions();

  public static void setInstance( WsiAdditions instance ) {
    _instance = instance;
  }

  public static WsiAdditions getInstance() {
    return _instance;
  }

  public URI substituteProductCode( String location ) throws URISyntaxException {
    return null;
  }

  public Wsdl createWsdl(WsdlTypeLoader wsdlTypeLoader, String namespace, IFile resourceFile, Map<Pair<URL, String>, XmlSchema> caches) throws IOException {
    return new Wsdl( wsdlTypeLoader, namespace, resourceFile );
  }

  public String getTargetNamespace( IType type ) {
    return XmlMarshaller.createTargetNamespace( null, type.getNamespace().replace( ".", "/" ) );
  }

}
