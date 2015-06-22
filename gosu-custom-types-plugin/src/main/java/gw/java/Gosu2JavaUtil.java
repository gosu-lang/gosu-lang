/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.java;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Gosu2JavaUtil {

  // Arrays.toList
  public static <T> ArrayList<T> list(T... values) {
    ArrayList<T> arrayList = new ArrayList<>(values.length);
    for (T value : values) {
      arrayList.add(value);
    }
    return arrayList;
  }

  public static <I, O> List<O> map(Collection<I> in, Mapper<I, O> m) {
    ArrayList<O> out = new ArrayList<>(in.size());
    for (I value : in) {
      out.add(m.map(value));
    }
    return out;
  }

  public static <I, O> List<O> map(List<I> in, Mapper<I, O> m) {
    ArrayList<O> out = new ArrayList<>(in.size());
    for (I value : in) {
      out.add(m.map(value));
    }
    return out;
  }

  public static <I, O, C> O[] map(I[] in, Class<C> theType, Mapper<I, O> m) {
    Object array = Array.newInstance(theType, in.length);
    for (int i = 0; i < in.length; i++) {
      Array.set(array, i, m.map(in[i]));
    }
    return (O[]) array;
  }

  public static void main(String[] args) {
    Integer[] map = map(new String[]{"1", "2"}, Integer.class, new Mapper<String, Integer>() {
      @Override
      public Integer map(String elt) {
        return Integer.parseInt(elt);
      }
    });
  }

}
