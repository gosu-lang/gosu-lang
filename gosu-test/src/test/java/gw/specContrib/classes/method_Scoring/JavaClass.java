package gw.specContrib.classes.method_Scoring;

import java.util.*;

public class JavaClass
{
  public static <E> Set<E> newHashSet(E... e) {
    return new HashSet<E>(Arrays.asList(e));
  }
  public static <E> Set<E> newHashSet(Iterable<? extends E> elements) {
    return new HashSet<E>();
  }
  public static <E> Set<E> newHashSet(Iterator<? extends E> elements) {
    return new HashSet<E>();
  }
  public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
    return new HashSet<E>();
  }
}