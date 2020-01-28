/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.cache;

import gw.util.Predicate;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 */
public class WeakFqnCache<T> implements IFqnCache<T> {
  private FqnCache<Reference<T>> _cache;

  public WeakFqnCache() {
    _cache = new FqnCache<>();
  }

  @Override
  public void add( String fqn ) {
    add( fqn, null );
  }

  @Override
  public void add( String fqn, T userData ) {
    Reference<T> ref = new SoftReference<>( userData );
    _cache.add( fqn, ref );
  }

  @Override
  public boolean remove( String fqn ) {
    return _remove( fqn );
  }
  private boolean _remove( String fqn ) {
    return _cache.remove( fqn );
  }

  @Override
  public T get( String fqn ) {
    Reference<T> ref = _cache.get( fqn );
    return ref == null ? null : ref.get();
  }

  @Override
  public FqnCacheNode<Reference<T>> getNode( String fqn ) {
    return _cache.getNode( fqn );
  }

  @Override
  public boolean contains( String fqn ) {
    return _cache.contains( fqn );
  }

  @Override
  public void remove( String[] fqns ) {
//    removeReleasedEntries();
    _cache.remove( fqns );
  }

  @Override
  public void clear() {
    _cache.clear();
  }

  @Override
  public Set<String> getFqns() {
    return _cache.getFqns();
  }

  @Override
  public void visitDepthFirst( final Predicate<T> visitor ) {
    Predicate<Reference<T>> delegate = node -> {
      T userData = node == null ? null : node.get();
      return visitor.evaluate( userData );
    };
    List<FqnCacheNode<Reference<T>>> copy = new ArrayList<>( _cache.getRoot().getChildren() );
    for( FqnCacheNode<Reference<T>> child: copy ) {
      if( !child.visitDepthFirst( delegate ) ) {
        return;
      }
    }
  }

  public void visitNodeDepthFirst( final Predicate<FqnCacheNode> visitor ) {
    List<FqnCacheNode<Reference<T>>> copy = new ArrayList<>( _cache.getRoot().getChildren() );
    for( FqnCacheNode<Reference<T>> child: copy ) {
      if( !child.visitNodeDepthFirst( visitor ) ) {
        return;
      }
    }
  }

  @Override
  public void visitBreadthFirst( final Predicate<T> visitor ) {
    Predicate<Reference<T>> delegate = node -> {
      T userData = node == null ? null : node.get();
      return visitor.evaluate( userData );
    };
    List<FqnCacheNode<Reference<T>>> copy = new ArrayList<>( _cache.getRoot().getChildren() );
    for( FqnCacheNode<Reference<T>> child: copy ) {
      child.visitBreadthFirst( delegate );
    }
  }
}
