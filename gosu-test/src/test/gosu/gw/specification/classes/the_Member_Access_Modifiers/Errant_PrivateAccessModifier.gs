package gw.specification.classes.the_Member_Access_Modifiers

class Errant_PrivateAccessModifier {
  private var f0: int
  var f6: int
  public var f4: int

  private function m0() {}
  function m6() {}

  class nested {
    private var f1: int = f0
    public var foo : int = f0

    function m(): void {
      var tmp : int
      tmp = f0
      tmp = f1
      tmp = f6
      m0()
      m6()
    }
  }

  class subNested extends nested {
    var f4 : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var f0 : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var foo : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var foo2 : int = f1
    var foo3 : int = new nested().f1
  }

  function m(): void {
    var tmp : int
    tmp = f0
    tmp = new nested().f1
    tmp = f6
    m0()
    m6()
  }

  class nested2 {
    private var f3: int = new nested().f1
    private var f5: int = f6

    function bar() {
      m0()
      m6()
    }
  }

  function testObvious() {
    var array = "".value  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  }
}