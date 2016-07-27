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

  function foo<T extends Enum<T>>( render(T): String ) : String {
    print( T )
    var values = (T as EnumValues<T>).values()
    var result = ""
    for( v in values ) {
      result += render( v )
    }
    return result
  }
}