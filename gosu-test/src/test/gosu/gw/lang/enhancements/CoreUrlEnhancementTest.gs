package gw.lang.enhancements

uses gw.test.TestClass
uses java.io.File
uses java.net.URL

class CoreUrlEnhancementTest extends TestClass
{
  function testTextContent()
  {
    var file = File.createTempFile( "tmp", "txt" )
    var sample = "Это is a test"
    file.write( sample )
    var url = file.toURI().toURL()
    var text = url.TextContent
    assertEquals( sample, text )
    // again
    url = file.toURI().toURL()
    text = url.TextContent
    assertEquals( sample, text )
  }
}
