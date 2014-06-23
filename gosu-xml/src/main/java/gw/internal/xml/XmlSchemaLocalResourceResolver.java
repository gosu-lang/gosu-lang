/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.util.GosuExceptionUtil;
import gw.xml.XmlException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.InputStream;
import java.io.Reader;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Collections;

/**
 * A resource resolver that only resolves local resources.
 */
//public class XmlSchemaLocalResourceResolver implements LSResourceResolver, XMLResolver {
public class XmlSchemaLocalResourceResolver implements LSResourceResolver {

  private final Map<String, byte[]> _schemasBySystemId;
  private final Map<String, byte[]> _schemasByNamespace;
  private final Map<String, String> _externalLocationAliases;

  public XmlSchemaLocalResourceResolver( Map<String, byte[]> schemasBySystemId, Map<String, byte[]> schemasByNamespace, Map<String, String> externalLocationAliases ) {
    _schemasBySystemId = schemasBySystemId;
    _schemasByNamespace = schemasByNamespace;
    _externalLocationAliases = externalLocationAliases;
  }

  public XmlSchemaLocalResourceResolver() {
    _schemasBySystemId = Collections.emptyMap();
    _schemasByNamespace = Collections.emptyMap();
    _externalLocationAliases = Collections.emptyMap();
  }

  @Override
  public LSInput resolveResource( String type, final String namespaceURI, final String publicId, final String systemId, final String baseURI ) {
    return new LSInput() {
      @Override
      public Reader getCharacterStream() {
        return null;
      }

      @Override
      public void setCharacterStream( Reader characterStream ) {
        throw new UnsupportedOperationException();
      }

      @Override
      public InputStream getByteStream() {
        try {
          byte[] schemaBytes;
          if ( systemId != null ) {
            String actualSystemId = _externalLocationAliases.get( systemId );
            if ( actualSystemId == null ) {
              actualSystemId = systemId;
            }
            URL baseUrl = new URL( baseURI );
            URL newUrl = new URL( baseUrl, actualSystemId );
            String newUrlString = newUrl.toExternalForm();
            schemaBytes = _schemasBySystemId.get( newUrlString );
          }
          else {
            schemaBytes = _schemasByNamespace.get( namespaceURI );
          }
          if ( schemaBytes == null ) {
            throw new XmlException( "Schema not found for system id " + systemId + " namespace " + namespaceURI + " with base url " + baseURI );
          }
          return new ByteArrayInputStream( schemaBytes );
        }
        catch ( MalformedURLException ex ) {
          throw GosuExceptionUtil.forceThrow( ex );
        }
      }

      @Override
      public void setByteStream( InputStream byteStream ) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getStringData() {
        return null;
      }

      @Override
      public void setStringData( String stringData ) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getSystemId() {
        return systemId;
      }

      @Override
      public void setSystemId( String systemId ) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getPublicId() {
        return publicId;
      }

      @Override
      public void setPublicId( String publicId ) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getBaseURI() {
        return baseURI;
      }

      @Override
      public void setBaseURI( String baseURI ) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getEncoding() {
        return null;
      }

      @Override
      public void setEncoding( String encoding ) {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean getCertifiedText() {
        throw new UnsupportedOperationException();
      }

      @Override
      public void setCertifiedText( boolean certifiedText ) {
        throw new UnsupportedOperationException();
      }
    };
  }

}
