package gw.internal.gosu.parser.classTests.gwtest.interfaces

uses gw.test.TestClass

class InterfaceTest extends TestClass
{
  function testInterfacesCannotHaveImplementations() {
    assertFalse( Errant_InterfaceWithImplemetation.Type.Valid )
    var pre = Errant_InterfaceWithImplemetation.Type.ParseResultsException
    for( line in 1..9 ) {
      assertFalse( pre.ParseExceptions.hasMatch(\ i -> i.Line == line ) )
    }
    for( line in 10..18 ) {
      assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == line ) )
    }
    for( line in 19..100 ) {
      assertFalse( pre.ParseExceptions.hasMatch(\ i -> i.Line == line ) )
    }

  }

  function testCanHaveMethodWithSameNameAsClassDifferingByCaseOnly()
  {
    assertTrue( CanHaveMethodWithSameNameAsClassDifferingByCaseOnly.Type.Valid )
    assertNotNull( CanHaveMethodWithSameNameAsClassDifferingByCaseOnly.Type.TypeInfo.getMethod( "canHaveMethodWithSameNameAsClassDifferingByCaseOnly", {} ) )
  }
}
