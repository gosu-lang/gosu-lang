package gw.internal.gosu.regression

uses gw.test.TestClass
uses java.io.StringWriter

class TemplateWArrayArgsTest extends TestClass {

  function testTemplateWArrayArgsRendersToString() {
    assertEquals("a,b,c", TemplateWArrayArgs.renderToString({"a", "b", "c"}))
  }

  function testTemplateWArrayArgsRenders() {
    var writer = new StringWriter()
    TemplateWArrayArgs.render(writer, {"a", "b", "c"})
    assertEquals("a,b,c", writer.toString()) 
  }

}
