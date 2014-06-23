/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd;

import gw.fs.IFile;
import gw.internal.xml.xsd.typeprovider.LocationMap;
import gw.internal.xml.xsd.typeprovider.SimpleLocationMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public final class ResourceFileXmlSchemaSource implements XmlSchemaSource {
  private final IFile _resourceFile;

  public ResourceFileXmlSchemaSource(IFile resourceFile) {
    _resourceFile = resourceFile;
  }

  public URL getBlueprintURL() {
    try {
      return _resourceFile.toURI().toURL();
    }
    catch ( MalformedURLException ex ) {
      throw new RuntimeException( ex );
    }
  }

  public InputStream getInputStream( boolean isLocal ) {
    try {
      return _resourceFile.openInputStream();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getDescription() {
    return _resourceFile.toURI().toString();
  }

  public IFile getResourceFile() {
    return _resourceFile;
  }

  @Override
  public LocationMap createLocationMap() {
    return new SimpleLocationMap();
  }
}
