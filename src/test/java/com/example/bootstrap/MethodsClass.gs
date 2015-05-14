package com.example.bootstrap

class MethodsClass extends MethodClassParent {

  function publicMethod() {}

  function publicMethodWithArgs( str : String, i : int ) {}

  private function privateMethod() {}

  /** publicMethodWithDocComment comment */
  function publicMethodWithDocComment() {}

  /* publicMethodWithCComment comment */
  function publicMethodWithCComment() {}

  // publicMethodWithLineComment comment
  function publicMethodWithLineComment() {}

  /**
   * @throws java.lang.NullPointerException
   */
  function publicMethodWithJavaStyleThrows() {}

  /** staticPublicMethodWithDocComment comment */
  static function staticPublicMethodWithDocComment() {}

}