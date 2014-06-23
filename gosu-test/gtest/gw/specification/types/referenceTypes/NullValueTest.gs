package gw.specification.types.referenceTypes

uses gw.BaseVerifyErrantTest

class NullValueTest extends BaseVerifyErrantTest {
  structure struct {
  }

  enum enumeration {ONE, TWO}

  function testReferenceTypesAreCoercableFromTheNullType()
  {
    var str : String = null
    assertNull( str )
    var obj : Object = null
    assertNull( obj )
    var lst : List = null
    assertNull( lst )
    var blk : block(s : String) : int = null
    assertNull( blk )
    var arr : int[] = null
    assertNull(arr)
    var struct : struct = null
    assertNull(struct)
    var e : enumeration = null
    assertNull(e)
  }

  function testErrant_NullValueTest() {
    processErrantType(Errant_NullValueTest)
  }

}