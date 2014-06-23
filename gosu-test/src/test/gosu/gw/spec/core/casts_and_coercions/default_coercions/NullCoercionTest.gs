package gw.spec.core.casts_and_coercions.default_coercions
uses gw.test.TestClass
uses java.util.ArrayList

class NullCoercionTest extends TestClass
{
  function testReferenceTypesAreCoercableFromTheNullType()
  {
    var str : String = null
    assertNull( str )
    var obj : Object = null
    assertNull( obj )
    var lst : List = null
    assertNull( lst )
  }
}
