/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.google.common.base.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author bchang
 */
public class UnidirectionalCyclicGraph<E> {

  private final Map<String, Node<E>> _nodes = new HashMap<>();

  public E get(@NotNull String id) {
    return getNode(id).getValue();
  }

  @Nullable
  protected Node<E> getNode(@NotNull String id) {
    if (id.contains("/")) {
      // super hacky way for determining if it's a Carbon-style notation for a jar dependency, which we no longer have in Diamond
      // the problem is that we use ModuleGraph to parse Carbon modules.xml files, which have a slightly different schema
      return null;
    }
    Node<E> node = _nodes.get(id);
    if (node == null) {
      throw new IllegalArgumentException("could not find graph node for id \"" + id + "\"");
    }
    return node;
  }

  public void registerNode(String id, Node<E> node) {
    if (_nodes.containsKey(id)) {
      throw new IllegalArgumentException("graph already contains an item with id \"" + id + "\"");
    }
    _nodes.put(id, node);
  }

  public void resolveLinks() {
    for (Node<E> node : _nodes.values()) {
      for (String linkId : node.getLinks()) {
        if (linkId.contains("/")) {
          // super hacky way for determining if it's a Carbon-style notation for a jar dependency, which we no longer have in Diamond
          // the problem is that we use ModuleGraph to parse Carbon modules.xml files, which have a slightly different schema
          continue;
        }
        if (!_nodes.containsKey(linkId)) {
          throw new IllegalStateException("node \"" + node.getId() + "\" contains a link with ID \"" + linkId + "\" which couldn't be found");
        }
      }
    }

    for (Node<E> node : _nodes.values()) {
      searchDeepForCircularPath(node, node, new HashSet<Node<E>>(), new ArrayList<Node<E>>());
    }
  }

  @NotNull
  public LinkedHashSet<Node<E>> buildGraphPath(@NotNull String id) {
    return buildGraphPathImpl(id, null, null);
  }

  @NotNull
  public LinkedHashSet<Node<E>> buildGraphPath(@NotNull String id, NodeLinkPredicate<E> predicate) {
    return buildGraphPathImpl(id, predicate, null);
  }

  @NotNull
  private LinkedHashSet<Node<E>> buildGraphPathImpl(@NotNull String id, @Nullable NodeLinkPredicate<E> predicate, @Nullable String avoidId) {
    LinkedHashSet<Node<E>> list = new LinkedHashSet<>();
    Node<E> node = getNode(id);
    if (node != null) {
      for (String linkId : node.getLinks()) {
        if (predicate == null || predicate.acceptLink(node, getNode(linkId))) {
          // we check for avoidId in order to avoid revisiting this node while visiting a cross dependency
          if (avoidId == null || !avoidId.equals(linkId)) {
            LinkedHashSet<Node<E>> linkList = buildGraphPathImpl(linkId, predicate, id);
            list.addAll(linkList);
          }
        }
      }
      list.add(node);
    }
    return list;
  }

  public Collection<Node<E>> getAll() {
    return _nodes.values();
  }

  private void searchDeepForCircularPath(@NotNull Node<E> node, Node<E> badNode, @NotNull Set<Node<E>> visited, @NotNull List<Node<E>> path) {
    path.add(node);
    for (String linkId : node.getLinks()) {
      Node<E> linkNode = getNode(linkId);
      if (linkNode == null) {
        continue;
      }
      if (!visited.contains(linkNode)) {
        if (linkNode.equals(badNode)) {
          path.add(linkNode);
          throw new IllegalStateException(Joiner.on(" \u2192 ").join(path));
        }
        visited.add(linkNode);
        searchDeepForCircularPath(linkNode, badNode, visited, path);
      }
    }
    path.remove(path.size() - 1);
  }

  public static interface NodeLinkPredicate<E> {
    boolean acceptLink( Node<E> node, Node<E> linkNode );
  }

  public static class Node<E> implements Comparable<Node<E>> {
    private final String _id;
    private final E _value;
    private final TreeSet<String> _links = new TreeSet<>();

    public Node(String id, E value) {
      _id = id;
      _value = value;
    }

    public String getId() {
      return _id;
    }

    public E getValue() {
      return _value;
    }

    public void addLink(String linkId) {
      if (_id.equals(linkId)) {
        throw new IllegalArgumentException("a node cannot link to itself");
      }
      _links.add(linkId);
    }

    @NotNull
    public SortedSet<String> getLinks() {
      return _links;
    }

    public int hashCode() {
      return _id.hashCode();
    }

    public boolean equals(@Nullable Object o) {
      return o != null && (o == this || o instanceof Node && _id.equals(((Node) o)._id));
    }

    public int compareTo(@NotNull Node<E> that) {
      return _id.compareTo(that._id);
    }

    public String toString() {
      return getId();
    }
  }
}
