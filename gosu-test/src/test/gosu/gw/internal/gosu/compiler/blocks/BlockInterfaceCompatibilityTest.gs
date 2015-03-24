package gw.internal.gosu.compiler.blocks
uses java.util.concurrent.Callable

class BlockInterfaceCompatibilityTest extends gw.test.TestClass
{
  function testGenericInterfaceCompatibility() {
    var caller = new BlockInterfaceCompatibiltyHelper<String>()
    assertEquals( "test", caller.callItUnparameterized( \-> "test" ) )
    assertEquals( "test", caller.callItFunctionParameterized( \-> "test" ) )
    assertEquals( "test", caller.callItClassParameterized( \-> "test" ) )
  }

  function testGenericInterfaceCompatibility2() {
    var caller = new BlockInterfaceCompatibiltyHelper<String>()
    assertEquals( "test", caller.callItIndirectlyUnparameterized( \ genericIface -> genericIface.returnTestString() ) )
  }

  function testJavaFunctionalInterfaceHavingGetter() {
    var hiya = TestClassLocal.constant( \ -> "hi" )
    assertEquals( "hi", hiya.Init )
  }
}