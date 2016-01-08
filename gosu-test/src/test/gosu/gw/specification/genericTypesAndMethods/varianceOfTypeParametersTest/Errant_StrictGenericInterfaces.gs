package gw.specification.genericTypesAndMethods.varianceOfTypeParametersTest

class Errant_StrictGenericInterfaces {

  @StrictGenerics
  interface Setter<T> {
    function add( x: T ): boolean
  }

  @StrictGenerics
  interface Getter<T> {
    function iterator(): Iterator<T>
  }

  @StrictGenerics
  interface Collection<T> extends Setter<T>, Getter<T> {
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
}