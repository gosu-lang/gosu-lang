package gw.specContrib.generics

uses java.lang.Integer

class Errant_CompoundTypeWithTypeVar {
  static class RelinkFilter {
    function foo() : boolean { return false}
  }
  var rf: List<RelinkFilter>

  var ll: List<RelinkFilter> = Lists.newArrayList( FooJava.filter( rf, \ r -> r.foo() ) )
  var ll2: List<RelinkFilter> = Lists.newArrayList( FooJava.filter( rf, FooJava.not( \ r -> r.foo() ) ) )

  var ll3: List<String> = Lists.newArrayList( FooJava.filter( {""}, FooJava.not( \ r -> r.Alpha ) ) )
  var ll4: List<String> = Lists.newArrayList( FooJava.filter( {}, FooJava.not( \ r -> r.Alpha ) ) )

  var ll5: List<Object> = Lists.newArrayList( FooJava.filter( {""}, FooJava.not( \ r -> r.Alpha ) ) )  //## issuekeys: MSG_TYPE_MISMATCH, MSG_NO_PROPERTY_DESCRIPTOR_FOUND
}