package gw.gosudoc

uses com.example.bootstrap.Super
uses com.example.bootstrap.Sub
uses gw.gosudoc.util.BaseGosuDocTest
uses org.junit.Assert
uses org.jsoup.Jsoup
uses org.junit.Test

class OverridingTest extends BaseGosuDocTest {

  @Test
  function superMethodHasDocs() {
    var docs = gosuDocForType(com.example.bootstrap.Super)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( Super#methodToOverrideDocs() )
    Assert.assertTrue(methodDocs.text().contains("From super"))
  }

  @Test
  function subMethodHasOverridingDocs() {
    var docs = gosuDocForType(com.example.bootstrap.Sub)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( Sub#methodToOverrideDocs() )
    Assert.assertTrue(methodDocs.text().contains("From sub"))
  }

  @Test
  function superPropertyHasDocs() {
    var docs = gosuDocForType(com.example.bootstrap.Super)
    var doc = Jsoup.parse(docs.read())
    var propertyDocs = doc.findPropertyList( Super#PropertyToOverrideDocs )
    Assert.assertTrue(propertyDocs.text().contains("From super"))
  }

  @Test
  function subPropertyHasOverridingDocs() {
    var docs = gosuDocForType(com.example.bootstrap.Sub)
    var doc = Jsoup.parse(docs.read())
    var propertyDocs = doc.findPropertyList(Sub#PropertyToOverrideDocs)
    Assert.assertTrue(propertyDocs.text().contains("From sub"))
  }


  @Test
  function superMethodHasDocsNoOverride() {
    var docs = gosuDocForType(com.example.bootstrap.Super)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( Super#methodToNotOverrideDocs() )
    Assert.assertTrue(methodDocs.text().contains("From super"))
  }

  @Test
  function subMethodHasOverridingDocsNoOverride() {
    var docs = gosuDocForType(com.example.bootstrap.Sub)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( Sub#methodToNotOverrideDocs() )
    Assert.assertFalse(methodDocs.text().contains("From super"))
    Assert.assertFalse(methodDocs.text().contains("From sub"))
  }

  @Test
  function superPropertyHasDocsNoOverride() {
    var docs = gosuDocForType(com.example.bootstrap.Super)
    var doc = Jsoup.parse(docs.read())
    var propertyDocs = doc.findPropertyList( Super#PropertyToNotOverrideDocs )
    Assert.assertTrue(propertyDocs.text().contains("From super"))
  }

  @Test
  function subPropertyHasOverridingDocsNoOverride() {
    var docs = gosuDocForType(com.example.bootstrap.Sub)
    var doc = Jsoup.parse(docs.read())
    var propertyDocs = doc.findPropertyList(Sub#PropertyToNotOverrideDocs)
    Assert.assertFalse(propertyDocs.text().contains("From super"))
    Assert.assertFalse(propertyDocs.text().contains("From sub"))
  }

  @Test
  function subMethodHasInlinedInheritedDocs() {
    var docs = gosuDocForType(com.example.bootstrap.Sub)
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( Sub#methodToInheritDoc() )
    Assert.assertTrue(methodDocs.text().contains("From super"))
  }

  @Test
  function subPropertyHasInlinedInheritedDocs() {
    var docs = gosuDocForType(com.example.bootstrap.Sub)
    var doc = Jsoup.parse(docs.read())
    var propertyDocs = doc.findPropertyList(Sub#PropertyToInheritDoc)
    Assert.assertTrue(propertyDocs.text().contains("From super"))
  }

}