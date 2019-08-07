package gw.specContrib.classes

class Errant_ComponentTypeInBound<F extends int & Comparable> {      //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE
  interface MyInterface {}

  var comp1 : int & MyInterface        //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE

  class BB<T extends Object[]> {}
  class A<T extends int & MyInterface> {}        //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE
  class B<T extends Object[] & MyInterface> {}      //## issuekeys: ARRAYS ARE NOT ALLOWED IN A COMPONENT TYPE
  class C<T extends MyInterface & Object[]> {}       //## issuekeys: ARRAYS ARE NOT ALLOWED IN A COMPONENT TYPE
  class D<T extends int> { //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE
    function do1(): T {
      return null
    }
  }
  class E<T> {
    function do1(): T {
      return null
    }
  }

  function test1<T extends Integer & MyInterface>() {
    var i = new D().do1() // No error because new D() => new D<Integer>()
  }

  function test2<T extends Integer & MyInterface>() {
    var i = new D<T>().do1() //## issuekeys: test2() must be declared with the "reified" modifier to access the type variable "T" at runtime
  }

  function test3<T extends Integer & MyInterface>() {
    var i = new E<T>().do1()  //## issuekeys: test3() must be declared with the "reified" modifier to access the type variable "T" at runtime
  }

  function test4<T>() {
    var i = new E().do1()  // No error because new D() => new D<Object>()
  }

  function test5<T extends int & MyInterface>() { //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE
    var i = new D().do1()
  }
}

