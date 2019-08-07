package gw.specContrib.generics

abstract class Errant_ReifiedTest2<X> implements TestItf<X> {

  var myX : X

  function foo1<T,U>(t : T, u : U) {
    new T()      //## issuekeys: THE METHOD 'FOO1<T,U>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'T' AT RUNTIME
    new U()      //## issuekeys: THE METHOD 'FOO1<T,U>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'U' AT RUNTIME
  }

  function foo2<T>(t : T) {
    if (t typeis String) {
      new T()       //## issuekeys: THE METHOD 'FOO2<T>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'T' AT RUNTIME
    }
  }

  reified function foo3<T>(t : T) {     //## issuekeys: MUST OVERRIDE FUNCTION WITH THE SAME 'REIFIED' SETTING
    if (t typeis String) {
      new T()
    }
  }

  abstract function foo4<T>(t : T);

  reified abstract function foo5<T>(t : T);

  function foo6<T>(t : T) { }

  reified function foo7<T>(t : T) { }

  function foo<T>() : T[] {
    return T.Type.makeArrayInstance(2) as T[]      //## issuekeys: THE METHOD 'FOO<T>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'T' AT RUNTIME
  }

  reified function gatherNoReturn<E>(e : E) {
    var list : List
    for (elem in list) {
      if( elem typeis E ) {
        process( elem )
      }
    }
  }

  reified function gatherOnlyReturn<E>() : List<E> {
    return new ArrayList<E>()
  }

  reified function gatherBoth<E>(e : E) : List<E> {
    var list : List
    for (elem in list) {
      if( elem typeis E ) {
        process( elem )
      }
    }
    return new ArrayList<E>()
  }

  reified function bar() {      //## issuekeys: THE METHOD 'BAR' HAS NOTHING TO REIFY
    var v : String
  }

  reified function bar2() : X {   //## issuekeys: THE METHOD 'BAR2' HAS NOTHING TO REIFY
    return this as X
  }

  function process<E>(elem : E) {
    if (typeof E == String) { }  //## issuekeys: THE METHOD 'PROCESS<E>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'E' AT RUNTIME
    if (typeof elem == E) { }    //## issuekeys: THE METHOD 'PROCESS<E>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'E' AT RUNTIME
    if (elem typeis E ) { }      //## issuekeys: THE METHOD 'PROCESS<E>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'E' AT RUNTIME
    if (typeof X == String) { }  // Okay because X is not a type parameter of process()
    if (myX typeis String) { }
  }

  function process2<E>(elem : E) {
    gatherNoReturn(elem);       //## issuekeys: THE METHOD 'PROCESS2<E>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'E' AT RUNTIME
    var result = gatherOnlyReturn(); // <<<< okay because type param is only on return type
    gatherBoth(elem);      //## issuekeys: THE METHOD 'PROCESS2<E>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'E' AT RUNTIME
  }

  function process3<E>(elem : E) {
    foo5(myX) // okay because myX does not need to be reified
  }

  function process4<E>(elem : E) {
    foo5(elem)      //## issuekeys: THE METHOD 'PROCESS4<E>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'E' AT RUNTIME
  }

  function process5<T>(t : T) : T {
    return this as T       //## issuekeys: THE METHOD 'PROCESS5<T>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'T' AT RUNTIME
  }

  function process6<T>(t : T) : X {
    return this as X // okay because X is not a method type parameter
  }

  function process7<T>(t : T) {
    var typ = (typeof T).TypeInfo  //## issuekeys: THE METHOD 'PROCESS7<T>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'T' AT RUNTIME
  }

  function process8<T>(t : T) {
    var typ = (typeof t).TypeInfo  // okay because typeof is on t, not T, so no reification of T is necessary
  }

  function process9<T>(t : T) {
    if (typeof t == T) { }        //## issuekeys: THE METHOD 'PROCESS9<T>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'T' AT RUNTIME
  }

  function process10<T>(t : T) {
    if (t.Class == T.Class) { }        //## issuekeys: THE METHOD 'PROCESS10<T>' MUST BE DECLARED WITH THE 'REIFIED' MODIFIER TO ACCESS THE TYPE VARIABLE 'T' AT RUNTIME
  }

}
