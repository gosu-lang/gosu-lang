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
    Assert.assertNull( methodDocs )
  }

  @Test
  function publicMethodWithDocCommentHasDescription() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithDocComment() )
    Assert.assertTrue( methodDocs.text().contains( "publicMethodWithDocComment comment" ) )
  }

  @Test
  function publicMethodWithCStyleCommentHasDescription() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithCComment() )
    Assert.assertTrue( methodDocs.text().contains( "publicMethodWithCComment comment" ) )
  }

  @Test
  function publicMethodWithLineCommentDoesNotHaveDescription() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithLineComment() )
    Assert.assertFalse( methodDocs.text().contains( "publicMethodWithLineComment comment" ) )
  }

  @Test
  function publicMethodOnParentIsNotIncluded() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodFromParent() )
    Assert.assertNull( methodDocs )
  }

  @Test
  function publicMethodOnDirectEnhancementIsIncluded() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodFromDirectEnhancement() )
    Assert.assertNotNull( methodDocs )
  }

  @Test
  function publicMethodOnParentEnhancementIsIncluded() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodFromParentEnhancement() )
    Assert.assertNotNull( methodDocs )
  }

  @Test
  function publicMethodWithJavadocStyleThrowsAnnotationHasProperDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithJavaStyleThrows() )
    Assert.assertTrue( methodDocs.text().contains( "throws java.lang.NullPointerException" ) )
  }

}