package com.example.bootstrap

class Example {

  construct() {
  }

  function foo() {
  }

  /**
   * @return this
   */
  function func2() : Example {
    return this
  }

  /**
   * @return this
   */
  function func3() : block():String {
    return null
  }

  /**
   * @return false
   */
  property get Bar() : boolean {
    return false
  }

}