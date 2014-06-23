/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.xml.XmlException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public class XmlSchemaCachedResourceResolver implements LSResourceResolver {

  private final Map<String, byte[]> _cache;

  public XmlSchemaCachedResourceResolver( Map<String, byte[]> cache ) {
    _cache = cache;
  }

  @Override
  public LSInput resolveResource( String type, String namespaceURI, final String publicId, final String systemId, final String baseURI ) {
    final byte[] bytes = _cache.get( namespaceURI == null ? "" : namespaceURI );
    if ( bytes == null ) {
      throw new XmlException( "Schema not found with namespace " + ( namespaceURI == null ? "null" : '\'' + namespaceURI + '\'' ) );
    }
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
        return new ByteArrayInputStream( bytes );
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
