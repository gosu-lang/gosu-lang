package gw.specContrib.shadowing

/**
 Testing inner class, static nested clas, anonymous class
 */
class Errant_Shadowing_SubClass extends Errant_Shadowing_BaseClass {
  var baseInt = 55

  var baseSubVar = "sdf"            //## issuekeys: VARIABLE 'BASESUBVAR' IS ALREADY DEFINED IN THE SCOPE

  var subVar: String
  public var enclosingVar: String = "non-static"
  public static var enclosingStaticVar: String = "static"


  //Property - Overriding in sub class

  override property get baseProperty(): String {
    return "sub foo"
  }

  override property set baseProperty(str1: String) {
    print("set baseProperty in subclass")
  }
  //Property end



  override function testShadowing(str: String) {
    var baseSubVar = "should be an error"            //## issuekeys: VARIABLE 'BASESUBVAR' IS ALREADY DEFINED IN THE SCOPE
  }

  class innerClass {
    var enclosingVar = "was warning earlier but now an error"      //## issuekeys: VARIABLE 'ENCLOSINGVAR' IS ALREADY DEFINED IN THE SCOPE


    function somefun() {
      print(baseInt)
    }
  }

  static class staticNestedClass {
    //No Warning/error because encloingVar is not static outside
    var enclosingVar = "hello"

    //Error as enclosingStaticVar is a static field in outer class
    var enclosingStaticVar = "hello"      //## issuekeys: VARIABLE 'ENCLOSINGSTATICVAR' IS ALREADY DEFINED IN THE SCOPE


    function somefun() {
      print(enclosingVar)
      print(enclosingStaticVar)

    }

  }

  function encloseAnonymousClass() {
    var anonymousLocalVar = "something"
    var pInstance = new Errant_Shadowing_SubClass() {
      var enclosingVar = "outer most string"            //## issuekeys: VARIABLE 'ENCLOSINGVAR' IS ALREADY DEFINED IN THE SCOPE

      var anonymousLocalVar = "something"            //## issuekeys: VARIABLE 'ANONYMOUSLOCALVAR' IS ALREADY DEFINED IN THE SCOPE


      function read() {
        print(enclosingVar)
        print("Reading from anonymous class")
      }

      function write() {
        print("Writing to/from anonymous class")
      }
    }
  }
}



