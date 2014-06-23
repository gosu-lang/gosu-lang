/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 */
public abstract class DerivedKeyHashMap<K, V> implements Map<K, V> {

  private Object[] _table;
  private int _size;

  public DerivedKeyHashMap() {
    _table = new Object[16];
    _size = 0;
  }

  public DerivedKeyHashMap(Collection<V> values) {
    _table = new Object[(int) (values.size() / loadFactor())];
    _size = 0;
    for (V value : values) {
      V existingValue = putImpl(getKeyForValue(value), value, _table, false);
      if (existingValue != null) {
        _size++;
      }
    }
  }

  // ----------------- Methods that subclasses can or must override -----------------

  protected int hash(Object key) {
    return key.hashCode();
  }

  protected abstract boolean keyMatches(Object key, V value);

  protected abstract K getKeyForValue(V value);

  protected abstract double loadFactor();

  // ----------------- Map methods -----------------

  public int size() {
    return _size;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public boolean containsKey(Object key) {
    if (key == null) {
      throw new NullPointerException("containsKey on a DerivedKeyHashMap cannot be called with a null key");
    } else {
      return findValueWithMatchingKeyInChain(key, _table[bucket(key, _table.length)]) != null;
    }
  }

  public V get(Object key) {
    if (key == null) {
      throw new NullPointerException("get on a DerivedKeyHashMap cannot be called with a null key");
    } else {
      return findValueWithMatchingKeyInChain(key, _table[bucket(key, _table.length)]);
    }
  }

  @Override
  public boolean containsValue(Object value) {
    if (value == null) {
      throw new NullPointerException("containsValue on a DerivedKeyHashMap cannot be called with a null argument");
    }

    for (int i = 0; i < _table.length; i++) {
      if (hasMatchingValueInChain(value, _table[i])) {
        return true;
      }
    }

    return false;
  }

  @Override
  public V put(K key, V value) {
    if (value == null) {
      throw new IllegalArgumentException("DerivedKeyHashMaps cannot currently be used with null values");
    }

    if (!getKeyForValue(value).equals(key)) {
      throw new IllegalArgumentException("The derived key [" + getKeyForValue(value) + "] for value [" + value + "] does not match the supplied key [" + key + "]");
    }

    V existingValue = putImpl(key, value, _table, true);
    if (existingValue == null) {
      _size++;
    }
    return existingValue;
  }

  @Override
  public V remove(Object key) {
    if (key == null) {
      return null;
    }

    V priorValue = null;
    int bucket = bucket(key, _table.length);

    if (_table[bucket] == null) {
      // Nothing to do
    } else if (_table[bucket] instanceof ChainedEntry) {
      // The front of the bucket is a ChainedEntry, so we need to walk the chain and splice out the link
      ChainedEntry<V> previousLink = null;
      ChainedEntry<V> currentLink = (ChainedEntry<V>) _table[bucket];

      while (true) {
        if (keyMatches(key, currentLink._entry)) {
          // We have a match, so splice out the link
          priorValue = currentLink._entry;
          if (previousLink == null) {
            // There's no prior link, so pull the next object directly into the table
            _table[bucket] = currentLink._next;
          } else {
            // There's a prior link, so just splice out the current one
            previousLink._next = currentLink._next;
          }
          break;
        } else if (currentLink._next instanceof ChainedEntry) {
          // Just
          previousLink = currentLink;
          currentLink = (ChainedEntry<V>) currentLink._next;
        } else {
          // The next link in the chain is a direct entry.  If it matches the key we're looking
          // for, we need to remove the current link, as it's no longer necessary, and set
          // the _next pointer (if any) of the prior link to point directly to the current link's
          // entry
          if (keyMatches(key, (V) currentLink._next)) {
            priorValue = (V)currentLink._next;
            if (previousLink == null) {
              _table[bucket] = currentLink._entry;
            } else {
              previousLink._next = currentLink._entry;
            }
          }
          break;
        }
      }
    } else {
      // The entry is directly in _table, so just null it out
      priorValue = (V) _table[bucket];
      _table[bucket] = null;
    }

    if (priorValue != null) {
      _size--;
    }
    return priorValue;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    // We want to validate all the values/entries BEFORE we make any modifications to the map, for the sake of consistency
    for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
      if (entry.getKey() == null) {
        throw new IllegalArgumentException("Cannot add a value with a null key to a DerivedKeyHashMap");
      } else if (entry.getValue() == null) {
        throw new IllegalArgumentException("Cannot add a null value to a DerivedKeyHashMap");
      } else if (!getKeyForValue(entry.getValue()).equals(entry.getKey())) {
        throw new IllegalArgumentException("The derived key [" + getKeyForValue(entry.getValue()) + "] for value [" + entry.getValue() + "] does not match the supplied key [" + entry.getKey() + "]");
      }
    }

    for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
      V existingValue = putImpl(entry.getKey(), entry.getValue(), _table, false);
      if (existingValue == null) {
        _size++;
      }
    }
  }

  @Override
  public void clear() {
    _table = new Object[16];
    _size = 0;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return null;
  }

  public Set<K> keySet() {
    return new KeySet();
  }

  public Collection<V> values() {
    List<V> values = new ArrayList<V>();
    for (int i = 0; i < _table.length; i++) {
      if (_table[i] != null) {
        if (_table[i] instanceof ChainedEntry) {
          ChainedEntry<V> entry = (ChainedEntry) _table[i];
          values.add(entry._entry);
          while (entry._next instanceof ChainedEntry) {
            entry = (ChainedEntry<V>) entry._next;
            values.add(entry._entry);
          }
          values.add((V) entry._next);
        } else {
          values.add((V)_table[i]);
        }
      }
    }

    return values;
  }

  private V findValueWithMatchingKeyInChain(Object key, Object entry) {
    if (entry == null) {
      return null;
    } else if (entry instanceof ChainedEntry) {
      ChainedEntry<V> chainedEntry = (ChainedEntry<V>) entry;
      if (keyMatches(key, chainedEntry._entry)) {
        return chainedEntry._entry;
      } else {
        return findValueWithMatchingKeyInChain(key, chainedEntry._next);
      }
    } else {
      if (keyMatches(key, (V) entry)) {
        return (V) entry;
      } else {
        return null;
      }
    }
  }

  private boolean hasMatchingValueInChain(Object value, Object entry) {
    if (entry == null) {
      return false;
    } else if (entry instanceof ChainedEntry) {
      ChainedEntry<V> chainedEntry = (ChainedEntry<V>) entry;
      if (chainedEntry._entry.equals(value)) {
        return true;
      } else {
        return hasMatchingValueInChain(value, chainedEntry._next);
      }
    } else {
      return entry.equals(value);
    }
  }

  private int bucket(Object key, int tableLength) {
    // This assumes that the hash codes will be evenly distributed with respect to all bytes;
    // if the hash code doesn't have much entropy in the lower-order bits, this won't work out so well
    return Math.abs(hash(key) % (tableLength));
  }

  public V putImpl(K key, V value, Object[] table, boolean resizeIfNecessary) {
    if (key == null) {
      throw new IllegalArgumentException("Cannot add a value with a null key to a DerivedKeyHashMap");
    }

    if (value == null) {
      throw new IllegalArgumentException("Cannot add a null value to a DerivedKeyHashMap");
    }

    V priorValue = null;
    int bucket = bucket(key, table.length);
    if (table[bucket] == null) {
      table[bucket] = value;
    } else if (table[bucket] instanceof ChainedEntry) {
      // The front of the bucket is a ChainedEntry, so we need to walk the chain
      ChainedEntry<V> lastChain = (ChainedEntry<V>) table[bucket];
      while (true) {
        if (keyMatches(key, lastChain._entry)) {
          // If the entry of the chain directly matches our value's key, then update it in place
          priorValue = lastChain._entry;
          lastChain._entry = value;
          break;
        } else if (lastChain._next instanceof ChainedEntry) {
          // If the entry doesn't match and the next object is another chain entry, keep walking the chain
          lastChain = (ChainedEntry<V>) lastChain._next;
        } else {
          // If the next link in the chain isn't another chain link, either update the value pointer directly,
          // if it matches this value's key, or
          if (keyMatches(key, (V) lastChain._next)) {
            priorValue = (V) lastChain._next;
            lastChain._next = value;
          } else {
            ChainedEntry<V> newChain = new ChainedEntry<V>((V)lastChain._next, value);
            lastChain._next = newChain;
          }
          break;
        }
      }
    } else {
      // The entry is directly in _table, so if the existing value there matches this key, overwrite it
      // Otherwise, chain the new value in
      if (keyMatches(key, (V) table[bucket])) {
        priorValue = (V) table[bucket];
        table[bucket] = value;
      } else {
        table[bucket] = new ChainedEntry<V>((V)table[bucket], value);
      }
    }

    return priorValue;
  }

  private void resize(int newTableSize) {
    Object[] newTable = new Object[newTableSize];
    for (V value : values()) {
      putImpl(getKeyForValue(value), value, newTable, false);
    }
    _table = newTable;
  }

  private static class ChainedEntry<V> {
    private V _entry;
    private Object _next;

    private ChainedEntry(V entry, Object next) {
      _entry = entry;
      _next = next;
    }
  }

  private class ValueIterator implements Iterator<V> {

    private int _bucket;
    private Object _entry;


    private ValueIterator() {
      _bucket = -1;
      _entry = null;
      advanceIterator();
    }

    @Override
    public boolean hasNext() {
      return _entry != null;
    }

    @Override
    public V next() {
      V result;
      if (_entry == null) {
        throw new NoSuchElementException();
      } else if (_entry instanceof ChainedEntry) {
        result = ((ChainedEntry<V>) _entry)._entry;
      } else {
        result = (V) _entry;
      }
      advanceIterator();
      return result;
    }

    @Override
    public void remove() {
      //To change body of implemented methods use File | Settings | File Templates.
    }

    private void advanceIterator() {
      if (_entry instanceof ChainedEntry) {
        _entry = ((ChainedEntry) _entry)._next;
      } else {
        _entry = null;
        for (int i = _bucket + 1; i < _table.length; i++) {
          if (_table[i] != null) {
            _bucket = i;
            _entry = _table[i];
            break;
          }
        }
      }
    }
  }

  private class KeyIterator implements Iterator<K> {
    private ValueIterator _valueIterator;

    private KeyIterator() {
      _valueIterator = new ValueIterator();
    }

    @Override
    public boolean hasNext() {
      return _valueIterator.hasNext();
    }

    @Override
    public K next() {
      return getKeyForValue(_valueIterator.next());
    }

    @Override
    public void remove() {
      _valueIterator.remove();
    }
  }

  private class KeySet extends AbstractSet<K> {
    private KeySet() {
    }

    @Override
    public int size() {
      return DerivedKeyHashMap.this.size();
    }

    @Override
    public boolean contains(Object o) {
      return DerivedKeyHashMap.this.containsKey(o);
    }

    @Override
    public Iterator<K> iterator() {
      return new KeyIterator();
    }

    @Override
    public boolean add(K k) {
      throw new UnsupportedOperationException("You cannot add directly to the key set for a Map; you must use put() on the Map instead");
    }

    @Override
    public boolean remove(Object o) {
      return DerivedKeyHashMap.this.remove(o) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      if (c == null) {
        throw new NullPointerException("containsAll(Collection) cannot be called with a null argument");
      }

      return super.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends K> c) {
      throw new UnsupportedOperationException("You cannot add directly to the key set for a Map; you must use put() on the Map instead");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      if (c == null) {
        throw new NullPointerException("retainAll(Collection) cannot be called with a null argument");
      }

      for (Object o : c) {
        if (o == null) {
          throw new NullPointerException("The elements within the argument to retainAll(Collection) on the key set of a DerivedKeyHashMap cannot be null");
        }
      }

      return super.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      if (c == null) {
        throw new NullPointerException("removeAll(Collection) cannot be called with a null argument");
      }

      for (Object o : c) {
        if (o == null) {
          throw new NullPointerException("The elements within the argument to removeAll(Collection) on the key set of a DerivedKeyHashMap cannot be null");
        }
      }

      return super.removeAll(c);
    }

    @Override
    public void clear() {
      DerivedKeyHashMap.this.clear();
    }
  }
}
