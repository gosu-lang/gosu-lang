/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import gw.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

class SimpleXmlNodeHandler extends DefaultHandler {
  private Stack<SimpleXmlNode> _elementStack = new Stack<SimpleXmlNode>();
  private Stack<StringBuilder> _textStack = new Stack<StringBuilder>();
  private SimpleXmlNode _root = null;
  private StringBuilder _currentText;

  public SimpleXmlNodeHandler() {
  }

  public SimpleXmlNode getRoot() {
    return _root;
  }

  @Override
  public void startElement(String uri, String localName,
                           String qualifiedName, Attributes attributes)
      throws SAXException {
    SimpleXmlNode element = new SimpleXmlNode(removePrefix(qualifiedName));

    for (int i = 0; i < attributes.getLength(); i++) {
      String attributeLocalName = removePrefix(attributes.getQName(i));
      if (element.getAttributes().containsKey(attributeLocalName)) {
        throw new IllegalStateException("Duplicate attributes were found with identical local names of " + attributeLocalName);
      }
      element.getAttributes().put(attributeLocalName, attributes.getValue(i));
    }

    if (!_elementStack.isEmpty()) {
      SimpleXmlNode parent = _elementStack.peek();
      parent.getChildren().add(element);
    } else {
      _root = element;
    }

    _elementStack.push(element);
    _textStack.push(_currentText);
    _currentText = null;
  }

  /**
   */
  @Override
  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    if (!_elementStack.isEmpty()) {
      SimpleXmlNode ended = _elementStack.pop();
      if (_currentText != null && !isBlank(_currentText)) {
        ended.setText(_currentText.toString());
      } else {
        ended.setText(null);
      }
      _currentText = _textStack.pop();
    }
  }

  @Override
  public void characters(char ch[], int start, int length) throws SAXException {
    if (_currentText == null) {
      _currentText = new StringBuilder();
      _currentText.append(ch, start, length);
      if (isBlank(_currentText)) {
        _currentText = null;
      }
    } else {
      _currentText.append(ch, start, length);
    }
  }

  private boolean isBlank(StringBuilder sb) {
    for (int i = 0; i < sb.length(); i++) {
      if (!Character.isWhitespace(sb.charAt(i))) {
        return false;
      }
    }

    return true;
  }

  @Override
  public void error(SAXParseException e) throws SAXException {
    // For now, do nothing
  }

  private String removePrefix(String localName) {
    int lastIndex = localName.lastIndexOf(':');
    if (lastIndex != -1) {
      return localName.substring(lastIndex + 1);
    } else {
      return localName;
    }
  }

}