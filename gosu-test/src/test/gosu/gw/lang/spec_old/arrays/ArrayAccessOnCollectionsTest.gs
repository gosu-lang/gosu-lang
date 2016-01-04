package gw.lang.spec_old.arrays
uses gw.lang.reflect.gs.IGosuClass

class ArrayAccessOnCollectionsTest extends gw.test.TestClass
{
  function testAcceptable()
  {
    assertTrue( ArrayAccessOnCollectionsTestClass.Type.Valid )
    assertNull( (ArrayAccessOnCollectionsTestClass as IGosuClass).ParseResultsException )
  }
  
  function testNotAcceptible()
  {
    assertFalse( Errant_NonListsNotAllowedAsArrayAccess.Type.Valid )
    assertEquals( 4, (Errant_NonListsNotAllowedAsArrayAccess as IGosuClass).ParseResultsException.ParseExceptions.Count )
    assertEquals( 0, (Errant_NonListsNotAllowedAsArrayAccess as IGosuClass).ParseResultsException.ParseWarnings.Count )
  }
}
