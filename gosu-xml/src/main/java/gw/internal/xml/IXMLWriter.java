/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.xml.XmlSerializationOptions;

import java.io.IOException;
import java.util.Map;

/**
 * Interface description...
 *
 * @author dandrews
 */
public interface IXMLWriter {
  void newLine() throws IOException;

  void writeComment( String comment ) throws IOException;

  void startElement(String name) throws IOException;

  void endElement() throws IOException;

  void addText(String text) throws IOException;

  void addAttribute(String attrName, String attrValue) throws IOException;

  void writeElement(String name, Map<String, String> attributes, String body) throws IOException;

  void finish() throws IOException;

  void addElement( String name, String value ) throws IOException;

  XmlSerializationOptions getWriterOptions();
}
