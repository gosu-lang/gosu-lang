/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import javax.xml.namespace.QName;

public final class XmlSchemaComplexType extends XmlSchemaType<XmlSchemaComplexType> {

  private final XmlSchemaContentModel<?> _contentModel;
  private String _gwTypeName;
  private XmlSchemaAnyAttribute _anyAttributeRecursive;
  private boolean _anyAttributeSupertypesResolved;
  private XmlSchemaAny _anyRecursive;
  private boolean _anySupertypesResolved;
  private final boolean _mixed;

  public XmlSchemaComplexType( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String name, QName qname, XmlSchemaContentModel<?> contentModel, String gwTypeName, boolean mixed ) {
    super( schemaIndex, locationInfo, name, qname );
    _contentModel = contentModel;
    _gwTypeName = gwTypeName;
    _mixed = mixed;
  }

  public XmlSchemaContentModel getContentModel() {
    return _contentModel;
  }

  public String getGwTypeName() {
    return _gwTypeName;
  }

  @Override
  public XmlSchemaComplexType copy( XmlSchemaIndex schemaIndex ) {
    XmlSchemaComplexType copy = new XmlSchemaComplexType( schemaIndex, getLocationInfo(), getName(), getQName(), _contentModel == null ? null : _contentModel.copy( schemaIndex ), _gwTypeName, isMixed() );
    copy._anyAttributeRecursive =(_anyAttributeRecursive == null? null : _anyAttributeRecursive.copy( schemaIndex ));
    copy._anyAttributeSupertypesResolved = _anyAttributeSupertypesResolved;
    copy._anyRecursive = ( _anyRecursive == null? null : _anyRecursive.copy( schemaIndex ));
    copy._anySupertypesResolved = _anySupertypesResolved;
    return copy;
  }


  @Override
  public XmlSchemaAnyAttribute getAnyAttributeRecursiveIncludingSupertypes() {
    if ( ! _anyAttributeSupertypesResolved ) {
      if ( _anyAttributeRecursive == null ) {
        if ( _contentModel instanceof XmlSchemaComplexContent ) {
          XmlSchemaComplexContent contentModel = (XmlSchemaComplexContent) _contentModel;
          if ( contentModel.getContent() instanceof XmlSchemaComplexContentExtension ) {
            XmlSchemaComplexContentExtension extension = (XmlSchemaComplexContentExtension) contentModel.getContent();
            QName baseTypeName = extension.getBaseTypeName();
            XmlSchemaType superType = getSchemaIndex().getXmlSchemaTypeByQName( baseTypeName );
            _anyAttributeRecursive = superType.getAnyAttributeRecursiveIncludingSupertypes();
          }
        }
      }
      _anyAttributeSupertypesResolved = true;
    }
    return _anyAttributeRecursive;
  }

  public void setAnyAttributeRecursive( XmlSchemaAnyAttribute anyAttributeRecursive ) {
    _anyAttributeRecursive = anyAttributeRecursive;
    _anyAttributeSupertypesResolved = false;
  }

  @Override
  public XmlSchemaAny getAnyRecursiveIncludingSupertypes() {
    if ( ! _anySupertypesResolved ) {
      if ( _anyRecursive == null ) {
        if ( _contentModel instanceof XmlSchemaComplexContent ) {
          XmlSchemaComplexContent contentModel = (XmlSchemaComplexContent) _contentModel;
          if ( contentModel.getContent() instanceof XmlSchemaComplexContentExtension ) {
            XmlSchemaComplexContentExtension extension = (XmlSchemaComplexContentExtension) contentModel.getContent();
            QName baseTypeName = extension.getBaseTypeName();
            XmlSchemaType superType = getSchemaIndex().getXmlSchemaTypeByQName( baseTypeName );
            _anyRecursive = superType.getAnyRecursiveIncludingSupertypes();
          }
        }
      }
      _anySupertypesResolved = true;
    }
    return _anyRecursive;
  }

  public void setAnyRecursive( XmlSchemaAny anyRecursive ) {
    _anyRecursive = anyRecursive;
    _anySupertypesResolved = false;
  }

  public boolean isMixed() {
    return _mixed;
  }

}
