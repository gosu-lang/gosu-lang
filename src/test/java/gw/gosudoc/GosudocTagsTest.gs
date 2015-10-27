package gw.gosudoc

uses com.example.bootstrap.TagsDocClass
uses gw.gosudoc.util.BaseGosuDocTest
uses org.jsoup.Jsoup
uses org.junit.Assert
uses org.junit.Test

class GosudocTagsTest extends BaseGosuDocTest {

  @Test
  function simpleCodeBlock() {
    var docs = gosuDocForType(TagsDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( TagsDocClass#hasCodeTag() )
    Assert.assertTrue(methodDocs.html().contains("<code>var x = 10</code>"))
  }

  @Test
  function emptyCodeBlock() {
    var docs = gosuDocForType(TagsDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( TagsDocClass#emptyCodeTag() )
    Assert.assertTrue(methodDocs.html().contains("<code></code>"))
  }

  @Test
  function docRootTag() {
    var docs = gosuDocForType(TagsDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( TagsDocClass#docRootTag() )
    print(methodDocs.html())
    Assert.assertFalse(methodDocs.html().contains("{@docRoot}"))
  }

}