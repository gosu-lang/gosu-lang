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
      return doSomething()
  }   //## issuekeys: MISSED RETURN

  function tricky3(val: boolean): int {
    if (val)
      return
        doSomething()
  }   //## issuekeys: MISSED RETURN
}