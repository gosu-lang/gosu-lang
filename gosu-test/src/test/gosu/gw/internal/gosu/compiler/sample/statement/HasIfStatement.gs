package gw.internal.gosu.compiler.sample.statement
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Byte
uses java.lang.Character
uses java.lang.Short
uses java.lang.Float
uses java.lang.Double

class HasIfStatement {
    
  static function pBooleanTest(arg : boolean) : String {
    if (arg) {
      return "if"
    } else {
      return "else"
    }
  }
  
  static function booleanTest(arg : Boolean) : String {
    if (arg) {
      return "if"
    } else {
      return "else"
    }  
  }
  
  static function pByteTest(arg : byte) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function byteTest(arg : Byte) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function pCharTest(arg : char) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function characterTest(arg : Character) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function pShortTest(arg : short) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function shortTest(arg : Short) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function pIntTest(arg : int) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function integerTest(arg : Integer) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function pLongTest(arg : long) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function longTest(arg : Long) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function pFloatTest(arg : float) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function floatTest(arg : Float) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function pDoubleTest(arg : double) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function doubleTest(arg : Double) : String {
    if (arg as boolean) {
      return "if"
    } else {
      return "else"
    }    
  }
  
  static function stringTest(arg : String) : String {
    if (arg.toBoolean()) {
      return "if"
    } else {
      return "else"
    }    
  }

  // TODO - AHK - Any other objects that can be auto-coerced?
  
  static function testWithNoElse(arg : String) : String {
    if (arg == "true") {
      return "if"
    }
    return "else"
  }
  
  static function testWithElse(arg : String) : String {
    if (arg == "true") {
      return "if"
    } else {
      return "else"
    }
  }
  
  static function testWithElseNonTerminal(arg : String) : String {
    var ret : String
    if (arg == "true") {
      ret = "if"
    } else {
      ret = "else"    
    }
    return ret
  }
  
  static function testIfWithNoCurlyBraces(arg : String) : String {
    if (arg == "true")
      return "if"
    return "else"  
  }

  static function testIfWithNoCurlyBracesWithVariableInsideInnerStatement(arg : String) : String {
    if (arg == "true")
      var localVar = "foo"
    return arg  
  }
   
  static function testIfElseWithNoCurlyBraces(arg : String) : String {
    if (arg == "true")
      return "if"
    else
      return "else"
  }
  
  static function testIfWithNoCurlyBracesOnElseWithVariableInsideInnerStatement(arg : String) : String {
    if (arg == "true")
      return "if"
    else
      var localVar = "bar"
    return "else"  
  }
  
  static function testSimpleNested(arg : String) : String {
    if (arg.startsWith("a")) {
      if (arg == "a1") {
        return "if"
      } else {
        return "else"
      }
    }
    
    return "neither"
  }
  
  static function testDeeplyNested(arg : String) : String {    
    if (arg.startsWith("a")) {
      if (arg.startsWith("aa")) {
        if (arg.startsWith("aaa")) {
          if (arg.startsWith("aaaa")) {
            return "aaaa-"
          }
        } else {
          return "aa-"
        }
      } else {
        if (arg.startsWith("ab")) {
          if (arg.startsWith("aba")) {
            return "aba-"
          } else {
            return "ab-"
          }
        }
      }
    } else {
      return "-"
    }
    
    return "neither"  
  }
  
  static function testCascadingIfElseWithTerminalElse(arg : String) : String {
    if (arg.startsWith("a")) {
      return "a-"       
    } else if (arg.startsWith("b")) {
      return "b-"
    } else if (arg.startsWith("c")) {
      return "c-"
    } else {
      return "else"
    }    
  }
  
  static function testCascadingIfElseWithNoElse(arg : String) : String {
    if (arg.startsWith("a")) {
      return "a-"       
    } else if (arg.startsWith("b")) {
      return "b-"
    } else if (arg.startsWith("c")) {
      return "c-"
    }
      
    return "else"   
  }
}