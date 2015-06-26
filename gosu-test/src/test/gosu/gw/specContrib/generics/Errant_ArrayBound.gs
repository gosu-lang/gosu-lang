package gw.specContrib.generics

class Errant_ArrayBound {
  class A {}
  class B<T> {}

  function foo1<K extends A[]>(p: B<K>) {}
  function foo2<K extends A[][]>(p: B<K>) {}

  function test() {
    var b0: B<A>
    foo1(b0)          //## issuekeys: INFERRED TYPE FOR 'K' IS NOT WITHIN ITS BOUNDS
    foo2(b0)          //## issuekeys: INFERRED TYPE FOR 'K' IS NOT WITHIN ITS BOUNDS

    // IDE-2144
    var b1: B<A[]>
    foo1(b1)
    foo2(b1)          //## issuekeys: INFERRED TYPE FOR 'K' IS NOT WITHIN ITS BOUNDS

    var b2: B<A[][]>
    foo1(b2)          //## issuekeys: INFERRED TYPE FOR 'K' IS NOT WITHIN ITS BOUNDS
    foo2(b2)
  }
}