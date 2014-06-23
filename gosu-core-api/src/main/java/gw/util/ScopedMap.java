/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.util.*;

public class ScopedMap<K,V> extends AbstractMap<K,V> {

  private final ScopedMap<K,V> _parent;
  private Map<K,V> _overlayMap = Collections.emptyMap();

  public ScopedMap() {
    _parent = null;
  }

  private ScopedMap(ScopedMap<K,V> parent) {
    _parent = parent;
  }

  public int size() {
    return getEntryMap().size();
  }

  public boolean isEmpty() {
    return getEntryMap().isEmpty();
  }

  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  public boolean containsValue(Object value) {
    return getEntryMap().containsValue( value );
  }

  public V get(Object key) {
    V value = _overlayMap.get(key);
    if (value != null) {
      return value;
    }
    //noinspection SuspiciousMethodCalls
    if (_overlayMap.containsKey(key)) {
      return null;
    }
    return _parent == null ? null : _parent.get(key);
  }

  public V put(K key, V value) {
    initOverlayMap();
    V oldValue = get(key);
    _overlayMap.put(key, value);
    return oldValue;
  }

  public V remove(Object key) {
    initOverlayMap();
    V oldValue = get(key);
    //noinspection unchecked
    _overlayMap.put((K) key, null);
    return oldValue;
  }

  public void putAll(Map<? extends K, ? extends V> t) {
    for (Entry<? extends K, ? extends V> entry : t.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  public ScopedMap<K,V> pushScope() {
    if( !_overlayMap.isEmpty() ) {
      ((SpaceEfficientHashMap<K,V>)_overlayMap).trimToSize();
    }
    return new ScopedMap<K,V>(this);
  }

  public ScopedMap<K,V> popScope() {
    return _parent;
  }

  public void clear() {
    throw new UnsupportedOperationException();
  }

  public Set<K> keySet() {
    return getEntryMap().keySet();
  }

  public Collection<V> values() {
    return getEntryMap().values();
  }

  public Set<Entry<K, V>> entrySet() {
    return getEntryMap().entrySet();
  }

  private Map<K, V> getEntryMap() {
    Map<K,V> results = new HashMap<K,V>();
    getEntryMap( results );
    return results;
  }

  private void getEntryMap( Map<K, V> results ) {
    if ( _parent != null ) {
      _parent.getEntryMap( results );
    }
    initOverlayMap();
    results.putAll( _overlayMap );
  }

  public boolean containsKeyDirect( K key ) {
    return _overlayMap.containsKey( key );
  }

  public V getDirect( K key ) {
    return _overlayMap.get( key );
  }

  private void initOverlayMap() {
    if( _overlayMap == Collections.emptyMap() ) {
      _overlayMap = new SpaceEfficientHashMap<K,V>();
    }
  }
}
