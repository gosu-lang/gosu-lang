package gw.gosudoc

uses com.example.bootstrap.MethodsClass
uses gw.gosudoc.util.BaseGosuDocTest
uses org.junit.Assert
uses org.jsoup.Jsoup
uses org.junit.Test

class MethodDocsTest extends BaseGosuDocTest {

  @Test
  function publicMethodIsInDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethod() )
    Assert.assertNotNull(methodDocs)
  }

  @Test
  function publicMethodWithArgsIsInDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithArgs() )
    Assert.assertNotNull(methodDocs)
  }

  @Test
  function privateMethodIsNotInDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#privateMethod() )
    Assert.assertNull(methodDocs)
  }

}