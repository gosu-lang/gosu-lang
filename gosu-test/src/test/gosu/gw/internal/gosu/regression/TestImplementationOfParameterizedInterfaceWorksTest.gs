package gw.internal.gosu.regression
uses gw.test.TestClass

class TestImplementationOfParameterizedInterfaceWorksTest extends TestClass {

  function testIt() {
    assertEquals( "yes", new ImplementsJavaGenericInterface().invokeIt() )
  }

}
