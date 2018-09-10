package gw.specContrib.blocks

uses gw.test.TestClass

class BlockMiscTest extends TestClass {

  function testCharInit() {
    var c: char
    var fun = \-> print( c )
    fun()
  }

}