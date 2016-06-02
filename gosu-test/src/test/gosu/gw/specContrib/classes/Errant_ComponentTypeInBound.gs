package gw.specContrib.classes

class Errant_ComponentTypeInBound<F extends int & Comparable> {      //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE
  interface MyInterface {}

  var comp1 : int & MyInterface        //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE

  class BB<T extends Object[]> {}
  class A<T extends int & MyInterface> {}        //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE
  class B<T extends Object[] & MyInterface> {}      //## issuekeys: ARRAYS ARE NOT ALLOWED IN A COMPONENT TYPE
  class C<T extends MyInterface & Object[]> {}       //## issuekeys: ARRAYS ARE NOT ALLOWED IN A COMPONENT TYPE
  class D<T extends int> {
    function do1(): T {
      return null
    }
  }

  function test<T extends int & MyInterface>() {      //## issuekeys: PRIMITIVES ARE NOT ALLOWED IN A COMPONENT TYPE
    var i = new D().do1().intValue()
  }
}

