package gw.specification.statements.choiceStatements.theIfElseStatement

uses java.lang.Character
uses java.lang.Byte
uses java.lang.Short
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double

class IfStmtExpressionTypeConversion {
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
}