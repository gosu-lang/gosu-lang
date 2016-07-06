package gw.specContrib.statements

class Errant_ReturnStatement {
  function doSomething(): int { return 0 }

  // IDE-1414
  function tricky1(val: boolean) {
    if (val)
      return
    doSomething()
  }

  function tricky2(val: boolean): int {
    if (val)
      return doSomething() // still fails due to if
  }      //## issuekeys: MISSING RETURN STATEMENT

  function tricky3(val: boolean): int {
    if (val)
      return
          doSomething() // still fails due to if
  }      //## issuekeys: MISSING RETURN STATEMENT


  function aaa(): boolean {
    return
        true
  }

  function bbb(): boolean {
    return
    true
  }

  function ccc(): boolean {
    return    // no error

        true      // no error
  }

  function ddd() {
    return
  }

  function trick1_else(val: boolean) {
    if (val)
      val = !val
    else
      return
    doSomething()
  }

  function trick1_brace(val: boolean) {
    if (val)
      val = !val
    else {
      return
          doSomething()   //## issuekeys: MSG_UNREACHABLE_STMT
    }
  }


  function nestedTricky(val: boolean): int {
    if (true) {
      if (true)
        return // Studio BAD: Missing return value
      doSomething()
      return
          doSomething()
    }
    return
        doSomething()
  }
}
