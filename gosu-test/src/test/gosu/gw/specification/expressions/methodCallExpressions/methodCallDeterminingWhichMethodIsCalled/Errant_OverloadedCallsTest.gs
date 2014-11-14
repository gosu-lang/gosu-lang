package gw.specification.expressions.methodCallExpressions.methodCallDeterminingWhichMethodIsCalled

uses gw.specification.types.signaturesAndSubsumption.*
uses java.util.*
uses java.lang.Integer

class Errant_OverloadedCallsTest {
  function callsWithPrimitives() {
    var a = new GA()
    a.m(1.0, 1.0)
    a.m(1.0, 1)
    a.m(1, 1.0)
    a.m(1, 1)

    var b = new GB()
    b.m(1.0, 1.0)  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    b.m(1.0, 1)
    b.m(1, 1.0)  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    b.m(1, 1)

    var c = new GC()
    c.m(1.0, 1.0)  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    c.m(1.0, 1)  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    c.m(1, 1.0)
    c.m(1, 1)

    var d = new GD()
    d.m(1.0, 1.0)  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    d.m(1.0, 1)
    d.m(1, 1.0)
    d.m(1, 1)   //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION

    var e = new GE()
    e.m(1.0, 1.0)  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    e.m(1.0, 1)
    e.m(1, 1.0)
    e.m(1, 1)

    var f = new GF()
    f.m(1.0, 1.0)
    f.m(1.0, 1)
    f.m(1, 1.0)
    f.m(1, 1)

    var g = new GG()
    g.m(1.0, 1.0)
    g.m(1.0, 1)
    g.m(1, 1.0)
    g.m(1, 1)

    var h = new GH()
    h.m(1.0, 1.0)
    h.m(1.0, 1)
    h.m(1, 1.0)
    h.m(1, 1)

    var i = new GI()
    i.m(1.0, 1.0)
    i.m(1.0, 1)
    i.m(1, 1.0)
    i.m(1, 1)

    var j = new GJ()
    j.m(1.0, 1.0)
    j.m(1.0, 1)
    j.m(1, 1.0)
    j.m(1, 1)

    var k = new GK()
    k.m(1.0, 1.0)
    k.m(1.0, 1)
    k.m(1, 1.0)
    k.m(1, 1)  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION

    var l = new GL()
    l.m(true as int)
    l.m(true as long)
    l.m('1' as int)
    l.m('1' as long)
    l.m(1 as byte)
    l.m(1b)
    l.m(1 as short)
    l.m(1s)
    l.m(1 as int)  //## issuekeys: MSG_UNNECESSARY_COERCION
    l.m(1)
    l.m(1 as long)
    l.m(1L)
    l.m(1.0f as int)
    l.m(1.0f as long)
    l.m(1.0 as int)
    l.m(1.0 as long)
  }

  function callsWithObjs() {
    var a = new GAObj()
    a.m(new GAnimal(), new GAnimal())
    a.m(new GAnimal(), new GDog())
    a.m(new GDog(), new GAnimal())
    a.m(new GDog(), new GDog())

    var b = new GBObj()
    b.m(new GAnimal(), new GAnimal())  //## issuekeys: MSG_TYPE_MISMATCH
    b.m(new GAnimal(), new GDog())
    b.m(new GDog(), new GAnimal())  //## issuekeys: MSG_TYPE_MISMATCH
    b.m(new GDog(), new GDog())

    var c = new GCObj()
    c.m(new GAnimal(), new GAnimal())  //## issuekeys: MSG_TYPE_MISMATCH
    c.m(new GAnimal(), new GDog())  //## issuekeys: MSG_TYPE_MISMATCH
    c.m(new GDog(), new GAnimal())
    c.m(new GDog(), new GDog())

    var d = new GDObj()
    d.m(new GAnimal(), new GAnimal())  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    d.m(new GAnimal(), new GDog())
    d.m(new GDog(), new GAnimal())
    d.m(new GDog(), new GDog())  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION

    var e = new GEObj()
    e.m(new GAnimal(), new GAnimal())  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    e.m(new GAnimal(), new GDog())
    e.m(new GDog(), new GAnimal())
    e.m(new GDog(), new GDog())

    var f = new GFObj()
    f.m(new GAnimal(), new GAnimal())
    f.m(new GAnimal(), new GDog())
    f.m(new GDog(), new GAnimal())
    f.m(new GDog(), new GDog())

    var g = new GGObj()
    g.m(new GAnimal(), new GAnimal())
    g.m(new GAnimal(), new GDog())
    g.m(new GDog(), new GAnimal())
    g.m(new GDog(), new GDog())

    var h = new GHObj()
    h.m(new GAnimal(), new GAnimal())
    h.m(new GAnimal(), new GDog())
    h.m(new GDog(), new GAnimal())
    h.m(new GDog(), new GDog())

    var i = new GIObj()
    i.m(new GAnimal(), new GAnimal())
    i.m(new GAnimal(), new GDog())
    i.m(new GDog(), new GAnimal())
    i.m(new GDog(), new GDog())

    var j = new GJObj()
    j.m(new GAnimal(), new GAnimal())
    j.m(new GAnimal(), new GDog())
    j.m(new GDog(), new GAnimal())
    j.m(new GDog(), new GDog())

    var k = new GKObj()
    k.m(new GAnimal(), new GAnimal())
    k.m(new GAnimal(), new GDog())
    k.m(new GDog(), new GAnimal())
    k.m(new GDog(), new GDog())  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION

  }

  function initFun0(x : Set) {}
  function initFun0(x : List) {}

  function initFun1(x : HashSet) {}
  function initFun1(x : ArrayList) {}

  function initFun2(x : ArrayList<Integer>) {}
  function initFun2(x : Integer[]) {}

  function initFun3(x : ArrayList) {}
  function initFun3(x : Object[]) {}

  function initFun4(x : HashSet<Integer>) {}
  function initFun4(x : ArrayList<Integer>) {}

  function callsWithInitializer() {
    initFun0( {1, 2})  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    initFun1( {1, 2})  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    initFun2( {1, 2})  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    initFun3( {1, 2})  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    initFun4( {1, 2})  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
  }

  class A {}
  class B {}
  class C {}

  function fun(i: int, j: int): A {return null }
  function fun(i: float, j: float): B {return null }

  function caller() {
    var r0 : A = fun(42.5f, 42)  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var r1 : B = fun(42.5f, 42)  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
  }

  function fun2(i: int, j: int): A {return null }
  function fun2(i: float, j: float): B {return null }
  function fun2(i: double, j: double): C {return null }

  function caller2() {
    var r0 : A = fun2(42.5f, 42)  //## issuekeys: MSG_TYPE_MISMATCH
    var r1 : B = fun2(42.5f, 42)  //## issuekeys: MSG_TYPE_MISMATCH
    var r2 : C = fun2(42.5f, 42)
  }

  function fun3(i: short, j: short): A {return null }
  function fun3(i: int, j: int): B {return null }

  function caller3() {
    var s : short
    var r0 : A = fun3(42s, 33333)  //## issuekeys: MSG_TYPE_MISMATCH
    var r1 : B = fun3(42s, 33333)
    var r2 : A = fun3(42s, s)
  }

  function fun4(i: long, j: long): A {return null }
  function fun4(i: float, j: float): B {return null }

  function caller4() {

    var r0 : A = fun4(42L, 42.5f)  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var r1 : B = fun4(42L, 42.5f)  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var r2 : B = fun4(42, 42.5f)

  }

  function fun11(i : float, j : float): A {return null }
  function fun11(i: float, j: int): B {return null }

  function caller11() {

    var r0 : A = fun11(1.0f, 1)  //## issuekeys: MSG_TYPE_MISMATCH
    var r1 : B = fun11(1.0f, 1)

  }

  function fun7<T>(s: T) : A {return null }
  function fun7(s: String) : B {return null }

  function caller7() {
    var r0 : A = fun7("")  //## issuekeys: MSG_TYPE_MISMATCH
    var r1 : B = fun7("")
    var r2 : A = fun7<String>("")
  }


  function fun8(arrayList1 : ArrayList<Integer>): A {return null}
  function fun8(hashMap1 : HashMap<Integer, String>): B {return null}

  function fun9(list11: HashMap<Integer, String>): A {return null}
  function fun9(list11: HashSet<Integer>): B {return null}

  function fun10(list11: List<Integer>): A {return null}
  function fun10(list22: String[]): B {return null}

  function callers() {
    var r1: A = fun8({1,2,3})
    var r2: B = fun8({1,2,3})  //## issuekeys: MSG_TYPE_MISMATCH

    var r3: A = fun9({1,2,3})    //## issuekeys: MSG_TYPE_MISMATCH
    var r4: B = fun9({1,2,3})

    var r5: B = fun10({"1", "2", "3"})
    var r6: A = fun10({"1", "2", "3"} as List<Integer>)  //## issuekeys: MSG_TYPE_MISMATCH
    var r7: B = fun10({  //## issuekeys: MSG_TYPE_MISMATCH, MSG_TYPE_MISMATCH
        1,
        "2",
        3
    })
  }


}
