package gw.internal.gosu.parser.generics.gwtest

interface IGenericInterface<T>
{
  function foo( t : T ) : T
  
  property get Prop1() : T
  property set Prop1( t : T )
}