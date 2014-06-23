/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaObject;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaType;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.internal.xml.xsd.typeprovider.validator.XmlSimpleValueValidator;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

import javax.xml.namespace.QName;

/**
 * Represents the information about a statically-typed child (attribute/element) property on an xml schema type instance.
 */
public class XmlSchemaPropertySpec extends XmlSchemaSimpleValueProvider {

  private final String _elementPropertyName; // the name of the "element" property, or null
  private final String _simpleTypePropertyName; // the name of the "simple" property (i.e. int), or null
  private final String _qnamePropertyName; // the name of the static ElementName_QName property that returns the QName of the element/attribute
  private final XmlSchemaObject _xmlSchemaObject; // The backing schema object (xsd element or attribute)
  private final XmlSchemaType _xmlSchemaType; // the backing schema object's schema type
  private final XmlSchemaPropertyType _propertyType; // Either ELEMENT or ATTRIBUTE
  private final QName _qname; // The QName of the element or attribute
  private final String _defaultValue; // The default value of the attribute, or null
  private final boolean _isPlural; // True if the element's maxOccurs > 0
  private final boolean _isProhibited;

  public XmlSchemaPropertySpec( String elementPropertyName, String simpleTypePropertyName, String qnamePropertyName, QName qname, XmlSchemaObject xmlSchemaObject, XmlSchemaType xmlSchemaType, XmlSchemaPropertyType propertyType, boolean isPlural, String defaultValue, boolean isProhibited ) {
    _elementPropertyName = elementPropertyName;
    _simpleTypePropertyName = simpleTypePropertyName;
    _qnamePropertyName = qnamePropertyName;
    _xmlSchemaObject = xmlSchemaObject;
    _xmlSchemaType = xmlSchemaType;
    _propertyType = propertyType;
    _qname = qname;
    _isPlural = isPlural;
    _defaultValue = defaultValue;
    _isProhibited = isProhibited;
  }

  /**
   * The name of the "element" property, or null
   */
  public String getElementPropertyName() {
    return _elementPropertyName;
  }

  /**
   * The name of the "simple" property (i.e. int), or null
   */
  public String getSimpleTypePropertyName() {
    return _simpleTypePropertyName;
  }

  /**
   * Returns the backing element or attribute
   */
  public XmlSchemaObject getXmlSchemaObject() {
    return _xmlSchemaObject;
  }

  /**
   * Returns the backing element or attribute's schema type
   */
  public XmlSchemaType getXmlSchemaType() {
    return _xmlSchemaType;
  }

  /**
   * Returns ELEMENT or ATTRIBUTE, depending on whether this property will create/set an element or attribute value
   */
  public XmlSchemaPropertyType getPropertyType() {
    return _propertyType;
  }

  /**
   * Returns the QName of this element or attribute
   */
  public QName getQName() {
    return _qname;
  }

  /**
   * True if the element's maxOccurs > 0
   */
  public boolean isPlural() {
    return _isPlural;
  }

  public String toString() {
    return String.valueOf( getQName() ) + " ( plural=" + _isPlural + ", type=" + _propertyType + ", propertyName=" + _elementPropertyName + ", simplePropertyName=" + _simpleTypePropertyName + " )";
  }

  /**
   * Returns the IType of the "element" property
   */
  public IType getElementPropertyGosuType() {
    return XmlSchemaIndex.getGosuTypeBySchemaObject( _xmlSchemaObject );
  }

  /**
   * Returns the IXmlSchemaTypeData of the "element" property
   */
  public IXmlSchemaElementTypeData getElementPropertyGosuTypeData() {
    return (IXmlSchemaElementTypeData) XmlSchemaIndex.getGosuTypeDataBySchemaObject( _xmlSchemaObject );
  }

  /**
   * Returns the IType of the "element" property in optionally plural form, i.e. List&lt;Whatever&gt;
   */
  public IType getElementPropertyGosuType( boolean plural ) {
    IType propType = getElementPropertyGosuType();
    if ( plural ) {
      propType = JavaTypes.LIST().getParameterizedType( propType );
    }
    return propType;
  }

  /**
   * Returns the attribute's default value, or null
   */
  public String getDefaultValue() {
    return _defaultValue;
  }

  /**
   * Returns a schema validator for validating values being set into this simple type
   */
  public XmlSimpleValueValidator getValidator() {
    return XmlSchemaIndex.getSimpleValueValidatorForSchemaType( _xmlSchemaType );
  }

  /**
   * Returns a factory for creating values to be placed into this simple type
   */
  public XmlSimpleValueFactory getSimpleValueFactory() {
    return XmlSchemaIndex.getSimpleValueFactoryForSchemaType( _xmlSchemaType );
  }

  /**
   * Returns the name of the static $ELEMENT_QNAME_Blah or $ATTRIBUTE_QNAME_Blah property that returns the QName of the element/attribute.
   */
  public String getQNamePropertyName() {
    return _qnamePropertyName;
  }

  /**
   * Returns true if this property represents and attribute, and that attribute is use="prohibited"
   */
  public boolean isProhibited() {
    return _isProhibited;
  }

  /**
   * Returns true if this is a simple type, since complex types with simple contents don't appear as simple properties
   */
  @Override
  public boolean hasSimpleContent() {
    return getSimpleTypePropertyName() != null;
  }


}
