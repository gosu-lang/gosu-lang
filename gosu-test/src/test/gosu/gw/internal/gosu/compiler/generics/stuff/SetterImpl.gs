package gw.internal.gosu.compiler.generics.stuff
uses java.util.Map

class SetterImpl<E> implements ISetter<E>
{
  override function method_A<T>( other: Map<E, T> ) {
    print("SetterImpl.method_A")
  }
}
