package gw.specContrib.statements.usesStatement

class ClassWithBlocks {
  static function f1(b: block(String, Integer): Long) {}

  static function f2<T1, T2, T3>(b: block(T1, T2): T3) {}

  static function f3<T1 extends String, T2 extends Long, T3 extends Integer>(b: block(T1, T2): T3) {}
}