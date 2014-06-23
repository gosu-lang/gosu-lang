package gw.internal.gosu.regression
uses gw.test.TestClass
uses java.lang.Runnable

class OuterKeywordWorksAsArgInTemplateTest extends TestClass {
  function testIt() {
    assertEquals( "length: 3", OuterKeywordAsArg.renderToString("Foo") )
  }
}
