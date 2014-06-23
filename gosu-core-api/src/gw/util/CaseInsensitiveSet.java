/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveSet<T extends CharSequence> extends AbstractSet<T> implements Set<T>, Serializable {
  private final Map<T, Object> _map;
  private static final Object MARKER = new Serializable() { };

  public CaseInsensitiveSet() {
    _map = new CiHashMap<T, Object>();
  }

  public CaseInsensitiveSet(Collection<? extends T> c) {
    this();
    addAll(c);
  }

  public CaseInsensitiveSet(T[] stringArray) {
    this(Arrays.asList(stringArray));
  }

  public CaseInsensitiveSet(int initialCapacity) {
    _map = new CiHashMap<T, Object>(initialCapacity);
  }

  public int size() {
    return _map.size();
  }

  public boolean isEmpty() {
    return _map.isEmpty();
  }

  public boolean contains(Object o) {
    //noinspection SuspiciousMethodCalls
    return _map.containsKey(o);
  }

  public Iterator<T> iterator() {
    return _map.keySet().iterator();
  }

  public boolean add(T o) {
    return _map.put(o, MARKER) == null;
  }

  public boolean remove(Object o) {
    return _map.remove(o) == MARKER;
  }

  public void clear() {
    _map.clear();
  }
}
