package gw.internal.gosu.regression;

import java.util.Comparator;

abstract class JavaBase<T> implements Comparator<T> {
  public abstract int compare( T b, T c );
}