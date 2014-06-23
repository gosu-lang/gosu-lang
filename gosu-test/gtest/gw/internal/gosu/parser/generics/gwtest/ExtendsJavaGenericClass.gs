package gw.internal.gosu.parser.generics.gwtest

uses gw.internal.gosu.parser.generics.TestGenericClass

class ExtendsJavaGenericClass<T> extends TestGenericClass<T>
{
  construct( t : T )
  {
    super( t )
  }
}