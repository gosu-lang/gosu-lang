package gw.specContrib.statements.usesStatement

class ClassWithOverriddenMethods {
  static function f1(i: int): int {
    return i
  }

  static function f1(s: String): String {
    return s
  }

  static function f2(i: int): int {
    return i
  }

  static function f2(s: String): String {
    return s
  }

  static function f3(s: String): String {
    return s
  }

  static function f3(i: int): int {
    return i
  }
}