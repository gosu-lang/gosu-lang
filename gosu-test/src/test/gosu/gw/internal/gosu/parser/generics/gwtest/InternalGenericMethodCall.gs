package gw.internal.gosu.parser.generics.gwtest

uses java.io.Serializable
uses java.lang.StringBuilder

class InternalGenericMethodCall
{
  function getCount() : int
  {
    return execute( \->8 )
  }

  private function execute<T>( foo() : T ) : T
  {
    return foo()
  }

  function callGenMethodWithBockArgUsingTypeVar() : List<String>
  {
    return genMethodWithBockArgUsingTypeVar( \ l -> l as List<String>, "hello" )
  }
  function genMethodWithBockArgUsingTypeVar<T>( foo(l: List<T>): List<T>, t: T ) : List<T>
  {
    var ll : List<T> = {t,t}
    return foo( ll )
  }

  function lubInferenceAcrossArgsTest() : Type
  {
    var l : String = "hi"
    var s : StringBuilder = new StringBuilder( "bai" )
    var r = lubInferenceAcrossArgs( l, s )
    return statictypeof r
  }

  function lubInferenceAcrossArgs<T>( t1: T, t2: T ): T { return null }
}