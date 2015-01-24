package gw.spec.core

class CompileTimeConstant {

  static final var TEST_STRING = "a"
  static enum C {c, C}
  static final var CAP_TEST_STRING = TEST_STRING.toUpperCase()

  static property get CompileTimeConstantExpr() : String {
    return TEST_STRING + "b" + C.c
  }

  static property get NotCompileTimeConstantExpr() : String {
    return CAP_TEST_STRING + "B" + C.C
  }

}
