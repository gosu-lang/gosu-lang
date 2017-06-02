package gw.specContrib.statements.usesStatement

uses java.io.Serializable

class ClassWithGenericMethods {
  static function f1<T>(t: T) {}

  static function f2<T>(t: T) {}

  static function f3<T>(l: List<T>) {}

  static function f4<T>(l: List<T>) {}

  static function f5<T>(l: List<T>) {}

  static function f6<T>(l: List<List<List<T>>>) {}

  static function f7<T>(l: List<List<List<T>>>) {}

  static function f8(l: List<List<List<String>>>) {}

  static function f9(l: List<List<List<String>>>) {}

  static function f10(l: List<List<List<String>>>) {}

  static function f11(l: List<List<List<String>>>) {}

  static function f12(l: List<List<List<String>>>) {}

  static function f13<T>(l: List<List<List<T>>>) {}

  static function f14<T extends Number>(l: List<List<List<T>>>) {}

  static function f15<T extends Number>(l: List<List<List<T>>>) {}

  static function f16<T extends Runnable & Serializable>(l: List<List<List<T>>>) {}
}