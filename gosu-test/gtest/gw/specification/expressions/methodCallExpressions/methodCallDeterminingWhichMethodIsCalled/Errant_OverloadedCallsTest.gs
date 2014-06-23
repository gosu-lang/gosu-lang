package gw.specification.expressions.methodCallExpressions.methodCallDeterminingWhichMethodIsCalled

uses gw.specification.types.signaturesAndSubsumption.*

class Errant_OverloadedCallsTest {
  function callsWithPrimitives() {
    var a = new GA()
    a.m(1.0, 1.0)
    a.m(1.0, 1)
    a.m(1, 1.0)
    a.m(1, 1)

    var b = new GB()
    b.m(1.0, 1.0)  //## issuekeys: MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE
    b.m(1.0, 1)
    b.m(1, 1.0)  //## issuekeys: MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE
    b.m(1, 1)

    var c = new GC()
    c.m(1.0, 1.0)  //## issuekeys: MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE
    c.m(1.0, 1)  //## issuekeys: MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE
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
}
