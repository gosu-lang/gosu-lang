package gw.specContrib.typeinference

uses java.util.Date
uses java.util.List
uses java.util.Map

class Errant_IfTypeNarrowing {
  interface I1 {
    function foo1()
  }

  interface I2 {
    function foo2()
  }

  class A implements I1 {
    function foo1() {}
  }

  class B implements I2 {
    function foo2() {}
  }

  function test() {
    var x: Object = "neat"

    if (x typeis String) {
      print(x.charAt(0))
    } else if (x typeis Date) {
      print(x.Time)
    }

    // IDE-2031
    if (x typeis I1) {
      if (x typeis I2) {
        x.foo1()
        x.foo2()
      }
    }

    if (x typeis I1 && x typeis I2) {
      x.foo1()
      x.foo2()
    }

    var i1: I1
    if (i1 typeis I2) {
      i1.foo1()
      i1.foo2()
    }

    var l: List<Object>
    // IDE-2131
    if (l.get(0) typeis A &&
        l.get(1) typeis B) {
    }

    if (l.get(0) typeis A) {
      l.get(0).foo1()                //## issuekeys: CANNOT RESOLVE 'foo1()'
    }

    var arr: Object[]
    if (arr[0] typeis A) {
      arr[0].foo1()                 //## issuekeys: CANNOT RESOLVE 'foo1()'
    }
    var ind: int
    if (arr[ind] typeis A) {
      arr[ind].foo1()               //## issuekeys: CANNOT RESOLVE 'foo1()'
    }

    var map: Map<String, Object>
    if (map["key"] typeis A) {
      map["key"].foo1()             //## issuekeys: CANNOT RESOLVE 'foo1()'
    }
    var key: String
    if (map[key] typeis A) {
      map[key].foo1()               //## issuekeys: CANNOT RESOLVE 'foo1()'
    }
  }
}