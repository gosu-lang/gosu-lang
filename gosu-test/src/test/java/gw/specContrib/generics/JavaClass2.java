package gw.specContrib.generics;

/**
 */
public class JavaClass2<T> {
  public static class Builder<Q> {
    public JavaClass2<Q> build() { return null; }
  }

  public static <S> Builder<S> builder1() { return null; }

  public static <T> Builder<T> builder2() { return null; }
}