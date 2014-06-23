package gw.internal.gosu.compiler.generics

uses gw.test.TestClass
uses gw.internal.gosu.compiler.generics.stuff.*
uses gw.lang.parser.resources.Res

class GenericsTest extends TestClass
{
  function testGosuInvocationOfGenericMethodsFromJavaSuperclassWorks() {
    var o = new GosuClassExtendingJavaClassWithGenericMethods()
    assertEquals( "foo", o.callIdentity() )
    assertEquals( "foo", o.callStaticIdentity() )
    assertEquals( "foo", o.staticCallStaticIdentity() )
  }

  function testGosuInvocationOfGenericMethodsFromJavaSuperclassWorksThroughThisPointer() {
    var o = new GosuClassExtendingJavaClassWithGenericMethods()
    assertEquals( "foo", o.callIdentityIndirectly() )
    assertEquals( "foo", o.callStaticIdentityIndirectly() )
    assertEquals( "foo", o.staticCallStaticIdentityIndirectly() )
  }

  function testGenericFieldCanBeAccessedOnParameterizedExtensionsOfGenericParent() {
    var child = new ExtendsParameterizationOfGenericParentWithField()
    assertEquals( "test1", child.getTee1() )
    assertEquals( "test2", child.getTee2() )
    assertEquals( "test1", child.getTee1Indirectly() )
  }
  
  function testGenericFieldCanBeWrittenToOnParameterizedExtensionsOfGenericParent() {
    var child = new ExtendsParameterizationOfGenericParentWithField()
    child.setTee1( "foo" )
    assertEquals( "foo", child.getTee1() )
    child.setTee1Indirectly( "bar" )
    assertEquals( "bar", child.getTee1() )
  }

  function testGenericMethodInvocationFromConstructorWorks() {
    var x = new CallsGenericMethodInConstructor()
  }

  function testGosuInvocationOfGenericMethodsThatAreOverriddenFromJavaSuperclassWorks() {
    var o = new GosuClassExtendingJavaClassWithGenericMethods()
    assertEquals( null, o.callIdentityToOverride() )
    assertEquals( null, o.callStaticIdentityToOverride() )
    assertEquals( null, o.staticCallStaticIdentityToOverride() )
  }

  function testGosuInvocationOfGenericMethodsThatAreOverriddenFromJavaSuperclassWorksThroughThisPointer() {
    var o = new GosuClassExtendingJavaClassWithGenericMethods()
    assertEquals( null, o.callIdentityIndirectlyToOverride() )
    assertEquals( null, o.callStaticIdentityIndirectlyToOverride() )
    assertEquals( null, o.staticCallStaticIdentityIndirectlyToOverride() )
  }
  
  function testInnerClassesCanHaveSameTopLevelTypeVariableDeclaration() {
    assertTrue( ClassWithTwoGenericInnersWithTypeVarsWithSameName.Type.Valid ) 
  }

  function testBottomClassImplementsInterfaceWithGenericMethodIndirectly() {
    assertTrue( Bottom.Type.Valid )
  }

  function testErrant_TextendsT()
  {
    assertFalse( Errant_TextendsT.Type.Valid )
    var errors = Errant_TextendsT.Type.ParseResultsException.ParseExceptions
    assertEquals( Res.MSG_CYCLIC_INHERITANCE, errors.get( 0 ).MessageKey )
  }
}