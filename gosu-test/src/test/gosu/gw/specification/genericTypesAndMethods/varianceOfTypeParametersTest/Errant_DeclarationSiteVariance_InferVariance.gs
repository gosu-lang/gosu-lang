package gw.specification.genericTypesAndMethods.varianceOfTypeParametersTest

uses java.util.function.Predicate
uses java.util.function.Function


class Errant_DeclarationSiteVariance_InferVariance {

  interface InIn<in T, in U> {
    function add( x: T ): boolean
    function helo( u: U )
  }

  interface InOut<in T, out U> {
    function add( x: T ): boolean
    function guhbye(): U
  }

  interface OutOut<out T, out U> {
    function iterator(): Iterator<T>
    function guhbye(): U
  }

  interface OutIn<out T, in U> {
    function iterator(): Iterator<T>
    function helo( u: U )
  }

  interface OutInDefault<out T, in U, V> {
    function iterator(): Iterator<T>
    function helo( u: U )
    function foo( v: V ) : V
  }

  interface IFoo<out T> extends Iterator<T> {
  }
  interface IFoo2<in T> extends Iterator<T> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface IFoo3<in out T> extends Iterator<T> {
  }

  interface IBar<in T> extends Predicate<T> {
  }
  interface IBar2<out T> extends Predicate<T> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

  interface Happening<in A, out B> extends Function<A, B> {
  }
  interface Happening1<in A, in B> extends Function<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Happening2<out A, in B> extends Function<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Happening3<out A, out B> extends Function<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

  interface Param_In_Out__Super_Extends<in A, out B> extends WildcardsInFunctionParam_Super_Extends<A, B> {
  }
  interface Param_In_In__Super_Extends<in A, in B> extends WildcardsInFunctionParam_Super_Extends<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_Out_In__Super_Extends<out A, in B> extends WildcardsInFunctionParam_Super_Extends<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_Out_Out__Super_Extends<out A, out B> extends WildcardsInFunctionParam_Super_Extends<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

  interface Param_In_Out__Super_Super<in A, out B> extends WildcardsInFunctionParam_Super_Super<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_In_In__Super_Super<in A, in B> extends WildcardsInFunctionParam_Super_Super<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_Out_In__Super_Super<out A, in B> extends WildcardsInFunctionParam_Super_Super<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_Out_Out__Super_Super<out A, out B> extends WildcardsInFunctionParam_Super_Super<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }

  interface Param_In_Out__Extends_Super<in A, out B> extends WildcardsInFunctionParam_Extends_Super<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_In_In__Extends_Super<in A, in B> extends WildcardsInFunctionParam_Extends_Super<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_Out_In__Extends_Super<out A, in B> extends WildcardsInFunctionParam_Extends_Super<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_Out_Out__Extends_Super<out A, out B> extends WildcardsInFunctionParam_Extends_Super<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }

  interface Param_In_Out__Extends_Extendst<in A, out B> extends WildcardsInFunctionParam_Extends_Extends<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_In_In__Extends_Extends<in A, in B> extends WildcardsInFunctionParam_Extends_Extends<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_Out_In__Extends_Extends<out A, in B> extends WildcardsInFunctionParam_Extends_Extends<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }
  interface Param_Out_Out__Extends_Extends<out A, out B> extends WildcardsInFunctionParam_Extends_Extends<A, B> {  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
  }

  static class Foo<in A, out B> implements Function<A, B> {
    override function apply(t: A): B {
      return null
    }

    override function compose<V>(before: Function<V, A>): Function<V, B> {
      return null
    }

    override function andThen<V>(after: Function<B, V>): Function<A, V> {
      return null
    }
  }

  static class Bar<in A, out B> {
    var _f: Function<A, B>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
    var _a: A  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    var _b: B  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    function helo( f: Function<B, A> ) {
    }
    function helo2( f: Function<A, B> ) {
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
    function helo3( f: Function<A, A> ) {
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    function helo4( f: Function<B, B> ) {
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    function helo(): Function<B, A> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
    function helo2(): Function<A, B> {
      return null
    }
    function helo3(): Function<A, A> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    function helo4(): Function<B, B> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    property get Func1(): Function<A, B> {
      return null
    }
    property set Func1( v: Function<A, B> ) {
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR

    property get Func2(): Function<B, A> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
    property set Func2( v: Function<B, A> ) {
    }

    property get Func3(): Function<B, B> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    property set Func3( v: Function<B, B> ) {
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    property get Func4(): Function<A, A> {
      return null
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    property set Func4( v: Function<A, A> ) {
    }  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }
}