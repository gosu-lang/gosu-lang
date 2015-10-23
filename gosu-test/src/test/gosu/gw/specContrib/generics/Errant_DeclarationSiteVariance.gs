package gw.specContrib.generics

uses java.util.function.Consumer
uses java.util.function.Predicate
uses java.util.function.Function

class Errant_DeclarationSiteVariance {

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

  interface In_Out<in T, out U> {}
  interface JustOut<out T> {}
  interface JustIn<in U> {}

  interface InOutExtends<in T, out U> extends InOut<T, U>, JustOut<U>, JustIn<T> {}
  interface InOutExtends1<in T, out U> extends InOut<T, U>, JustOut<U>,
      JustIn<U> {}  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  interface InOutExtends2<in T, out U> extends InOut<T, U>,
      JustOut<T>,   //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
      JustIn<T> {}
  interface InOutExtends3<in T, out U> extends
      InOut<T, T>,   //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
      JustOut<U>, JustIn<T> {}
  interface InOutExtends4<in T, out U> extends
      InOut<U, T>,   //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
      JustOut<U>, JustIn<T> {}
  interface InOutExtends5<in T, out U> extends
      InOut<U, U>,   //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
      JustOut<U>, JustIn<T> {}

  static class InOutClass<in T, out U> implements In_Out<T, U>, JustOut<U>, JustIn<T> {}
  static class InOutClass1<in T, out U> implements In_Out<T, U>, JustOut<U>,
      JustIn<U> {}  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  static class InOutClass2<in T, out U> implements In_Out<T, U>,
      JustOut<T>,  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
      JustIn<T> {}
  static class InOutClass3<in T, out U> implements
      In_Out<T, T>,  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
      JustOut<U>, JustIn<T> {}
  static class InOutClass4<in T, out U> implements
      In_Out<U, T>,  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR, MSG_TYPE_VAR_VARIANCE_ERROR
      JustOut<U>, JustIn<T> {}
  static class InOutClass5<in T, out U> implements
      In_Out<U, U>,  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
      JustOut<U>, JustIn<T> {}

  function test_in_in() {
    var St_St: InIn<String, String> = null
    var Cs_Cs: InIn<CharSequence, CharSequence> = null

    var St_Cs: InIn<String, CharSequence> = null
    var Cs_St: InIn<CharSequence, String> = null

    St_St = Cs_Cs
    St_St = Cs_St
    St_St = St_Cs
    St_St = St_St  //## issuekeys: MSG_SILLY_ASSIGNMENT

    St_Cs = Cs_Cs
    St_Cs = Cs_St  //## issuekeys: MSG_TYPE_MISMATCH
    St_Cs = St_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    St_Cs = St_St  //## issuekeys: MSG_TYPE_MISMATCH

    Cs_St = Cs_Cs
    Cs_St = Cs_St  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_St = St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_St = St_St  //## issuekeys: MSG_TYPE_MISMATCH

    Cs_Cs = Cs_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_Cs = Cs_St  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_Cs = St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_Cs = St_St  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function test_in_out() {
    var St_St: InOut<String, String> = null
    var Cs_Cs: InOut<CharSequence, CharSequence> = null

    var St_Cs: InOut<String, CharSequence> = null
    var Cs_St: InOut<CharSequence, String> = null

    St_St = Cs_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_St = Cs_St
    St_St = St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_St = St_St  //## issuekeys: MSG_SILLY_ASSIGNMENT

    St_Cs = Cs_Cs
    St_Cs = Cs_St
    St_Cs = St_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    St_Cs = St_St

    Cs_St = Cs_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_St = Cs_St  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_St = St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_St = St_St  //## issuekeys: MSG_TYPE_MISMATCH

    Cs_Cs = Cs_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_Cs = Cs_St
    Cs_Cs = St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_Cs = St_St  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function test_out_out() {
    var St_St: OutOut<String, String> = null
    var Cs_Cs: OutOut<CharSequence, CharSequence> = null

    var St_Cs: OutOut<String, CharSequence> = null
    var Cs_St: OutOut<CharSequence, String> = null

    St_St = Cs_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_St = Cs_St  //## issuekeys: MSG_TYPE_MISMATCH
    St_St = St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_St = St_St  //## issuekeys: MSG_SILLY_ASSIGNMENT

    St_Cs = Cs_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_Cs = Cs_St  //## issuekeys: MSG_TYPE_MISMATCH
    St_Cs = St_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    St_Cs = St_St

    Cs_St = Cs_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_St = Cs_St  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_St = St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_St = St_St

    Cs_Cs = Cs_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_Cs = Cs_St
    Cs_Cs = St_Cs
    Cs_Cs = St_St
  }

  function test_out_in() {
    var St_St: OutIn<String, String> = null
    var Cs_Cs: OutIn<CharSequence, CharSequence> = null

    var St_Cs: OutIn<String, CharSequence> = null
    var Cs_St: OutIn<CharSequence, String> = null

    St_St = Cs_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_St = Cs_St  //## issuekeys: MSG_TYPE_MISMATCH
    St_St = St_Cs
    St_St = St_St  //## issuekeys: MSG_SILLY_ASSIGNMENT

    St_Cs = Cs_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_Cs = Cs_St  //## issuekeys: MSG_TYPE_MISMATCH
    St_Cs = St_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    St_Cs = St_St  //## issuekeys: MSG_TYPE_MISMATCH

    Cs_St = Cs_Cs
    Cs_St = Cs_St  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_St = St_Cs
    Cs_St = St_St

    Cs_Cs = Cs_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_Cs = Cs_St  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_Cs = St_Cs
    Cs_Cs = St_St  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function test_out_in_default() {
    var St_St_St: OutInDefault<String, String, String> = null
    var Cs_Cs_Cs: OutInDefault<CharSequence, CharSequence, CharSequence> = null

    var St_Cs_St: OutInDefault<String, CharSequence, String> = null
    var Cs_St_Cs: OutInDefault<CharSequence, String, CharSequence> = null

    St_St_St = Cs_Cs_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_St_St = Cs_St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_St_St = St_Cs_St
    St_St_St = St_St_St  //## issuekeys: MSG_SILLY_ASSIGNMENT

    St_Cs_St = Cs_Cs_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_Cs_St = Cs_St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    St_Cs_St = St_Cs_St  //## issuekeys: MSG_SILLY_ASSIGNMENT
    St_Cs_St = St_St_St  //## issuekeys: MSG_TYPE_MISMATCH

    Cs_St_Cs = Cs_Cs_Cs
    Cs_St_Cs = Cs_St_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_St_Cs = St_Cs_St
    Cs_St_Cs = St_St_St

    Cs_Cs_Cs = Cs_Cs_Cs  //## issuekeys: MSG_SILLY_ASSIGNMENT
    Cs_Cs_Cs = Cs_St_Cs  //## issuekeys: MSG_TYPE_MISMATCH
    Cs_Cs_Cs = St_Cs_St
    Cs_Cs_Cs = St_St_St  //## issuekeys: MSG_TYPE_MISMATCH
  }

  interface Setter<in T> {
    function add( x: T ): boolean
  }


  interface Getter<out T> {
    function iterator(): Iterator<T>
  }


  interface Collection<in out T> extends Setter<T>, Getter<T> {
  }

  class Sack<T> implements Collection<T> {
    var _sack = new ArrayList<T>()

    override function iterator(): Iterator<T> {
      return _sack.iterator()
    }

    override function add(x: T): boolean {
      return _sack.add( x )
    }
  }

  function testMe() {
    var list1: Collection<String> = new Sack<String>()
    var p: Getter<CharSequence> = list1 // covariant generics for free
    for( e in p ) { // calls iterator() implicitly
      print( e )
    }

    var p2 : Getter<String> = list1
    p2 = p // error: enforce covariant generics :)  //## issuekeys: MSG_TYPE_MISMATCH

    var list2: Collection<CharSequence> = new Sack<CharSequence>()
    var c: Setter<String> = list2  // contravariant generics for free
    c.add( "hi" )

    p = list2
    c = list1

    var c2: Setter<CharSequence> = list2
    c2 = c // error: enforce contravariant generics :)  //## issuekeys: MSG_TYPE_MISMATCH

    list1 = list2 // error: enforce invariant generics :)  //## issuekeys: MSG_TYPE_MISMATCH
    list2 = list1 // error: enforce invariant generics :)  //## issuekeys: MSG_TYPE_MISMATCH

    var list3 : Collection<String>
    list1 = list3 // invariance ok
  }

  abstract class Prod<out T> { // extends Foo<T> implements Hi<T> {
    var _t: T  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    var _foo: Foo<T>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    var _bar: Bar<T>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    var _baz: Baz<T>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    abstract function foo( h: T )  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    abstract function foo1() : T

    abstract function foo_cb( cb(t:T) )
    abstract function foo_cb1( cb():T )  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    abstract function foo_cb2() : block(): T
    abstract function foo_cb3() : block(t:T)  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    abstract function foo_g( t: Hi<T> )
    abstract function foo_g1() : Hi<T>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR

    abstract function foo_gg( t: Hi<Hi<T>> )  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    abstract function foo_gg1() : Hi<Hi<T>>

    abstract function foo_h( b: Baz<T> )  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    abstract function foo_h() : Baz<T>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  }

 abstract class Helo<out T>
    extends Bar<T>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
    implements Hi<T>,  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
               Getter<T>
  {
  }

  abstract class Helo2<in T>
      extends Bar<T>
      implements Hi<T>,
          Getter<T>  //## issuekeys: MSG_TYPE_VAR_VARIANCE_ERROR
  {
  }

  abstract class Helo3<in out T>
      extends Bar<T>
      implements Hi<T>,
          Getter<T>
  {
  }

  abstract class Helo4<T>
      extends Bar<T>
      implements Hi<T>,
          Getter<T>
  {
  }

  interface Hi<in T> {
    function foo2( t: T )
  }

  class Foo<out R> {}
  class Bar<in R> {}
  class Baz<in out R> {}
}