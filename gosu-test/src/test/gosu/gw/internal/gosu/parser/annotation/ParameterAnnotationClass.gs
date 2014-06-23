package gw.internal.gosu.parser.annotation

uses java.lang.Deprecated
uses java.lang.SuppressWarnings

class ParameterAnnotationClass {
  construct( @SuppressWarnings(:value={"blah"}) a: Object ) {}

  function asdf( @SuppressWarnings("blah") a: Object ) {}

  property set F( @Deprecated @SuppressWarnings(:value={"settervalue"}) a: Object ) {}

  function f() {
    var bl = \ @SuppressWarnings(:value={"blah"}) a: int -> print( a )
  }

  function ff() {
    var bl = \ @Deprecated @SuppressWarnings(:value={"blah"}) a: int -> print( a )
  }
}
