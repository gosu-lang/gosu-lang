/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.util.Objects;

public class Triple<F, S, T> {
  final F _first;
  final S _second;
  final T _third;

  public Triple(F first, S second, T third) {
    _first = first;
    _second = second;
    _third = third;
  }

  public F getFirst() {
    return _first;
  }

  public S getSecond() {
    return _second;
  }

  public T getThird() {
    return _third;
  }

  public static <F, S, T> Triple<F, S, T> make(F f, S s, T t) {
    return new Triple<F, S, T>(f, s, t);
  }

  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( !(o instanceof Triple) ) {
      return false;
    }

    Triple other = (Triple) o;
    return Objects.equals(_first, other._first)
            && Objects.equals(_second, other._second)
            && Objects.equals(_third, other._third);
  }

  public int hashCode() {
    return Objects.hash(_first, _second, _third);
  }

  @Override
  public String toString() {
    return "(" + _first + ", " + _second + ", " + _third + ")";
  }

}
