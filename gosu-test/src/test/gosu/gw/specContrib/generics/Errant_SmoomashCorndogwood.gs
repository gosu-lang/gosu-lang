package gw.specContrib.generics

uses java.lang.Integer
uses java.lang.CharSequence
uses java.util.ArrayList
uses java.util.function.Predicate

class Errant_SmoomashCorndogwood {
  static class RelinkFilter {
    function foo() : boolean { return false}
  }
  var rf: List<RelinkFilter>

  var ll: List<RelinkFilter> = FooJava.newArrayList( FooJava.filter( rf, \ r -> r.foo() ) )
  var ll2: List<RelinkFilter> = FooJava.newArrayList( FooJava.filter( rf, FooJava.not( \ r -> r.foo() ) ) )

  var ll3: List<String> = FooJava.newArrayList( FooJava.filter( {""}, FooJava.not( \ r -> r.Alpha ) ) )
  var ll4: List<String> = FooJava.newArrayList( FooJava.filter( {}, FooJava.not( \ r -> r.Alpha ) ) )

  var ll5: List<Object> = FooJava.newArrayList( FooJava.filter( {""}, FooJava.not( \ r -> r.Alpha ) ) )
  var ll6: List<RelinkFilter> = FooJava.newArrayList( FooJava.filter( {""}, FooJava.not( \ r -> r.Alpha ) ) )  //## issuekeys: MSG_TYPE_MISMATCH, MSG_TYPE_MISMATCH, MSG_NO_PROPERTY_DESCRIPTOR_FOUND

  var ll7 = FooJava.newArrayList( FooJava.filter( rf, FooJava.not( \ t: RelinkFilter -> true ) ) )
  var ll8 = FooJava.newArrayList( FooJava.filter( rf, FooJava.not( \ t: Object -> true) ) )
  var ll9 = FooJava.newArrayList( FooJava.filter( rf, FooJava.not( \ t -> true) ) )

  function testReverseInferenceOnClosureFromClosureParam() {
    var result = f1( \ p: List<CharSequence> -> true )
    result.charAt(0) // confirms infers as CharSequence
  }
  function f1<T>( f(t: ArrayList<T>) ) : T  {
    return null
  }

  function testReverseInferenceOnFunctionalInterfaceFromClosureParam() {
    var result = f2( \ p: List<CharSequence> -> true )
    result.charAt(0) // confirms infers as CharSequence
  }
  function f2<T>( p: Predicate<ArrayList<T>> ) : T  {
    return null
  }
}