/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;


import java.util.Collection;
import java.util.HashSet;

public class PredicatedSet<E> extends HashSet<E> {
  private final Predicate _predicate;

  public PredicatedSet(int initialCapacity, Predicate predicate) {
    super(initialCapacity);
    _predicate = predicate;
  }

  public PredicatedSet(Predicate predicate) {
    _predicate = predicate;
  }

  public boolean add(E o) {
    if (_predicate.evaluate(o)) {
      return super.add(o);
    }
    return false;
  }

  public boolean addAll(Collection<? extends E> c) {
    boolean changed = false;
    for(E e: c) {
      if (_predicate.evaluate(e)) {
        if (super.add(e)) {
          changed = true;
        }
      }
    }
    return changed;
  }
}
