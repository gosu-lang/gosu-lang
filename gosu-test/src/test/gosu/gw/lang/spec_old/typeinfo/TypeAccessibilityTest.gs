package gw.lang.spec_old.typeinfo
uses gw.lang.spec_old.typeinfo.public.PublicClass
uses gw.lang.spec_old.typeinfo.internal.AccessesInternalClass
uses gw.lang.spec_old.typeinfo.public.Errant_AccessesPrivateClass
uses gw.lang.parser.resources.Res
uses gw.lang.spec_old.typeinfo.public.Errant_AccessesInternalClass

class TypeAccessibilityTest extends gw.test.TestClass
{
  function testPublicAccess()
  {
    print( PublicClass )
  }
  
  function testInternalAccess()
  {
    assertTrue( AccessesInternalClass.Type.Valid )
  }

  function testErrant_AccessesInternalClass()
  {
    assertFalse( Errant_AccessesInternalClass.Type.Valid )
    var errors = Errant_AccessesInternalClass.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_TYPE_HAS_XXX_ACCESS, errors.get( 0 ).MessageKey )
  }

  function testErrant_AccessesPrivateClass()
  {
    assertFalse( Errant_AccessesPrivateClass.Type.Valid )
    var errors = Errant_AccessesPrivateClass.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_TYPE_HAS_XXX_ACCESS, errors.get( 0 ).MessageKey )
  }
}
