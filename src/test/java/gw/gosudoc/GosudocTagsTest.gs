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
    Assert.assertTrue(methodDocs.html(), methodDocs.html().contains("<code>var x = 10</code>"))
  }

  @Test
  function emptyCodeBlock() {
    var docs = gosuDocForType(TagsDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( TagsDocClass#emptyCodeTag() )
    Assert.assertTrue(methodDocs.html(), methodDocs.html().contains("<code></code>"))
  }

  @Test
  function docRootTag() {
    var docs = gosuDocForType(TagsDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( TagsDocClass#docRootTag() )
    Assert.assertFalse(methodDocs.html(), methodDocs.html().contains("{@docRoot}"))
  }

  @Test
  function relativeLinkTag() {
    var docs = gosuDocForType(TagsDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( TagsDocClass#localFeatureLinkTag() )
    Assert.assertTrue(methodDocs.html(), methodDocs.html().contains('<a href="#docRootTag()">#docRootTag()</a>'))
  }

  @Test
  function packageRelativeLinkTag() {
    var docs = gosuDocForType(TagsDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( TagsDocClass#packageRelativeLink() )
    Assert.assertTrue(methodDocs.html(), methodDocs.html().contains('<a href="com.example.bootstrap.Super.html#methodToOverrideDocs()">Super#methodToOverrideDocs()</a>'))
  }

  @Test
  function absoluteLinkTag() {
    var docs = gosuDocForType(TagsDocClass)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( TagsDocClass#fullyQualifiedLink() )
    var link = '<a href="../../../com/example/bootstrap/test/com.example.bootstrap.test.AnotherTestClass.html#foo()">com.example.bootstrap.test.AnotherTestClass#foo()</a>'
    Assert.assertTrue(methodDocs.html(), methodDocs.html().contains(link))
  }

}