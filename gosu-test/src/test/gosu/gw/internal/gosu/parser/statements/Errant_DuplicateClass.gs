class Errant_DuplicateClass {

  class A<T extends A<T>> {
    function foo(): A {
      return null
    }
  }

  class B extends A<B> {      //## issuekeys: CYCLIC INHERITANCE INVOLVING 'GOSUONE.B'
    function foo(): A {
      return null
    }
  }

  class A extends B {      //## issuekeys: DUPLICATE CLASS: 'A'
  }

}
