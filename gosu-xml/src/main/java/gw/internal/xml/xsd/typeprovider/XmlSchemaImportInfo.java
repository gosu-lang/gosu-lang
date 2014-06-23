/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

public class XmlSchemaImportInfo {

  private final String _targetNamespace;
  private final String _xsdSourcePath;
  private final boolean _isWsdl;

  public XmlSchemaImportInfo( String targetNamespace, String xsdSourcePath, boolean isWsdl ) {
    _targetNamespace = targetNamespace;
    _xsdSourcePath = xsdSourcePath;
    _isWsdl = isWsdl;
  }

  public String getTargetNamespace() {
    return _targetNamespace;
  }

  public String getXsdSourcePath() {
    return _xsdSourcePath;
  }

  public boolean isWsdl() {
    return _isWsdl;
  }

}
