/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SimpleXmlNodeWriter {
  private StringBuilder _sb = new StringBuilder();
  private int _indent = 0;

  public static String writeToString(SimpleXmlNode node) {
    SimpleXmlNodeWriter writer = new SimpleXmlNodeWriter();
    return writer.createXml(node);
  }

  private SimpleXmlNodeWriter() { }

  private String createXml(SimpleXmlNode node) {
    appendNode(node);
    // appendNode will result in a trailing \n character, so we want to delete that.  It's simpler than
    // trying to track when to add \n and when not to . . .
    _sb.deleteCharAt(_sb.length() - 1);
    return _sb.toString();
  }

  private void appendNode(SimpleXmlNode node) {
    appendIndent();
    _sb.append("<").append(node.getName());
    appendAttributes(node);
    if (node.getChildren().isEmpty()) {
      if (node.getText() == null) {
        _sb.append("/>\n");
      } else {
        _sb.append(">");
        _sb.append(xmlEncode(node.getText(), false));
        _sb.append("</").append(node.getName()).append(">\n");
      }
    } else {
      _sb.append(">\n");
      pushIndent();
      if (node.getText() != null) {
        appendIndent();
        _sb.append(xmlEncode(node.getText(), false)).append("\n");
      }
      appendChildren(node);
      popIndent();
      appendIndent();
      _sb.append("</").append(node.getName()).append(">\n");
    }
  }

  private void appendAttributes(SimpleXmlNode node) {
    List<String> orderedKeys = new ArrayList<String>(node.getAttributes().keySet());
    Collections.sort(orderedKeys);
    for (String attribute : orderedKeys) {
      _sb.append(" ").append(attribute).append("=").append(xmlEncode(node.getAttributes().get(attribute), true)).append("");
    }
  }

  private void appendChildren(SimpleXmlNode node) {
    for (SimpleXmlNode child : node.getChildren()) {
      appendNode(child);
    }
  }

  private void pushIndent() {
    _indent+=2;
  }

  private void popIndent() {
    _indent-=2;
  }

  private void appendIndent() {
    for (int i = 0; i < _indent; i++) {
      _sb.append(" ");
    }
  }

  /**
   * Properly encodes user input for inclusion in an XML document.
   * @param input the input to encode
   * @param attribute is this for an attribute? (returned value will be pre-quoted)
   * @return the XML-encoded input
   */
   private static String xmlEncode(String input, boolean attribute) {
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
}
