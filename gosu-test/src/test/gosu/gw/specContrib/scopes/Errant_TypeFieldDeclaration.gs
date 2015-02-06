package gw.specContrib.scopes

class Errant_TypeFieldDeclaration {
  static class A1 {
    var Type: int
  }

  static class A2 {
    static property get Type(): int { return 0 }
  }

  static class A3 {}
  static class A4 {}

  // IDE-1702
  function test(i: int) {
    var t1 = A1.Type
    i = t1                      //## issuekeys: INCOMPATIBLE TYPES
    var t2 = new A1().Type
    i = t2

    var t3 = A2.Type
    i = t3
    var t4 = new A2().Type
    i = t4

    var t5 = A3.Type
    i = t5                      //## issuekeys: INCOMPATIBLE TYPES
    var t6 = new A3().Type
    i = t6

    var t7 = A4.Type
    i = t7
    var t8 = new A4().Type
    i = t8
  }
}