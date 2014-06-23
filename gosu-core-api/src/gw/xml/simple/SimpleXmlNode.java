/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;
import java.io.InputStream;

public class SimpleXmlNode {

  private List<SimpleXmlNode> _children;
  private Map<String, String> _attributes;
  private String _name;
  private String _text;
  private SimpleXmlNode _parent;

  /**
   * Construct a new SimpleXmlNode with the given element name.  The name cannot be null and should be
   * a valid XML element name.
   *
   * @param name the name of the node
   */
  public SimpleXmlNode(String name) {
    setName(name);
    _children = new XmlChildList<SimpleXmlNode>(this);
    _attributes = new HashMap<String, String>();
  }

  /**
   * Parse the given String to create a SimpleXmlNode.
   *
   * @param s the XML content to be parsed
   * @return the resulting SimpleXmlNode
   */
  public static SimpleXmlNode parse(String s) {
    return SimpleXmlParser.parseString(s);
  }

  /**
   * Parse the given File to create a SimpleXmlNode.
   *
   * @param f the File containing XML content to be parsed
   * @return the resulting SimpleXmlNode
   */
  public static SimpleXmlNode parse(File f) {
    return SimpleXmlParser.parseFile(f);
  }

  /**
   * Parse the given InputStream to create a SimpleXmlNode.
   *
   * @param is the InputStream containing XML content to be parsed
   * @return the resulting SimpleXmlNode
   */
  public static SimpleXmlNode parse(InputStream is) {
    return SimpleXmlParser.parseInputStream(is);
  }

  /**
   * Returns the immediate children of this node.  Adding or removing to this list
   * will automatically set or null out the Parent on the node being added or removed.
   *
   * @return the children of this node
   */
  public List<SimpleXmlNode> getChildren() {
    return _children;
  }

  /**
   * Returns an Iterable over the descendents of this node (not including this node).  Descendents
   * are traversed in pre-order.
   *
   * @return a pre-ordered iterator over the descendents of this node
   */
  public Iterable<SimpleXmlNode> getDescendents() {
    return new Iterable<SimpleXmlNode>() {
      @Override
      public Iterator<SimpleXmlNode> iterator() {
        return new DescendentsIterator(SimpleXmlNode.this);
      }
    };
  }

  /**
   * Returns the attributes associated with this node.  Note that all prefixes are removed from attribute
   * names during parsing, so the XML foo:bar="xyz" will result in an entry in this map with the key "bar"
   * and the value "xyz".
   *
   * @return the map of attributes for this node.
   */
  public Map<String, String> getAttributes() {
    return _attributes;
  }

  /**
   * Returns the name of this node.
   *
   * @return the name of this node
   */
  public String getName() {
    return _name;
  }

  /**
   * Sets the name of this node.  The name cannot be null, and should be a legal XML element identifier.
   *
   * @param name the new name
   */
  public void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("The name of a SimpleXmlNode can never be set to null");
    }
    _name = name;
  }

  /**
   * Returns the textual content of this node, if any.  This property may return null in the event that the element
   * has no text content.
   *
   * @return the text content of this node
   */
  public String getText() {
    return _text;
  }

  /**
   * Sets the text content of this node.
   *
   * @param text the text content for the node
   */
  public void setText(String text) {
    _text = text;
  }

  /**
   * Returns the parent of this node, or null if this node is a root node.  This property is set automatically
   * when a node is added to the child list of another node, and nulled out automatically when the node is removed
   * from the child list of its parent.  It cannot be set explicitly.
   *
   * @return the parent of this node
   */
  public SimpleXmlNode getParent() {
    return _parent;
  }

  // Package local since it shouldn't be called generally; parents/children should be hooked up by adding the node
  // as a child of another node
  void setParent(SimpleXmlNode parent) {
    _parent = parent;
  }

  /**
   * Makes a shallow copy of this node, including its name, text, and attributes.  The returned node will
   * have no children and no parent.
   *
   * @return a shallow copy of this node
   */
  public SimpleXmlNode shallowCopy() {
    SimpleXmlNode copy = new SimpleXmlNode(_name);
    copy.setText(_text);
    copy.getAttributes().putAll(_attributes);
    return copy;
  }

  /**
   * Makes a deep copy of this node, including copies of all contained children.  The returned node will
   * have the same name, text, and attributes as this node, and its list of children will contain deep copies
   * of each child of this node.
   *
   * @return a deep copy of this node
   */
  public SimpleXmlNode deepCopy() {
    SimpleXmlNode rootCopy = shallowCopy();
    for (SimpleXmlNode child : _children) {
      rootCopy.getChildren().add(child.deepCopy());
    }
    return rootCopy;
  }

  /**
   * Returns a version of this node, including all its children, as a valid XML String.
   *
   * @return a String containing the XML for this node and its children
   */
  public String toXmlString() {
    return SimpleXmlNodeWriter.writeToString(this);
  }
}
