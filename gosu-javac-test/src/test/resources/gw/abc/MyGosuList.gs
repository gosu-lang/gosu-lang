package gw.abc

class MyGosuList<E> extends ArrayList<E>
{
  construct( initialSize: int )
  {
    super( initialSize )
  }

  function myGenFun<A>( arg: A ) : A
  {
    return arg
  }
}