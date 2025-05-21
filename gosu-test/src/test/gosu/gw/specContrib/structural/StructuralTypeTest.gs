package gw.specContrib.structural

class StructuralTypeTest extends gw.BaseVerifyErrantTest
{
  function testMetaTypeSatisfiesStructure() {
    assertEquals( "AB", foo( \f: Fas -> f.Name ) )
  }

  structure EnumValues<E extends Enum<E>> {
    function values() : E[]
  }

  enum Fas {
   A, B
  }

  reified function foo<T extends Enum<T>>( render(T): String ) : String {
    print( T )
    var values = (T as EnumValues<T>).values()
    var result = ""
    for( v in values ) {
      result += render( v )
    }
    return result
  }


  function testEnhancementOnStructure()
  {
    var c: MyStructure = new MyClass()
    assertEquals( "bar", c.bar() )
    assertEquals( "bar", c.hard( c ) )
    assertEquals( "MyProp", c.MyProp )
  }
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

  function testStructuralExpansionOperator()
  {
    var o = new Outer()
    var ll : List<Outer> = {o,o,o}

    assertEquals( "[9hi, 9hi, 9hi, 9hi, 9hi, 9hi, 9hi, 9hi, 9hi]", java.util.Arrays.toString( ll*.classArray ) )
    assertEquals( "[9hi, 9hi, 9hi, 9hi, 9hi, 9hi, 9hi, 9hi, 9hi]", java.util.Arrays.toString( ll*.structArray ) )

    assertEquals( "[9, 9, 9, 9, 9, 9, 9, 9, 9]", java.util.Arrays.toString( ll*.classArray*.N ) )
    assertEquals( "[9, 9, 9, 9, 9, 9, 9, 9, 9]", java.util.Arrays.toString( ll*.structArray*.N ) )
  }
  structure Struct {
    property get N(): int
  }
  static class Nominal {
    var n : int as N = 9
    function toString() : String {
      return N + "hi"
    }
  }
  static class Outer {
    var c = new Nominal()
    var classArray : Nominal[] = {c,c,c}
    var structArray : Struct[] = {c,c,c}
  }
}