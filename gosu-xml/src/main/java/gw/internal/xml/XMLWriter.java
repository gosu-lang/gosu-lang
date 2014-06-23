/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.util.Stack;
import gw.xml.XmlSerializationOptions;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Map;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Writes XML to a stream.
 *
 *
 */
public class XMLWriter implements IXMLWriter {

  private final OutputStreamWriter _out;
  private final XmlSerializationOptions _options;
  private boolean gotRoot = false; // got root tag already?

  // element name stack for tracking element recursion
  private final Stack<String> _elementNameStack = new Stack<String>();

  private String _elementName = null; // the name of the current element
  private boolean _gotValue = false; // does the current element have a value set
  private boolean _gotSubelements = false; // does the current element have any subelements
  private boolean _writeNewLine = false;

  private static final SimpleAttributeSet PI_ATTRIBUTES = new SimpleAttributeSet();
  private static final SimpleAttributeSet COMMENT_ATTRIBUTES = new SimpleAttributeSet();
  private static final SimpleAttributeSet ATTRIBUTE_NAME_ATTRIBUTES = new SimpleAttributeSet();
  private static final SimpleAttributeSet ATTRIBUTE_VALUE_ATTRIBUTES = new SimpleAttributeSet();
  private static final SimpleAttributeSet ELEMENT_NAME_ATTRIBUTES = new SimpleAttributeSet();

  static {
    PI_ATTRIBUTES.addAttribute( StyleConstants.Italic, true );

    ATTRIBUTE_NAME_ATTRIBUTES.addAttribute( StyleConstants.Foreground, Color.GREEN );

    ATTRIBUTE_VALUE_ATTRIBUTES.addAttribute( StyleConstants.Foreground, Color.PINK );

    COMMENT_ATTRIBUTES.addAttribute( StyleConstants.Italic, true );

    ELEMENT_NAME_ATTRIBUTES.addAttribute( StyleConstants.Foreground, Color.CYAN );
  }

  XMLWriter(OutputStream out, XmlSerializationOptions options) throws IOException {
    Charset charset = options.getEncoding() == null ? Charset.forName("UTF-8") : options.getEncoding();
    CharsetEncoder encoder = charset.newEncoder();
    encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
    encoder.onMalformedInput(CodingErrorAction.REPORT);
    _out = new OutputStreamWriter(out, encoder);
    _options = options;
    try {
      setAttributes( PI_ATTRIBUTES );
      if (options.getXmlDeclaration()) {
        if (options.getEncoding() != null) {
          if (options.getEncoding().name().startsWith("UTF-16") && !options.getEncoding().name().equals("UTF-16")) {
            _out.write(0xFEFF);
          }
        }
        write("<?xml ");
        setAttributes( ATTRIBUTE_NAME_ATTRIBUTES );
        write("version=");
        setAttributes( ATTRIBUTE_VALUE_ATTRIBUTES );
        write(xmlEncode( "1.0", true ));
        if (_options.getEncoding() != null) {
          write(" ");
          setAttributes( ATTRIBUTE_NAME_ATTRIBUTES );
          write("encoding=");
          setAttributes( ATTRIBUTE_VALUE_ATTRIBUTES );
          if (options.getEncoding().name().startsWith("UTF-16")) {
            write(xmlEncode("UTF-16", true));
          }
          else {
            write(xmlEncode(options.getEncoding().name(), true));
          }
        }
        setAttributes( PI_ATTRIBUTES );
        write("?>");
      }
    }
    finally {
      setAttributes( null );
    }
  }

  private void write(String str) throws IOException {
    try {
      _out.write(str);
    }
    catch (CharacterCodingException e) {
       throw new RuntimeException("attempting to encode '" + str +"'", e);
    }
  }

  private void setAttributes( SimpleAttributeSet attributes ) {
  }

  /**
   * Writes the stylesheet element. This should be called immediately after the constructor.
   *
   * @param type The stylesheet type such as text/xsl.
   * @param href The stylesheet location.
   * @throws IOException if an I/O error occurs.
   */
  public void writeStyleSheet(String type, String href) throws IOException {
    if (_options.getPretty()) {
      writeNewLine();
    }
    write("<?xml-stylesheet type=");
    write(xmlEncode(type, true));
    write(" href=");
    write(xmlEncode(href, true));
    write("?>");
  }

  /**
   * Appends a newLine at the next opportunity.
   * @throws IOException if an I/O error occurs.
   */
  public void newLine() throws IOException {
    _writeNewLine = true;
  }

  /**
   * Adds a comment to the XML.
   * @param comment The comment text to add to the XML.
   * @throws IOException if an I/O error occurs.
   */
  public void writeComment( String comment ) throws IOException {
    if ( _options.getComments() && comment != null ) {
      if (gotRoot) {
        finishStartElement();
      }
      if (_options.getPretty()) {
        writeNewLine();
        writeIndent(0);
      }
      try {
        setAttributes( COMMENT_ATTRIBUTES );
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < comment.length(); i++ ) {
          char ch = comment.charAt( i );
          sb.append( ch );
          if ( ch == '-' ) {
            if ( i == comment.length() - 1 || comment.charAt( i + 1 ) == '-' ) {
              sb.append( " " );
            }
          }
        }
        write("<!--" + sb + "-->");
      }
      finally {
        setAttributes( null );
      }
    }
  }

  private void writeNewLine() throws IOException {
    write(_options.getLineSeparator());
  }

  private void writeExplicitNewLineIfNecessary() throws IOException {
    if (_writeNewLine) {
      _writeNewLine = false;
      write(_options.getLineSeparator());
    }
  }

  /**
   * Properly encodes user input for inclusion in an XML document.
   * @param input the input to encode
   * @param attribute is this for an attribute? (returned value will be pre-quoted)
   * @return the XML-encoded input
   */
   static String xmlEncode(String input, boolean attribute) {
    if (input == null || input.length() == 0) {
      return attribute ? "\"\"" : input;
    }
     StringBuilder output = new StringBuilder();
    if (attribute) {
      output.append(0); // reserve space for leading quote
    }
    char quoteChar = 0;
    for (int i = 0; i < input.length(); i++) {
      char ch = input.charAt(i);
      switch (ch) {
        case '<':
          output.append("&lt;");
          break;
        case '>':
          output.append("&gt;");
          break;
        case '&':
          output.append("&amp;");
          break;
        case '"':
          if (attribute && quoteChar == '"') {
            output.append("&quot;");
          }
          else {
            output.append(ch);
            quoteChar = '\'';
          }
          break;
        case '\'':
          if (attribute && quoteChar == '\'') {
            output.append("&apos;");
          }
          else {
            output.append(ch);
            quoteChar = '"';
          }
          break;
        case 0x0009: // tab
        case 0x000A: // linefeed
        case 0x000D: // carriage return
          if (attribute) {
            output.append("&#");
            output.append((int) ch);
            output.append(";");
          }
          else {
            output.append(ch);
          }
          break;
        default:
          if (ch < 32 || ch >= 0xFFFE) {
            // TODO dlank - allow the option of stripping invalid characters rather than throwing an exception
            throw new IllegalArgumentException("UTF-16 Codepoint 0x" + Integer.toString(ch, 16) + " is not valid for XML content");
          }
          output.append(ch);
      }
    }
    if (attribute) {
      if (quoteChar == 0) {
        quoteChar = '"';
      }
      output.setCharAt(0, quoteChar);
      output.append(quoteChar);
    }
    return output.toString();
  }

  /**
   * Starts a new element in the output document.
   * @param name the element name
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public void startElement(String name) throws IOException {
    if (name == null) {
      throw new NullPointerException("name");
    }
    if (_elementName == null) {
      if (gotRoot) {
        throw new IllegalStateException("XML only allows one root element");
      }
      gotRoot = true;
      writeExplicitNewLineIfNecessary();
    }
    else {
      finishStartElement();
    }
    if (_options.getPretty()) {
      writeNewLine();
      writeIndent(0);
    }
    try {
      setAttributes( ELEMENT_NAME_ATTRIBUTES );
      write("<");
      write(xmlEncode(name, false));
    }
    finally {
      setAttributes( null );
    }
    _elementNameStack.push(_elementName);
    _elementName = name;
    _gotValue = false;
    _gotSubelements = false;
  }

  private void writeIndent(int additionalLevels) throws IOException {
    write(copy(_options.getIndent(), _elementNameStack.size() + additionalLevels));
  }

  /**
   * Ends the most recently added element.
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public void endElement() throws IOException {
    try {
      setAttributes( ELEMENT_NAME_ATTRIBUTES );
      if (_elementName == null) {
        throw new IllegalStateException("No enclosing element for endElement()");
      }
      if (!_gotValue) {
        write("/>");
      }
      else {
        writeExplicitNewLineIfNecessary();
        if (_options.getPretty() && _gotSubelements) {
          writeNewLine();
          // write indentation
          write(copy(_options.getIndent(), (_elementNameStack.size() - 1)));
        }
        write("</");
        write(xmlEncode(_elementName, false));
        write(">");
      }
      _elementName = _elementNameStack.pop();
      _gotValue = true;
      _gotSubelements = true;
    }
    finally {
      setAttributes( null );
    }
  }

  /**
   * Adds text to the current element.
   * @param text the text to add
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public void addText(String text) throws IOException {
    if (_elementName == null) {
      throw new IllegalStateException("No enclosing element for addText()");
    }
    if ( text == null || text.length() == 0 ) {
      return; // ignore call
    }
    finishStartElement();
    write(xmlEncode(String.valueOf(text), false));
  }

  /**
   * Adds an attribute pair to the current element. Cannot be called once any values or subelements have
   * been added.
   * @param attrName the attribute name
   * @param attrValue the attribute value
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public void addAttribute(String attrName, String attrValue) throws IOException {
    if (_elementName == null) {
      throw new IllegalStateException("No enclosing element for addAttribute()");
    }
    if (_gotValue) {
      throw new IllegalStateException("Attributes cannot be added once an element contains data");
    }
    if (_writeNewLine || _options.getAttributeNewLine() ) {
      _writeNewLine = false;
      write(_options.getLineSeparator());
      writeIndent(_options.getAttributeIndent());
    }
    else {
      write(" ");
    }
    try {
      setAttributes( ATTRIBUTE_NAME_ATTRIBUTES );
      write(xmlEncode(attrName, false) + "=");
      setAttributes( ATTRIBUTE_VALUE_ATTRIBUTES );
      write(xmlEncode(attrValue, true));
    }
    finally {
      setAttributes( null );
    }
  }

  /**
   * Convenience method for starting an element, adding attributes and text,
   * and ending the element in one call.
   *
   * @param name the element name
   * @param attributes a Map of element attributes, can be null
   * @param body the element text, can be null
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public void writeElement(String name, Map<String, String> attributes, String body) throws IOException {
    assert name != null;
    startElement(name);
    if (attributes != null) {
      for (Map.Entry<String, String> attribute : attributes.entrySet()) {
        addAttribute(attribute.getKey(), attribute.getValue());
      }
    }
    if (body != null) addText(body);
    endElement();
  }

  /**
   * Completes writing of the XML document. All started elements must be ended before calling this method.
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public void finish() throws IOException {
    _out.flush();
    if (_elementName != null) {
      throw new IllegalStateException("Elements must be balanced before calling finish");
    }
    if (!gotRoot) {
      throw new IllegalStateException("An XML document must have a root element");
    }
  }

  /**
   * Flushes the writing process by calling finish(), then closes the underlying output stream. Even if
   * finish() fails, the outputstream will be closed. If this is not the desired behaviour, then make
   * a call to finish() manually in order to ensure it succeeds.
   * @throws IOException if an I/O error occurs while writing to the stream
   */
  public void close() throws IOException {
    try {
      finish();
    }
    finally {
      _out.close();
    }
  }

  private void finishStartElement() throws IOException {
    if (!_gotValue) {
      try {
        setAttributes( ELEMENT_NAME_ATTRIBUTES );
        write(">");
      }
      finally {
        setAttributes( null );
      }
      _gotValue = true;
    }
  }

  /**
   * Makes a concatenated copy of the input string <i>count</i> number of times.
   * @param input the input string
   * @param count the copy count
   * @return the copied output
   */
  public static String copy(String input, int count) {
    assert input != null;
    switch (count) {
      case 0: return "";
      case 1: return input;
      default:
        assert count >= 0;
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < count; i++) {
          output.append(input);
        }
        return output.toString();
    }
  }

  /**
   * Shorthand for if ( value != null ) { startElement( name ); addText( value ); endElement(); }.
   * @param name the element name
   * @param value the element value
   * @throws IOException if an I/O error occurs
   */
  public void addElement( String name, String value ) throws IOException {
    startElement( name );
    addText( value );
    endElement();
  }

 /**
   * Returns the XML writer options
   * @return XML writer options
   */
  @Override
  public XmlSerializationOptions getWriterOptions() {
    return _options;
  }
}
