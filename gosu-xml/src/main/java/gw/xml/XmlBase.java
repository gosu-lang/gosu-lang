/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.internal.xml.XmlTypeInstanceInternals;
import gw.lang.Autoinsert;
import gw.lang.PublishInGosu;
import gw.lang.reflect.gs.IGosuObject;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * The base of all XmlElements and XmlTypeInstances. Provides common methods to both, by delegating to the
 * type instance.
 */
@PublishInGosu
public abstract class XmlBase implements IGosuObject {
  
  private XmlTypeInstance _typeInstance;

  // Please leave constructors package-only
  XmlBase() {
    Class<? extends XmlBase> clazz = getClass();
    //noinspection ObjectEquality
    if ( clazz != XmlElement.class && ! clazz.getName().startsWith( "gw.internal.schema." ) ) {
      throw new RuntimeException( "Invalid XmlBase subclass: " + clazz.getName() );
    }
  }

  protected void setTypeInstance( XmlTypeInstance xmlTypeInstance ) {
    if ( xmlTypeInstance == null ) {
      throw new IllegalArgumentException( "xmlTypeInstance cannot be null" );
    }
    _typeInstance = xmlTypeInstance;
  }

  public XmlTypeInstance getTypeInstance() {
    return _typeInstance;
  }

  /**
   * Returns a map of all attribute simple values by QName.
   * @return a map of all attribute simple values by QName
   */
  public final Set<QName> getAttributeNames() {
    return XmlTypeInstanceInternals.instance()._getAttributeNames( _typeInstance );
  }

  /**
   * Sets the simple value of an attribute by QName.
   * @param attributeName The attribute name
   * @param value The new simple value for the attribute
   * @return the old simple value of the attribute, or null if the attribute was not previously set
   */
  public final XmlSimpleValue setAttributeSimpleValue( QName attributeName, XmlSimpleValue value ) {
    return XmlTypeInstanceInternals.instance()._setAttributeSimpleValue( _typeInstance, attributeName, value );
  }

  /**
   * Sets the simple value of an attribute in the null namespace by local name.
   * @param attributeName The local name of the attribute in the null namespace
   * @param value The new simple value for the attribute
   * @return the old simple value of the attribute, or null if the attribute was not previously set
   */
  public final XmlSimpleValue setAttributeSimpleValue( String attributeName, XmlSimpleValue value ) {
    return XmlTypeInstanceInternals.instance()._setAttributeSimpleValue( _typeInstance, attributeName, value );
  }

  /**
   * Returns the simple value of an attribute by QName.
   * @param attributeName The attribute name
   * @return the simple value of the attribute, or null if the attribute is not set
   */
  public final XmlSimpleValue getAttributeSimpleValue( QName attributeName ) {
    return XmlTypeInstanceInternals.instance()._getAttributeSimpleValue( _typeInstance, attributeName );
  }

  /**
   * Returns the simple value of an attribute in the null namespace by local name.
   * @param attributeName The local name of the attribute in the null namespace
   * @return the simple value of the attribute, or null if the attribute is not set
   */
  public final XmlSimpleValue getAttributeSimpleValue( String attributeName ) {
    return XmlTypeInstanceInternals.instance()._getAttributeSimpleValue( _typeInstance, attributeName );
  }

  /**
   * Returns the string value of an attribute in the null namespace by local name. This may or may not be
   * the same value that will eventually be serialized, such as in the case of QNames, IDREFs, and other
   * special purpose simple types.
   * @param attributeName The local name of the attribute in the null namespace
   * @return the string value of the attribute, or null if the attribute is not set
   */
  public final String getAttributeValue( String attributeName ) {
    return XmlTypeInstanceInternals.instance()._getAttributeValue( _typeInstance, attributeName );
  }

  /**
   * Returns the string value of an attribute by QName. This may or may not be the same value that will
   * eventually be serialized, such as in the case of QNames, IDREFs, and other special purpose simple types.
   * @param attributeName The attribute name
   * @return the string value of the attribute, or null if the attribute is not set
   */
  public final String getAttributeValue( QName attributeName ) {
    return XmlTypeInstanceInternals.instance()._getAttributeValue( _typeInstance, attributeName );
  }

  /**
   * Sets the string value of an attribute in the null namespace by local name. The attribute will lose
   * any special simple type treatment, such as QName or IDREF handling, and will be serialized exactly
   * as set by this method.
   * @param attributeName The local name of the attribute in the null namespace
   * @param value The new string value of the attribute, or null to remove the attribute
   * @return the old string value of the attribute, or null if the attribute was not previously set
   */
  public final String setAttributeValue( String attributeName, String value ) {
    return XmlTypeInstanceInternals.instance()._setAttributeValue( _typeInstance, attributeName, value );
  }

  /**
   * Sets the string value of an attribute by name. The attribute will lose any special simple type treatment,
   * such as QName or IDREF handling, and will be serialized exactly as set by this method.
   * @param attributeName The name of the attribute
   * @param value The new string value of the attribute, or null to remove the attribute
   * @return the old string value of the attribute, or null if the attribute was not previously set
   */
  public final String setAttributeValue( QName attributeName, String value ) {
    return XmlTypeInstanceInternals.instance()._setAttributeValue( _typeInstance, attributeName, value );
  }

  /**
   * Returns a list of direct child elements by name.
   * @param qname The name of the child elements
   * @return a list of child elements with the specified name
   */
  public final List<XmlElement> getChildren( QName qname ) {
    return XmlTypeInstanceInternals.instance()._getChildren( _typeInstance, qname );
  }

  /**
   * Returns a list of direct child elements with the specified local name in the null namespace.
   * @param name The local name of the child elements
   * @return a list of child elements with the specified name
   */
  public final List<XmlElement> getChildren( String name ) {
    return XmlTypeInstanceInternals.instance()._getChildren( _typeInstance, new QName( name ) );
  }

  /**
   * Removes all direct child elements with the specified name.
   * @param qname The name of the child elements
   * @return a list of the removed elements
   */
  public final List<XmlElement> removeChildren( QName qname ) {
    return XmlTypeInstanceInternals.instance()._removeChildren( _typeInstance, qname );
  }

  /**
   * Removes all direct child elements with the specified local name in the null namespace.
   * @param name The local name of the child elements
   * @return a list of removed elements
   */
  public final List<XmlElement> removeChildren( String name ) {
    return XmlTypeInstanceInternals.instance()._removeChildren( _typeInstance, new QName( name ) );
  }

  /**
   * Returns the direct child element with the specified name, or null if no such direct child element exists.
   * @param qname The name of the child element
   * @return the direct child element with the specified name
   * @throws MultipleContentMatchesException if there are multiple direct children with the specified name.
   */
  public final XmlElement getChild( QName qname ) {
    return XmlTypeInstanceInternals.instance()._getChild( _typeInstance, qname );
  }

  /**
   * Returns the direct child element with the specified local name in the null namespace, or null if no such direct child element exists.
   * @param name The local name of the child element in the null namespace
   * @return the direct child element with the specified name
   * @throws MultipleContentMatchesException if there are multiple direct children with the specified name.
   */
  public final XmlElement getChild( String name ) {
    return XmlTypeInstanceInternals.instance()._getChild( _typeInstance, new QName( name ) );
  }

  /**
   * Removes the direct child element with the specified name if it exists.
   * @param qname The name of the child element to remove
   * @return the direct child element that was removed, or null if no action was taken
   * @throws MultipleContentMatchesException if there are multiple existing direct children with the specified name.
   */
  public XmlElement removeChild( QName qname ) {
    return XmlTypeInstanceInternals.instance()._removeChild( _typeInstance, qname );
  }

  /**
   * Removes the direct child element with the specified local name in the null namespace if it exists.
   * @param name The local name of the child element in the null namespace to remove
   * @return the direct child element that was removed, or null if no action was taken
   * @throws MultipleContentMatchesException if there are multiple existing direct children with the specified name.
   */
  public XmlElement removeChild( String name ) {
    return XmlTypeInstanceInternals.instance()._removeChild( _typeInstance, new QName( name ) );
  }

  /**
   * Returns a list of all direct child elements.
   * @return a list of all direct child elements
   */
  @Autoinsert
  public final List<XmlElement> getChildren() {
    return XmlTypeInstanceInternals.instance()._getChildren( _typeInstance );
  }

  /**
   * Adds a new direct child element.
   * @param element The element to add
   */
  public final void addChild( XmlElement element ) {
    XmlTypeInstanceInternals.instance()._addChild( _typeInstance, element );
  }

  /**
   * Returns the simple value content, or null if one does not exist.
   * @return The simple value content, or null if one does not exist
   * @throws MultipleContentMatchesException if there are multiple simple value contents.
   */
  public final XmlSimpleValue getSimpleValue() {
    return XmlTypeInstanceInternals.instance()._getSimpleValue( _typeInstance );
  }

  /**
   * Sets the simple value content.
   * @param xmlSimpleValue The new simple value content, or null to remove the existing simple value content
   */
  public final void setSimpleValue( XmlSimpleValue xmlSimpleValue ) {
    XmlTypeInstanceInternals.instance()._setSimpleValue( _typeInstance, xmlSimpleValue );
  }

  /**
   * Returns the text content.
   * This might not be the same text content that will actually be written at serialization time due
   * to special serialization-time handling of some simple types, such as xsd:QName or xsd:IDREF.
   * @return The text content
   */
  public final String getText() {
    return XmlTypeInstanceInternals.instance()._getText( _typeInstance );
  }

  /**
   * Sets the text content. This string value will be output verbatim at serialization time, removing any special
   * serialization-time special simple value handling, such as xsd:QName or xsd:IDREF that previously existed,
   * if any. Calling this method with a null parameter will remove any existing text contents or simple values,
   * if any.
   * @param text The new text content, or null to remove the existing text content
   */
  public final void setText( String text ) {
    XmlTypeInstanceInternals.instance()._setText( _typeInstance, text );
  }

  /**
   * Returns the mixed content of this element.
   * @return the mixed content of this element
   */
  @Autoinsert
  public final List<IXmlMixedContent> getMixedContent() {
    return _typeInstance._children;
  }

  /**
   * Returns a string representation of this XML type instance.
   * @return a string representation of this XML type instance.
   */
  @Override
  public String toString() {
    return getIntrinsicType().getName() + " instance";
  }

}
