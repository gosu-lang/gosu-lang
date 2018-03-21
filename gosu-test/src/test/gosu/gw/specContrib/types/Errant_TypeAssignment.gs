package gw.specContrib.types

uses gw.lang.reflect.IType

class Errant_TypeAssignment {

  function foo(): EntityInfo {
    var x:  Type = "Hi"  //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'TYPE<JAVA.LANG.OBJECT>'
    return bar(x) // this should be okay
  }

  function bar<T extends KeyableBean>(type: Type<T>): EntityInfo<T> {
    return null;
  }

  reified function whereTypeIs<R>( type : Type<R> ) : R[] {
    var result : List<R>
    return result.toTypedArray()
  }

  function testSwitchInference02() {
    var x : String
    switch(typeof x) { //## issuekeys: MSG_TYPE_MISMATCH
      case  Boolean:  //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'TYPE<JAVA.LANG.STRING>' TO 'TYPE<JAVA.LANG.BOOLEAN>'
    }
  }

  class KeyableBean { }

  class EntityInfo<T extends KeyableBean> {
  }


  class Ide1958 {

    construct(p: String) {}
    construct(p: Type<String>) {}
    function test() {
      var x: gw.lang.reflect.IType
      new Ide1958(x) //## issuekeys: CANNOT RESOLVE CONSTRUCTOR 'IDE1958(GW.LANG.REFLECT.ITYPE)'
    }

  }

  interface IEntityType { }
  class Bean { }

  class Ide3611 {

    function test(x : IEntityType) : Type<Bean> {
      return x as Type<Bean>      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'GW.ENTITY.IENTITYTYPE' TO 'TYPE<GW.PL.PERSISTENCE.CORE.BEAN>'
    }

    function test1(x : IType) : Type<Bean> {
      return x as  Type<Bean>
    }

    function test2() : java.util.LinkedList {
      var x: java.util.List
      return x as  java.util.LinkedList
    }

    function test3() : javax.script.Bindings {
      var x: java.lang.Object
      return  x as javax.script.Bindings
    }

  }

}
