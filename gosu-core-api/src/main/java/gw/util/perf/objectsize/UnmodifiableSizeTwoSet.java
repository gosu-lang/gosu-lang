/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.perf.objectsize;

import java.util.AbstractSet;
import java.util.Iterator;

/**
 */
public class UnmodifiableSizeTwoSet<T> extends AbstractSet<T> {
  private T _o1;
  private T _o2;

  public UnmodifiableSizeTwoSet(T o1, T o2) {
    if (eq(o1, o2)) {
      _o1 = o1;
      _o2 = null;
    } else {
      _o1 = o1;
      _o2 = o2;
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
        private int i = 0;

        public boolean hasNext() {
            return i < size();
        }

        public T next() {
          if (i == 0) {
            i++;
            return _o1;
          } else if (i == 1) {
            i++;
            return _o2;
          } else {
            throw new RuntimeException("Implementation error.");
          }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    };
  }

  public boolean contains(Object o) {
    return eq(o, _o1) || eq(o, _o2);
  }

  private static boolean eq(Object o1, Object o2) {
    return (o1==null ? o2==null : o1.equals(o2));
  }

  @Override
  public final int size() {
    return _o2 == null ? 1 : 2;
  }
}
