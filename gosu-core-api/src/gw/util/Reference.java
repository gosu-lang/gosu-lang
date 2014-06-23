/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

public class Reference<T> {

  T _value;

  public Reference(T value) {
    _value = value;
  }

  public Reference() {
  }

  public T get() {
    return _value;
  }

  public void set(T value) {
    _value = value;
  }

  public static <T> Reference<T> make(T v) {
    return new Reference<T>(v);
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !(o instanceof Reference) )
    {
      return false;
    }

    Reference ref = (Reference)o;

    return !(_value != null ? !_value.equals(ref._value) : ref._value != null);

  }

  public int hashCode()
  {
    int result;
    result = (_value != null ? _value.hashCode() : 0);
    return result;
  }

  @Override
  public String toString()
  {
    return "(" + _value + ")";
  }

}