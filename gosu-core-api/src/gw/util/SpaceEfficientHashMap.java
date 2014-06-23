/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.AbstractMap;

@SuppressWarnings({"unchecked"})
public class SpaceEfficientHashMap<K, V> implements Map<K, V> {
  private static final Object EMPTY_KEY = new Object();

  private Object _keys = EMPTY_KEY;
  private Object _values;
  private static final int MAX_LIST_SIZE = 16;

  /**
   * The number of times this HashMap has been structurally modified
   * Structural modifications are those that change the number of mappings in
   * the HashMap or otherwise modify its internal structure (e.g.,
   * rehash).  This field is used to make iterators on Collection-views of
   * the HashMap fail-fast.  (See ConcurrentModificationException).
   */
  transient volatile int modCount;

  public SpaceEfficientHashMap() {
  }

  /**
   * Constructs a new <tt>HashMap</tt> with the same mappings as the
   * specified <tt>Map</tt>.  The <tt>HashMap</tt> is created with
   * default load factor (0.75) and an initial capacity sufficient to
   * hold the mappings in the specified <tt>Map</tt>.
   *
   * @param m the map whose mappings are to be placed in this map
   * @throws NullPointerException if the specified map is null
   */
  public SpaceEfficientHashMap(Map<? extends K, ? extends V> m) {
    putAll(m);
  }

  @Override
  public int size() {
    if (isObject()) {
      return _keys == EMPTY_KEY ? 0 : 1;
    } else if (isList()) {
      return ((List) _keys).size();
    }
    return ((Map) _keys).size();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    if (isMap()) {
      return ((Map) _keys).containsKey(key);
    } else if (isList()) {
      List keyList = (List) _keys;
      return keyList.contains(key);
    } else {
      return GosuObjectUtil.equals(_keys, key);
    }
  }

  @Override
  public boolean containsValue(Object value) {
    if (isMap()) {
      return ((Map) _keys).containsValue(value);
    } else if (isList()) {
      List valueList = (List) _values;
      return valueList.contains(value);
    } else {
      return _keys != EMPTY_KEY && GosuObjectUtil.equals(_values, value);
    }
  }

  @Override
  public V get(Object key) {
    if (isMap()) {
      return (V) ((Map) _keys).get(key);
    } else if (isList()) {
      List keyList = (List) _keys;
      for (int i = 0; i < keyList.size(); i++) {
        Object o = keyList.get(i);
        if (GosuObjectUtil.equals(o, key)) {
          return (V) ((List) _values).get(i);
        }
      }

      return null;
    } else {
      return _keys != EMPTY_KEY && GosuObjectUtil.equals(_keys, key) ? (V) _values : null;
    }
  }

  @Override
  public V put(K key, V value) {
    if (isMap()) {
      return (V) ((Map) _keys).put(key, value);
    } else if (isList()) {
      ArrayList keyList = (ArrayList) _keys;
      ArrayList valueList = (ArrayList) _values;
      for (int i = 0; i < keyList.size(); i++) {
        Object o = keyList.get(i);
        if (GosuObjectUtil.equals(o, key)) {
          V oldValue = (V) valueList.get(i);
          valueList.set(i, value);
          return oldValue;
        }
      }

      if (keyList.size() + 1 > MAX_LIST_SIZE) {
        Map map = new HashMap(keyList.size() + 1);
        for (int i = 0; i < keyList.size(); i++) {
          map.put(keyList.get(i), valueList.get(i));
        }
        map.put(key, value);
        _keys = map;
        _values = null;
      } else {
        keyList.add(key);
        valueList.add(value);
      }
      return null;
    } else {
      if (GosuObjectUtil.equals(_keys, key)) {
        V oldValue = (V) _values;
        _values = value;
        return oldValue;
      } else if (_keys == EMPTY_KEY) {
        _keys = key;
        _values = value;
        return null;
      } else {
        List keyList = new ArrayList(2);
        keyList.add(_keys);
        keyList.add(key);
        List valueList = new ArrayList(2);
        valueList.add(_values);
        valueList.add(value);
        _keys = keyList;
        _values = valueList;

        return null;
      }
    }
  }

  @Override
  public V remove(Object key) {
    if (isMap()) {
      return (V) ((Map) _keys).remove(key);
    } else if (isList()) {
      ArrayList keyList = (ArrayList) _keys;
      ArrayList valueList = (ArrayList) _values;
      for (int i = 0; i < keyList.size(); i++) {
        Object o = keyList.get(i);
        if (GosuObjectUtil.equals(o, key)) {
          V oldValue = (V) valueList.get(i);
          // The next two lines are expensive...
          keyList.remove(i);
          valueList.remove(i);

          return oldValue;
        }
      }
      return null;
    } else {
      if (GosuObjectUtil.equals(_keys, key)) {
        V oldValue = (V) _values;
        _values = null;
        _keys = EMPTY_KEY;
        return oldValue;
      } else {
        return null;
      }
    }
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    if (m.isEmpty()) {
      return;
    }
    if (isMap()) {
      ((Map) _keys).putAll(m);
    } else {
      if (m.size() < 10) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
          put(entry.getKey(), entry.getValue());
        }
      } else {
        Map map = new HashMap();
        if (isList()) {
          ArrayList keyList = (ArrayList) _keys;
          ArrayList valueList = (ArrayList) _values;
          for (int i = 0; i < keyList.size(); i++) {
            map.put(keyList.get(i), valueList.get(i));
          }
        } else if (_keys != EMPTY_KEY) {
          map.put(_keys, _values);
        }
        map.putAll(m);
        _keys = map;
        _values = null;
        trimToSize();
      }
    }
  }

  @Override
  public void clear() {
    _keys = EMPTY_KEY;
    _values = null;
  }

  @Override
  public Set<K> keySet() {
    if (isMap()) {
      return ((Map) _keys).keySet();
    } else {
      return new MyListSet<K>(true);
    }
  }

  @Override
  public Collection<V> values() {
    if (isMap()) {
      return ((Map) _keys).values();
    } else {
      return new MyListSet<V>(false);
    }
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    if (isMap()) {
      return ((Map) _keys).entrySet();
    } else {
      return new MyEntrySet<K, V>();
    }
  }

  /**
   * Compares the specified object with this map for equality.  Returns
   * <tt>true</tt> if the given object is also a map and the two maps
   * represent the same mappings.  More formally, two maps <tt>m1</tt> and
   * <tt>m2</tt> represent the same mappings if
   * <tt>m1.entrySet().equals(m2.entrySet())</tt>.  This ensures that the
   * <tt>equals</tt> method works properly across different implementations
   * of the <tt>Map</tt> interface.
   * <p/>
   * <p>This implementation first checks if the specified object is this map;
   * if so it returns <tt>true</tt>.  Then, it checks if the specified
   * object is a map whose size is identical to the size of this map; if
   * not, it returns <tt>false</tt>.  If so, it iterates over this map's
   * <tt>entrySet</tt> collection, and checks that the specified map
   * contains each mapping that this map contains.  If the specified map
   * fails to contain such a mapping, <tt>false</tt> is returned.  If the
   * iteration completes, <tt>true</tt> is returned.
   *
   * @param o object to be compared for equality with this map
   * @return <tt>true</tt> if the specified object is equal to this map
   */
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Map)) {
      return false;
    }
    Map<K, V> m = (Map<K, V>) o;
    if (m.size() != size()) {
      return false;
    }

    try {
      Iterator<Entry<K, V>> i = entrySet().iterator();
      while (i.hasNext()) {
        Entry<K, V> e = i.next();
        K key = e.getKey();
        V value = e.getValue();
        if (value == null) {
          if (!(m.get(key) == null && m.containsKey(key))) {
            return false;
          }
        } else {
          if (!value.equals(m.get(key))) {
            return false;
          }
        }
      }
    } catch (ClassCastException unused) {
      return false;
    } catch (NullPointerException unused) {
      return false;
    }

    return true;
  }

  /**
   * Returns the hash code value for this map.  The hash code of a map is
   * defined to be the sum of the hash codes of each entry in the map's
   * <tt>entrySet()</tt> view.  This ensures that <tt>m1.equals(m2)</tt>
   * implies that <tt>m1.hashCode()==m2.hashCode()</tt> for any two maps
   * <tt>m1</tt> and <tt>m2</tt>, as required by the general contract of
   * {@link Object#hashCode}.
   * <p/>
   * <p>This implementation iterates over <tt>entrySet()</tt>, calling
   * {@link Map.Entry#hashCode hashCode()} on each element (entry) in the
   * set, and adding up the results.
   *
   * @return the hash code value for this map
   * @see Map.Entry#hashCode()
   * @see Object#equals(Object)
   * @see Set#equals(Object)
   */
  public int hashCode() {
    int h = 0;
    Iterator<Entry<K, V>> i = entrySet().iterator();
    while (i.hasNext()) {
      h += i.next().hashCode();
    }
    return h;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append("{");
    int i = 0;
    for (Entry<K, V> entry : entrySet()) {
      if (i > 0) {
        buf.append(", ");
      }
      buf.append(entry.getKey()).append("=").append(entry.getValue());
      i++;
    }
    buf.append("}");
    return buf.toString();
  }

  private boolean isObject() {
    return !isList() && !isMap();
  }

  private boolean isList() {
    return _keys instanceof List;
  }

  private boolean isMap() {
    return _keys instanceof Map;
  }

  public void trimToSize() {
    if (isList()) {
      ArrayList keyList = (ArrayList) _keys;
      ArrayList valueList = (ArrayList) _values;
      if (keyList.size() == 0) {
        _keys = EMPTY_KEY;
        _values = null;
      } else if (keyList.size() == 1) {
        _keys = keyList.get(0);
        _values = valueList.get(0);
      } else {
        keyList.trimToSize();
        valueList.trimToSize();
      }
    } else if (isMap()) {
      Map map = (Map) _keys;
      if (map.size() == 0) {
        // compact to object
        _keys = EMPTY_KEY;
        _values = null;
      } else if (map.size() == 1) {
        Map.Entry entry = (Entry) map.entrySet().iterator().next();
        _keys = entry.getKey();
        _values = entry.getValue();
      } else if (map.size() <= MAX_LIST_SIZE) {
        ArrayList keyList = new ArrayList(map.size());
        ArrayList valueList = new ArrayList(map.size());
        for (Object o : map.entrySet()) {
          Map.Entry entry = (Entry) o;
          keyList.add(entry.getKey());
          valueList.add(entry.getValue());
        }
        keyList.trimToSize();
        valueList.trimToSize();
        _keys = keyList;
        _values = valueList;
      } else {
        // can't compact a map.  Too bad, so sad.  Or we could reallocate the map...
        _keys = new HashMap(map);
      }
    }
  }

  private final class MyListSet<T> extends AbstractSet<T> {
    boolean _returnKeySet;

    MyListSet(boolean returnKeySet) {
      _returnKeySet = returnKeySet;
    }

    public Iterator<T> iterator() {
      if (isList()) {
        return new Iterator<T>() {
          private Iterator _kit = ((List) _keys).iterator();
          private Iterator _vit = ((List) _values).iterator();

          @Override
          public boolean hasNext() {
            return _kit.hasNext();
          }

          @Override
          public T next() {
            Object key = _kit.next();
            Object value = _vit.next();
            return (T) (_returnKeySet ? key : value);
          }

          @Override
          public void remove() {
            _kit.remove();
            _vit.remove();
          }
        };
      }

      if (_keys == EMPTY_KEY && _values == null) {
        return new Iterator<T>() {
          @Override
          public boolean hasNext() {
            return false;
          }

          @Override
          public T next() {
            throw new NoSuchElementException();
          }

          @Override
          public void remove() {
            throw new NoSuchElementException();
          }
        };
      } else {
        return new Iterator<T>() {
          boolean _onFirst = true;

          @Override
          public boolean hasNext() {
            return _onFirst;
          }

          @Override
          public T next() {
            if (!_onFirst) {
              throw new NoSuchElementException();
            }
            _onFirst = false;
            if (_returnKeySet) {
              return (T) _keys;
            } else {
              return (T) _values;
            }
          }

          @Override
          public void remove() {
            if (_onFirst) {
              throw new IllegalStateException();
            }
            _keys = EMPTY_KEY;
            _values = null;
          }
        };
      }
    }

    public int size() {
      return isList() ? ((List) _keys).size() : (_keys == EMPTY_KEY) ? 0 : 1;
    }

    public boolean contains(Object o) {
      if (_returnKeySet) {
        return containsKey(o);
      } else {
        return containsValue(o);
      }
    }

    public boolean remove(Object o) {
      if (_returnKeySet) {
        return SpaceEfficientHashMap.this.remove(o) != null;
      } else {
        Iterator<T> e = iterator();
        if (o == null) {
          while (e.hasNext()) {
            if (e.next() == null) {
              e.remove();
              return true;
            }
          }
        } else {
          while (e.hasNext()) {
            if (o.equals(e.next())) {
              e.remove();
              return true;
            }
          }
        }
        return false;
      }
    }

    public void clear() {
      SpaceEfficientHashMap.this.clear();
    }
  }

  private final class MyEntrySet<S, T> extends AbstractSet<Map.Entry<S, T>> {
    MyEntrySet() {
    }

    public Iterator<Map.Entry<S, T>> iterator() {
      if (isList()) {
        return new Iterator<Entry<S, T>>() {
          private Iterator _kit = ((List) _keys).iterator();
          private Iterator _vit = ((List) _values).iterator();

          @Override
          public boolean hasNext() {
            return _kit.hasNext();
          }

          @Override
          public Entry<S, T> next() {
            Object key = _kit.next();
            Object value = _vit.next();
            return new AbstractMap.SimpleEntry<S, T>((S) key, (T) value);
          }

          @Override
          public void remove() {
            _kit.remove();
            _vit.remove();
          }
        };
      }

      if (_keys == EMPTY_KEY) {
        return new Iterator<Map.Entry<S, T>>() {
          @Override
          public boolean hasNext() {
            return false;
          }

          @Override
          public Entry<S, T> next() {
            throw new NoSuchElementException();
          }

          @Override
          public void remove() {
            throw new NoSuchElementException();
          }
        };
      } else {
        return new Iterator<Map.Entry<S, T>>() {
          boolean _onFirst = true;

          @Override
          public boolean hasNext() {
            return _onFirst;
          }

          @Override
          public Entry<S, T> next() {
            if (!_onFirst) {
              throw new NoSuchElementException();
            }
            _onFirst = false;
            return new AbstractMap.SimpleEntry<S, T>((S) _keys, (T) _values);
          }

          @Override
          public void remove() {
            _keys = null;
            _values = null;
          }
        };
      }
    }

    public int size() {
      return isList() ? ((List) _keys).size() : (_keys == EMPTY_KEY) ? 0 : 1;
    }

    public boolean contains(Object o) {
      return containsKey(o);
    }

    public boolean remove(Object o) {
      return SpaceEfficientHashMap.this.remove(o) != null;
    }

    public void clear() {
      SpaceEfficientHashMap.this.clear();
    }
  }
}
