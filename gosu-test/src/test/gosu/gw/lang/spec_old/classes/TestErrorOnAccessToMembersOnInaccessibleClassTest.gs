package gw.lang.spec_old.classes
uses gw.lang.spec_old.classes.test.Errant_IllegalAccessToInternalClassMemberTest
uses gw.lang.parser.resources.Res
uses gw.test.TestClass

class TestErrorOnAccessToMembersOnInaccessibleClassTest extends TestClass
{
  function testErrant_IllegalAccessToInternalClassMemberTest() 
  {
    assertFalse( Errant_IllegalAccessToInternalClassMemberTest.Type.Valid )
    var errors = Errant_IllegalAccessToInternalClassMemberTest.Type.ParseResultsException.ParseExceptions
    assertEquals( Res.MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, errors.get( 0 ).MessageKey )
  }

  function testErrant_IllegalAccessToPrivateClassMemberTest() 
  {
    assertFalse( Errant_IllegalAccessToPrivateClassMemberTest.Type.Valid )
    var errors = Errant_IllegalAccessToPrivateClassMemberTest.Type.ParseResultsException.ParseExceptions
    assertEquals( Res.MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, errors.get( 0 ).MessageKey )
  }

  function testCanAccessInternalClassViaCastToObject()
  {
    assertEquals( "InternalClass", (new OtherClass().callme() as Object).toString() )
  }
  
  function testCanAccessPrivateClassViaCastToObject()
  {
    assertEquals( "PrivateClass", (new OtherClass().getPrivateClass() as Object).toString() )
  }
}
