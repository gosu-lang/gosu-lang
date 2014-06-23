package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

class Errant_constructorsTest {

  class X {
    var a : int
    construct() {
      a = 0
    }
  }

  class Y extends X {
    construct() {
      var b = 0
      super()  //## issuekeys: MSG_NO_SUCH_FUNCTION
    }
  }

  class Y2 extends X {
    construct() {
      super()
      var b = 0
    }
  }

  class XPrivate {
    var a : int
    private construct() {
      a = 0
    }
  }

  class Y3 extends XPrivate {
    construct() {
      super()  //## issuekeys: MSG_NO_SUCH_FUNCTION
      var b = 0
    }
  }

  class W {
    var a : int
    construct(val : int) {
      a = val
    }
  }

  class Z extends W {
    construct() {
      super()  //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
      var b = 0
    }
  }

  class V extends W {
    construct() {
      super(2)
      var b = 0
    }
  }

}
