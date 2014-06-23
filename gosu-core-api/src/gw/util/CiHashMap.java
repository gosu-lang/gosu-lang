/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.lang.parser.CICS;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class CiHashMap<K extends CharSequence, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

  private static final int NULL_KEY_HASH_CODE = 0;
  private static final int DEFAULT_INITIAL_CAPACITY = 16;
  private static final float DEFAULT_LOAD_FACTOR = 0.75f;
  private static final int MAX_TABLE_SIZE = 1 << 30;

  private int _size;
  private Entry<K, V>[] _table;
  private int _resizeThreshold;
  private float _loadFactor;
  private int _modCount;

  public CiHashMap() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  public CiHashMap( int initialCapacity ) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  @SuppressWarnings({"unchecked"})
  public CiHashMap( int initialCapacity, float loadFactor ) {
    initialCapacity = findNearestPowerOfTwo(initialCapacity);
    _table = new Entry[initialCapacity];
    _resizeThreshold = (int) (initialCapacity * loadFactor);
    _loadFactor = loadFactor;
    _size = 0;
  }

  private int findNearestPowerOfTwo(int i) {
    if (i > MAX_TABLE_SIZE) {
      return MAX_TABLE_SIZE;
    }

    int result = 1;
    while (result < i) {
      result <<= 1;
    }
    return result;
  }

  public CiHashMap( Map<? extends K, ? extends V> m ) {
    this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,
            DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
    putAllImpl(m, false);
  }

  @Override
  public int size() {
    return _size;
  }

  @Override
  public V get(Object key) {
    Entry<K, V> entry = findEntry(key, hash(key));
    return (entry == null ? null : entry.getValue());
  }

  @Override
  public boolean containsKey(Object key) {
    return findEntry(key, hash(key)) != null;
  }

  private Entry<K, V> findEntry(Object key, int hash) {
    Entry<K, V> entry = _table[bucketNumber(hash)];
    while (entry != null) {
      if (entryMatches(key, hash, entry)) {
        return entry;
      }

      entry = entry.getNext();
    }

    return null;
  }

  private boolean entryMatches(Object key, int hash, Entry<K, V> entry) {
    return entry.getHash() == hash && keysMatch(key, entry.getKey());
  }

  @Override
  public boolean containsValue(Object value) {
    for (Entry<K, V> entry : _table) {
      while (entry != null) {
        if (GosuObjectUtil.equals(value, entry.getValue())) {
          return true;
        }

        entry = entry.getNext();
      }
    }

    return false;
  }

  @Override
  public V put(K key, V value) {
    return putImpl(key, value, true);
  }

  private V putImpl(K key, V value, boolean resizeIfNecessary) {
    int hash = hash(key);
    Entry<K, V> existing = findEntry(key, hash);

    if (existing != null) {
      // Just update the old value in place
      V oldValue = existing.getValue();
      existing.setValue(value);
      existing.setKey(key); // We need to re-set the key, since the key could be a different case of the same string
      return oldValue;
    } else {
      // We need to create a new entry, chain it in place, and then possibly resize the table
      _modCount++;
      int bucketIndex = bucketNumber(hash);
      Entry<K, V> bucketHead = _table[bucketIndex];
      _table[bucketIndex] = new Entry<K, V>(key, value, hash, bucketHead);
      _size++;

      if (resizeIfNecessary && _size >= _resizeThreshold) {
        resize(2 * _table.length);
      }

      return null;
    }
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    putAllImpl(m, true);
  }

  private void putAllImpl(Map<? extends K, ? extends V> map, boolean resizeIfNecessary) {
    // Performance optimization:  resize once up front if it looks like we'll have to resize
    // so we avoid multiple resizes when adding lots of entries.  For example, when adding 10000
    // keys to an empty map with an initialize size of 16, we want to resize once up to 16384
    // entries instead of 11 times.  Note that if the passed in map duplicates existing keys,
    // this resize might prove to be unnecessary
    if (resizeIfNecessary && map.size() + _size >= _resizeThreshold) {
      resize(findNearestPowerOfTwo(map.size() + _size));
    }

    for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
      putImpl(entry.getKey(), entry.getValue(), resizeIfNecessary);
    }
  }

  @Override
  public V remove(Object key) {
    Entry<K, V> removed = removeEntry(key);
    return (removed == null ? null : removed.getValue());
  }

  private Entry<K, V> removeEntry(Object key) {
    int hash = hash(key);
    int bucketNumber = bucketNumber(hash);
    Entry<K, V> previous = null;
    Entry<K, V> current = _table[bucketNumber];

    while (current != null) {
      if (entryMatches(key, hash, current)) {
        if (previous == null) {
          // No previous entry, so just chop it out of the list
          _table[bucketNumber] = current.getNext();
        } else {
          // There is a previous link in the list, so remove current by linking previous to next
          previous.setNext(current.getNext());
        }

        _size--;
        _modCount++;
        return current;
      }

      previous = current;
      current = current.getNext();
    }
    
    return null;
  }

  @Override
  public void clear() {
    // Just null out the whole table
    for (int i = 0 ; i < _table.length; i++) {
      _table[i] = null;
    }

    _modCount++;
    _size = 0;
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public Object clone() {
    CiHashMap<K, V> result;
    try {
      result = (CiHashMap<K, V>) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }

    result._table = new Entry[_table.length];
    result._modCount = 0;
    result._size = 0;
    result._resizeThreshold = _resizeThreshold;
    result._loadFactor = _loadFactor;
    result.putAllImpl(this, false);

    return result;
  }

  @Override
  public Set<K> keySet() {
    return new KeySet();
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    return new EntrySet();
  }

  @Override
  public Collection<V> values() {
    return new ValuesCollection();
  }

  private static boolean keysMatch(Object x, Object y) {
    if (x == y) {
      return true;
    }

    if (x == null || y == null) {
      return false;
    }

    if (!(x instanceof CharSequence)) {
      throw new IllegalArgumentException("Key value must be instanceof CharSequence");
    }
    if (!(y instanceof CharSequence)) {
      throw new IllegalArgumentException("Key value must be instanceof CharSequence");
    }

    return CICS.equalsIgnoreCase((CharSequence) x, (CharSequence) y);
  }

  private static int hash(Object x) {
    if (x instanceof CICS) {
      return x.hashCode();
    }

    if (x == null) {
      return NULL_KEY_HASH_CODE;
    }

    if (!(x instanceof CharSequence)) {
      throw new IllegalArgumentException("Key value must be instanceof CharSequence it is a type of " + x.getClass());
    }

    return CICS.getLowerCaseHashCode((CharSequence) x);
  }

  private int bucketNumber(int hashCode) {
    return bucketNumber(hashCode, _table.length);
  }

  private int bucketNumber(int hashCode, int tableLength) {
    // This assumes that the hash codes will be evenly distributed with respect to all bytes;
    // if the hash code doesn't have much entropy in the lower-order bits, this won't work out so well
    return hashCode & (tableLength - 1);
  }

  @SuppressWarnings({"unchecked"})
  private void resize(int newCapacity) {
    // If we're already at the max table size, set the resize threshold as high as possible
    // so we don't attempt to resize in the future
    if (_table.length == MAX_TABLE_SIZE) {
      _resizeThreshold = Integer.MAX_VALUE;
      return;
    }

    Entry<K, V>[] newTable = new Entry[newCapacity];
    for (Entry<K, V> entry : _table) {
      while (entry != null) {
        int newBucketNumber = bucketNumber(entry.getHash(), newTable.length);
        Entry<K, V> next = entry.getNext();
        entry.setNext(newTable[newBucketNumber]);
        newTable[newBucketNumber] = entry;
        entry = next;
      }
    }
    _table = newTable;

    _resizeThreshold = (int) (newCapacity * _loadFactor);
  }

  private static class Entry<K extends CharSequence, V> implements Map.Entry<K, V>, Serializable {
    private K _key;
    private V _value;
    private int _hash;
    private Entry<K, V> _next;

    public Entry(K key, V value, int hash, Entry<K, V> next) {
      _key = key;
      _value = value;
      _hash = hash;
      _next = next;
    }

    @Override
    public K getKey() {
      return _key;
    }

    public void setKey(K key) {
      _key = key;
    }

    @Override
    public V getValue() {
      return _value;
    }

    @Override
    public V setValue(V value) {
      return _value = value;
    }

    public int getHash() {
      return _hash;
    }

    public Entry<K, V> getNext() {
      return _next;
    }

    public void setNext(Entry<K, V> next) {
      _next = next;
    }

    @Override
    public boolean equals( Object o )
    {
      if( o instanceof Entry )
      {
        Entry entry = (Entry)o;
        return GosuObjectUtil.equals( getKey(), entry.getKey() ) &&
               GosuObjectUtil.equals( getValue(), entry.getValue() );
      }
      else
      {
        return false;
      }
    }

    @Override
    public int hashCode()
    {
      int result = _key != null ? _key.hashCode() : 0;
      result = 31 * result + (_value != null ? _value.hashCode() : 0);
      return result;
    }
  }

  private class KeySet extends AbstractSet<K> {
    @Override
    public Iterator<K> iterator() {
      return new KeyIterator();
    }

    @Override
    public int size() {
      return _size;
    }

    @Override
    public boolean contains(Object o) {
      return containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
      return removeEntry(o) != null;
    }

    @Override
    public void clear() {
      CiHashMap.this.clear();
    }
  }

  private class ValuesCollection extends AbstractCollection<V> {

    @Override
    public Iterator<V> iterator() {
      return new ValuesIterator();
    }

    @Override
    public int size() {
      return _size;
    }

    @Override
    public boolean contains(Object o) {
      return containsValue(o);
    }

    @Override
    public void clear() {
      CiHashMap.this.clear();
    }
  }

  private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
      return new MapEntryIterator();
    }

    @Override
    public boolean contains(Object o) {
      if (!(o instanceof Entry)) {
        return false;
      }

      Entry e = (Entry) o;
      Entry<K, V> currentEntry = findEntry(e.getKey(), e.getHash());
      return (e == currentEntry);
    }

    @Override
    public boolean remove(Object o) {
      if (!(o instanceof Entry)) {
        return false;
      }

      Object key = ((Entry) o).getKey();
      return removeEntry(key) != null;
    }

    @Override
    public int size() {
      return _size;
    }

    @Override
    public void clear() {
      CiHashMap.this.clear();
    }
  }

  private class KeyIterator extends EntryIterator<K> {
    @Override
    public K next() {
      return nextEntry().getKey();
    }
  }

  private class ValuesIterator extends EntryIterator<V> {
    @Override
    public V next() {
      return nextEntry().getValue();
    }
  }

  private class MapEntryIterator extends EntryIterator<Map.Entry<K, V>> {
    @Override
    public Entry<K, V> next() {
      return nextEntry();
    }
  }

  private abstract class EntryIterator<E> implements Iterator<E> {

    private Entry<K, V> _lastReturned;
    private Entry<K, V> _next;
    private int _nextBucket = 0;
    private int _expectedModCount;

    protected EntryIterator() {
      _expectedModCount = _modCount; // Capture the expected modification count
      advanceIterator();
    }

    public Entry<K, V> nextEntry() {
      checkForConcurrentModification();
      if (_next == null) {
        throw new NoSuchElementException();
      }
      _lastReturned = _next;
      advanceIterator();
      return _lastReturned;
    }

    private void checkForConcurrentModification() {
      if (_expectedModCount != _modCount) {
        throw new ConcurrentModificationException();
      }
    }

    @Override
    public boolean hasNext() {
      return _next != null;
    }

    @Override
    public void remove() {
      if (_lastReturned == null) {
        throw new IllegalStateException("remove() can only be called once after a call to next()");
      }
      checkForConcurrentModification();
      removeEntry(_lastReturned.getKey());
      _lastReturned = null;
      _expectedModCount = _modCount; // Update the modification count, since the remove has presumably resulted in a modification
    }

    private void advanceIterator() {
      _next = null; // Null out _next before we start so it ends up as null even if our loop never executes
      if (_lastReturned != null && _lastReturned.getNext() != null) {
        _next = _lastReturned.getNext();
      } else {
        while (_nextBucket < _table.length) {
          _next = _table[_nextBucket];
          _nextBucket++; // Increment here so we'll always increment at least once
          if (_next != null) {
            break;
          }
        }
      }
    }
  }

}
