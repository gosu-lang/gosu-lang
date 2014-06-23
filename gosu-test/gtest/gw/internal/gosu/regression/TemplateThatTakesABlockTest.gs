package gw.internal.gosu.regression
uses gw.test.TestClass

class TemplateThatTakesABlockTest extends TestClass {

  function testTemplateCanTakeBlockArgument() {
    assertEquals( "Output of block: foo", TemplateThatTakesABlock.renderToString( \ -> "foo" ) )
  }

}
