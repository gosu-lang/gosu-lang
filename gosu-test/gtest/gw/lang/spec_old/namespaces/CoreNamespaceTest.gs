package gw.lang.spec_old.namespaces
uses gw.test.TestClass

class CoreNamespaceTest extends TestClass 
{
  function testGoodNamespacesParseCorrectly() 
  {
    assertTrue( GoodNameSpaces.Type.Valid )
    assertTrue( GoodNameSpaces2.Type.Valid )
  }

  function testBadnamespacesDoNotParse() 
  {
    assertFalse( Errant_BadNameSpaces.Type.Valid )
    var pre = Errant_BadNameSpaces.Type.ParseResultsException
    //should be errors on lines 3 through 19
    for( i in 4..19 ) 
    {
      assertTrue( pre.ParseExceptions.hasMatch( \ pe -> pe.Line == i  ) )
    }
  }
}
