package gw.specContrib.generics

class Errant_DeclarationSiteVariance {

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

//
//  class PString implements Prod<String> {
//    override function foo( cb(t:String) ) {
//      cb( "" )
//    }
//    override function foo2( h: Hi<String> ) {}
//  }
//  class PChar implements Prod<CharSequence> {
//    override function foo( cb(t:CharSequence) ) {
//      cb( new StringBuilder() )
//    }
//    override function foo2( h: Hi<CharSequence> ) {}
//  }
//
//  var x: Prod<CharSequence>
//  var y: Prod<String>
//  function asdf() {
//    x = y
////   x.foo( \ t:Object -> null )
//  }
}