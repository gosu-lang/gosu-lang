package gw.internal.gosu.regression
uses gw.lang.parser.resources.Res
uses gw.test.TestClass

class ConstructRecursiveTypeTest extends TestClass {

  function testErrant_ConstructRecursiveType() {
    assertFalse( Errant_ConstructRecursiveType.Type.Valid )
    var errors = Errant_ConstructRecursiveType.Type.ParseResultsException.ParseExceptions
    assertEquals( 2, errors.Count )
    errors.each( \ e -> assertEquals( Res.MSG_CANNOT_CONSTRUCT_RECURSIVE_CLASS, e.MessageKey ) )
  }

}
