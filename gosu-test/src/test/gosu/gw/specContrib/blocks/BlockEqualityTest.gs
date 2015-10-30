package gw.specContrib.blocks

uses gw.test.TestClass
uses gw.lang.reflect.IFunctionType

class BlockEqualityTest extends TestClass {
  function testIdentityEqualityForBlockInstances() {
    var b1 : block(i : int) = \i->print("")
    var b2 : block(j : int) = \j->print("")
    assertFalse( b1.equals( b2 ) )
    assertTrue( b1.equals( b1 ) )
    assertFalse( b1 == b2 )
    assertTrue( b1 == b1 )
  }

  function testStructuralEqualityForBlockTypes() {
    assertEquals( block():String, typeof \->"hello" )
    assertEquals( block(i: int), typeof \i:int->print(i) )

    var b1 : block(i : int) = \i->print("")
    var b2 : block(j : int) = \j->print("")
    var b3 : block(j : String) = \j->print("")
    var b4 : block(j : int):int= \j->j

    assertTrue( (typeof b1) typeis IFunctionType )

    assertTrue( (typeof b1).equals( typeof b2 ) )
    assertTrue( (typeof b1) == (typeof b2) )

    assertFalse( (typeof b1) == (typeof b3) )
    assertFalse( (typeof b1) == (typeof b4) )

    assertTrue( block(i : int).equals(block(j : int)) )
    assertTrue( (block(i : int):int).equals(block(j : int):int) )
    assertFalse( (block(i : String):int).equals(block(j : int):int) )
  }
}
