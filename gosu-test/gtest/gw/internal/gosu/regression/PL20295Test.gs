package gw.internal.gosu.regression

uses gw.test.TestClass

uses java.lang.Exception

class PL20295Test extends TestClass {

  function testSwitchWithVarsInDefaultBlockWorks() {
    assertTrue( doTest("yay") )
  }

  function doTest(s: String) : boolean {
    try {
      switch(s.length) {
        case 0:
        case 1:

        break

        default:
          var i = 5
      }
    } catch (ex : Exception) {
      print(ex)
    }
    return true
  }

}
