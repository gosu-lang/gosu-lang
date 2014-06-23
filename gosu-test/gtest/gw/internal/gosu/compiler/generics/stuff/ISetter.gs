package gw.internal.gosu.compiler.generics.stuff

uses java.util.Map

interface ISetter<E>  {

  function method_A<T>(other: Map<E, T> )

}