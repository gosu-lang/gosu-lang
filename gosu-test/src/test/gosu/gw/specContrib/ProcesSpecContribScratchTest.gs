package gw.specContrib

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuClass

class ProcesSpecContribScratchTest extends BaseVerifyErrantTest {

  function testIt() {
    // add the test in question here
    var t = gw.specContrib.expressions.cast.Errant_CastToPrimitiveTest

    if((t as IGosuClass).ParseResultsException != null ) {
      print((t as IGosuClass).ParseResultsException)
    } else {
      print( "No errors found..." )
    }
    processErrantType(t)
  }

}