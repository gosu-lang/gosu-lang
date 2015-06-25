package gw.specContrib.types

uses gw.lang.reflect.IType
uses gw.lang.reflect.gs.IGosuClass

class Errant_MetaType {
  public static final var OH: String = "oh"
  static function staticFunc() {}

  class A<T> {
  }

  structure CharAt {
    function charAt(i: int): char
  }

  function test1() {
    var itype: IType
    var t: Type = itype
    print(t == String)
    var a: IType = t
    var ts: Type<String> = t
    var t1: Type<java.lang.CharSequence> = itype    //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.LANG.REFLECT.ITYPE', REQUIRED: 'TYPE<JAVA.LANG.CHARSEQUENCE>'
    var u3: Type<CharAt> = Runnable                 //## issuekeys: MSG_TYPE_MISMATCH
  }

  function test2() {
    var list111 = List as List    //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'TYPE<JAVA.UTIL.LIST<JAVA.LANG.OBJECT>>' TO 'JAVA.UTIL.LIST'
    var s = char as Runnable      //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'TYPE<CHAR>' TO 'JAVA.LANG.RUNNABLE'
  }

  function test3() {
    var x: Object
    var a = typeof(x)
    var b: int
    a = b                         //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'TYPE<JAVA.LANG.OBJECT>'
  }

  function test4() {
    var a: IGosuClass
    var b: Type = a
    var c: Type<Object> = a
  }

  function test5() {
    var a: java.lang.Class<Boolean>
    var b: Type<Boolean>
    a = b
    var t = a as Type<Boolean>
  }

  function test6() {
    var a: java.lang.Class<Boolean>
    var b: java.lang.Class<Object> = a
    var t = b as Type<Boolean>

    var o: Object
    switch (o.Class) {
      case Boolean:
    }

    var a3: java.lang.Class<ArrayList>
    var t3 = a3 as Type<CharSequence>

    var a5: java.lang.Class<AbstractList>
    var t5 = a5 as Type<ArrayList>
  }

  function test7() {
    var aaa: A
    var a1: A<String> = aaa        //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FOO.A', REQUIRED: 'FOO.A<JAVA.LANG.STRING>'
  }

  static function main(args: String[]) {
    var b = new Errant_MetaType()

    var t1 = Errant_MetaType
    var t2 = statictypeof b
    var t3 = Errant_MetaType.Type

    print(t1.OH)
    print(t2.OH)  //## issuekeys: MSG_INVALID_REFERENCE
    print(t3.OH)  //## issuekeys: MSG_INVALID_REFERENCE

    t1.staticFunc()
    t2.staticFunc()  //## issuekeys: MSG_INVALID_REFERENCE
    t3.staticFunc()  //## issuekeys: MSG_INVALID_REFERENCE
  }

  function foo<T>(): T[] {
    return T.Type.makeArrayInstance(2) as T[]
  }

  function test8() {
    var t1 = String.Type
    var t2 = boolean.Type
    var t3 = boolean[].Type
    var t4 = String[][].Type
    var t5 = java.util.List<Object>.Type
    // IDE-1797
    var t6 = gw.lang.reflect.Type<Object>.Type
  }

  static class GosuClass1 {
    enum Type {
      ONE
    }
    // IDE-2283
    var a = GosuClass1.Type.ONE   // here 'Type' is enum
  }

  // IDE-1958
  class Foo {
    construct(p: String) {}
    construct(p: Type<String>) {}

    function test() {
      var x: gw.lang.reflect.IType
      var p: Type<String>

      new Foo(x)  //## issuekeys: CANNOT RESOLVE

      x = p
      p = x       //## issuekeys: NOT ASSIGNABLE
    }
  }

  function testTypeOf() {
    var s: String
    var i: Integer
    var b = typeof s == typeof i
    var a1: Type<String> = typeof s
    var a2: Type<Object> = typeof s
    var b1: Type<Integer> = typeof i
    var b2: Type<Object> = typeof i
  }

  // IDE-2238
  function testA(o: Object) {
    if (typeof o == int) {
    }

    switch (typeof o) {
      case int:
        break
    }
  }

  function testB(){
    var y : Object = "neat"
    print(y as int)
    switch(typeof(y)){
      case int:
        break
      case String:
        break
    }
  }

  function testC(){
    var x = 1
    switch(typeof(x)){
      case int:
        break;
      case Integer:
        break;
    }
  }

  var myFoo: Type<Foo>
  function testIsAssignableFrom() {
    myFoo.isAssignableFrom(Foo)  //## issuekeys: CANNOT RESOLVE METHOD 'ISASSIGNABLEFROM(TYPE<GW.SPECCONTRIB.TYPES.ERRANT_METATYPE.FOO>)'
    myFoo.Type.isAssignableFrom(A)
  }

}