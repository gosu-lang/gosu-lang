package gw.specContrib.generics;



import java.util.ArrayList;
import java.util.function.Predicate;

public class FooJava {

  public static <T> Predicate<T> not(Predicate<? super T> predicate) { return null; }
  public static <E> Iterable<E> filter(Iterable<E> unfiltered, Predicate<? super E> predicate) { return null;}
  public static <E> Iterable<E> filter(Iterable<E> unfiltered, Iterable<E> unfiltered2) { return null;}
  public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) { return null; }

}
