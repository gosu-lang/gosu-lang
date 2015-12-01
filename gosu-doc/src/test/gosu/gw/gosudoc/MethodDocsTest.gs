package gw.gosudoc

uses com.example.bootstrap.MethodsClass
uses gw.gosudoc.util.BaseGosuDocTest
uses org.junit.Assert
uses org.jsoup.Jsoup
uses org.junit.Ignore
uses org.junit.Test

class MethodDocsTest extends BaseGosuDocTest {

  @Test
  function publicMethodIsInDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    print(doc)
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
    Assert.assertContains( methodDocs.text(),  "publicMethodWithDocComment comment" )
  }

  @Test
  function publicMethodWithCStyleCommentHasDescription() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithCComment() )
    Assert.assertContains( methodDocs.text(), "publicMethodWithCComment comment" )
  }

  @Test
  function publicMethodWithLineCommentDoesNotHaveDescription() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithLineComment() )
    Assert.assertDoesNotContain( methodDocs.text(), "publicMethodWithLineComment comment" )
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
    Assert.assertContains( methodDocs.text(), "throws java.lang.NullPointerException" )
  }

  @Test
  function publicStaticMethodWithDocCommentHasDescriptionAndStaticModifier() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#staticPublicMethodWithDocComment() )
    Assert.assertContains( methodDocs.text(), " public static void staticPublicMethodWithDocComment" )
  }

  @Test
  function publicMethodWithParametersHaveParametersDocumented() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithOneArgNoComment() )
    Assert.assertContains( methodDocs.text(), "Parameters: str -" )
  }

  @Test
  function publicMethodWithParameterDocumentedJavaDocStyleIncludesDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithOneArgJavaDocStyleComment() )
    Assert.assertContains( methodDocs.text(), "Parameters: str - javadoc-style comment" )
  }

  @Test
  function publicMethodWithParameterDocumentedGosuStyleIncludesDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithOneArgGosuStyleComment() )
    Assert.assertContains( methodDocs.text(), "Parameters: str - gosu-style param" )
  }

  @Test
  @Ignore("@Params annotation is not currently working")
  function publicMethodWithParameterDocumentedWithParamsAnntationIncludesDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithOneArgsAndParamsAnnotationComment() )
    Assert.assertContains( methodDocs.text(), "Parameters: str - str1 param" )
  }

  @Test
  function publicMethodWithTwoParametersHaveParametersDocumented() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithTwoArgNoComment() )
    Assert.assertContains( methodDocs.text(), "str2 -" )
  }

  @Test
  function publicMethodWithTwoParametersDocumentedJavaDocStyleIncludesDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithTwoArgJavaDocStyleComment() )
    Assert.assertContains( methodDocs.text(), "str2 - javadoc-style comment 2" )
  }

  @Test
  function publicMethodWithTwoParametersDocumentedGosuStyleIncludesDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithTwoArgGosuStyleComment() )
    Assert.assertContains( methodDocs.text(), "str2 - gosu-style param 2" )
  }

  @Test
  @Ignore("@Params annotation is not currently working")
  function publicMethodWithTwoParametersDocumentedWithParamsAnntationIncludesDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicMethodWithTwoArgsAndParamsAnnotationComment() )
    Assert.assertContains( methodDocs.text(), "str2 - str2 param" )
  }

  @Test
  function overLoadedMethodsAppearInDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var methodDocs = doc.findMethodList( MethodsClass#publicOverloadedMethod() )
    Assert.assertContains( methodDocs.text(), "Overloaded 1" )

    methodDocs = doc.findMethodList( MethodsClass#publicOverloadedMethod(String) )
    Assert.assertContains( methodDocs.text(), "Overloaded 2" )
  }

}