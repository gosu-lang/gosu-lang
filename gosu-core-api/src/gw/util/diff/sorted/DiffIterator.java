/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.diff.sorted;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DiffIterator<T> implements Iterator<Diff<T>> {

  private final Iterator<T> _oldIterator;
  private final Iterator<T> _newIterator;
  private T _oldItem;
  private T _newItem;
  private final Comparator<? super T> _comparator;
  private Diff<T> _next;

  public DiffIterator(Iterator<T> oldIterator, Iterator<T> newIterator) {
    this(oldIterator, newIterator, new Comparator<T>() {
      @SuppressWarnings("unchecked")
      public int compare(T o1, T o2) {
        return ((Comparable<T>) o1).compareTo(o2);
      }
    });
  }

  public DiffIterator(Iterable<T> oldIterable, Iterable<T> newIterable) {
    this(oldIterable.iterator(), newIterable.iterator());
  }

  public DiffIterator(Iterable<T> oldIterable, Iterable<T> newIterable, Comparator<? super T> comparator) {
    this(oldIterable.iterator(), newIterable.iterator(), comparator);
  }

  public DiffIterator(Iterator<T> oldIterator, Iterator<T> newIterator, Comparator<? super T> comparator) {
    this._oldIterator = oldIterator;
    this._newIterator = newIterator;
    this._comparator = comparator;
    // prefetch first item from each iterator
    if (oldIterator.hasNext()) {
      _oldItem = oldIterator.next();
    }
    if (newIterator.hasNext()) {
      _newItem = newIterator.next();
    }
    calcNext();
  }

  public boolean hasNext() {
    return _next != null;
  }

  public Diff<T> next() {
    if (_next == null) {
      throw new NoSuchElementException();
    }
    Diff<T> lastNext = _next;
    calcNext();
    return lastNext;
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }

  private void calcNext() {
    Integer compare = compare();
    if (compare == null) {
      _next = null;
    }
    else if (compare > 0) {
      _next = new Diff<T>(null, _newItem);
      nextNew();
    }
    else if (compare < 0) {
      _next = new Diff<T>(_oldItem, null);
      nextOld();
    }
    else {
      _next = new Diff<T>(_oldItem, _newItem);
      nextOld();
      nextNew();
    }
  }

  private void nextOld() {
    _oldItem = _oldIterator.hasNext() ? _oldIterator.next() : null;
  }

  private void nextNew() {
    _newItem = _newIterator.hasNext() ? _newIterator.next() : null;
  }

  private Integer compare() {
    if (_oldItem == null) {
      if (_newItem == null) {
        return null; // no more items
      }
      return 1;
    }
    else if (_newItem == null) {
      return -1;
    }
    else {
      return _comparator.compare(_oldItem, _newItem);
    }
  }

}
