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

  /** publicMethodWithOneArgNoComment comment */
  function publicMethodWithOneArgNoComment( str: String ) {}


  /** publicMethodWithOneArgGosuStyleComment comment
   *
   *   @param str javadoc-style comment
   *
   * */
  function publicMethodWithOneArgJavaDocStyleComment( str: String ) {}

  /** publicMethodWithOneArgGosuStyleComment comment */
  @Param( 'str', 'gosu-style param' )
  function publicMethodWithOneArgGosuStyleComment( str: String ) {}

  /** publicMethodWithOneArgsAndParamsAnnotationComment comment */
  @Params({
      new( 'str', 'str1 param' )
  })
  function publicMethodWithOneArgsAndParamsAnnotationComment( str: String ) {}

  /** publicMethodWithTwoArgNoComment comment */
  function publicMethodWithTwoArgNoComment( str: String, str2 : String ) {}

  /** publicMethodWithTwoArgGosuStyleComment comment
   *
   *   @param str javadoc-style comment
   *   @param str2 javadoc-style comment 2
   *
   * */
  function publicMethodWithTwoArgJavaDocStyleComment( str: String, str2 : String ) {}

  /** publicMethodWithTwoArgGosuStyleComment comment */
  @Param( 'str', 'gosu-style param' )
  @Param( 'str2', 'gosu-style param 2' )
  function publicMethodWithTwoArgGosuStyleComment( str: String, str2 : String ) {}

  /** publicMethodWithTwoArgsAndParamsAnnotationComment comment */
  @Params({
      new( 'str', 'str1 param' ),
      new( 'str2', 'str2 param' )
  })
  function publicMethodWithTwoArgsAndParamsAnnotationComment( str: String, str2 : String ) {}


}