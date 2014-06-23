/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.util.Collection;
import java.util.Iterator;

public class IdentitySet<T> implements Iterable<T> {
  private int _index;
  private T[] _set;

  public IdentitySet(int size) {
    _set = (T[]) new Object[size];
  }

  public boolean isEmpty() {
    return _index == 0;
  }


  public int size() {
    return _index;
  }


  public boolean contains(T o) {
    for (int i = 0; i < _index; i++) {
      if (_set[i] == o) {
        return true;
      }
    }
    return false;
  }


  public void add(T o) {
    if (!contains(o)) {
      if (_index == _set.length) {
        T[] oldSet = _set;
        _set = (T[]) new Object[_set.length * 2];
        System.arraycopy(oldSet, 0, _set, 0, size());
      }
      _set[_index] = o;
      _index++;
    }
  }


  public void remove(T o) {
    throw new RuntimeException("Not supported");
//    for (int i = 0; i < _index; i++) {
//      if (_set[i] == o) {
//        if (i != _index - 1) {
//          _set[i] = _set[_index - 1];
//        }
//        _set[i] = null;
//        _index--;
//      }
//    }
  }


  public void addAll(Collection<? extends T> c) {
    for (T o : c) {
      add(o);
    }
  }


  public void clear() {
    for (int i = 0; i < _index; i++) {
      _set[i] = null;
    }
    _index = 0;
  }

  public Iterator<T> iterator() {
    return new Iterator<T>() {
      int i = 0;

      @Override
      public boolean hasNext() {
        return i <= _index - 1;
      }

      @Override
      public T next() {
        T o = _set[i];
        i++;
        return o;
      }

      @Override
      public void remove() {
        throw new RuntimeException("Not supported");
      }
    };
  }

  public String toString() {
    return size() + " items";
  }

  public T[] toArray(T[] array) {
    Iterator<T> iterator = iterator();
    int i = 0;
    while (iterator.hasNext()) {
      array[i] = iterator.next();
      i++;
    }
    return array;
  }
}
