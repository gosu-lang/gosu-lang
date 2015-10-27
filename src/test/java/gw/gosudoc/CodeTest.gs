package gw.gosudoc

uses com.example.bootstrap.CodeDocClass
uses com.example.bootstrap.MethodsClass
uses com.example.bootstrap.TagsDocClass
uses gw.gosudoc.util.BaseGosuDocTest
uses org.jsoup.Jsoup
uses org.junit.Assert
uses org.junit.Test

class CodeTest extends BaseGosuDocTest {

  @Test
  function simpleCodeBlock() {
    var docs = gosuDocForType(CodeDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( CodeDocClass#hasCodeTag() )
    print(methodDocs)
    Assert.assertTrue(methodDocs.text().contains("<code>var x = 10</code>"))
  }

  @Test
  function emptyCodeBlock() {
    var docs = gosuDocForType(CodeDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( CodeDocClass#emptyCodeTag() )
    print(methodDocs)
    Assert.assertTrue(methodDocs.text().contains("<code></code>"))
  }

}