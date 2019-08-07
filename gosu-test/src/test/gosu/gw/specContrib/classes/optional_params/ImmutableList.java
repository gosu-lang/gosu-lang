package gw.specContrib.classes.optional_params;


public class ImmutableList<E>
{
  public static <E> ImmutableList<E> of() {
    return null;
  }

    //  This one is no longer required, as it conflicts with
    //  the next instance with no VarArgs parameters
    //  public static <E> ImmutableList<E> of(E element) {
    //    return null;
    //  }

  public static <E> ImmutableList<E> of(E e, E... others) {
    return null;
  }
}
