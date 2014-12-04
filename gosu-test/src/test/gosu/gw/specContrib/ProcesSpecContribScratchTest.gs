package gw.specContrib

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuClass

class ProcesSpecContribScratchTest extends BaseVerifyErrantTest {

  function testIt() {
    // add the test in question here
    var t = gw.specContrib.expressions.Errant_FeatureLiteralMethodLookup

    if(not (t as IGosuClass).Valid) {
      print((t as IGosuClass).ParseResultsException)
    } else {
      print( "No errors found..." )
    }
    processErrantType(t)
  }

}