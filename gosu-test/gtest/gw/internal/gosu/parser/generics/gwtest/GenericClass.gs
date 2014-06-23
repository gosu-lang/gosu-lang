package gw.internal.gosu.parser.generics.gwtest

class GenericClass<T> implements IGenericInterface<T>
{
  var _t : T as Prop1

  function foo( t : T ) : T
  {
    return t
  }
}