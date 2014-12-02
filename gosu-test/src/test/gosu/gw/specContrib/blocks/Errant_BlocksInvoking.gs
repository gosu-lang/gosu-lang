package gw.specContrib.blocks

uses java.lang.Double
uses java.lang.Integer

class Errant_BlocksInvoking {

  //Invoking Blocks
  function testInvokeBlocks() {
    var adder = \x: Integer, y: Double -> x + y
    var mysum1 = adder(10, 20)
    var mysum2 = adder(10, 20.5)
    var mysum3 = adder(10.5, 20.5)      //## issuekeys: 'INVOKE(JAVA.LANG.INTEGER, JAVA.LANG.DOUBLE)' IN '' CANNOT BE APPLIED TO '(DOUBLE, DOUBLE)'

    var blockReturningFloat111: block(x: float): float = \x -> x
    var checkReturnType1111: int = blockReturningFloat111(42.4)      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'FLOAT', REQUIRED: 'INT'
    var checkReturnType1112: float = blockReturningFloat111(42.4)

    var blockReturningFloat112: block(): float = \-> 42.5f
    var checkReturnType1121 = blockReturningFloat112()
    var checkReturnType1122: float = blockReturningFloat112()
    var checkReturnType1123: float = blockReturningFloat112(42.5f)      //## issuekeys: 'INVOKE()' IN '' CANNOT BE APPLIED TO '(FLOAT)'
  }

}