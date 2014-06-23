/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.fs.IFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlSchemaDefaultSchemaLocations {

  private final Map<String, IFile> _localResourcePathsByXmlNamespace = new HashMap<String, IFile>();
  private final Map<String, IFile> _localResourcePathsByExternalLocation = new HashMap<String, IFile>();
  private final Map<String, List<String>> _externalLocationsByLocalResourcePath = new HashMap<String, List<String>>();

  public Map<String, IFile> getLocalResourcePathByXmlNamespace() {
    return _localResourcePathsByXmlNamespace;
  }

  public Map<String, IFile> getLocalResourcePathsByExternalLocation() {
    return _localResourcePathsByExternalLocation;
  }

  public Map<String, List<String>> getExternalLocationsByLocalResourcePath() {
    return _externalLocationsByLocalResourcePath;
  }
}
