/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.perf.objectsize;


import java.util.*;

/**
 */
public class UnmodifiableArraySet<T> extends AbstractSet<T> {
  private Object[] array;

  public UnmodifiableArraySet(Set<T> set) {
    array = new Object[set.size()];
    int i = 0;
    for (T o : set) {
      array[i] = o;
      i++;
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
        private int i = 0;

        public boolean hasNext() {
            return i < array.length;
        }

        public T next() {
          T v = (T) array[i];
          i++;
          return v;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    };
  }

  @Override
  public int size() {
    return array.length;
  }
}
