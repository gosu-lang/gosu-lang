/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.util.GosuClassUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * A node in a tree representation of an underlying {@link PropertySet}. Any compound names, such
 * as a.b.c and a.b.d, in the keys of the property set are split into a tree representation. In the
 * a.b.c/a.b.d example there would be a property node for a, with a child node b with two further
 * leaf children c and d.
 */
public class PropertyNode implements IGosuObject {

  private final PropertyNode _parent;
  private final String _name;
  private final String _path;
  private final PropertySet _propertySet;
  private final Map<String, PropertyNode> _children = new TreeMap<String, PropertyNode>();

  public static PropertyNode buildTree(PropertySet propertySet) {
    final PropertyNode root = new PropertyNode(null, "", propertySet);
    for (String propertyPath : propertySet.getKeys()) {
      PropertyNode currentNode = root;
      for (String pathPart : propertyPath.split("\\.")) {
        PropertyNode childNode = currentNode.getChild(pathPart);
        if (childNode == null) {
          if (!isGosuIdentifier(pathPart)) {
            break; // don't consider the whole property
          }
          childNode = new PropertyNode(currentNode, pathPart, propertySet);
          currentNode.addChild(childNode);
        }
        currentNode = childNode;
      }
    }

    root.removeUseless();
    return root;
  }

  private PropertyNode(PropertyNode parent, String name, PropertySet propertySet) {
    _name = name;
    _parent = parent;
    _path = parent != null ? join(parent.getPath(), name) : name;
    _propertySet = propertySet;
  }

  /**
   * The full property name, for example a.b
   * @return a non null name, which must be one or more valid Gosu identifiers separated by periods
   */
  public String getFullName() {
    return join(_propertySet.getName(), _path);
  }

  /**
   * The last part of the property name, for example b if the full name is a.b
   * @return a non null name, which must be a valid Gosu identifier
   */
  public String getRelativeName() {
    return GosuClassUtil.getShortClassName(getFullName());
  }

  /**
   * Return the name that should be used for the type based on this property node
   * @return a non null type name
   */
  public String getTypeName() {
    return isRoot() ? getFullName() : getFullName();
  }

  /**
   * Return the intrinsic type based on this property node
   * @return intrinsic type
   */
  @Override
  public IType getIntrinsicType() {
    return TypeSystem.getByFullName(getTypeName());
  }

  /**
   * Does this property node have a value in the underlying {@link PropertySet}
   * @return true if the node has an underlying value, false otherwise
   */
  public boolean hasValue() {
    return _propertySet.getKeys().contains(_path);
  }

  /**
   * Return the value for this property as given by the underlying {@link PropertySet}
   * @return the property value, or null if it doesn't have one
   */
  public String getValue() {
    return hasValue() ? _propertySet.getValue(_path) : null;
  }

  /**
   * Is this a leaf node - that is, does it have no children?
   * @return true if this node has no children, false otherwise
   */
  public boolean isLeaf() {
    return _children.isEmpty();
  }

  /**
   * Is this the root of a property node tree?
   * @return true if this is the root, false otherwise
   */
  public boolean isRoot() {
    return _parent == null;
  }

  /**
   * The direct children of this property node
   * @return a non null, though possibly empty, list of children
   */
  public List<PropertyNode> getChildren() {
    return new ArrayList<PropertyNode>(_children.values()); // for API backward compatibility returns list
  }

  /**
   * Return the value for the named child property; this is just like doing lookup on the underlying
   * {@link PropertySet} except that the name is prefixed with the full name of this property. For
   * example if this property is a then getting the child value b.c will return the value of a.b.c
   * in the original property set
   * @param name non null name of child property
   * @return the child property value, or null if there is no such child property
   */
  public String getChildValue(String name) {
    return _propertySet.getValue(join(_path, name));
  }

  /**
   * If this node has a property value, returns the value of that property. Otherwise returns
   * a string describing the property name.
   */
  @Override
  public String toString() {
    return hasValue() ? getValue() : String.format("Property <%s>", _path);
  }

  // returns child node if such one with the specified name exists, otherwise <code>null</code>
  private PropertyNode getChild(String name) {
    return _children.get(name);
  }

  private void addChild(PropertyNode node) {
    _children.put(node.getName(), node);
  }

  private boolean isUseless() {
    return _children.isEmpty() && !hasValue();
  }

  // removes all useless nodes in the tree represented by this node as a root
  private void removeUseless() {
    Set<Map.Entry<String, PropertyNode>> entries = _children.entrySet();
    for (Iterator<Map.Entry<String, PropertyNode>> it = entries.iterator(); it.hasNext(); ) {
      Map.Entry<String, PropertyNode> entry = it.next();
      PropertyNode child = entry.getValue();
      child.removeUseless();
      if (child.isUseless()) {
        it.remove();
      }
    }
  }

  static boolean isGosuIdentifier(String name) {
    boolean result = name.length() > 0 && isGosuIdentifierStart(name.charAt(0));
    for (int i = 1; i < name.length() && result; i++) {
      if (!isGosuIdentifierPart(name.charAt(i))) {
        result = false;
      }
    }
    return result;
  }

  private static boolean isGosuIdentifierStart(char ch) {
    return Character.isJavaIdentifierStart(ch) && ch != '$';
  }

  private static boolean isGosuIdentifierPart(char ch) {
    return Character.isJavaIdentifierPart(ch) && ch != '$';
  }

  private static String join(String head, String tail) {
    if (head.isEmpty()) {
      return tail;
    } else if (tail.isEmpty()) {
      return head;
    } else {
      return head + "." + tail;
    }
  }

  private String getName() {
    return _name;
  }

  public String getPath() {
    return _path;
  }

  public PropertyNode getParent() {
    return _parent;
  }
}
