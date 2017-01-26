class Errant_TypeAssignment {

  function foo(): EntityInfo {
    var x:  Type = "Hi"  //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.LANG.STRING', REQUIRED: 'TYPE<JAVA.LANG.OBJECT>'
    return bar(x) // this should be okay
  }

  function bar<T extends KeyableBean>(type: Type<T>): EntityInfo<T> {
    return null;
  }

  function whereTypeIs<R>( type : Type<R> ) : R[] {
    var result : List<R>
    return result.toTypedArray()
  }

  function testSwitchInference02() {
    var x : String
    switch(typeof x) {
      case  Boolean:  //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'TYPE<JAVA.LANG.STRING>' TO 'TYPE<JAVA.LANG.BOOLEAN>'
    }
  }

  class EntityInfo<T extends KeyableBean> {
  }

  class KeyableBean { }

  class Ide1958 {

    construct(p: String) {}
    construct(p: Type<String>) {}
    function test() {
      var x: gw.lang.reflect.IType
      new Ide1958(x) //##issuekeys: CANNOT RESOLVE CONSTRUCTOR 'IDE1958(GW.LANG.REFLECT.ITYPE)'
    }

  }

}
