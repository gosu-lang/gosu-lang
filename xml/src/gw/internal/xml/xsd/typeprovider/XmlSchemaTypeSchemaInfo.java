/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContent;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexContentRestriction;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaComplexType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleValueValidator;
import gw.lang.reflect.IType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

public final class XmlSchemaTypeSchemaInfo extends XmlSchemaSimpleValueProvider {

  private final XmlSchemaTypeInstanceTypeData _typeData;
  private final List<XmlSchemaPropertySpec> _properties = new ArrayList<XmlSchemaPropertySpec>( 0 );
  private final Map<QName, XmlSchemaPropertySpec> _propertyNameByElementName = new HashMap<QName, XmlSchemaPropertySpec>();
  private final LinkedHashMap<QName, XmlSchemaPropertySpec> _propertyNameByAttributeName = new LinkedHashMap<QName, XmlSchemaPropertySpec>();
  private final XmlSimpleValueFactory _xmlSimpleValueFactory;
  private final XmlSimpleValueValidator _validator;
  private final boolean _hasSimpleContent;
  private final boolean _isComplexRestriction;
  private final boolean _mixed;

  public XmlSchemaTypeSchemaInfo( XmlSchemaTypeInstanceTypeData type, XmlSimpleValueFactory xmlSimpleValueFactory, XmlSimpleValueValidator validator ) {
    if ( ( xmlSimpleValueFactory == null ) != ( validator == null ) ) {
      throw new RuntimeException( "Expected either a factory and a validator, or neither" );
    }
    _hasSimpleContent = validator != null;
    _typeData = type;
    _xmlSimpleValueFactory = xmlSimpleValueFactory;
    _validator = validator;
    boolean isComplexRestriction = false;
    boolean mixed = false;
    if ( _typeData.getXsdType() instanceof XmlSchemaComplexType ) {
      XmlSchemaComplexType complexType = (XmlSchemaComplexType) _typeData.getXsdType();
      if ( complexType.isMixed() ) {
        mixed = true;
      }
      if ( complexType.getContentModel() instanceof XmlSchemaComplexContent ) {
        XmlSchemaComplexContent complexContent = (XmlSchemaComplexContent) complexType.getContentModel();
        if ( complexContent.getContent() instanceof XmlSchemaComplexContentRestriction ) {
          isComplexRestriction = true;
        }
      }
    }
    _mixed = mixed;
    _isComplexRestriction = isComplexRestriction;
  }

  public boolean hasSimpleContent() {
    return _hasSimpleContent;
  }

  public XmlSimpleValueFactory getSimpleValueFactory() {
    return _xmlSimpleValueFactory;
  }

  public XmlSimpleValueValidator getValidator() {
    return _validator;
  }

  public void addProperty( XmlSchemaPropertySpec prop ) {
    _properties.add( prop );
    if ( prop.getPropertyType() == XmlSchemaPropertyType.ELEMENT ) {
      XmlSchemaPropertySpec oldValue = _propertyNameByElementName.put( prop.getQName(), prop );
      if ( oldValue != null && ! oldValue.equals( prop ) ) {
        // should not happen due to earlier checks in XmlSchemaGosuTypeInfoClass...
        throw new RuntimeException( "Ambiguous schema: Found element " + prop + " and " + oldValue );
      }
    }
    else if ( prop.getPropertyType() == XmlSchemaPropertyType.ATTRIBUTE ) {
      XmlSchemaPropertySpec oldValue = _propertyNameByAttributeName.put( prop.getQName(), prop );
      if ( oldValue != null && ! oldValue.equals( prop ) ) {
        // should not happen due to earlier checks in XmlSchemaGosuTypeInfoClass...
        throw new RuntimeException( "Ambiguous schema: Found attribute " + prop + " and " + oldValue );
      }
    }
    else {
      throw new RuntimeException( "Unknown property type: " + prop.getPropertyType() );
    }
  }

  public XmlSchemaTypeSchemaInfo getSuperElementInfo() {
    IType type = _typeData.getType();
    XmlSchemaTypeSchemaInfo superElementInfo = null;
    if ( type.getSupertype() != null ) {
      superElementInfo = XmlSchemaIndex.getSchemaInfoByType( type.getSupertype() );
    }
    return superElementInfo;
  }

  public List<XmlSchemaPropertySpec> getProperties() {
    return Collections.unmodifiableList( _properties );
  }

  public XmlSchemaPropertySpec getPropertyByAttributeName( QName attributeName ) {
    XmlSchemaPropertySpec property = _propertyNameByAttributeName.get( attributeName );
    if ( property == null ) {
      XmlSchemaTypeSchemaInfo superElementInfo = getSuperElementInfo();
      if ( superElementInfo != null ) {
        property = superElementInfo.getPropertyByAttributeName( attributeName );
      }
    }
    return property;
  }

  public XmlSchemaPropertySpec getPropertyByElementName( QName elementName ) {
    XmlSchemaPropertySpec property = _propertyNameByElementName.get( elementName );
    if ( property == null ) {
      XmlSchemaTypeSchemaInfo superElementInfo = getSuperElementInfo();
      if ( superElementInfo != null ) {
        property = superElementInfo.getPropertyByElementName( elementName );
      }
    }
    return property;
  }

  public XmlSchemaType getXsdType() {
    return _typeData.getXsdType();
  }

  /**
   * Returns an ordered set of attribute names.
   * @return an ordered set of attribute names
   */
  public Set<QName> getAttributeNames() {
    return _propertyNameByAttributeName.keySet();
  }

  public IXmlSchemaTypeInstanceTypeData getTypeData() {
    return _typeData;
  }

  public boolean isComplexRestriction() {
    return _isComplexRestriction;
  }

  public boolean isMixed() {
    return _mixed;
  }

}
