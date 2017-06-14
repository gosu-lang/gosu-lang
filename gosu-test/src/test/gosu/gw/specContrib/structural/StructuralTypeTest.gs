package gw.specContrib.structural

class StructuralTypeTest extends gw.BaseVerifyErrantTest
{
  static class MyClass
  {
    function foo()
    {
      print( "foo" )
    }
  }

  /**
   * https://github.com/gosu-lang/gosu-lang/issues/114
   */
  function testWhereTypeIsUsingStructure()
  {
    var x = new MyClass()
    var y = new Object()
    var inputs = { x, y }
    assertEquals( { x }, inputs.whereTypeIs( MyStructure ) )
  }
}