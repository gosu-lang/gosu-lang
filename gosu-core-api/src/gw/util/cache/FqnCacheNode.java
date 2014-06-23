/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.cache;

import gw.util.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FqnCacheNode<K> {
  private final String name;
  private final FqnCacheNode<K> parent;
  private K userData;
  private Map<String, FqnCacheNode<K>> children;

  public FqnCacheNode( String text, FqnCacheNode<K> parent ) {
    this.name = text;
    this.parent = parent;
  }

  public final FqnCacheNode<K> getChild( String segment ) {
    if (children != null) {
      return children.get(segment);
    } else {
      return null;
    }
  }

  public final void clear() {
    children = null;
  }

  public FqnCacheNode<K> getOrCreateChild( String segment ) {
    if (children == null) {
      children = new ConcurrentHashMap<String, FqnCacheNode<K>>(2);
    }
    FqnCacheNode<K> node = children.get(segment);
    if (node == null) {
      node = new FqnCacheNode<K>(segment, this);
      children.put(segment, node);
    }
    return node;
  }

  public final void delete() {
    parent.deleteChild(this);
  }

  private void deleteChild(FqnCacheNode<K> child) {
    if (children != null) {
      children.remove(child.name);
      if (children.isEmpty()) {
        children = null;
      }
    }
  }

  public final K getUserData() {
    return userData;
  }

  public final void setUserData( K userData ) {
    this.userData = userData;
  }

  public final boolean isLeaf() {
    return children == null || children.isEmpty();
  }

  public void collectNames( Set<String> names, String s ) {
    if (children != null) {
      for (FqnCacheNode<K> child : children.values()) {
        String path = s.length() == 0
                      ? child.name
                      : s + child.separator() + child.name;
        if (child.isLeaf()) {
          names.add(path);
        } else {
          child.collectNames(names, path);
        }
      }
    }
  }

  public final Collection<FqnCacheNode<K>> getChildren() {
    if (children != null) {
      return children.values();
    } else {
      return Collections.emptySet();
    }
  }

  public final boolean visitDepthFirst( Predicate<K> visitor ) {
    if (children != null) {
      List<FqnCacheNode<K>> copy = new ArrayList<FqnCacheNode<K>>( children.values() );
      for( FqnCacheNode<K> child: copy ) {
        if( !child.visitDepthFirst( visitor ) ) {
          return false;
        }
      }
    }
    return visitor.evaluate( getUserData() );
  }

  public final boolean visitNodeDepthFirst( Predicate<FqnCacheNode> visitor ) {
    if (children != null) {
      List<FqnCacheNode<K>> copy = new ArrayList<FqnCacheNode<K>>( children.values() );
      for( FqnCacheNode<K> child: copy ) {
        if( !child.visitNodeDepthFirst( visitor ) ) {
          return false;
        }
      }
    }
    return visitor.evaluate( this );
  }

  public final boolean visitBreadthFirst( Predicate<K> visitor ) {
    if( !visitor.evaluate( getUserData() ) ) {
      return false;
    }
    if (children != null) {
      List<FqnCacheNode<K>> copy = new ArrayList<FqnCacheNode<K>>( children.values() );
      for( FqnCacheNode<K> child: copy ) {
        child.visitBreadthFirst( visitor );
      }
    }
    return true;
  }

  public final boolean visitNodeBreadthFirst( Predicate<FqnCacheNode> visitor ) {
    if( !visitor.evaluate( this ) ) {
      return false;
    }
    if( children != null ) {
      List<FqnCacheNode<K>> copy = new ArrayList<FqnCacheNode<K>>( children.values() );
      for( FqnCacheNode<K> child: copy ) {
        child.visitNodeBreadthFirst( visitor );
      }
    }
    return true;
  }

  public final String getName() {
    return name;
  }

  public final String getFqn() {
    StringBuilder sb = new StringBuilder();
    FqnCacheNode<K> node = this;
    while( node.parent != null ) {
        String str = node.name + (sb.length() == 0 ? "" : separator());
        sb.insert( 0, str);
        node = node.parent;
    }
    return sb.toString();
  }

  private String separator() {
      char c = name.charAt(0);
      return c == '[' || c == '<'? "" : ".";
  }

  @Override
  public String toString() {
    return name;
  }
}
