/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

public final class XmlSchema extends XmlSchemaObject<XmlSchema> {

  private final List<XmlSchemaImport> _imports;
  private final Map<QName, XmlSchemaElement> _topLevelElements;
  private final String _targetNamespace;
  private final Map<QName, XmlSchemaType> _topLevelTypes;
  private final Map<QName, XmlSchemaAttribute> _topLevelAttributes;
  private final Map<QName, XmlSchemaGroup> _topLevelGroups;
  private final Map<QName, XmlSchemaAttributeGroup> _topLevelAttributeGroups;
  private final String _declaredTargetNamespace;

  public XmlSchema( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, List<XmlSchemaImport> imports, Map<QName, XmlSchemaElement> topLevelElements, String targetNamespace, Map<QName, XmlSchemaType> topLevelTypes, Map<QName, XmlSchemaAttribute> topLevelAttributes, Map<QName, XmlSchemaGroup> topLevelGroups, Map<QName, XmlSchemaAttributeGroup> topLevelAttributeGroups, String declaredTNS ) {
    super( schemaIndex, locationInfo );
    _imports = imports;
    _topLevelElements = topLevelElements;
    _targetNamespace = targetNamespace;
    _topLevelTypes = topLevelTypes;
    _topLevelAttributes = topLevelAttributes;
    _topLevelGroups = topLevelGroups;
    _topLevelAttributeGroups = topLevelAttributeGroups;
    _declaredTargetNamespace = declaredTNS;
  }

  public boolean isEmpty() {
    return _topLevelElements.isEmpty() && _topLevelTypes.isEmpty() && _topLevelGroups.isEmpty() && _topLevelAttributeGroups.isEmpty() && _topLevelAttributes.isEmpty();
  }  

  public String getTargetNamespace() {
    return _targetNamespace;
  }

  public String getDeclaredTargetNamespace() {
    return _declaredTargetNamespace;
  }

  public Map<QName, XmlSchemaType> getTypes() {
    return _topLevelTypes;
  }

  public Map<QName, XmlSchemaElement> getElements() {
    return _topLevelElements;
  }

  public Map<QName, XmlSchemaAttribute> getAttributes() {
    return _topLevelAttributes;
  }

  public Map<QName, XmlSchemaGroup> getGroups() {
    return _topLevelGroups;
  }

  public Map<QName, XmlSchemaAttributeGroup> getAttributeGroups() {
    return _topLevelAttributeGroups;
  }

  public List<XmlSchemaImport> getImports() {
    return _imports;
  }

  public XmlSchema copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchema( schemaIndex, getLocationInfo(), copyList( schemaIndex, _imports ), copyMap( schemaIndex, _topLevelElements ), _targetNamespace, copyMap( schemaIndex, _topLevelTypes ), copyMap( schemaIndex, _topLevelAttributes ), copyMap( schemaIndex, _topLevelGroups ), copyMap( schemaIndex, _topLevelAttributeGroups ), _declaredTargetNamespace );
  }

}
