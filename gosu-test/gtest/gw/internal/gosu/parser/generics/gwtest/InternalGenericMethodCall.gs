package gw.internal.gosu.parser.generics.gwtest

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
}