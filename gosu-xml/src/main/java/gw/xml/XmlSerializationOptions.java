/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import java.nio.charset.Charset;

/**
 * The XmlSerializationOptions class encapsulates settings that can be passed to the XML subsystem when serializing XML.
 */

public final class XmlSerializationOptions {

  private boolean _validate = true;
  private boolean _sort = true;
  private boolean _comments = true;
  private boolean _pretty = true;
  private String _indent = "  ";
  private String _lineSeparator = "\n";
  private boolean _xmlDeclaration = true;
  private boolean _attributeNewLine = false;
  private int _attributeIndent = 2;
  private Charset _encoding = null;

  /**
   * Returns the Validate property.
   * @return the Validate property
   */
  public boolean getValidate() {
    return _validate;
  }

  /**
   * @param validate the new value of the validate property
   */
  public void setValidate( boolean validate ) {
    _validate = validate;
  }

  /**
   * Returns whether to write the xml declaration
   * @return the xml declaration
   */
  public boolean getXmlDeclaration() {
    return _xmlDeclaration;
  }

  /**
   * @param xmlDeclaration whether to write the xml declaration
   */
  public void setXmlDeclaration(boolean xmlDeclaration) {
    _xmlDeclaration = xmlDeclaration;
  }

 /**
  * Returns the Sort property.
  * @return the Sort property
  */
  public boolean getSort() {
    return _sort;
  }

  /**
   * @param sort the new value of the sort property
   */
  public void setSort(boolean sort) {
    _sort = sort;
  }

 /**
  * Returns the Comments property.
  * @return the Comments property
  */
  public boolean getComments() {
    return _comments;
  }

  /**
   * @param comments the new value of the comments property
   */
  public void setComments(boolean comments) {
    _comments = comments;
  }

  /**
   * @return value for Pretty property
   */
  public boolean getPretty() {
    return _pretty;
  }

  /**
   * Sets the pretty printing property.
   * @param pretty value for the Pretty property
   */
  public void setPretty( boolean pretty ) {
    _pretty = pretty;
  }

  /**
   * @return value for Indent property
   */
  public String getIndent() {
    return _indent;
  }

  /**
   * Sets the indent.
   * @param indent value for the Indent property
   */
  public void setIndent( String indent ) {
    if (indent == null) {
      throw new IllegalArgumentException("The value for the indent property must not be null");
    }
    for (char c : indent.toCharArray()) {
      if (!(c == 0x20 || c == 0x9)) {
        throw new IllegalArgumentException("Attempt to set \"indent\" property to string containing non-whitespace characters");
      }
    }
    _indent = indent;
  }

  /**
   * @return value for LineSeparator property
   */
  public String getLineSeparator() {
    return _lineSeparator;
  }

  /**
   * Sets the LineSeparator property
   * @param lineSeparator value for the LineSeparator property
   */
  public void setLineSeparator( String lineSeparator ) {
    if (lineSeparator == null) {
      throw new IllegalArgumentException("The value for the lineSeparator property must not be null");
    }
    for (char c : lineSeparator.toCharArray()) {
      if (!(c == 0xD || c == 0xA)) {
        throw new IllegalArgumentException("Attempt to set \"lineSeparator\" property to string containing characters other than 0xD and 0xA");
      }
    }
    _lineSeparator = lineSeparator;
  }

  /**
   * Returns a new XmlSerializationOptions with sort and validate turned off.
   * @return a new XmlSerializationOptions with sort and validate turned off
   */
  public static XmlSerializationOptions debug() {
    return new XmlSerializationOptions().withSort( false ).withValidate( false );
  }

  /**
   * Returns a new XmlSerializationOptions with sort, validate, and pretty turned off.
   * @return a new XmlSerializationOptions with sort, validate, and pretty turned off
   */
  public static XmlSerializationOptions debugCompressed() {
    return new XmlSerializationOptions().withSort( false ).withValidate( false ).withPretty( false ).withXmlDeclaration(false);
  }

  /**
   * Sets the Validate property.
   * @param validate the new value for the Validate property
   * @return this
   */
  public XmlSerializationOptions withValidate( boolean validate ) {
    setValidate( validate );
    return this;
  }

  /**
   * Sets the Sort property.
   * @param sort the new value for the sort property
   * @return this
   */
  public XmlSerializationOptions withSort( boolean sort ) {
    setSort( sort );
    return this;
  }

  /**
   * Sets the Comments property.
   * @param comments the new value for the Comments property
   * @return this
   */
  public XmlSerializationOptions withComments( boolean comments ) {
    setComments( comments );
    return this;
  }

  /**
   * Sets the Pretty property.
   * @param pretty the new value for the Pretty property
   * @return this
   */
  public XmlSerializationOptions withPretty( boolean pretty ) {
    setPretty( pretty );
    return this;
  }

  /**
   * Sets the Indent property.
   * @param indent the new value for the Indent property
   * @return this
   */
  public XmlSerializationOptions withIndent( String indent ) {
    setIndent( indent );
    return this;
  }

  /**
   * Sets the LineSeparator property.
   * @param lineSeparator the new value for the LineSeparator property
   * @return this
   */
  public XmlSerializationOptions withLineSeparator( String lineSeparator ) {
    setLineSeparator( lineSeparator );
    return this;
  }

  /**
   * Sets the XmlDeclaration property.
   * @param xmlDeclaration the new value for the XmlDeclaration property
   * @return this
   */
  public XmlSerializationOptions withXmlDeclaration(boolean xmlDeclaration) {
    _xmlDeclaration = xmlDeclaration;
    return this;
  }

  /**
   * Sets the encoding property.
   * @param encoding new value for the XmlDeclaration property
   * @return this
   */
  public XmlSerializationOptions withEncoding(String encoding) {
    _encoding = Charset.forName(encoding);
    return this;
  }

  /**
   * Sets the encoding property.
   * @param encoding new value for the XmlDeclaration property
   * @return this
   */
  public XmlSerializationOptions withEncoding(Charset encoding) {
    _encoding = encoding;
    return this;
  }

  /**
   * Sets the writer to put each attribute on a new line
   * @param attributeNewLine boolean
   * @return this
   */
  public XmlSerializationOptions withAttributeNewLine(boolean attributeNewLine) {
    _attributeNewLine = attributeNewLine;
    return this;
  }

  /**
   * @return whether to put each attribute on a new line
   */
  public boolean getAttributeNewLine() {
    return _attributeNewLine;
  }

  /**
   * @param value whether to put each attribute on a new line
   */
  public void setAttributeNewLine(boolean value) {
    _attributeNewLine = value;
  }

  /**
   * Sets the writer to put each attribute on a new line
   * @param attributeIndent the number of additional indents
   * @return this
   */
  public XmlSerializationOptions withAttributeIndent(int attributeIndent) {
    _attributeIndent = attributeIndent;
    return this;
  }
  
  /**
   * @return value for Indent property
   */
  public int getAttributeIndent() {
    return _attributeIndent;
  }

  /**
   * @parm  value for Indent property
   */
  public void setAttributeIndent(int value) {
    _attributeIndent = value;
  }

  /** the character set for encoding
   *
   * @return the Charset
   */
  public Charset getEncoding() {
    return _encoding;
  }

  /** the character set for encoding
   *
   * @param value the Charset
   */
  public void setEncoding(Charset value) {
    _encoding = value;
  }

  /**
   * Makes a deep copy of this object.
   * @return a deep copy of this object
   */
  public XmlSerializationOptions copy() {
    XmlSerializationOptions copy = new XmlSerializationOptions();
    copy._validate = _validate;
    copy._sort = _sort;
    copy._comments = _comments;
    copy._pretty = _pretty;
    copy._indent = _indent;
    copy._lineSeparator = _lineSeparator;
    copy._xmlDeclaration = _xmlDeclaration;
    copy._attributeNewLine = _attributeNewLine;
    copy._attributeIndent = _attributeIndent;
    copy._encoding = _encoding;
    return copy;
  }

}
