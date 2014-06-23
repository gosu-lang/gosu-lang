/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd;

import gw.fs.IFile;
import gw.internal.xml.xsd.typeprovider.LocationMap;

import java.io.InputStream;
import java.net.URL;

public interface XmlSchemaSource {

  URL getBlueprintURL();

  InputStream getInputStream( boolean isLocal );

  String getDescription();

  IFile getResourceFile();

  LocationMap createLocationMap();

}
