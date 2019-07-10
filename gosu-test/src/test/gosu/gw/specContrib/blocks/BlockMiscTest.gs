package gw.specContrib.blocks

uses gw.test.TestClass

class BlockMiscTest extends TestClass {
  function testBadFunctionCallBacksOutAndRemovesBlock() {
    assertEquals( "bye", runMe() )
  }

  function testCharInit() {
    var c: char
    var fun = \-> print( c )
    fun()
  }

  function runMe(): String {
    var res = "hi"

    // This is ambiguous: res, or res(... )?  It's res, but the block
    // resulting from trying res(...) needs to be removed from the parse tree
    var Xxx = res
    (\ -> {
        Xxx = "bye"
    })()
    return Xxx
  }
}