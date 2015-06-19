package gw.specification.statements.choiceStatements.theIfElseStatement

class IfStmtMiscellaneousTestCases {

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