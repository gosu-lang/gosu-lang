/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings({"unchecked"})
public class PropertyNameMap<T extends CharSequence> implements Map<T, IPropertyInfo> {
  private List _unmodList;
  private Map<T,IPropertyInfo> _map;

  public PropertyNameMap() {
    _map = new LinkedHashMap();
  }

  public PropertyNameMap(int size) {
    _map = new LinkedHashMap(size);
  }

  public void freeze() {
    if (_map.size() == 0) {
      _map = Collections.emptyMap();
      _unmodList = Collections.emptyList();
    } else {
      // Do the array list first so it stays ordered!
      ArrayList arrayList = new ArrayList(_map.values());
      Map<T, IPropertyInfo> newMap = new HashMap<T, IPropertyInfo>(_map.size());
      newMap.putAll(_map);
      _map = newMap;
      arrayList.trimToSize();
      _unmodList = Collections.unmodifiableList(arrayList);
    }
  }

  public int size() {
    return _map.size();
  }

  public boolean isEmpty() {
    return _map.isEmpty();
  }

  public boolean containsKey(Object key) {
    return _map.containsKey(key);
  }

  public boolean containsValue(Object value) {
    return _map.containsValue(value);
  }

  public IPropertyInfo get(Object key) {
    return _map.get(key);
  }

  public IPropertyInfo put(T key, IPropertyInfo value) {
    return _map.put(key, value);
  }

  public IPropertyInfo remove(Object key) {
    return _map.remove(key);
  }

  public void putAll(Map<? extends T, ? extends IPropertyInfo> t) {
    _map.putAll(t);
  }

  public void clear() {
    _map.clear();
  }

  public Set<T> keySet() {
    return _map.keySet();
  }

  public List<IPropertyInfo> values() {
    return _unmodList == null ? new ArrayList(_map.values()) : _unmodList;
  }

  public Set<Entry<T, IPropertyInfo>> entrySet() {
    return _map.entrySet();
  }
}
